package input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import components.ControllableComponent;
import util.EntityUtils;

public class DirectionControl {
    public static class MoveUp extends UserAction {
        public MoveUp() {
            super("Move Up");
        }

        @Override
        protected void onAction() {
            int playerID = FXGL.getWorldProperties().getInt("CurrentPlayerID");
            EntityUtils.getEntityByNetworkID(playerID).ifPresent(player -> {
                player.getComponent(ControllableComponent.class).moveUp();
            });
        }

        @Override
        protected void onActionEnd() {
            int playerID = FXGL.getWorldProperties().getInt("CurrentPlayerID");
            EntityUtils.getEntityByNetworkID(playerID).ifPresent(player -> {
                player.getComponent(ControllableComponent.class).stopY();
            });
        }
    }

    public static class MoveDown extends UserAction {
        public MoveDown() {
            super("Move Down");
        }

        @Override
        protected void onAction() {
            int playerID = FXGL.getWorldProperties().getInt("CurrentPlayerID");
            EntityUtils.getEntityByNetworkID(playerID).ifPresent(player -> {
                player.getComponent(ControllableComponent.class).moveDown();
            });
        }

        @Override
        protected void onActionEnd() {
            int playerID = FXGL.getWorldProperties().getInt("CurrentPlayerID");
            EntityUtils.getEntityByNetworkID(playerID).ifPresent(player -> {
                player.getComponent(ControllableComponent.class).stopY();
            });
        }
    }

    public static class MoveLeft extends UserAction {
        public MoveLeft() {
            super("Move Left");
        }

        @Override
        protected void onAction() {
            int playerID = FXGL.getWorldProperties().getInt("CurrentPlayerID");
            EntityUtils.getEntityByNetworkID(playerID).ifPresent(player -> {
                player.getComponent(ControllableComponent.class).moveLeft();
            });
        }

        @Override
        protected void onActionEnd() {
            int playerID = FXGL.getWorldProperties().getInt("CurrentPlayerID");
            EntityUtils.getEntityByNetworkID(playerID).ifPresent(player -> {
                player.getComponent(ControllableComponent.class).stopX();
            });
        }
    }

    public static class MoveRight extends UserAction {
        public MoveRight() {
            super("Move Right");
        }

        @Override
        protected void onAction() {
            int playerID = FXGL.getWorldProperties().getInt("CurrentPlayerID");
            EntityUtils.getEntityByNetworkID(playerID).ifPresent(player -> {
                player.getComponent(ControllableComponent.class).moveRight();
            });
        }

        @Override
        protected void onActionEnd() {
            int playerID = FXGL.getWorldProperties().getInt("CurrentPlayerID");
            EntityUtils.getEntityByNetworkID(playerID).ifPresent(player -> {
                player.getComponent(ControllableComponent.class).stopX();
            });
        }
    }
}
