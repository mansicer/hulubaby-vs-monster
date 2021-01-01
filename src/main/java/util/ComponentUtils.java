package util;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.SerializableComponent;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import components.AttackComponent;
import components.MovableComponent;
import components.ControllableComponent;
import components.SpawnDataComponent;

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
    public static void packUpComponentBundle(Bundle bundle, SerializableComponent component) {
        Bundle temporaryBundle = new Bundle("temporaryBundle");
        component.write(temporaryBundle);
        temporaryBundle.getData().forEach((k, v) -> {
            String newKey = component.getClass().getSimpleName() + "." + k;
            bundle.put(newKey, v);
        });
    }

    public static void unpackComponentBundle(Bundle bundle, SerializableComponent component) {
        String componentName = component.getClass().getSimpleName();
        Bundle temporaryBundle = new Bundle("temporaryBundle");
        bundle.getData().forEach((k, v) -> {
            if (k.startsWith(componentName)) {
                temporaryBundle.put(k.substring(componentName.length() + 1), v);
            }
        });
        component.read(temporaryBundle);
    }
    public static boolean checkComponentUpdatable(Component component) {
        return component instanceof SerializableComponent &&
                !(component instanceof BoundingBoxComponent) &&
                !(component instanceof SpawnDataComponent)
                ;
    }
}
