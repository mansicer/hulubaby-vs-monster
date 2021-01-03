package components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.SerializableComponent;
import org.jetbrains.annotations.NotNull;
import util.EntityUtils;
import util.NetworkUtils;

import java.util.ArrayList;

public class MovableComponent extends OperableComponent implements SerializableComponent {
    protected int speedX = 0;
    protected int speedY = 0;
    protected double forwardDistance = 0;

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

            double dx = speedX * tpf;
            double dy = speedY * tpf;

            entity.translateX(dx);
            entity.translateY(dy);

            double distance = Math.sqrt(dx * dx + dy * dy);
            forwardDistance += distance;

            // check out of bound entity, only server can remove
            if (checkOutOfBound() && NetworkUtils.isServer()) {
                entity.removeFromWorld();
                ArrayList<Integer> removeIDs = FXGL.geto("removeIDs");
                removeIDs.add(EntityUtils.getNetworkID(entity));
            }
        }
    }

    public double getForwardDistance() {
        return forwardDistance;
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

    @Override
    public void read(@NotNull Bundle bundle) {
        super.read(bundle);
        speedX = bundle.get("speedX");
        speedY = bundle.get("speedY");
    }

    @Override
    public void write(@NotNull Bundle bundle) {
        super.write(bundle);
        bundle.put("speedX", speedX);
        bundle.put("speedY", speedY);
    }
}
