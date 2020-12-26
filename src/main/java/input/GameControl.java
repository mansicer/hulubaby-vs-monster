package input;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Server;
import components.NetworkIDComponent;
import service.MultiplayerConnectionService;

public class GameControl {
    public static class StartGame extends UserAction {
        public StartGame() {
            super("Start Game");
        }

        @Override
        protected void onActionBegin() {
            if (FXGL.getWorldProperties().getBoolean("isServer")) {
                Entity entity = FXGL.spawn("TestCharacter1", FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.getWorldProperties().setValue("CurrentPlayerID", entity.getComponent(NetworkIDComponent.class).getId());
            } else {
                FXGL.getWorldProperties().setValue("CurrentPlayerID", 0);
            }
        }
    }
}
