package input;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import util.EntityUtils;
import util.NetworkUtils;
import util.PropertyUtils;

public class GameControl {
    public static class StartGame extends UserAction {
        public StartGame() {
            super("Start Game");
        }

        @Override
        protected void onActionBegin() {
            if (FXGL.getWorldProperties().getBoolean("isServer")) {
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
    }
}
