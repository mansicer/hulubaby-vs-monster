import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Connection;
import com.almasb.fxgl.net.Server;
import com.almasb.fxgl.physics.CollisionHandler;
import components.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.util.Duration;
import network.OperateInfo;
import network.OperateInfoBuilder;
import types.BasicEntityTypes;
import types.NetMsgTypes;
import util.ComponentUtils;

import java.util.Optional;

import static com.almasb.fxgl.dsl.FXGL.*;


public class Main extends GameApplication {
    private static final String GameTitle = "Hulubabies vs Monsters";
    private static final int GameWidth = 1600;
    private static final int GameHeight = 900;
    private static final String GameVersion = "0.0.2";

    private Optional<Entity> currentPlayer;
    private Optional<Polygon> playerIcon;

    private boolean isServer;
    private static final String ip = "localhost";
    private static final int port = 55555;
    private Server<Bundle> server;
    private Client<Bundle> client;


    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle(GameTitle);
        gameSettings.setWidth(GameWidth);
        gameSettings.setHeight(GameHeight);
        gameSettings.setVersion(GameVersion);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new HvMFactory());
        FXGL.getGameScene().getViewport().setBounds(0, 0, GameWidth, GameHeight);
        currentPlayer = Optional.of(FXGL.spawn("TestCharacter1",
                new SpawnData(GameWidth/3, GameHeight/2)
                        .put("name","player1")
                        .put("id", 111)));
        geneartePlayerIcon(currentPlayer.get());
        FXGL.spawn("TestCharacter1-Enemy",
                new SpawnData(GameWidth/2, GameHeight/3)
                .put("name","player2")
                .put("id", 222));
        runOnce(()->{
            getDialogService().showConfirmationBox("Is Server?", answer -> {
                isServer = answer;
                if (isServer) {
                    server = getNetService().newUDPServer(port);
                    server.setOnConnected(bundleConnection -> {
                        bundleConnection.addMessageHandlerFX(this::handleMessage);
                    });
                    server.startAsync();
                } else {
                    client = getNetService().newUDPClient(ip, port);
                    client.setOnConnected(bundleConnection -> {
                        bundleConnection.addMessageHandlerFX(this::handleMessage);
                    });
                    client.connectAsync();
                }
            });
        }, Duration.seconds(0.2));
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();
        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                currentPlayer.ifPresent(player -> {
                    moveFunc(player,"UP");
                });
            }
            @Override
            protected void onActionEnd() {
                currentPlayer.ifPresent(player -> {
                    stopFunc(player);
                });
            }
        }, KeyCode.UP);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                currentPlayer.ifPresent(player -> {
                    moveFunc(player,"DOWN");
                });

            }
            @Override
            protected void onActionEnd() {
                currentPlayer.ifPresent(player -> {
                    stopFunc(player);
                });
            }
        }, KeyCode.DOWN);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                currentPlayer.ifPresent(player -> {
                    moveFunc(player,"LEFT");
                });
            }
            @Override
            protected void onActionEnd() {
                currentPlayer.ifPresent(player -> {
                    stopFunc(player);
                });
            }
        }, KeyCode.LEFT);

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                currentPlayer.ifPresent(player -> {
                    moveFunc(player,"RIGHT");
                });
            }
            @Override
            protected void onActionEnd() {
                currentPlayer.ifPresent(player -> {
                    stopFunc(player);
                });
            }
        }, KeyCode.RIGHT);

        input.addAction(new UserAction("Attack") {
            @Override
            protected void onActionBegin() {
                currentPlayer.ifPresent(player -> {
//                    ComponentUtils.getAttackComponent(player).ifPresent(AttackComponent::attack);
                    attackFunc(player);
                });
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Choose Player") {
            @Override
            protected void onActionBegin() {
                var position = input.getMousePositionWorld();
                var range = new Rectangle2D(position.getX() - 6, position.getY() - 10, 12, 20);
                var entities = FXGL.getGameWorld().getEntitiesInRange(range);

                // TODO: choose the closest one
                for (Entity entity: entities) {
                    if (entity.getComponentOptional(ControllableComponent.class).isPresent()) {
                        if (currentPlayer.isPresent() && currentPlayer.get().isActive()) {
                            playerIcon.ifPresent(polygon -> currentPlayer.get().getViewComponent().removeChild(polygon));
                        }
                        currentPlayer = Optional.of(entity);
                        geneartePlayerIcon(currentPlayer.get());
                        break;
                    }
                }
            }
        }, MouseButton.PRIMARY);
    }

    private void moveFunc(Entity player,String ori) {
        IDComponent idComponent = player.getComponent(IDComponent.class);
//        OperateInfo moveinfo = new OperateInfo(idComponent.getName(),idComponent.getId(),player.getX(),player.getY(),ori);
        OperateInfo moveinfo = new OperateInfoBuilder()
                .name(idComponent.getName())
                .id(idComponent.getId())
                .position(player.getPosition())
                .ori(ori)
                .buildOperate();
        var bundle = new Bundle("");
        bundle.put("NetMsg",NetMsgTypes.MOVE);
        bundle.put("operateInfo",moveinfo);
        if (!isServer){
            client.broadcast(bundle);
        }
    }

    private void attackFunc(Entity player){
        IDComponent idComponent = player.getComponent(IDComponent.class);
        System.out.println(idComponent.getName());
        OperateInfo playInfo = new OperateInfoBuilder()
                .name(idComponent.getName())
                .id(idComponent.getId())
                .buildOperate();
        var bundle = new Bundle("");
        bundle.put("NetMsg",NetMsgTypes.ATTACK);
        bundle.put("operateInfo",playInfo);
        if(!isServer)
            client.broadcast(bundle);
    }

    private void stopFunc(Entity player){
        IDComponent idComponent = player.getComponent(IDComponent.class);
//        OperateInfo moveinfo = new OperateInfo(idComponent.getName(),idComponent.getId(),player.getX(),player.getY(),"");
        OperateInfo stopinfo = new OperateInfoBuilder()
                .name(idComponent.getName())
                .id(idComponent.getId())
                .position(player.getPosition())
                .buildOperate();
        var bundle = new Bundle("");
        bundle.put("NetMsg", NetMsgTypes.STOP);
        bundle.put("operateInfo",stopinfo);
        if (!isServer){
            client.broadcast(bundle);
        }
    }
    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(BasicEntityTypes.PLAYER, BasicEntityTypes.PLAYER) {
            @Override
            protected void onCollision(Entity a, Entity b) {
                // TODO: add collision on x and y separately
                ComponentUtils.getControllableComponent(a).ifPresent(o -> o.resignLastMove());
                ComponentUtils.getControllableComponent(b).ifPresent(o -> o.resignLastMove());
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(BasicEntityTypes.BULLET, BasicEntityTypes.PLAYER) {
            @Override
            protected void onCollisionBegin(Entity a, Entity b) {
                var typeA = a.getComponent(DetailedTypeComponent.class);
                var typeB = b.getComponent(DetailedTypeComponent.class);
                if (typeA.isEnemy(typeB)) {
                    int damage = a.getComponent(BulletComponent.class).getDamage();
                    if(isServer) {
                        IDComponent idComponent = b.getComponent(IDComponent.class);
                        OperateInfo operateInfo = new OperateInfoBuilder()
                                .name(idComponent.getName())
                                .id(idComponent.getId())
                                .damage(damage)
                                .buildOperate();
                        var bundle = new Bundle("");
                        bundle.put("NetMsg",NetMsgTypes.HIT);
                        bundle.put("operateInfo",operateInfo);
                        server.broadcast(bundle);
                        b.getComponent(HealthComponent.class).decreaseHealth(damage);
                        checkCurrentPlayerAlive();
                    }
                    a.removeFromWorld();
                }

            }
        });
    }

    protected void checkCurrentPlayerAlive() {
        if (!currentPlayer.get().isActive()) {
            currentPlayer = Optional.empty();
        }
    }

    protected void geneartePlayerIcon(Entity player) {
        int ICON_HEIGHT = 12, ICON_WIDTH = 10;
        var icon = new Polygon(0, 0, ICON_WIDTH, 0, ICON_WIDTH / 2, ICON_HEIGHT);
        icon.setLayoutY(-10 - ICON_HEIGHT - 3);
        icon.setLayoutX(player.getWidth() / 2 - ICON_WIDTH / 2);
        icon.setFill(Color.BLUE);
        player.getViewComponent().addChild(icon);
        playerIcon = Optional.of(icon);
    }

    protected void handleMessage(Connection<Bundle> connection,Bundle bundle){
        NetMsgTypes nmt = bundle.get("NetMsg");
        OperateInfo operateInfo = (OperateInfo)bundle.get("operateInfo");
        String name = operateInfo.name;
        int id = operateInfo.id;
        var player = getGameWorld().getEntityByID(name,id);
        switch (nmt){
            case MOVE: {
                if (isServer) {
                    player.ifPresent(p -> {
                        String ori = operateInfo.ori;
                        moveOri(ori, p);
                        operateInfo.x = p.getX();
                        operateInfo.y = p.getY();
                        bundle.put("operateInfo", operateInfo);
                        server.broadcast(bundle);
                    });
                } else {
                    player.ifPresent(p -> {
                        double x = operateInfo.x;
                        double y = operateInfo.y;
                        String ori = operateInfo.ori;
                        p.getComponent(ControllableComponent.class).controlOri(ori);
                        System.out.println(x);
                        System.out.println(y);
                        p.setPosition(x,y);
                    });
                }
                break;
            }
            case STOP: {
                if(isServer){
                    player.ifPresent(p->{
                        p.getComponent(ControllableComponent.class).stop();
                        operateInfo.x = p.getX();
                        operateInfo.y = p.getY();
                        bundle.put("operateInfo", operateInfo);
                        server.broadcast(bundle);
                    });
                }
                else{
                    player.ifPresent(p -> {
                        double x = operateInfo.x;
                        double y = operateInfo.y;
                        p.getComponent(ControllableComponent.class).stop();
                        p.setPosition(x,y);
                    });
                }
                break;
            }
            case ATTACK:{
                if(isServer){
                    player.ifPresent(play->{
                        ComponentUtils.getAttackComponent(play).ifPresent(c->{
                            c.attack();
                            server.broadcast(bundle);
                        });
                    });
                }
                else{
                    player.ifPresent(play->{
                        ComponentUtils.getAttackComponent(play).ifPresent(c->{
                            c.attack();
//                            server.broadcast(bundle);
                        });
                    });
                }
                break;
            }
            case HIT:{
                if(!isServer){
                    int damage = operateInfo.damage;
                    player.ifPresent(p->{
                        p.getComponent(HealthComponent.class).decreaseHealth(damage);
                        checkCurrentPlayerAlive();
                    });
                }
            }
        }

    }

    private void moveOri(String ori, Entity p) {
        switch (ori) {
            case "UP":
                p.getComponent(ControllableComponent.class).moveUp();
                break;
            case "DOWN":
                p.getComponent(ControllableComponent.class).moveDown();
                break;
            case "LEFT":
                p.getComponent(ControllableComponent.class).moveLeft();
                break;
            case "RIGHT":
                p.getComponent(ControllableComponent.class).moveRight();
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
