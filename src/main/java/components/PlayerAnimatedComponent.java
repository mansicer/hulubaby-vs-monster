package components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import util.ComponentUtils;

public class PlayerAnimatedComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel animatedIdle;
    private final AnimationChannel animatedWalk;
    private final AnimationChannel animatedAttack;
    private MovableComponent movableComponent;
    private AttackComponent attackComponent;

    public PlayerAnimatedComponent(AnimationChannel animatedIdle, AnimationChannel animatedWalk, AnimationChannel animatedAttack) {
        this.animatedIdle = animatedIdle;
        this.animatedWalk = animatedWalk;
        this.animatedAttack = animatedAttack;
        texture = new AnimatedTexture(animatedIdle);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(entity.getWidth()/2, entity.getHeight()/2));
        entity.getViewComponent().addChild(texture);
        movableComponent = ComponentUtils.getMovableComponent(entity).get();
        attackComponent = ComponentUtils.getAttackComponent(entity).get();
    }

    @Override
    public void onUpdate(double tpf) {
        if (attackComponent.isAttacking()) {
            if (texture.getAnimationChannel() != animatedAttack) {
                texture.loopAnimationChannel(animatedAttack);
            }
        } else if (movableComponent.isMoving()) {
            if (texture.getAnimationChannel() != animatedWalk) {
                texture.loopAnimationChannel(animatedWalk);
            }
        }  else {
            if (texture.getAnimationChannel() != animatedIdle) {
                texture.loopAnimationChannel(animatedIdle);
            }
        }
    }
}
