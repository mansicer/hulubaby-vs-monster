package input;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import components.NetworkIDComponent;
import util.EntityUtils;
import util.NetworkUtils;

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
}
