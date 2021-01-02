package components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import util.EntityUtils;

import java.util.List;

public class MeleeAttackComponent extends AttackComponent {
    private int attackRangeWidth;
    private int attackRangeHeight;

    public MeleeAttackComponent(
            double attackAnimationTime,
            double attackBackSwingTime,
            int damage,
            int attackRangeWidth,
            int attackRangeHeight
    ) {
        super(attackAnimationTime, attackBackSwingTime, damage);
        this.attackRangeWidth = attackRangeWidth;
        this.attackRangeHeight = attackRangeHeight;
    }

    @Override
    protected void doAttack() {
        // calculate entities in attack range
        double x = entity.getX();
        if (entity.getScaleX() > 0) {
            x += entity.getWidth();
        } else if (entity.getScaleX() < 0) {
            x -= attackRangeWidth;
        }
        double y = entity.getY() + entity.getHeight() / 2 - attackRangeHeight / 2;

        Rectangle2D range = new Rectangle2D(x, y, attackRangeWidth, attackRangeHeight);
        List<Entity> entityList = FXGL.getGameWorld().getEntitiesInRange(range);
        entityList.removeIf(e -> !e.hasComponent(ControllableComponent.class));
        entityList.removeIf(e -> !EntityUtils.isEnemy(entity, e));
//        System.err.println(entityList.size());
        for (Entity e : entityList) {
            e.getComponent(HealthComponent.class).decreaseHealth(damage);
        }
    }

    @Override
    public boolean isInAttackRangeX(Entity enemy) {
        double ix = entity.getX();
        double ox = enemy.getX();
        double scaleX = entity.getScaleX();
        double relativeDistance;
        if (scaleX > 0) {
            relativeDistance = scaleX * (ox - ix - entity.getWidth());
        } else {
            relativeDistance = scaleX * (ox - ix - enemy.getWidth());
        }
        if (relativeDistance >= 0 && relativeDistance <= attackRangeWidth) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isInAttackRangeY(Entity enemy) {
        double iy = entity.getY();
        double oy = enemy.getY();
        if (oy - attackRangeHeight <= iy && iy <= oy + attackRangeHeight) {
            return true;
        } else {
            return false;
        }
    }
}
