package components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import util.ComponentUtils;
import util.EntityUtils;
import util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class AIComponent extends Component {
    boolean isAIActive = false;
    List<Entity> enemies = new ArrayList<>();

    public void setAIActive(boolean AIActive) {
        isAIActive = AIActive;
    }

    @Override
    public void onUpdate(double tpf) {
        if (isAIActive && NetworkUtils.isServer()) {
            step();
        }
    }

    protected void step() {
        // if enemy in attack range, than do attack
        // else choose for closest enemy
        // calculate enemy position and choose move actions

        if (enemies.isEmpty()) {
            enemies = EntityUtils.getEnemies(entity);
        }
        else {
            checkCurrentEnemies();
        }

        AttackComponent attackComponent = ComponentUtils.getAttackComponent(entity).get();
        ControllableComponent controllableComponent = ComponentUtils.getControllableComponent(entity).get();

        if (enemies.isEmpty()) {
            controllableComponent.stop();
            return;
        }

        boolean chooseAttack = false;
        for (Entity enemy : enemies) {
            if (attackComponent.isInAttackRange(enemy)) {
                chooseAttack = true;
                break;
            }
        }
        if (chooseAttack) {
            attackComponent.attack();
        }
        // moving to closest enemy
        else {
            // choose closest entity
            Entity closestEnemy = getClosestEnemy();

            if (!attackComponent.isInAttackRangeX(closestEnemy)) {
                // move left / right
                if (closestEnemy.getX() - entity.getX() < 0) {
                    controllableComponent.moveLeft();
                }
                else if (closestEnemy.getX() - entity.getX() > 0) {
                    controllableComponent.moveRight();
                }

            }
            else {
                controllableComponent.stopX();
            }
            if (!attackComponent.isInAttackRangeY(closestEnemy)) {
                // move up / down
                if (closestEnemy.getY() - entity.getY() < 0) {
                    controllableComponent.moveUp();
                }
                if (closestEnemy.getY() - entity.getY() > 0) {
                    controllableComponent.moveDown();
                }
            }
            else {
                controllableComponent.stopY();
            }
        }
    }

    protected Entity getClosestEnemy() {
        Entity ret = enemies.get(0);
        double minDistance = entity.distance(ret);
        for (Entity enemy : enemies) {
            double distance = entity.distance(enemy);
            if (distance < minDistance) {
                minDistance = distance;
                ret = enemy;
            }
        }
        return ret;
    }

    protected void checkCurrentEnemies() {
        enemies.removeIf(entity ->
            !entity.isActive()
        );
    }
}
