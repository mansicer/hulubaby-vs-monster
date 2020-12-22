package components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

public class BulletComponent extends Component {
    Entity source;
    int damage;

    public BulletComponent(int damage, Entity source) {
        this.damage = damage;
        this.source = source;
    }

    public int getDamage() {
        return damage;
    }
}
