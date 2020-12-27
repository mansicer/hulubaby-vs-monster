package components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import util.NetworkUtils;
import util.PropertyUtils;

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

        if (NetworkUtils.isClient()) {
            NetworkUtils.getClient().getConnections().forEach(connection -> {
                Bundle message = new Bundle("Action: moveUp");
                message.put("playerID", PropertyUtils.getCurrentPlayerID());
                NetworkUtils.getMultiplayerService().sendMessage(connection, message);
            });
        }
    }

    public void moveDown() {
        speedY = maxSpeed;

        if (NetworkUtils.isClient()) {
            NetworkUtils.getClient().getConnections().forEach(connection -> {
                Bundle message = new Bundle("Action: moveDown");
                message.put("playerID", PropertyUtils.getCurrentPlayerID());
                NetworkUtils.getMultiplayerService().sendMessage(connection, message);
            });
        }
    }

    public void moveLeft() {
        speedX = -maxSpeed;

        if (NetworkUtils.isClient()) {
            NetworkUtils.getClient().getConnections().forEach(connection -> {
                Bundle message = new Bundle("Action: moveLeft");
                message.put("playerID", PropertyUtils.getCurrentPlayerID());
                NetworkUtils.getMultiplayerService().sendMessage(connection, message);
            });
        }
    }

    public void moveRight() {
        speedX = maxSpeed;

        if (NetworkUtils.isClient()) {
            NetworkUtils.getClient().getConnections().forEach(connection -> {
                Bundle message = new Bundle("Action: moveRight");
                message.put("playerID", PropertyUtils.getCurrentPlayerID());
                NetworkUtils.getMultiplayerService().sendMessage(connection, message);
            });
        }
    }

    public void stop() {
        // stop by collision, not by userAction, don't need to send messages.
        speedX = 0;
        speedY = 0;
    }

    public void stopX() {
        speedX = 0;

        if (NetworkUtils.isClient()) {
            NetworkUtils.getClient().getConnections().forEach(connection -> {
                Bundle message = new Bundle("Action: stopX");
                message.put("playerID", PropertyUtils.getCurrentPlayerID());
                NetworkUtils.getMultiplayerService().sendMessage(connection, message);
            });
        }
    }

    public void stopY() {
        speedY = 0;

        if (NetworkUtils.isClient()) {
            NetworkUtils.getClient().getConnections().forEach(connection -> {
                Bundle message = new Bundle("Action: stopY");
                message.put("playerID", PropertyUtils.getCurrentPlayerID());
                NetworkUtils.getMultiplayerService().sendMessage(connection, message);
            });
        }
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

}
