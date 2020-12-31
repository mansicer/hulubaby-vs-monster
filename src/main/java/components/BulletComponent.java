package components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.SerializableComponent;
import org.jetbrains.annotations.NotNull;
import util.ComponentUtils;

public class BulletComponent extends Component implements SerializableComponent {
    int sourceID;
    int damage;
    int bulletDistance;

    @Override
    public void onUpdate(double tpf) {
        MovableComponent movableComponent = ComponentUtils.getMovableComponent(entity).get();
        if (movableComponent.getForwardDistance() > bulletDistance) {
            entity.removeFromWorld();
        }
    }

    public BulletComponent(int damage, int sourceID, int bulletDistance) {
        this.damage = damage;
        this.sourceID = sourceID;
        this.bulletDistance = bulletDistance;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public void read(@NotNull Bundle bundle) {
        damage = bundle.get("damage");
        sourceID = bundle.get("sourceID");
    }

    @Override
    public void write(@NotNull Bundle bundle) {
        bundle.put("damage", damage);
        bundle.put("sourceID", sourceID);
    }
}
