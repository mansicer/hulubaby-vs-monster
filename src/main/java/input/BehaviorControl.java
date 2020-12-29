package input;

import com.almasb.fxgl.input.UserAction;
import util.ComponentUtils;
import util.EntityUtils;
import util.PropertyUtils;

public class BehaviorControl {
    public static class Attack extends UserAction {
        public Attack() {
            super("Attack");
        }

        @Override
        protected void onActionBegin() {
            int playerID = PropertyUtils.getCurrentPlayerID();
            EntityUtils.getEntityByNetworkID(playerID).ifPresent(entity -> {
                ComponentUtils.getAttackComponent(entity).get().attack();
            });
        }
    }
}
