package components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import util.ComponentUtils;

import java.util.Optional;

public class AnimatedComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel animatedIdle;
    private final AnimationChannel animatedWalk;
    private Optional<MovableComponent> movableComponent = Optional.empty();

    public AnimatedComponent(AnimationChannel animatedIdle, AnimationChannel animatedWalk) {
        this.animatedIdle = animatedIdle;
        this.animatedWalk = animatedWalk;
        texture = new AnimatedTexture(animatedIdle);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(entity.getWidth()/2, entity.getHeight()/2));
        entity.getViewComponent().addChild(texture);
        movableComponent = ComponentUtils.getMovableComponent(entity);
    }

    @Override
    public void onUpdate(double tpf) {
        if (movableComponent.isPresent()) {
            if (movableComponent.get().isMoving()) {
                if (texture.getAnimationChannel() != animatedWalk) {
                    texture.loopAnimationChannel(animatedWalk);
                }
            } else {
                if (texture.getAnimationChannel() != animatedIdle) {
                    texture.loopAnimationChannel(animatedIdle);
                }
            }
        }
    }
}
