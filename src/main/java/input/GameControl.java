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
                // spawn Hulubabies
                Entity entity = FXGL.spawn("Dawa", 0, 10);
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

                Entity enemy = FXGL.spawn("Snake1", (double) FXGL.getAppWidth() * 3 / 4,  0);
                FXGL.spawn("MonsterSoldier1", (double) FXGL.getAppWidth() * 3 / 4, (double) FXGL.getAppHeight() / 2 - 60);
                FXGL.spawn("MonsterSoldier1", (double) FXGL.getAppWidth() * 3 / 4, (double) FXGL.getAppHeight() / 2);
                FXGL.spawn("MonsterSoldier1", (double) FXGL.getAppWidth() * 3 / 4, (double) FXGL.getAppHeight() / 2 + 60);
                FXGL.spawn("MonsterSoldier2", (double) FXGL.getAppWidth() * 3 / 4 + 60, (double) FXGL.getAppHeight() / 2);

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
