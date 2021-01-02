import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.net.Server;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import components.*;
import config.DawaConfig;
import config.TestCharacter1Config;
import config.TestCharacter1EnemyConfig;
import util.NetworkUtils;

import java.io.Serializable;

public class HvMFactory implements EntityFactory {
    private void broadcastSpawnEvent(Entity entity, SpawnData data, String entityName) {
        if (NetworkUtils.isServer()) {
            Server<Bundle> server = FXGL.getWorldProperties().getValue("server");
            server.getConnections().forEach(bundleConnection -> {
                NetworkUtils.getMultiplayerService().spawn(bundleConnection, entity, data, entityName);
            });
        }
    }

    @Spawns("TestCharacter1")
    public Entity newTestCharacter1(SpawnData data) {
        String spawnName = "TestCharacter1";
        SpawnDataComponent spawnDataComponent = createSpawnData(data);
        Entity entity = FXGL.entityBuilder(data)
                .type(TestCharacter1Config.basicEntityType)
                .with(new NetworkIDComponent(spawnName))
                .with(new DetailedTypeComponent(TestCharacter1Config.detailedEntityType, TestCharacter1Config.campType))
                .bbox(new HitBox(BoundingShape.box(TestCharacter1Config.width, TestCharacter1Config.height)))
                .with(new ControllableComponent(TestCharacter1Config.speed))
                .with(new RangedAttackComponent(TestCharacter1Config.attackAnimationTime,
                        TestCharacter1Config.attackBackSwingTime,
                        TestCharacter1Config.damage,
                        TestCharacter1Config.bullet.speed,
                        TestCharacter1Config.bullet.name,
                        TestCharacter1Config.bullet.height,
                        TestCharacter1Config.bullet.distance
                ))
                .with(new HealthComponent(TestCharacter1Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(TestCharacter1Config.animatedIdle, TestCharacter1Config.animatedWalk, TestCharacter1Config.animatedIdle, null))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "TestCharacter1");
        return entity;
    }

    @Spawns("TestCharacter1-Enemy")
    public Entity newTestCharacter1Enemy(SpawnData data) {
        String spawnName = "TestCharacter1-Enemy";
        SpawnDataComponent spawnDataComponent = createSpawnData(data);
        var entity = FXGL.entityBuilder(data)
                .type(TestCharacter1EnemyConfig.basicEntityType)
                .with(new NetworkIDComponent(spawnName))
                .with(new DetailedTypeComponent(TestCharacter1EnemyConfig.detailedEntityType, TestCharacter1EnemyConfig.campType))
                .bbox(new HitBox(BoundingShape.box(TestCharacter1EnemyConfig.width, TestCharacter1EnemyConfig.height)))
                .with(new ControllableComponent(TestCharacter1EnemyConfig.speed))
                .with(new RangedAttackComponent(TestCharacter1EnemyConfig.attackAnimationTime, TestCharacter1EnemyConfig.attackBackSwingTime, TestCharacter1EnemyConfig.damage, TestCharacter1EnemyConfig.bullet.speed, "TestCharacter1Bullet-Enemy", TestCharacter1EnemyConfig.bullet.height, TestCharacter1EnemyConfig.bullet.distance))
                .with(new HealthComponent(TestCharacter1EnemyConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(TestCharacter1EnemyConfig.animatedIdle, TestCharacter1EnemyConfig.animatedWalk, TestCharacter1EnemyConfig.animatedIdle, null))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "TestCharacter1-Enemy");
        return entity;
    }

    @Spawns("Dawa")
    public Entity newDawa(SpawnData data) {
        String spawnName = "Dawa";
        SpawnDataComponent spawnDataComponent = createSpawnData(data);
        var entity = FXGL.entityBuilder(data)
                .type(DawaConfig.basicEntityType)
                .with(new NetworkIDComponent(spawnName))
                .with(new DetailedTypeComponent(DawaConfig.detailedEntityType, DawaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(DawaConfig.width, DawaConfig.height)))
                .with(new ControllableComponent(DawaConfig.speed))
                .with(new MeleeAttackComponent(DawaConfig.attackAnimationTime, DawaConfig.attackBackSwingTime, DawaConfig.damage, DawaConfig.attackRangeWidth, DawaConfig.attackRangeHeight))
                .with(new HealthComponent(DawaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(DawaConfig.animatedIdle, DawaConfig.animatedWalk, DawaConfig.animatedAttack, DawaConfig.attackEffect))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "Dawa");
        return entity;
    }

    @Spawns("TestCharacter1Bullet")
    public Entity newTestCharacter1Bullet(SpawnData data) {
        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        String spawnName = "TestCharacter1Bullet";
        SpawnDataComponent spawnDataComponent = createSpawnData(data);
        var entity = FXGL.entityBuilder(data)
                .type(TestCharacter1Config.bullet.basicEntityType)
                .with(new NetworkIDComponent(spawnName))
                .with(new DetailedTypeComponent(TestCharacter1Config.bullet.detailedEntityType, TestCharacter1Config.campType))
                .bbox(new HitBox(BoundingShape.box(TestCharacter1Config.bullet.width, TestCharacter1Config.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, TestCharacter1Config.bullet.distance))
                .with(new ItemAnimatedComponent(TestCharacter1Config.bullet.animatedIdle))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "TestCharacter1Bullet");
        return entity;
    }

    @Spawns("TestCharacter1Bullet-Enemy")
    public Entity newTestCharacter1BulletEnemy(SpawnData data) {
        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        String spawnName = "TestCharacter1Bullet-Enemy";
        SpawnDataComponent spawnDataComponent = createSpawnData(data);
        var entity = FXGL.entityBuilder(data)
                .type(TestCharacter1EnemyConfig.bullet.basicEntityType)
                .with(new NetworkIDComponent(spawnName))
                .with(new DetailedTypeComponent(TestCharacter1EnemyConfig.bullet.detailedEntityType, TestCharacter1EnemyConfig.campType))
                .bbox(new HitBox(BoundingShape.box(TestCharacter1EnemyConfig.bullet.width, TestCharacter1EnemyConfig.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, TestCharacter1EnemyConfig.bullet.distance))
                .with(new ItemAnimatedComponent(TestCharacter1EnemyConfig.bullet.animatedIdle))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "TestCharacter1Bullet-Enemy");
        return entity;
    }
    private SpawnDataComponent createSpawnData(SpawnData data) {
        Bundle bd = new Bundle("");
        data.getData().forEach((s, o) -> {
            if(o instanceof Serializable){
                bd.put(s, (Serializable) o);
            }
        });
        SpawnDataComponent spawnDataComponent = new SpawnDataComponent();
        spawnDataComponent.read(bd);
        return spawnDataComponent;
    }
}
