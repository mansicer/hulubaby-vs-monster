package service;

import com.almasb.fxgl.core.EngineService;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.SerializableComponent;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.input.*;
import com.almasb.fxgl.net.Connection;
import com.almasb.fxgl.net.MessageHandler;
import components.NetworkIDComponent;
import kotlin.collections.AbstractMutableList;
import kotlin.collections.ArrayDeque;
import org.jetbrains.annotations.NotNull;
import util.EntityUtils;

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

    private void packUpComponentBundle(Bundle bundle, SerializableComponent component) {
        Bundle temporaryBundle = new Bundle("temporaryBundle");
        component.write(temporaryBundle);
        temporaryBundle.getData().forEach((k, v) -> {
            String newKey = component.getClass().getSimpleName() + "." + k;
            bundle.put(newKey, v);
        });
    }

    private void unpackComponentBundle(Bundle bundle, SerializableComponent component) {
        String componentName = component.getClass().getSimpleName();
        Bundle temporaryBundle = new Bundle("temporaryBundle");
        bundle.getData().forEach((k, v) -> {
            if (k.startsWith(componentName)) {
                temporaryBundle.put(k.substring(componentName.length() + 1), v);
            }
        });
        component.read(temporaryBundle);
    }

    public void updateReplicatedEntities(Connection<Bundle> connection, AbstractMutableList<Entity> entities){
        var updateBundle = new Bundle("ENTITY_UPDATES_EVENT");
        var removeBundle = new Bundle("ENTITY_REMOVALS_EVENT");

        entities.forEach(entity -> {
            var removeIDs = new ArrayList<Integer>();
            NetworkIDComponent networkIDComponent = entity.getComponent(NetworkIDComponent.class);
            var networkID = networkIDComponent.getId();

            if(entity.isActive()){
                entity.getComponents().forEach(component -> {
                    if (checkComponentUpdatable(component)) {
                        packUpComponentBundle(updateBundle, (SerializableComponent) component);
                    }
                });
                updateBundle.put("position",new Vec2(entity.getPosition()));
                updateBundle.put("NetID",networkID);
            }
            else{
                removeIDs.add(networkID);
            }

            if(!updateBundle.getData().isEmpty()){
                connection.send(updateBundle);
            }
            if(!removeIDs.isEmpty()){
                removeBundle.put("removeIDs", removeIDs);
                connection.send(removeBundle);
            }

//            if(!entity.isActive()){ // 有待商榷
//                entities.remove(entity);
//                entity.removeFromWorld();
//            }

        });
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
            if(bundle.getName().startsWith("ENTITY_SPAWN_EVENT")){
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
            else if(bundle.getName().startsWith("ENTITY_UPDATES_EVENT")){
                int netID = bundle.get("NetID");
                EntityUtils.getEntityByNetworkID(netID).ifPresent(entity -> {
                    entity.setPosition((Vec2) bundle.get("position"));
                    entity.getComponents().forEach(component -> {
                        if (checkComponentUpdatable(component)) {
                            unpackComponentBundle(bundle, (SerializableComponent) component);
                        }
                    });
                });
            }
            else if(bundle.getName().startsWith("ENTITY_REMOVALS_EVENT")){
                var removeIDs = (ArrayList<Integer>) bundle.get("removeIDs");
                removeIDs.forEach(id -> {
                    EntityUtils.getEntityByNetworkID(id).ifPresent(entity -> {
                        entity.removeFromWorld();
                    });
                });
            }
            else {
                System.err.println("Unrecognized Bundle Name " + bundle.getName());
            }
        });
    }

    public void registerMessageHandler(Connection<Bundle> connection, MessageHandler<Bundle> handler) {
        connection.addMessageHandlerFX(handler);
    }

    public void sendMessage(Connection<Bundle> connection, String name, Map<String, Serializable> data) {
        Bundle message = new Bundle(name);
        message.getData().putAll(data);
        connection.send(message);
    }

    public void addInputReplicationSender(Connection<Bundle> connection,Input input){
        input.addTriggerListener(new TriggerListener() {
            @Override
            protected void onActionBegin(@NotNull Trigger trigger) {
                var bundle = new Bundle("ActionBegin");
                if(trigger.isKey()){
                    var keyTrigger = (KeyTrigger)trigger;
                    bundle.put("key", keyTrigger.getKey());
                }
                else{
                    var mouseTrigger = (MouseTrigger)trigger;
                    bundle.put("btn", mouseTrigger.getButton());
                }
                connection.send(bundle);
            }

            @Override
            protected void onAction(@NotNull Trigger trigger) {
                super.onAction(trigger);
            }

            @Override
            protected void onActionEnd(@NotNull Trigger trigger) {
                var bundle = new Bundle("ActionEnd");
                if(trigger.isKey()){
                    var keyTrigger = (KeyTrigger)trigger;
                    bundle.put("key", keyTrigger.getKey());
                }
                else{
                    var mouseTrigger = (MouseTrigger)trigger;
                    bundle.put("btn",mouseTrigger.getButton());
                }
                connection.send(bundle);
            }
        });
    }

    public void addInputReplicationReceiver(Connection<Bundle> connection, Input input){
        connection.addMessageHandlerFX((connection1, bundle) -> {
            switch (bundle.getName()){
                case "ActionBegin":
                case "ActionEnd": {
                    var isKeyTrigger = bundle.exists("key");
                    Trigger trigger ;
                    if(isKeyTrigger){
                        trigger = new KeyTrigger(bundle.get("key"));
                    }
                    else{
                        trigger =  new MouseTrigger(bundle.get("btn"));
                    }

                    if(bundle.getName().equals("ActionBegin")){
                        input.mockTriggerPress(trigger);
                    }
                    else{
                        input.mockTriggerRelease(trigger);
                    }
                }
            }
        });
    }

    private boolean checkComponentUpdatable(Component component) {
        return component instanceof SerializableComponent &&
                !(component instanceof BoundingBoxComponent)
                ;
    }
}
