package components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import util.ComponentUtils;
import util.NetworkUtils;
import util.PropertyUtils;

import java.util.Optional;

public class AttackComponent extends OperableComponent {
    protected double attackAnimationTime = 0.3;
    protected double attackBackSwingTime = 0.2;
    protected int damage = 0;
    protected boolean isAttacking = false;

    AttackComponent(double attackAnimationTime, double attackBackSwingTime, int damage) {
        this.attackAnimationTime = attackAnimationTime;
        this.attackBackSwingTime = attackBackSwingTime;
        this.damage = damage;
    }

    public void attack() {
        if (NetworkUtils.isServer()) {
            isAttacking = true;
            if (isOperable) {
                disableOperation();
                FXGL.getGameTimer().runOnceAfter(() -> {
                    doAttack();
                }, Duration.seconds(attackAnimationTime));
                FXGL.getGameTimer().runOnceAfter(() -> {
                    enableOperation();
                    isAttacking = false;
                }, Duration.seconds(attackAnimationTime + attackBackSwingTime));
            }
        }
        if (NetworkUtils.isClient()) {
            NetworkUtils.getClient().getConnections().forEach(connection -> {
                Bundle message = new Bundle("Action: attack");
                message.put("playerID", PropertyUtils.getCurrentPlayerID());
                NetworkUtils.getMultiplayerService().sendMessage(connection, message);
            });
        }
    }

    protected void doAttack() {
        // do nothing
    }

    @Override
    public void read(@NotNull Bundle bundle) {
        super.read(bundle);
        damage = bundle.get("damage");
        isAttacking = bundle.get("isAttacking");
    }

    @Override
    public void write(@NotNull Bundle bundle) {
        super.write(bundle);
        bundle.put("damage", damage);
        bundle.put("isAttacking", isAttacking);
    }
}
