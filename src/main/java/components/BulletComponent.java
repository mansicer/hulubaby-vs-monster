package components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.SerializableComponent;
import org.jetbrains.annotations.NotNull;

public class BulletComponent extends Component implements SerializableComponent {
    int sourceID;
    int damage;

    public BulletComponent(int damage, int sourceID) {
        this.damage = damage;
        this.sourceID = sourceID;
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
