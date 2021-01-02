package util;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import components.ControllableComponent;
import components.DetailedTypeComponent;
import components.NetworkIDComponent;
import javafx.util.Duration;
import types.CampType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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

    public static CampType getCampType(Entity entity) {
        return entity.getComponent(DetailedTypeComponent.class).getCampType();
    }

    public static boolean isEnemy(Entity a, Entity b) {
        CampType aCamp = a.getComponent(DetailedTypeComponent.class).getCampType();
        CampType bCamp = b.getComponent(DetailedTypeComponent.class).getCampType();
        return !aCamp.equals(bCamp);
    }

    public static List<Entity> getEnemies(Entity currentEntity) {
        List<Entity> entities = FXGL.getGameWorld().getEntitiesByComponent(ControllableComponent.class);
        List<Entity> enemyEntities = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.isActive() && EntityUtils.isEnemy(currentEntity, entity)) {
                enemyEntities.add(entity);
            }
        }
        return enemyEntities;
    }

    public static void registerEntityTimer(Duration time, int entityID, Consumer<Entity> func) {
        Entity entity = getEntityByNetworkID(entityID).get();
        FXGL.runOnce(() -> {
            if (entity.isActive()) {
                func.accept(entity);
            }
        }, time);
    }

//    public static boolean isGameOver()
}
