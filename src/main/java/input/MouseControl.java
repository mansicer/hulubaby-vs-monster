package input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import components.ControllableComponent;
import javafx.geometry.Rectangle2D;

import java.util.Optional;

public class MouseControl {
    public static class ChoosePlayer extends UserAction {
        public ChoosePlayer() {
            super("Choose Player");
        }

        @Override
        protected void onActionBegin() {
            // TODO: choose player
//            Input input = FXGL.getInput();
//            var position = input.getMousePositionWorld();
//            var range = new Rectangle2D(position.getX() - 6, position.getY() - 10, 12, 20);
//            var entities = FXGL.getGameWorld().getEntitiesInRange(range);
//
//            // TODO: choose the closest one
//            for (Entity entity : entities) {
//                if (entity.getComponentOptional(ControllableComponent.class).isPresent()) {
//                    if (currentPlayer.isPresent() && currentPlayer.get().isActive()) {
//                        playerIcon.ifPresent(polygon -> currentPlayer.get().getViewComponent().removeChild(polygon));
//                    }
//                    currentPlayer = Optional.of(entity);
//                    geneartePlayerIcon(currentPlayer.get());
//                    break;
//                }
//            }
        }
    }
}
