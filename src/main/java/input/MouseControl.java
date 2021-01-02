package input;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import components.ControllableComponent;
import javafx.geometry.Rectangle2D;
import types.CampType;
import util.EntityUtils;
import util.NetworkUtils;
import util.PropertyUtils;

import java.util.Optional;

public class MouseControl {
    public static class ChoosePlayer extends UserAction {
        public ChoosePlayer() {
            super("Choose Player");
        }

        @Override
        protected void onActionBegin() {
            if (NetworkUtils.isServer()) {
                Input input = FXGL.getInput();
                var position = input.getMousePositionWorld();
                var range = new Rectangle2D(position.getX() - 6, position.getY() - 10, 12, 20);
                var entities = FXGL.getGameWorld().getEntitiesInRange(range);

                // TODO: choose the closest one, add choose rules by CampType
                for (Entity entity : entities) {
                    if (entity.hasComponent(ControllableComponent.class) && EntityUtils.getCampType(entity).equals(FXGL.geto("campType"))) {
                        int id = EntityUtils.getNetworkID(entity);
                        PropertyUtils.setCurrentPlayerID(id);
                        break;
                    }
                }
            }
            if (NetworkUtils.isClient()) {
                NetworkUtils.getClient().getConnections().forEach(connection -> {
                    Bundle bundle = new Bundle("Action: Choose Player");
                    Input input = FXGL.getInput();
                    bundle.put("mousePositionX", input.getMouseXWorld());
                    bundle.put("mousePositionY", input.getMouseYWorld());
                    NetworkUtils.getMultiplayerService().sendMessage(connection, bundle);
                });
            }
        }
    }
}
