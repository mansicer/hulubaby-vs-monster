package components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import javafx.util.Duration;
import util.ComponentUtils;

import java.util.Optional;

public class RangedAttackComponent extends AttackComponent {
    int bulletSpeed = 0;
    String bulletEntityName;
    int id = 0;
    public RangedAttackComponent(double attackAnimationTime, double attackBackSwingTime, int damage, int bulletSpeed, String bulletEntityName) {
        super(attackAnimationTime, attackBackSwingTime, damage);
        this.bulletEntityName = bulletEntityName;
        this.bulletSpeed = bulletSpeed;
    }

    @Override
    protected void doAttack() {
        FXGL.spawn(bulletEntityName,
                new SpawnData(entity.getX(), entity.getY())
                        .put("speedX", (int) (bulletSpeed * entity.getScaleX()))
                        .put("speedY", 0)
                        .put("damage", damage)
                        .put("source", entity)
                        .put("id",id++)
        );
    }
}
