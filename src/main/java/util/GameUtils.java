package util;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Server;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.MDIWindow;
import components.ControllableComponent;
import components.DetailedTypeComponent;
import config.Config;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.apache.commons.io.FileUtils;
import service.SocketClient;
import service.SocketService;
import types.CampType;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class GameUtils {
    public static boolean detectGameOver(){
        boolean isEnd=true;
        List<Entity> entities = FXGL.getGameWorld().getEntitiesByComponent(ControllableComponent.class);
        AtomicReference<Entity> last = new AtomicReference<>(null);
        if(entities.size()==0){
            return false;
        }
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).isActive()){
                if(last.get() == null){
                    last.set(entities.get(i));
                }
                else{
                    boolean b = last.get().getComponent(DetailedTypeComponent.class).getCampType() == entities.get(i).getComponent(DetailedTypeComponent.class).getCampType();
                    isEnd &= b;
                    last.set(entities.get(i));
                }
            }
        }
        return isEnd;
    }

    public static boolean isWin(){
        ArrayList<Entity> entities = FXGL.getGameWorld().getEntities();
        Optional<Entity> first = entities.stream().findFirst();
        if(first.isEmpty()){
            return false;
        }
        else{
            if(FXGL.getb("isServer")) {
                return EntityUtils.getCampType(first.get()) == FXGL.geto("campType");
            }
            else if (first.isPresent()) {
                return EntityUtils.getCampType(first.get()) == FXGL.geto("opponentCampType");
            }
        }
        return false;
    }

    public static void recordUI(){
        MDIWindow mdiWindow = FXGL.getUIFactoryService().newWindow();
        mdiWindow.setPrefSize(350,250);
        mdiWindow.setTranslateX(FXGL.getAppWidth()/3);
        mdiWindow.setTranslateY(FXGL.getAppHeight()/3);
        mdiWindow.setTitle("存档");
        mdiWindow.setCanResize(false);
        mdiWindow.setCanClose(false);
        Button button = FXGL.getUIFactoryService().newButton("test");
        button.setOnAction(e->{
            FXGL.getGameScene().getRoot().getChildren().remove(mdiWindow);
            FXGL.getGameController().resumeEngine();
        });

        TextField textField = new TextField();
        textField.setOnAction(actionEvent -> {
            if(textField.getText().isBlank()){

            }
            else{
                File file = new File("./temp_record_hulubrother");
                if(file.exists()&&file.listFiles().length>0){
                    ZipUtils.zip("./temp_record_hulubrother",textField.getText());
                    FileUtils.deleteQuietly(new File("./temp_record_hulubrother"));
                    FXGL.getGameScene().getRoot().getChildren().remove(mdiWindow);
                    FXGL.getGameController().resumeEngine();
                }
                else{
                    FXGL.getDialogService().showErrorBox("无存档",()->{});
                }
            }
        });
        Text text = FXGL.getUIFactoryService().newText("存档名字", Color.WHITE,40);
        Rectangle rec = new Rectangle();
        rec.setWidth(100);
        rec.setHeight(40);
        rec.setArcWidth(20);
        rec.setArcHeight(20);

        Button confirm = FXGL.getUIFactoryService().newButton("确定");
        confirm.setShape(rec);
        confirm.setMaxWidth(rec.getWidth());
        confirm.setMaxHeight(rec.getHeight());
        confirm.setOnAction(actionEvent -> {
            if(!textField.getText().isBlank()){
                ZipUtils.zip("./temp_record_hulubrother",textField.getText());
                FileUtils.deleteQuietly(new File("./temp_record_hulubrother"));
                FXGL.getGameScene().getRoot().getChildren().remove(mdiWindow);
                FXGL.getGameController().resumeEngine();
            }
        });

        Button cancel = FXGL.getUIFactoryService().newButton("取消");
        cancel.setShape(rec);
        cancel.setMaxWidth(rec.getWidth());
        cancel.setMaxHeight(rec.getHeight());
        cancel.setOnAction(actionEvent -> {
            FXGL.getGameScene().getRoot().getChildren().remove(mdiWindow);
            FXGL.getGameController().resumeEngine();
        });

        HBox buttons = new HBox(15,
                confirm,
                cancel);

        VBox vBox = new VBox(15,
                text,
                textField,
                buttons
        );
        vBox.setAlignment(Pos.CENTER);
        vBox.setTranslateX(60);
        vBox.setTranslateY(20);
        Pane pane = new Pane();
        pane.getStylesheets().add("style/textField.css");
        pane.getChildren().addAll(vBox);
        pane.setStyle("-fx-alignment: center");
        mdiWindow.setContentPane(pane);
        FXGL.getGameScene().getRoot().getStylesheets().add("style/textField.css");
        FXGL.getGameScene().getRoot().getChildren().add(mdiWindow);
//        FXGL.getGameController().pauseEngine();
    }

    public static boolean detectRecord(){
        File f = new File("./temp_record_hulubrother");
        if(f.exists()&&f.listFiles().length>0&&!FXGL.getb("replay")){
            return true;
        }
        else{
            return false;
        }
    }

    public static void gameOverUI(boolean winOrLose){
        VBox show = null;
        Texture win = new Texture(FXGL.image("win.png"));
        Texture lose = new Texture(FXGL.image("lose.png"));

        Rectangle rec = new Rectangle();
        rec.setWidth(180);
        rec.setHeight(40);
        rec.setArcWidth(20);
        rec.setArcHeight(20);

        Button exit = FXGL.getUIFactoryService().newButton("退出");
        exit.setMaxHeight(rec.getHeight());
        exit.setMaxWidth(rec.getWidth());
        exit.setShape(rec);
        exit.setTextAlignment(TextAlignment.CENTER);
        exit.setOnAction(actionEvent -> {
            File f = new File("./src/config.properties");
            if(f.exists()){
                f.delete();
            }
            FXGL.getGameController().exit();
        });
        Button goToMenu = FXGL.getUIFactoryService().newButton("返回菜单");
        goToMenu.setMaxHeight(rec.getHeight());
        goToMenu.setMaxWidth(rec.getWidth());
        goToMenu.setShape(rec);
        goToMenu.setTextAlignment(TextAlignment.CENTER);
        goToMenu.setOnAction(actionEvent -> {
            int size = FXGL.getGameScene().getRoot().getChildren().size();
            FXGL.getGameScene().getRoot().getChildren().remove(size-1);
            if(FXGL.getb("isServer")) {
                Server<Bundle> server = NetworkUtils.getServer();
                server.stop();
            }
            if(FXGL.getb("isClient")){
                Client<Bundle> client = NetworkUtils.getClient();
                client.disconnect();
            }
            NetworkUtils.getMultiplayerService().clearReplicatedEntitiesMap();
//            FXGL.getWorldProperties().clear();
            FXGL.getGameController().gotoMainMenu();
        });
        HBox choiceButton = new HBox(20,
                exit,
                goToMenu);

        if(winOrLose) {
            show = new VBox(25,
                    win,
                    choiceButton);
            show.setTranslateX(FXGL.getAppWidth() / 3);
            show.setTranslateY(FXGL.getAppHeight() / 4);
            show.setAlignment(Pos.CENTER);
            System.out.println("win");
        }
        else {
            show = new VBox(25,
                    lose,
                    choiceButton);
            show.setTranslateX(FXGL.getAppWidth() / 3);
            show.setTranslateY(FXGL.getAppHeight() / 4);
            show.setAlignment(Pos.CENTER);
            System.out.println("lose");
        }

        if (GameUtils.detectRecord()) {
            VBox finalShow = show;
            FXGL.getDialogService().showConfirmationBox("检测到存档，是否要保存？", answer -> {
                if (answer) {
                    GameUtils.recordUI();
                }
                finalShow.setViewOrder(1);
                FXGL.getGameScene().getRoot().getChildren().addAll(finalShow);
            });
        }
        else{
            FXGL.getGameScene().getRoot().getChildren().addAll(show);
        }
    }

    public static void initConnection(){
        Properties props = LoadConfigUtils.getProps();
        String isserver = (String)props.getOrDefault("isServer","false");
        String isclient = (String)props.getOrDefault("isClient","false");
//        System.out.println(isserver);
//        System.out.println(isclient);
        boolean isServer = Boolean.parseBoolean(isserver);
        boolean isClient= Boolean.parseBoolean(isclient);
//        System.out.println(isServer);
//        System.out.println(isClient);
        FXGL.getWorldProperties().setValue("isServer",isServer);
        FXGL.getWorldProperties().setValue("isClient",isClient);
//        System.err.println(FXGL.getb("isServer"));
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
    }

    public static void initEntity(){
        if (FXGL.getb("isServer")) {
            boolean b = new Random().nextBoolean();
            Entity entity = null;
            FXGL.spawn("Dawa", FXGL.getAppWidth() / 3, FXGL.getAppHeight() * 2 / 3);
            Entity enemy = null;
            if (b) {
                FXGL.set("campType", CampType.HuluBabyCamp);
                FXGL.set("opponentCampType",CampType.MonsterCamp);

                entity = FXGL.spawn("Dawa", 0, 10);
                FXGL.spawn("Erwa", 0,  (double) FXGL.getAppHeight() / 7);
                FXGL.spawn("Sanwa", 0,  (double) FXGL.getAppHeight() * 2 / 7);
                FXGL.spawn("Siwa", 0,  (double) FXGL.getAppHeight() * 3 / 7);
                FXGL.spawn("Wuwa", 0,  (double) FXGL.getAppHeight() * 4 / 7);
                FXGL.spawn("Liuwa", 0,  (double) FXGL.getAppHeight() * 5 / 7);
                FXGL.spawn("Qiwa", 0,  (double) FXGL.getAppHeight() * 6 / 7);

                FXGL.spawn("HuluSoldier1", (double) FXGL.getAppWidth() / 4, (double) FXGL.getAppHeight() / 2 - 60);
                FXGL.spawn("HuluSoldier1", (double) FXGL.getAppWidth() / 4, (double) FXGL.getAppHeight() / 2);
                FXGL.spawn("HuluSoldier1", (double) FXGL.getAppWidth() / 4, (double) FXGL.getAppHeight() / 2 + 60);
                FXGL.spawn("HuluSoldier2", (double) FXGL.getAppWidth() / 4 - 60, (double) FXGL.getAppHeight() / 2);

                enemy = FXGL.spawn("Snake1", (double) FXGL.getAppWidth() * 3 / 4,  0);
                FXGL.spawn("MonsterSoldier1", (double) FXGL.getAppWidth() * 3 / 4, (double) FXGL.getAppHeight() / 2 - 60);
                FXGL.spawn("MonsterSoldier1", (double) FXGL.getAppWidth() * 3 / 4, (double) FXGL.getAppHeight() / 2);
                FXGL.spawn("MonsterSoldier1", (double) FXGL.getAppWidth() * 3 / 4, (double) FXGL.getAppHeight() / 2 + 60);
                FXGL.spawn("MonsterSoldier2", (double) FXGL.getAppWidth() * 3 / 4 + 60, (double) FXGL.getAppHeight() / 2);
            }
            else{
                FXGL.set("campType", CampType.MonsterCamp);
                FXGL.set("opponentCampType",CampType.HuluBabyCamp);

                enemy = FXGL.spawn("Dawa", 0, 10);
                FXGL.spawn("Erwa", 0,  (double) FXGL.getAppHeight() / 7);
                FXGL.spawn("Sanwa", 0,  (double) FXGL.getAppHeight() * 2 / 7);
                FXGL.spawn("Siwa", 0,  (double) FXGL.getAppHeight() * 3 / 7);
                FXGL.spawn("Wuwa", 0,  (double) FXGL.getAppHeight() * 4 / 7);
                FXGL.spawn("Liuwa", 0,  (double) FXGL.getAppHeight() * 5 / 7);
                FXGL.spawn("Qiwa", 0,  (double) FXGL.getAppHeight() * 6 / 7);

                FXGL.spawn("HuluSoldier1", (double) FXGL.getAppWidth() / 4, (double) FXGL.getAppHeight() / 2 - 60);
                FXGL.spawn("HuluSoldier1", (double) FXGL.getAppWidth() / 4, (double) FXGL.getAppHeight() / 2);
                FXGL.spawn("HuluSoldier1", (double) FXGL.getAppWidth() / 4, (double) FXGL.getAppHeight() / 2 + 60);
                FXGL.spawn("HuluSoldier2", (double) FXGL.getAppWidth() / 4 - 60, (double) FXGL.getAppHeight() / 2);

                entity = FXGL.spawn("Snake1", (double) FXGL.getAppWidth() * 3 / 4,  0);
                FXGL.spawn("MonsterSoldier1", (double) FXGL.getAppWidth() * 3 / 4, (double) FXGL.getAppHeight() / 2 - 60);
                FXGL.spawn("MonsterSoldier1", (double) FXGL.getAppWidth() * 3 / 4, (double) FXGL.getAppHeight() / 2);
                FXGL.spawn("MonsterSoldier1", (double) FXGL.getAppWidth() * 3 / 4, (double) FXGL.getAppHeight() / 2 + 60);
                FXGL.spawn("MonsterSoldier2", (double) FXGL.getAppWidth() * 3 / 4 + 60, (double) FXGL.getAppHeight() / 2);
            }
            PropertyUtils.setCurrentPlayerID(EntityUtils.getNetworkID(entity));
            Entity finalEnemy = enemy;
            NetworkUtils.getServer().getConnections().forEach(connection ->  {
                PropertyUtils.setOpponentPlayerID(EntityUtils.getNetworkID(finalEnemy));
                Bundle message = new Bundle("PlayerAllocation");
                message.put("playerID", EntityUtils.getNetworkID(finalEnemy));
                NetworkUtils.getMultiplayerService().sendMessage(connection, message);
            });

        }
    }
}
