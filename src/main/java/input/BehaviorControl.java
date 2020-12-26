package input;

import com.almasb.fxgl.input.UserAction;

public class BehaviorControl {
    public static class Attack extends UserAction {
        public Attack() {
            super("Attack");
        }

        @Override
        protected void onActionBegin() {
            // TODO: attack
        }
    }
}
