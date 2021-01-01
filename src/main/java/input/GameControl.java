package input;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.ui.MDIWindow;
import components.NetworkIDComponent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import util.EntityUtils;
import util.NetworkUtils;
import util.ZipUtils;

import java.io.File;

public class GameControl {
    public static class StartGame extends UserAction {
        public StartGame() {
            super("Start Game");
        }

        @Override
        protected void onActionBegin() {
            if (FXGL.getWorldProperties().getBoolean("isServer")) {
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
    }

    public static class RecordGame extends UserAction {
        public RecordGame(){
            super("Record Game");
        }

        @Override
        protected void onActionBegin() {
            FXGL.set("record",!FXGL.getb("record"));
        }
    }

    public static class SaveRecord extends UserAction{

        public SaveRecord() {
            super("Save Record");
        }

        @Override
        protected void onActionBegin() {
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
            FXGL.getGameController().pauseEngine();
        }
    }
}
