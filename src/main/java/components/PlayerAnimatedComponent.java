package components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import util.ComponentUtils;

public class PlayerAnimatedComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimatedTexture attackAnimation;
    private final AnimationChannel animatedIdle;
    private final AnimationChannel animatedWalk;
    private final AnimationChannel animatedAttack;
    private final AnimationChannel attackEffect;
    private MovableComponent movableComponent;
    private AttackComponent attackComponent;

    public PlayerAnimatedComponent(AnimationChannel animatedIdle, AnimationChannel animatedWalk, AnimationChannel animatedAttack, AnimationChannel attackEffect) {
        this.animatedIdle = animatedIdle;
        this.animatedWalk = animatedWalk;
        this.animatedAttack = animatedAttack;
        this.attackEffect = attackEffect;

        texture = new AnimatedTexture(animatedIdle);
        if(attackEffect!=null) {
            attackAnimation = new AnimatedTexture(attackEffect);
            attackAnimation.setVisible(false);
        }
        else
            attackAnimation = null;
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(entity.getWidth()/2, entity.getHeight()/2));
        entity.getViewComponent().addChild(texture);
        if(attackAnimation!=null) {
            attackAnimation.setX(DawaConfig.attackRangeWidth * 1.5);
            attackAnimation.setY(DawaConfig.attackRangeHeight / 2);
            entity.getViewComponent().addChild(attackAnimation);
        }
        movableComponent = ComponentUtils.getMovableComponent(entity).get();
        attackComponent = ComponentUtils.getAttackComponent(entity).get();
    }

    @Override
    public void onUpdate(double tpf) {
        if (attackComponent.isAttacking()) {
            if (texture.getAnimationChannel() != animatedAttack) {
                texture.loopAnimationChannel(animatedAttack);
                if(attackAnimation!=null) {
                    attackAnimation.setVisible(true);
                    attackAnimation.play();
                }
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
