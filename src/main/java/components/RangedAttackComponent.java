package components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import org.jetbrains.annotations.NotNull;
import util.EntityUtils;
import util.NetworkUtils;

public class RangedAttackComponent extends AttackComponent {
    int bulletSpeed;
    String bulletEntityName;
    int bulletHeight;
    int bulletDistance;

    public RangedAttackComponent(
            double attackAnimationTime,
            double attackBackSwingTime,
            int damage,
            int bulletSpeed,
            String bulletEntityName,
            int bulletHeight,
            int bulletDistance
    ) {
        super(attackAnimationTime, attackBackSwingTime, damage);
        this.bulletEntityName = bulletEntityName;
        this.bulletSpeed = bulletSpeed;
        this.bulletHeight = bulletHeight;
        this.bulletDistance = bulletDistance;
    }

    @Override
    protected void doAttack() {
        FXGL.spawn(bulletEntityName,
                new SpawnData(entity.getX(), entity.getY())
                        .put("speedX", (int) (bulletSpeed * entity.getScaleX()))
                        .put("speedY", 0)
                        .put("damage", damage)
                        .put("sourceID", EntityUtils.getNetworkID(entity))
        );
    }

    @Override
    public boolean isInAttackRangeX(Entity enemy) {
        double ix = entity.getX();
        double ox = enemy.getX();
        double scaleX = entity.getScaleX();
        double relativeDistance = scaleX * (ox - ix);
        if (relativeDistance >= 0 && relativeDistance <= bulletDistance) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isInAttackRangeY(Entity enemy) {
        double iy = entity.getY();
        double oy = enemy.getY();
        if (oy - bulletHeight <= iy && iy <= oy + bulletHeight) {
            return true;
        } else {
            return false;
        }
    }
}
