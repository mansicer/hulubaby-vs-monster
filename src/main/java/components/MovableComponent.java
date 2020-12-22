package components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

public class MovableComponent extends OperableComponent {
    protected int speedX = 0;
    protected int speedY = 0;

    public MovableComponent(int speedX, int speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
    }

    @Override
    public void onUpdate(double tpf) {
        if (isOperable) {
            if (speedX < 0) {
                entity.setScaleX(-1);
            } else {
                entity.setScaleX(1);
            }
            entity.translateX(speedX * tpf);
            entity.translateY(speedY * tpf);
            if (checkOutOfBound()) {
                entity.removeFromWorld();
            }
        }
    }

    protected boolean checkOutOfBound() {
        if (entity.getX() < 0 || entity.getX() + entity.getWidth() >= FXGL.getAppWidth()) {
            return true;
        }
        if (entity.getY() < 0 || entity.getY() + entity.getHeight() >= FXGL.getAppHeight()) {
            return true;
        }
        return false;
    }
    protected boolean isMoving() {
        return speedX != 0 || speedY != 0;
    }
}
