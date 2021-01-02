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
import util.GameUtils;
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
            GameUtils.recordUI();
        }
    }


}
