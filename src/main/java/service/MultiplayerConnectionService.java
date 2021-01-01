package service;

import com.almasb.fxgl.core.EngineService;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.SerializableComponent;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.net.Connection;
import com.almasb.fxgl.net.MessageHandler;
import components.ControllableComponent;
import components.NetworkIDComponent;
import javafx.geometry.Rectangle2D;
import kotlin.collections.AbstractMutableList;
import kotlin.collections.ArrayDeque;
import types.CampType;
import util.ComponentUtils;
import util.EntityUtils;
import util.PropertyUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultiplayerConnectionService extends EngineService {
    private Map<Connection<Bundle>, AbstractMutableList<Entity>> replicatedEntitiesMap;

    public MultiplayerConnectionService(){
        replicatedEntitiesMap = new HashMap<>();
    }

    @Override
    public void onGameUpdate(double tpf) {
        if (replicatedEntitiesMap.isEmpty())
            return;
        replicatedEntitiesMap.forEach((connection, entities) -> {
            if(!entities.isEmpty())
                updateReplicatedEntities(connection,entities);
        });
    }

    public void updateReplicatedEntities(Connection<Bundle> connection, AbstractMutableList<Entity> entities){
        var removeIDs = new ArrayList<Integer>();
        var removeBundle = new Bundle("ENTITY_REMOVALS_EVENT");
        entities.forEach(entity -> {
            var updateBundle = new Bundle("ENTITY_UPDATES_EVENT");

            NetworkIDComponent networkIDComponent = entity.getComponent(NetworkIDComponent.class);
            var networkID = networkIDComponent.getId();

            if(entity.isActive()) {
                entity.getComponents().forEach(component -> {
                    if (ComponentUtils.checkComponentUpdatable(component)) {
                        ComponentUtils.packUpComponentBundle(updateBundle, (SerializableComponent) component);
                    }
                });
                updateBundle.put("position",new Vec2(entity.getPosition()));
                updateBundle.put("NetID",networkID);
                connection.send(updateBundle);
            }
            else {
                removeIDs.add(networkID);
            }
        });
        if(!removeIDs.isEmpty()){
            removeBundle.put("removeIDs", removeIDs);
            connection.send(removeBundle);
        }
        entities.removeIf(entity -> !entity.isActive());
    }

    public void spawn(Connection<Bundle> connection, Entity entity, SpawnData spawnData, String entityName){
        if(!entity.hasComponent(NetworkIDComponent.class)){
            System.err.printf("Attempted to network-spawn entity %s , but it does not have NetworkComponentPlus\n",entityName);
            return ;
        }
        var networkComponent = entity.getComponent(NetworkIDComponent.class);
        var bundle = new Bundle("ENTITY_SPAWN_EVENT");
        bundle.put("NetID", networkComponent.getId());
        bundle.put("PositionX", entity.getX());
        bundle.put("PositionY", entity.getY());
        bundle.put("entityName", entityName);
        spawnData.getData().forEach((k, v) -> {
            if (v instanceof Serializable) {
                bundle.put(k, (Serializable) v);
            }
        });

        AbstractMutableList<Entity> orDefault = replicatedEntitiesMap.getOrDefault(connection, new ArrayDeque<>());
        orDefault.add(entity);
        replicatedEntitiesMap.put(connection,orDefault);
        connection.send(bundle);
    }

    public void addEntityReplicationReceiver(Connection<Bundle> connection, GameWorld gameWorld){
        connection.addMessageHandlerFX((connection1, bundle) -> {
            if (bundle.getName().startsWith("ENTITY_SPAWN_EVENT")){
                int netID = bundle.get("NetID");
                String entityName = bundle.get("entityName");
                double x = bundle.get("PositionX");
                double y = bundle.get("PositionY");
                bundle.getData().remove("NetID");
                bundle.getData().remove("entityName");
                bundle.getData().remove("PositionX");
                bundle.getData().remove("PositionY");
                var spawnData = new SpawnData(x, y);
                spawnData.getData().putAll(bundle.getData());
                var e = gameWorld.spawn(entityName, spawnData);
                e.getComponent(NetworkIDComponent.class).setId(netID);
            }
            else if (bundle.getName().startsWith("ENTITY_UPDATES_EVENT")){
                int netID = bundle.get("NetID");
                EntityUtils.getEntityByNetworkID(netID).ifPresent(entity -> {
                    Vec2 position = bundle.get("position");
                    entity.setPosition(position);
                    entity.getComponents().forEach(component -> {
                        if (ComponentUtils.checkComponentUpdatable(component)) {
                            ComponentUtils.unpackComponentBundle(bundle, (SerializableComponent) component);
                        }
                    });
                });
            }
            else if (bundle.getName().startsWith("ENTITY_REMOVALS_EVENT")){
                var removeIDs = (ArrayList<Integer>) bundle.get("removeIDs");
                removeIDs.forEach(id -> {
                    EntityUtils.getEntityByNetworkID(id).ifPresent(entity -> {
                        entity.removeFromWorld();
                    });
                });
            }
            else if (bundle.getName().startsWith("Reply: ")) {
                String replyName = bundle.getName().substring("Reply: ".length());
                if (replyName.equals("Choose Player")) {
                    int id = bundle.get("playerID");
                    PropertyUtils.setCurrentPlayerID(id);
                }
            }
        });
    }

    public void registerMessageHandler(Connection<Bundle> connection, MessageHandler<Bundle> handler) {
        connection.addMessageHandlerFX(handler);
    }

    public void sendMessage(Connection<Bundle> connection, Bundle message) {
        connection.send(message);
    }

    public void addInputReplicationSender(Connection<Bundle> connection, GameWorld gameWorld) {
        connection.addMessageHandlerFX(((connection1, bundle) -> {
            if (bundle.getName().equals("PlayerAllocation")) {
                int id = bundle.get("playerID");
                gameWorld.getProperties().setValue("CurrentPlayerID", id);
                FXGL.getWorldProperties().setValue("CurrentPlayerID", id);
            }
        }));
    }

    public void addInputReplicationReceiver(Connection<Bundle> connection) {
        connection.addMessageHandlerFX((connection1, bundle) -> {
            if (bundle.getName().startsWith("Action: ")) {
                String actionName = bundle.getName().substring("Action: ".length());
                if (actionName.equals("moveUp")) {
                    int id = bundle.get("playerID");
                    if (id == PropertyUtils.getOpponentPlayerID()) {
                        EntityUtils.getEntityByNetworkID(id).ifPresent(entity -> {
                            entity.getComponent(ControllableComponent.class).moveUp();
                        });
                    }
                }
                else if (actionName.equals("moveDown")) {
                    int id = bundle.get("playerID");
                    if (id == PropertyUtils.getOpponentPlayerID()) {
                        EntityUtils.getEntityByNetworkID(id).ifPresent(entity -> {
                            entity.getComponent(ControllableComponent.class).moveDown();
                        });
                    }
                }
                else if (actionName.equals("moveLeft")) {
                    int id = bundle.get("playerID");
                    if (id == PropertyUtils.getOpponentPlayerID()) {
                        EntityUtils.getEntityByNetworkID(id).ifPresent(entity -> {
                            entity.getComponent(ControllableComponent.class).moveLeft();
                        });
                    }
                }
                else if (actionName.equals("moveRight")) {
                    int id = bundle.get("playerID");
                    if (id == PropertyUtils.getOpponentPlayerID()) {
                        EntityUtils.getEntityByNetworkID(id).ifPresent(entity -> {
                            entity.getComponent(ControllableComponent.class).moveRight();
                        });
                    }
                }
                else if (actionName.equals("stopX")) {
                    int id = bundle.get("playerID");
                    if (id == PropertyUtils.getOpponentPlayerID()) {
                        EntityUtils.getEntityByNetworkID(id).ifPresent(entity -> {
                            entity.getComponent(ControllableComponent.class).stopX();
                        });
                    }
                }
                else if (actionName.equals("stopY")) {
                    int id = bundle.get("playerID");
                    if (id == PropertyUtils.getOpponentPlayerID()) {
                        EntityUtils.getEntityByNetworkID(id).ifPresent(entity -> {
                            entity.getComponent(ControllableComponent.class).stopY();
                        });
                    }
                }
                else if (actionName.equals("attack")) {
                    int id = bundle.get("playerID");
                    if (id == PropertyUtils.getOpponentPlayerID()) {
                        EntityUtils.getEntityByNetworkID(id).ifPresent(entity -> {
                            ComponentUtils.getAttackComponent(entity).get().attack();
                        });
                    }
                }
                else if (actionName.equals("Choose Player")) {
                    double mouseX = bundle.get("mousePositionX");
                    double mouseY = bundle.get("mousePositionY");

                    var range = new Rectangle2D(mouseX - 6, mouseY - 10, 12, 20);
                    var entities = FXGL.getGameWorld().getEntitiesInRange(range);
                    // TODO: choose the closest one
                    for (Entity entity : entities) {
                        if (entity.hasComponent(ControllableComponent.class) && EntityUtils.getCampType(entity).equals(CampType.MonsterCamp)) {
                            int id = EntityUtils.getNetworkID(entity);

                            Bundle message = new Bundle("Reply: Choose Player");
                            message.put("playerID", id);
                            sendMessage(connection1, message);
                            PropertyUtils.setOpponentPlayerID(id);
                            break;
                        }
                    }

                }
                else {
                    System.err.println("Unrecognized Action token " + actionName);
                }
            }
        });
    }
}
