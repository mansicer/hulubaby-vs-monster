package util;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import components.NetworkIDComponent;

import java.util.Optional;

public class EntityUtils {
    public static Optional<Entity> getEntityByNetworkID(int id) {
        var entities = FXGL.getGameWorld().getEntitiesByComponent(NetworkIDComponent.class).stream().filter(entity ->
                entity.getComponent(NetworkIDComponent.class).getId() == id
        );
        var ret = entities.findFirst();
        if (ret.isEmpty()) {
            // this could be triggered, but not caused by error
            // maybe remove this print
            System.err.println("Entity with Network ID " + id + " not exist!");
        }
        return ret;
    }
    public static int getNetworkID(Entity entity) {
        return entity.getComponent(NetworkIDComponent.class).getId();
    }
}
