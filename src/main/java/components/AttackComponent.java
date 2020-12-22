package components;

import com.almasb.fxgl.dsl.FXGL;
import javafx.util.Duration;
import util.ComponentUtils;

import java.util.Optional;

public class AttackComponent extends OperableComponent {
    protected double attackAnimationTime = 0.3;
    protected double attackBackSwingTime = 0.2;
    protected int damage = 0;
    protected Optional<ControllableComponent> controllableComponent;

    AttackComponent(double attackAnimationTime, double attackBackSwingTime, int damage) {
        this.attackAnimationTime = attackAnimationTime;
        this.attackBackSwingTime = attackBackSwingTime;
        this.damage = damage;
    }

    @Override
    public void onAdded() {
        controllableComponent = ComponentUtils.getControllableComponent(entity);
    }

    public void attack() {
        if (isOperable) {
            disableOperation();
            controllableComponent.ifPresent(OperableComponent::disableOperation);
            FXGL.getGameTimer().runOnceAfter(() -> {
                doAttack();
            }, Duration.seconds(attackAnimationTime));
            FXGL.getGameTimer().runOnceAfter(() -> {
                enableOperation();
                controllableComponent.ifPresent(OperableComponent::enableOperation);
            }, Duration.seconds(attackAnimationTime + attackBackSwingTime));
        }
    }

    protected void doAttack() {

    }
}
