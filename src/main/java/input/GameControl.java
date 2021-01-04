package input;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import util.EntityUtils;
import util.GameUtils;
import util.NetworkUtils;
import util.PropertyUtils;

import java.io.File;

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

                NetworkUtils.getServer().getConnections().forEach(connection -> {
                    PropertyUtils.setOpponentPlayerID(EntityUtils.getNetworkID(enemy));
                    Bundle message = new Bundle("PlayerAllocation");
                    message.put("playerID", EntityUtils.getNetworkID(enemy));
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
            if(!FXGL.getb("replay")) {
                FXGL.set("record", !FXGL.getb("record"));
            }
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

    public static class PauseReplay extends UserAction{

        public PauseReplay() {
            super("Pause Replay");
        }

        @Override
        protected void onActionBegin() {
            FXGL.set("pause",!FXGL.getb("pause"));
        }
    }

    public static class BackReplay extends UserAction{

        public BackReplay() {
            super("Back Replay");
        }

        @Override
        protected void onActionBegin() {
            if(FXGL.geti("now")-125>=0) {
                FXGL.set("now", FXGL.geti("now")-125);
            }
            else{
                FXGL.set("now",0);
            }
        }
    }

    public static class ForwardReplay extends UserAction{

        public ForwardReplay() {
            super("Forward Replay");
        }

        @Override
        protected void onActionBegin() {
            File f = new File("./temp_replay_hulubrother");
            File[] files = f.listFiles();
            int now = FXGL.geti("now");
            if(now+125< files.length-1) {
                for(int i=now;i<now+125;i++){
                    FXGL.getSaveLoadService().readAndLoadTask(files[i].toString().split("\\\\",2)[1]).run();
                }
                FXGL.set("now", FXGL.geti("now")+125);
            }
            else{
                FXGL.set("now",files.length-1);
            }
        }
    }
}
