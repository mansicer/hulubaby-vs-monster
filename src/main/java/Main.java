import com.almasb.fxgl.animation.AnimatedValue;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.collection.PropertyChangeListener;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.SerializableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Server;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import com.almasb.fxgl.ui.MDIWindow;
import components.*;
import config.Config;
import input.BehaviorControl;
import input.DirectionControl;
import input.GameControl;
import input.MouseControl;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import menu.GameMenu;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import service.MultiplayerConnectionService;
import service.SocketClient;
import service.SocketService;
import types.BasicEntityTypes;
import util.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;



public class Main extends GameApplication {
    private int currentPlayerID = -1;
    private Polygon playerIcon;

    @Override
    protected void onPreInit() {
        File f = new File("./temp_record_hulubrother");
        if(f.exists()){
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
            f.mkdir();
        }
        FXGL.getSaveLoadService().addHandler(new SaveLoadHandler() {
            @Override
            public void onSave(@NotNull DataFile dataFile) {
                Bundle bundle = new Bundle("gameData");

                FXGL.getGameWorld().getEntities().forEach(entity -> {
                    Bundle entity_bd = new Bundle("");
                    NetworkIDComponent networkIDComponent = entity.getComponent(NetworkIDComponent.class);
                    var networkID = networkIDComponent.getId();
                    String spawnName = networkIDComponent.getSpawnName();
                    if(entity.isActive()) {
                        entity.getComponents().forEach(component -> {
                            if (ComponentUtils.checkComponentUpdatable(component)) {
                                ComponentUtils.packUpComponentBundle(entity_bd, (SerializableComponent) component);
                            }
                        });
                        entity_bd.put("position",new Vec2(entity.getPosition()));
                        entity_bd.put("spawnName",spawnName);
                        Bundle spawnData = new Bundle("");
                        entity.getComponent(SpawnDataComponent.class).write(spawnData);
                        entity_bd.put("spawnData",spawnData);
                        bundle.put(Integer.toString(networkID),entity_bd);
                    }
                });
                ArrayList<Integer> removeIDs = FXGL.geto("removeIDs");
                ArrayList<Integer> removeIDsCopy = (ArrayList<Integer>) removeIDs.clone();
                bundle.put("removeIDs",removeIDsCopy);
//                System.err.println(removeIDs.size());
                Bundle vars = new Bundle("");
//                vars.put("isServer",FXGL.getb("isServer"));
//                vars.put("isClient",FXGL.getb("isClient"));
                vars.put("CurrentPlayerID",FXGL.geti("CurrentPlayerID"));
                bundle.put("vars",vars);
                dataFile.putBundle(bundle);
                removeIDs.clear();
            }

            @Override
            public void onLoad(@NotNull DataFile dataFile) {
                Bundle bundle = dataFile.getBundle("gameData");
                bundle.getData().forEach((name, value) -> {
                    if(name.equals("removeIDs")){
                        ArrayList<Integer> removeIDs = (ArrayList<Integer>) value;
                        for(int id:removeIDs){
                            System.err.println(id);
                            EntityUtils.getEntityByNetworkID(id).get().removeFromWorld();
                        }
                    }
                    else if(name.equals("vars")){
                        Bundle vars = bundle.get("vars");
                        vars.getData().forEach(FXGL::set);
                    }
                    else{
                        int networkID = Integer.parseInt(name);
                        Bundle bd = (Bundle) value;
                        var entities = FXGL.getGameWorld().getEntitiesByComponent(NetworkIDComponent.class).stream().filter(entity ->
                                entity.getComponent(NetworkIDComponent.class).getId() == networkID
                        );
                        var ret = entities.findFirst();
                        if (ret.isEmpty()) {
                            String spawnName = bd.get("spawnName");
                            System.out.println(spawnName);
                            Vec2 position = bd.get("position");
                            SpawnData spawnData = new SpawnData(position.x,position.y);
                            Bundle spawnBundle = bd.get("spawnData");
                            spawnData.getData().putAll(spawnBundle.getData());
                            Entity spawn = FXGL.getGameWorld().spawn(spawnName, spawnData);
                            spawn.getComponents().forEach(component -> {
                                if (ComponentUtils.checkComponentUpdatable(component)) {
                                    ComponentUtils.unpackComponentBundle(bd, (SerializableComponent) component);
                                }
                            });
                            spawn.getComponent(NetworkIDComponent.class).setId(networkID);
                        }
                        else {
                            Optional<Entity> entityByNetworkID = EntityUtils.getEntityByNetworkID(networkID);
                            entityByNetworkID.ifPresent(entity -> {
                                Vec2 position = bd.get("position");
                                entity.setPosition(position);
                                entity.getComponents().forEach(component -> {
                                    if (ComponentUtils.checkComponentUpdatable(component)) {
                                        ComponentUtils.unpackComponentBundle(bd, (SerializableComponent) component);
                                    }
                                });
                            });
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void initUI() {
        HBox hBox = new HBox();

        Circle circle = new Circle();
        circle.setRadius(5);
        circle.fillProperty().setValue(Color.RED);

        FXGL.getGameTimer().runAtInterval(()->{
            circle.setVisible(!circle.isVisible());
        },Duration.seconds(0.8));
        Text text = FXGL.getUIFactoryService().newText("录制中",Color.RED,10);
        text.setTranslateX(5);
        hBox.getChildren().addAll(circle,text);
        hBox.setBackground(Background.EMPTY);
        hBox.setLayoutX(20);
        hBox.setLayoutY(20);
        hBox.visibleProperty().bind(FXGL.getbp("record"));
        FXGL.addUINode(hBox);
    }

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
        Properties props = LoadConfigUtils.getProps();
        String isserver = (String)props.getOrDefault("isServer","false");
        String isclient = (String)props.getOrDefault("isClient","false");
        System.out.println(isserver);
        System.out.println(isclient);
        boolean isServer = Boolean.parseBoolean(isserver);
        boolean isClient= Boolean.parseBoolean(isclient);
        System.out.println(isServer);
        System.out.println(isClient);
        FXGL.getWorldProperties().setValue("isServer",isServer);
        FXGL.getWorldProperties().setValue("isClient",isClient);
        System.err.println(FXGL.getb("isServer"));
        if (isServer) {
            try {
                new SocketService().serverConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Server<Bundle> server = FXGL.getNetService().newUDPServer(Config.GameNetworkPort);
            server.startAsync();
            FXGL.getWorldProperties().setValue("server", server);
            server.setOnConnected(bundleConnection -> {
                NetworkUtils.getMultiplayerService().addInputReplicationReceiver(bundleConnection);
            });
            try {
                TimeUnit.SECONDS.sleep(2);
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
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Client<Bundle> client = FXGL.getNetService().newUDPClient(props.getProperty("ip"), Config.GameNetworkPort);
            client.connectAsync();
            FXGL.getWorldProperties().setValue("client", client);
            client.setOnConnected(bundleConnection -> {
                NetworkUtils.getMultiplayerService().addEntityReplicationReceiver(bundleConnection, FXGL.getGameWorld());
                NetworkUtils.getMultiplayerService().addInputReplicationSender(bundleConnection, FXGL.getGameWorld());
            });
        }
        if (isServer) {
            Entity entity = FXGL.spawn("TestCharacter1", FXGL.getAppWidth() / 3, 0);
            FXGL.spawn("Dawa", FXGL.getAppWidth() / 3, FXGL.getAppHeight() * 2 / 3);
            Entity enemy = FXGL.spawn("TestCharacter1-Enemy", FXGL.getAppWidth() * 2 / 3, FXGL.getAppHeight() / 3);
            FXGL.spawn("TestCharacter1-Enemy", FXGL.getAppWidth() * 2 / 3, FXGL.getAppHeight() - 50);

            PropertyUtils.setCurrentPlayerID(EntityUtils.getNetworkID(entity));

            NetworkUtils.getServer().getConnections().forEach(connection ->  {
                PropertyUtils.setOpponentPlayerID(EntityUtils.getNetworkID(enemy));
                Bundle message = new Bundle("PlayerAllocation");
                message.put("playerID", EntityUtils.getNetworkID(enemy));
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
        vars.put("isServer", false);
        vars.put("isClient", false);
        vars.put("CurrentPlayerID", -1);
        vars.put("OpponentPlayerID", -1);
        vars.put("record",false);
        Properties props = LoadConfigUtils.getProps();
        boolean replay = Boolean.parseBoolean((String) props.getOrDefault("record","false"));
        vars.put("replay",replay);
        File f = new File("./temp_replay_hulubrother");
        if(!f.exists())
            f.mkdir();
        vars.put("files",f.listFiles());
        vars.put("now",0);
        if(!replay){
            vars.put("server", Optional.empty());
            vars.put("client", Optional.empty());
        }
        vars.put("removeIDs",new ArrayList<Integer>());
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();
        input.addAction(new DirectionControl.MoveUp(), KeyCode.UP);
        input.addAction(new DirectionControl.MoveDown(), KeyCode.DOWN);
        input.addAction(new DirectionControl.MoveLeft(), KeyCode.LEFT);
        input.addAction(new DirectionControl.MoveRight(), KeyCode.RIGHT);
        input.addAction(new GameControl.RecordGame(), KeyCode.SPACE);
        input.addAction(new BehaviorControl.Attack(), KeyCode.A);
        input.addAction(new MouseControl.ChoosePlayer(), MouseButton.PRIMARY);
        input.addAction(new GameControl.SaveRecord(), KeyCode.Z);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(BasicEntityTypes.BULLET, BasicEntityTypes.PLAYER) {
            @Override
            protected void onCollision(Entity a, Entity b) {
                if (NetworkUtils.isServer()) {
                    if (EntityUtils.isEnemy(a, b)) {
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
        if(FXGL.getb("record")){
            String path = "./temp_record_hulubrother/";
            String time = Long.toString(System.currentTimeMillis());
            String filename = path+time+".sav";
            FXGL.getSaveLoadService().saveAndWriteTask(filename).run();
        }
        else if(FXGL.getb("replay")){
            File[] files = FXGL.geto("files");
            int now = FXGL.geti("now");
            FXGL.getSaveLoadService().readAndLoadTask(files[now].toString().split("\\\\",2)[1]).run();
            if(now< files.length -1){
                FXGL.set("now",now+1);
            }
        }
        if (NetworkUtils.isServer()) {
            checkAI();
        }
    }

    protected void checkAI() {
        List<Entity> controllableEntites = FXGL.getGameWorld().getEntitiesByComponent(ControllableComponent.class);
        for (Entity entity : controllableEntites) {
            int id = EntityUtils.getNetworkID(entity);
            if (id != PropertyUtils.getCurrentPlayerID() && id != PropertyUtils.getOpponentPlayerID()) {
                entity.getComponent(AIComponent.class).setAIActive(true);
            }
            else {
                entity.getComponent(AIComponent.class).setAIActive(false);
            }
        }
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
