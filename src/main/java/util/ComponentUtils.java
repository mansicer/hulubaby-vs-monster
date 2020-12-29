package util;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import components.AttackComponent;
import components.MovableComponent;
import components.ControllableComponent;

import java.util.List;
import java.util.Optional;

public class ComponentUtils {
    public static Optional<MovableComponent> getMovableComponent(Entity entity) {
        Optional<MovableComponent> ret = Optional.empty();
        List<Component> components = entity.getComponents();
        for (Component component : components) {
            if (component instanceof MovableComponent) {
                ret = Optional.of((MovableComponent) component);
            }
        }
        return ret;
    }
    public static Optional<ControllableComponent> getControllableComponent(Entity entity) {
        Optional<ControllableComponent> ret = Optional.empty();
        List<Component> components = entity.getComponents();
        for (Component component : components) {
            if (component instanceof ControllableComponent) {
                ret = Optional.of((ControllableComponent) component);
            }
        }
        return ret;
    }

    public static Optional<AttackComponent> getAttackComponent(Entity entity) {
        Optional<AttackComponent> ret = Optional.empty();
        List<Component> components = entity.getComponents();
        for (Component component : components) {
            if (component instanceof AttackComponent) {
                ret = Optional.of((AttackComponent) component);
            }
        }
        return ret;
    }
}
