package components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import util.ComponentUtils;
import util.EntityUtils;
import util.NetworkUtils;
import util.PropertyUtils;

import java.util.Optional;

public abstract class AttackComponent extends OperableComponent {
    protected double attackAnimationTime;
    protected double attackBackSwingTime;
    protected int damage;
    protected boolean isAttacking = false;

    AttackComponent(double attackAnimationTime, double attackBackSwingTime, int damage) {
        this.attackAnimationTime = attackAnimationTime;
        this.attackBackSwingTime = attackBackSwingTime;
        this.damage = damage;
    }

    public void attack() {
        if (NetworkUtils.isServer()) {
            if (isOperable) {
                isAttacking = true;
                disableOperation();

                EntityUtils.registerEntityTimer(
                    Duration.seconds(attackAnimationTime),
                    EntityUtils.getNetworkID(entity),
                    e -> {
                    ComponentUtils.getAttackComponent(e).get().doAttack();
                });

                EntityUtils.registerEntityTimer(
                    Duration.seconds(attackAnimationTime + attackBackSwingTime),
                    EntityUtils.getNetworkID(entity),
                    e-> {
                    ComponentUtils.getAttackComponent(e).get().enableOperation();
                    ComponentUtils.getAttackComponent(e).get().isAttacking = false;
                });
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

    public boolean isInAttackRange(Entity enemy) {
        return isInAttackRangeX(enemy) && isInAttackRangeY(enemy);
    }

    public boolean isInAttackRangeX(Entity enemy) {
        return false;
    }

    public boolean isInAttackRangeY(Entity enemy) {
        return false;
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

    public boolean isAttacking() {
        return isAttacking;
    }
}
