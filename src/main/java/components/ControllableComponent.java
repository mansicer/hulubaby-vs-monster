package components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import util.NetworkUtils;
import util.PropertyUtils;

import java.util.List;

public class ControllableComponent extends MovableComponent {
    protected int maxSpeed = 200;

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
        if (Math.abs(speedX) > 0 && Math.abs(speedY) > 0) {
            speedX = (int) (speedX / Math.sqrt(2));
            speedY = (int) (speedY / Math.sqrt(2));
        }
        double dx = speedX * tpf;
        double dy = speedY * tpf;
        // only able to move when it is operable
        if (isOperable) {
            if (speedX < 0) {
                entity.setScaleX(-1);
            } else if (speedX > 0) {
                entity.setScaleX(1);
            }
            // constrain on other entities and boundaries
            if (!willBeOutOfBoundX(dx) && !willOnCollisionX(dx)) {
                entity.translateX(dx);
            }
            if (!willBeOutOfBoundY(dy) && !willOnCollisionY(dy)) {
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

    protected boolean willOnCollisionX(double dx) {
        List<Entity> entities = FXGL.getGameWorld().getEntitiesByComponent(ControllableComponent.class);
        entities.removeIf(e -> e == entity);
        Entity a = entity;
        for (Entity b : entities) {
            double minX1 = a.getX() + dx;
            double maxX1 = a.getX() + a.getWidth() + dx;
            double minY1 = a.getY();
            double maxY1 = a.getY() + a.getHeight();

            double minX2 = b.getX();
            double maxX2 = b.getX() + b.getWidth();
            double minY2 = b.getY();
            double maxY2 = b.getY() + b.getHeight();

            double minX = Math.max(minX1, minX2);
            double minY = Math.max(minY1, minY2);
            double maxX = Math.min(maxX1, maxX2);
            double maxY = Math.min(maxY1, maxY2);

            if (maxX - minX > 0 && maxY - minY >= 0) {
                return true;
            }
        }
        return false;
    }

    protected boolean willOnCollisionY(double dy) {
        List<Entity> entities = FXGL.getGameWorld().getEntitiesByComponent(ControllableComponent.class);
        entities.removeIf(e -> e == entity);
        Entity a = entity;
        for (Entity b : entities) {
            double minX1 = a.getX();
            double maxX1 = a.getX() + a.getWidth();
            double minY1 = a.getY() + dy;
            double maxY1 = a.getY() + a.getHeight() + dy;

            double minX2 = b.getX();
            double maxX2 = b.getX() + b.getWidth();
            double minY2 = b.getY();
            double maxY2 = b.getY() + b.getHeight();

            double minX = Math.max(minX1, minX2);
            double minY = Math.max(minY1, minY2);
            double maxX = Math.min(maxX1, maxX2);
            double maxY = Math.min(maxY1, maxY2);

            if (maxX - minX > 0 && maxY - minY >= 0) {
                return true;
            }
        }
        return false;
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
