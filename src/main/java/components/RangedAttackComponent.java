package components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import org.jetbrains.annotations.NotNull;
import util.EntityUtils;
import util.NetworkUtils;

public class RangedAttackComponent extends AttackComponent {
    int bulletSpeed = 0;
    String bulletEntityName;

    public RangedAttackComponent(double attackAnimationTime, double attackBackSwingTime, int damage, int bulletSpeed, String bulletEntityName) {
        super(attackAnimationTime, attackBackSwingTime, damage);
        this.bulletEntityName = bulletEntityName;
        this.bulletSpeed = bulletSpeed;
    }

    @Override
    protected void doAttack() {
        if (NetworkUtils.isServer()) {
            FXGL.spawn(bulletEntityName,
                    new SpawnData(entity.getX(), entity.getY())
                            .put("speedX", (int) (bulletSpeed * entity.getScaleX()))
                            .put("speedY", 0)
                            .put("damage", damage)
                            .put("sourceID", EntityUtils.getNetworkID(entity))
            );
        }
        if (NetworkUtils.isClient()) {
            // TODO: request for attacking
        }
    }

    @Override
    public void read(@NotNull Bundle bundle) {
        super.read(bundle);
    }
}
