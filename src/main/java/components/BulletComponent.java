package components;

import com.almasb.fxgl.entity.Entity;

public class BulletComponent extends MovableComponent {
    Entity source;
    int damage;

    public BulletComponent(int speedX, int speedY, int damage, Entity source) {
        super(speedX, speedY);
        this.damage = damage;
        this.source = source;
    }

    public int getDamage() {
        return damage;
    }
}
