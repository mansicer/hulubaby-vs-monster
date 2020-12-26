package input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import components.ControllableComponent;
import javafx.geometry.Rectangle2D;
import util.EntityUtils;
import util.PropertyUtils;

import java.util.Optional;

public class MouseControl {
    public static class ChoosePlayer extends UserAction {
        public ChoosePlayer() {
            super("Choose Player");
        }

        @Override
        protected void onActionBegin() {
            Input input = FXGL.getInput();
            var position = input.getMousePositionWorld();
            var range = new Rectangle2D(position.getX() - 6, position.getY() - 10, 12, 20);
            var entities = FXGL.getGameWorld().getEntitiesInRange(range);

            // TODO: choose the closest one, add choose rules by CampType
            for (Entity entity : entities) {
                if (entity.hasComponent(ControllableComponent.class)) {
                    int id = EntityUtils.getNetworkID(entity);
                    PropertyUtils.setCurrentPlayerID(id);
                    break;
                }
            }
        }
    }
}
