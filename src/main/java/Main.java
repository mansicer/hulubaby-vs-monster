import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Server;
import com.almasb.fxgl.net.UDPClientConfig;
import com.almasb.fxgl.net.UDPServerConfig;
import com.almasb.fxgl.physics.CollisionHandler;
import components.BulletComponent;
import components.DetailedTypeComponent;
import components.HealthComponent;
import components.NetworkIDComponent;
import config.Config;
import input.BehaviorControl;
import input.DirectionControl;
import input.GameControl;
import input.MouseControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import menu.GameMenu;
import org.jetbrains.annotations.NotNull;
import service.MultiplayerConnectionService;
import service.SocketClient;
import service.SocketService;
import types.BasicEntityTypes;
import util.ComponentUtils;
import util.EntityUtils;
import util.NetworkUtils;
import util.PropertyUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main extends GameApplication {
    private int currentPlayerID = -1;
    private Polygon playerIcon;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle(Config.GameTitle);
        gameSettings.setWidth(Config.GameWidth);
        gameSettings.setHeight(Config.GameHeight);
        gameSettings.setVersion(Config.GameVersion);
        gameSettings.addEngineService(MultiplayerConnectionService.class);
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setGameMenuEnabled(true);
        gameSettings.setManualResizeEnabled(true);
//        gameSettings.setDeveloperMenuEnabled(true);
        gameSettings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
        gameSettings.setSceneFactory(new SceneFactory(){
            @NotNull
            @Override
            public FXGLMenu newMainMenu() {
                return new GameMenu();
            }
        });
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new HvMFactory());
        FXGL.getGameScene().getViewport().setBounds(0, 0, Config.GameWidth, Config.GameHeight);
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/config.properties"));
        }
        catch (IOException e){
            System.out.println("no such file or directory");
        }
        String isserver = props.getProperty("isServer");
        String isclient = props.getProperty("isClient");
        boolean isServer=false;
        if (isserver.equals("true")){
            isServer = true;
        }
        boolean isClient=false;
        if(isclient.equals("true")){
            isClient = true;
        }

        FXGL.getWorldProperties().setValue("isServer",isServer);
        FXGL.getWorldProperties().setValue("isClient",isClient);

        if (isServer) {
            try {
                new SocketService().serverConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            UDPServerConfig<Bundle> bundle= new UDPServerConfig<Bundle>(Bundle.class,256);
            Server<Bundle> server = FXGL.getNetService().newUDPServer(Config.GameNetworkPort,bundle);
            server.startAsync();
            FXGL.getWorldProperties().setValue("server", server);
            server.setOnConnected(bundleConnection -> {
                NetworkUtils.getMultiplayerService().addInputReplicationReceiver(bundleConnection);
            });
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (isClient) {
            try {
                new SocketClient().clientConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            UDPClientConfig<Bundle> config = new UDPClientConfig<Bundle>(Bundle.class,256);
            Client<Bundle> client = FXGL.getNetService().newUDPClient(props.getProperty("ip"), Config.GameNetworkPort,config);
            client.connectAsync();
            FXGL.getWorldProperties().setValue("client", client);
            client.setOnConnected(bundleConnection -> {
                NetworkUtils.getMultiplayerService().addEntityReplicationReceiver(bundleConnection, FXGL.getGameWorld());
                NetworkUtils.getMultiplayerService().addInputReplicationSender(bundleConnection, FXGL.getGameWorld());
            });
        }

        if (isServer) {
            Entity entity = FXGL.spawn("TestCharacter1", FXGL.getAppWidth() / 3, FXGL.getAppHeight() / 3);
            FXGL.spawn("TestCharacter1", FXGL.getAppWidth() / 3, FXGL.getAppHeight() * 2 / 3);
            Entity enemy = FXGL.spawn("TestCharacter1-Enemy", FXGL.getAppWidth() * 2 / 3, FXGL.getAppHeight() / 3);
            FXGL.spawn("TestCharacter1-Enemy", FXGL.getAppWidth() * 2 / 3, FXGL.getAppHeight() * 2 / 3);

            FXGL.getWorldProperties().setValue("CurrentPlayerID", entity.getComponent(NetworkIDComponent.class).getId());

            Bundle message = new Bundle("PlayerAllocation");
            message.put("playerID", EntityUtils.getNetworkID(enemy));
            NetworkUtils.getServer().getConnections().forEach(connection ->  {
                NetworkUtils.getMultiplayerService().sendMessage(connection, message);
            });
        }
    }

//    private VBox vBox;
//    private void waitConnect(){
//        var progressIndicator = new ProgressIndicator();
//        Label label = new Label("等待连接中, 请稍后...");
//        label.setTextFill(Color.BLUE);
//        label.setTranslateX(-20);
//        progressIndicator.setProgress(-1F);
//        vBox = new VBox();
//        vBox.setSpacing(10);
//        vBox.setTranslateX(FXGL.getAppWidth()/2-30);
//        vBox.setTranslateY(FXGL.getAppHeight()/3);
//        vBox.setBackground(Background.EMPTY);
//        vBox.getChildren().addAll(progressIndicator,label);
//        FXGL.addUINode(vBox);
//    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("server", Optional.empty());
        vars.put("client", Optional.empty());
        vars.put("isServer", false);
        vars.put("isClient", false);
        vars.put("CurrentPlayerID", -1);
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();
        input.addAction(new DirectionControl.MoveUp(), KeyCode.UP);
        input.addAction(new DirectionControl.MoveDown(), KeyCode.DOWN);
        input.addAction(new DirectionControl.MoveLeft(), KeyCode.LEFT);
        input.addAction(new DirectionControl.MoveRight(), KeyCode.RIGHT);
        input.addAction(new GameControl.StartGame(), KeyCode.SPACE);
        input.addAction(new BehaviorControl.Attack(), KeyCode.A);
        input.addAction(new MouseControl.ChoosePlayer(), MouseButton.PRIMARY);
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
                if (NetworkUtils.isServer()) {
                    var typeA = a.getComponent(DetailedTypeComponent.class);
                    var typeB = b.getComponent(DetailedTypeComponent.class);
                    if (typeA.isEnemy(typeB)) {
                        int damage = a.getComponent(BulletComponent.class).getDamage();
                        b.getComponent(HealthComponent.class).decreaseHealth(damage);
                        a.removeFromWorld();
                    }
                }
            }
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        checkCurrentPlayer();
    }

    protected void checkCurrentPlayer() {
        int playerID = PropertyUtils.getCurrentPlayerID();
        if (playerID < 0) {
            // current player died
            currentPlayerID = -1;
        }
        else if (currentPlayerID != playerID) {
            if (currentPlayerID >= 0) {
                // remove icons
                EntityUtils.getEntityByNetworkID(currentPlayerID).get().getViewComponent().removeChild(playerIcon);
            }
            currentPlayerID = playerID;
            geneartePlayerIcon(EntityUtils.getEntityByNetworkID(playerID).get());
        }
    }

    protected void geneartePlayerIcon(Entity player) {
        int ICON_HEIGHT = 12, ICON_WIDTH = 10;
        var icon = new Polygon(0, 0, ICON_WIDTH, 0, ICON_WIDTH / 2, ICON_HEIGHT);
        icon.setLayoutY(-10 - ICON_HEIGHT - 3);
        icon.setLayoutX(player.getWidth() / 2 - ICON_WIDTH / 2);
        icon.setFill(Color.BLUE);
        player.getViewComponent().addChild(icon);
        playerIcon = icon;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
