package components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import javafx.util.Duration;

public class ControllableComponent extends MovableComponent {
    protected int maxSpeed = 200;
    protected double dx, dy;

    public ControllableComponent(int speed) {
        super(0, 0);
        maxSpeed = speed;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onUpdate(double tpf) {
        dx = speedX * tpf;
        dy = speedY * tpf;
        // only able to move when it is operable
        if (isOperable) {
            if (speedX < 0) {
                entity.setScaleX(-1);
            } else if (speedX > 0) {
                entity.setScaleX(1);
            }
            if (!willBeOutOfBoundX(dx)) {
                entity.translateX(dx);
            }
            if (!willBeOutOfBoundY(dy)) {
                entity.translateY(dy);
            }
        }
    }

    public void moveUp() {
        speedY = -maxSpeed;
    }

    public void moveDown() {
        speedY = maxSpeed;
    }

    public void moveLeft() {
        speedX = -maxSpeed;
    }

    public void moveRight() {
        speedX = maxSpeed;
    }

    public void stop() {
        speedX = 0;
        speedY = 0;
    }

    public void stopX() {
        speedX = 0;
    }

    public void stopY() {
        speedY = 0;
    }

    public void resignLastMove() {
        entity.translateX(-dx);
        entity.translateY(-dy);
    }

    protected boolean willBeOutOfBoundX(double dx) {
        if (entity.getX() + dx < 0 || entity.getX() + entity.getWidth() + dx >= FXGL.getAppWidth()) {
            return true;
        }
        return false;
    }

    protected boolean willBeOutOfBoundY(double dy) {
        if (entity.getY() + dy < 0 || entity.getY() + entity.getHeight() + dy >= FXGL.getAppHeight()) {
            return true;
        }
        return false;
    }

    public void controlOri(String ori){
        switch (ori){
            case "UP":
                speedY = -1;
                break;
            case "DOWN":
                speedY = 1;
                break;
            case "LEFT":
                speedX = -1;
                break;
            case "RIGHT":
                speedX = 1;
                break;
        }
    }
}
