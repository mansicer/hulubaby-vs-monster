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
import config.*;
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

    @Spawns(DawaConfig.name)
    public Entity newDawa(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);
        var entity = FXGL.entityBuilder(data)
                .type(DawaConfig.basicEntityType)
                .with(new NetworkIDComponent(DawaConfig.name))
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

        broadcastSpawnEvent(entity, data, DawaConfig.name);
        return entity;
    }

    @Spawns(ErwaConfig.name)
    public Entity newErwa(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(ErwaConfig.basicEntityType)
                .with(new NetworkIDComponent(ErwaConfig.name))
                .with(new DetailedTypeComponent(ErwaConfig.detailedEntityType, ErwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(ErwaConfig.width, ErwaConfig.height)))
                .with(new ControllableComponent(ErwaConfig.speed))
                .with(new RangedAttackComponent(ErwaConfig.attackAnimationTime, ErwaConfig.attackBackSwingTime, ErwaConfig.damage, ErwaConfig.bullet.speed, ErwaConfig.bullet.name, ErwaConfig.bullet.height, ErwaConfig.bullet.distance))
                .with(new HealthComponent(ErwaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(ErwaConfig.animatedIdle, ErwaConfig.animatedWalk, ErwaConfig.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, ErwaConfig.name);
        return entity;
    }

    @Spawns(ErwaConfig.bullet.name)
    public Entity newErwaBullet(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        var entity = FXGL.entityBuilder(data)
                .type(ErwaConfig.bullet.basicEntityType)
                .with(new NetworkIDComponent(ErwaConfig.bullet.name))
                .with(new DetailedTypeComponent(ErwaConfig.bullet.detailedEntityType, ErwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(ErwaConfig.bullet.width, ErwaConfig.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, ErwaConfig.bullet.distance))
                .with(new ItemAnimatedComponent(ErwaConfig.bullet.animatedIdle))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, ErwaConfig.bullet.name);
        return entity;
    }

    @Spawns(SanwaConfig.name)
    public Entity newSanwa(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(SanwaConfig.basicEntityType)
                .with(new NetworkIDComponent(SanwaConfig.name))
                .with(new DetailedTypeComponent(SanwaConfig.detailedEntityType, SanwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(SanwaConfig.width, SanwaConfig.height)))
                .with(new ControllableComponent(SanwaConfig.speed))
                .with(new MeleeAttackComponent(SanwaConfig.attackAnimationTime, SanwaConfig.attackBackSwingTime, SanwaConfig.damage, SanwaConfig.attackRangeWidth, SanwaConfig.attackRangeHeight))
                .with(new HealthComponent(SanwaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(SanwaConfig.animatedIdle, SanwaConfig.animatedWalk, SanwaConfig.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, SanwaConfig.name);
        return entity;
    }

    @Spawns(SiwaConfig.name)
    public Entity newSiwa(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(SiwaConfig.basicEntityType)
                .with(new NetworkIDComponent(SiwaConfig.name))
                .with(new DetailedTypeComponent(SiwaConfig.detailedEntityType, SiwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(SiwaConfig.width, SiwaConfig.height)))
                .with(new ControllableComponent(SiwaConfig.speed))
                .with(new RangedAttackComponent(SiwaConfig.attackAnimationTime, SiwaConfig.attackBackSwingTime, SiwaConfig.damage, SiwaConfig.bullet.speed, SiwaConfig.bullet.name, SiwaConfig.bullet.height, SiwaConfig.bullet.distance))
                .with(new HealthComponent(SiwaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(SiwaConfig.animatedIdle, SiwaConfig.animatedWalk, SiwaConfig.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, SiwaConfig.name);
        return entity;
    }

    @Spawns(SiwaConfig.bullet.name)
    public Entity newSiwaBullet(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        var entity = FXGL.entityBuilder(data)
                .type(SiwaConfig.bullet.basicEntityType)
                .with(new NetworkIDComponent(SiwaConfig.bullet.name))
                .with(new DetailedTypeComponent(SiwaConfig.bullet.detailedEntityType, SiwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(SiwaConfig.bullet.width, SiwaConfig.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, SiwaConfig.bullet.distance))
                .with(new ItemAnimatedComponent(SiwaConfig.bullet.animatedIdle))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, SiwaConfig.bullet.name);
        return entity;
    }


    @Spawns(WuwaConfig.name)
    public Entity newWuwa(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(WuwaConfig.basicEntityType)
                .with(new NetworkIDComponent(WuwaConfig.name))
                .with(new DetailedTypeComponent(WuwaConfig.detailedEntityType, WuwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(WuwaConfig.width, WuwaConfig.height)))
                .with(new ControllableComponent(WuwaConfig.speed))
                .with(new RangedAttackComponent(WuwaConfig.attackAnimationTime, WuwaConfig.attackBackSwingTime, WuwaConfig.damage, WuwaConfig.bullet.speed, WuwaConfig.bullet.name, WuwaConfig.bullet.height, WuwaConfig.bullet.distance))
                .with(new HealthComponent(WuwaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(WuwaConfig.animatedIdle, WuwaConfig.animatedWalk, WuwaConfig.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, WuwaConfig.name);
        return entity;
    }

    @Spawns(WuwaConfig.bullet.name)
    public Entity newWuwaBullet(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        var entity = FXGL.entityBuilder(data)
                .type(WuwaConfig.bullet.basicEntityType)
                .with(new NetworkIDComponent(WuwaConfig.bullet.name))
                .with(new DetailedTypeComponent(WuwaConfig.bullet.detailedEntityType, WuwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(WuwaConfig.bullet.width, WuwaConfig.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, WuwaConfig.bullet.distance))
                .with(new ItemAnimatedComponent(WuwaConfig.bullet.animatedIdle))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, WuwaConfig.bullet.name);
        return entity;
    }

    @Spawns(LiuwaConfig.name)
    public Entity newLiuwa(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(LiuwaConfig.basicEntityType)
                .with(new NetworkIDComponent(LiuwaConfig.name))
                .with(new DetailedTypeComponent(LiuwaConfig.detailedEntityType, LiuwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(LiuwaConfig.width, LiuwaConfig.height)))
                .with(new ControllableComponent(LiuwaConfig.speed))
                .with(new MeleeAttackComponent(LiuwaConfig.attackAnimationTime, LiuwaConfig.attackBackSwingTime, LiuwaConfig.damage, LiuwaConfig.attackRangeWidth, LiuwaConfig.attackRangeHeight))
                .with(new HealthComponent(LiuwaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(LiuwaConfig.animatedIdle, LiuwaConfig.animatedWalk, LiuwaConfig.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, LiuwaConfig.name);
        return entity;
    }

    @Spawns(QiwaConfig.name)
    public Entity newQiwa(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(QiwaConfig.basicEntityType)
                .with(new NetworkIDComponent(QiwaConfig.name))
                .with(new DetailedTypeComponent(QiwaConfig.detailedEntityType, QiwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(QiwaConfig.width, QiwaConfig.height)))
                .with(new ControllableComponent(QiwaConfig.speed))
                .with(new MeleeAttackComponent(QiwaConfig.attackAnimationTime, QiwaConfig.attackBackSwingTime, QiwaConfig.damage, QiwaConfig.attackRangeWidth, QiwaConfig.attackRangeHeight))
                .with(new HealthComponent(QiwaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(QiwaConfig.animatedIdle, QiwaConfig.animatedWalk, QiwaConfig.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, QiwaConfig.name);
        return entity;
    }

    @Spawns(HuluSoldier1Config.name)
    public Entity newHuluSoldier1(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(HuluSoldier1Config.basicEntityType)
                .with(new NetworkIDComponent(HuluSoldier1Config.name))
                .with(new DetailedTypeComponent(HuluSoldier1Config.detailedEntityType, HuluSoldier1Config.campType))
                .bbox(new HitBox(BoundingShape.box(HuluSoldier1Config.width, HuluSoldier1Config.height)))
                .with(new ControllableComponent(HuluSoldier1Config.speed))
                .with(new MeleeAttackComponent(HuluSoldier1Config.attackAnimationTime, HuluSoldier1Config.attackBackSwingTime, HuluSoldier1Config.damage, HuluSoldier1Config.attackRangeWidth, HuluSoldier1Config.attackRangeHeight))
                .with(new HealthComponent(HuluSoldier1Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(HuluSoldier1Config.animatedIdle, HuluSoldier1Config.animatedWalk, HuluSoldier1Config.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, HuluSoldier1Config.name);
        return entity;
    }

    @Spawns(HuluSoldier2Config.name)
    public Entity newHuluSoldier2(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(HuluSoldier2Config.basicEntityType)
                .with(new NetworkIDComponent(HuluSoldier2Config.name))
                .with(new DetailedTypeComponent(HuluSoldier2Config.detailedEntityType, HuluSoldier2Config.campType))
                .bbox(new HitBox(BoundingShape.box(HuluSoldier2Config.width, HuluSoldier2Config.height)))
                .with(new ControllableComponent(HuluSoldier2Config.speed))
                .with(new RangedAttackComponent(HuluSoldier2Config.attackAnimationTime, HuluSoldier2Config.attackBackSwingTime, HuluSoldier2Config.damage, HuluSoldier2Config.bullet.speed, HuluSoldier2Config.bullet.name, HuluSoldier2Config.bullet.height, HuluSoldier2Config.bullet.distance))
                .with(new HealthComponent(HuluSoldier2Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(HuluSoldier2Config.animatedIdle, HuluSoldier2Config.animatedWalk, HuluSoldier2Config.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, HuluSoldier2Config.name);
        return entity;
    }

    @Spawns(HuluSoldier2Config.bullet.name)
    public Entity newHuluSoldier2Bullet(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        var entity = FXGL.entityBuilder(data)
                .type(HuluSoldier2Config.bullet.basicEntityType)
                .with(new NetworkIDComponent(HuluSoldier2Config.bullet.name))
                .with(new DetailedTypeComponent(HuluSoldier2Config.bullet.detailedEntityType, HuluSoldier2Config.campType))
                .bbox(new HitBox(BoundingShape.box(HuluSoldier2Config.bullet.width, HuluSoldier2Config.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, HuluSoldier2Config.bullet.distance))
                .with(new ItemAnimatedComponent(HuluSoldier2Config.bullet.animatedIdle))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, HuluSoldier2Config.bullet.name);
        return entity;
    }

    @Spawns(Snake1Config.name)
    public Entity newSnake1(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(Snake1Config.basicEntityType)
                .with(new NetworkIDComponent(Snake1Config.name))
                .with(new DetailedTypeComponent(Snake1Config.detailedEntityType, Snake1Config.campType))
                .bbox(new HitBox(BoundingShape.box(Snake1Config.width, Snake1Config.height)))
                .with(new ControllableComponent(Snake1Config.speed))
                .with(new MeleeAttackComponent(Snake1Config.attackAnimationTime, Snake1Config.attackBackSwingTime, Snake1Config.damage, Snake1Config.attackRangeWidth, Snake1Config.attackRangeHeight))
                .with(new HealthComponent(Snake1Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(Snake1Config.animatedIdle, Snake1Config.animatedWalk, Snake1Config.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, Snake1Config.name);
        return entity;
    }

    @Spawns(Snake2Config.name)
    public Entity newSnake2(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(Snake2Config.basicEntityType)
                .with(new NetworkIDComponent(Snake2Config.name))
                .with(new DetailedTypeComponent(Snake2Config.detailedEntityType, Snake2Config.campType))
                .bbox(new HitBox(BoundingShape.box(Snake2Config.width, Snake2Config.height)))
                .with(new ControllableComponent(Snake2Config.speed))
                .with(new RangedAttackComponent(Snake2Config.attackAnimationTime, Snake2Config.attackBackSwingTime, Snake2Config.damage, Snake2Config.bullet.speed, Snake2Config.bullet.name, Snake2Config.bullet.height, Snake2Config.bullet.distance))
                .with(new HealthComponent(Snake2Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(Snake2Config.animatedIdle, Snake2Config.animatedWalk, Snake2Config.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, Snake2Config.name);
        return entity;
    }

    @Spawns(Snake2Config.bullet.name)
    public Entity newHuluSnake2Bullet(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        var entity = FXGL.entityBuilder(data)
                .type(Snake2Config.bullet.basicEntityType)
                .with(new NetworkIDComponent(Snake2Config.bullet.name))
                .with(new DetailedTypeComponent(Snake2Config.bullet.detailedEntityType, Snake2Config.campType))
                .bbox(new HitBox(BoundingShape.box(Snake2Config.bullet.width, Snake2Config.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, Snake2Config.bullet.distance))
                .with(new ItemAnimatedComponent(Snake2Config.bullet.animatedIdle))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, Snake2Config.bullet.name);
        return entity;
    }

    @Spawns(MonsterSoldier1Config.name)
    public Entity newMonsterSoldier1(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(MonsterSoldier1Config.basicEntityType)
                .with(new NetworkIDComponent(MonsterSoldier1Config.name))
                .with(new DetailedTypeComponent(MonsterSoldier1Config.detailedEntityType, MonsterSoldier1Config.campType))
                .bbox(new HitBox(BoundingShape.box(MonsterSoldier1Config.width, MonsterSoldier1Config.height)))
                .with(new ControllableComponent(MonsterSoldier1Config.speed))
                .with(new MeleeAttackComponent(MonsterSoldier1Config.attackAnimationTime, MonsterSoldier1Config.attackBackSwingTime, MonsterSoldier1Config.damage, MonsterSoldier1Config.attackRangeWidth, MonsterSoldier1Config.attackRangeHeight))
                .with(new HealthComponent(MonsterSoldier1Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(MonsterSoldier1Config.animatedIdle, MonsterSoldier1Config.animatedWalk, MonsterSoldier1Config.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, MonsterSoldier1Config.name);
        return entity;
    }

    @Spawns(MonsterSoldier2Config.name)
    public Entity newMonsterSoldier2(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(MonsterSoldier2Config.basicEntityType)
                .with(new NetworkIDComponent(MonsterSoldier2Config.name))
                .with(new DetailedTypeComponent(MonsterSoldier2Config.detailedEntityType, MonsterSoldier2Config.campType))
                .bbox(new HitBox(BoundingShape.box(MonsterSoldier2Config.width, MonsterSoldier2Config.height)))
                .with(new ControllableComponent(MonsterSoldier2Config.speed))
                .with(new RangedAttackComponent(MonsterSoldier2Config.attackAnimationTime, MonsterSoldier2Config.attackBackSwingTime, MonsterSoldier2Config.damage, MonsterSoldier2Config.bullet.speed, MonsterSoldier2Config.bullet.name, MonsterSoldier2Config.bullet.height, MonsterSoldier2Config.bullet.distance))
                .with(new HealthComponent(MonsterSoldier2Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(MonsterSoldier2Config.animatedIdle, MonsterSoldier2Config.animatedWalk, MonsterSoldier2Config.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, MonsterSoldier2Config.name);
        return entity;
    }

    @Spawns(MonsterSoldier2Config.bullet.name)
    public Entity newMonsterSoldier2Bullet(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        var entity = FXGL.entityBuilder(data)
                .type(MonsterSoldier2Config.bullet.basicEntityType)
                .with(new NetworkIDComponent(MonsterSoldier2Config.bullet.name))
                .with(new DetailedTypeComponent(MonsterSoldier2Config.bullet.detailedEntityType, MonsterSoldier2Config.campType))
                .bbox(new HitBox(BoundingShape.box(MonsterSoldier2Config.bullet.width, MonsterSoldier2Config.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, MonsterSoldier2Config.bullet.distance))
                .with(new ItemAnimatedComponent(MonsterSoldier2Config.bullet.animatedIdle))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, MonsterSoldier2Config.bullet.name);
        return entity;
    }

    @Spawns(MonsterSoldier3Config.name)
    public Entity newMonsterSoldier3(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(MonsterSoldier3Config.basicEntityType)
                .with(new NetworkIDComponent(MonsterSoldier3Config.name))
                .with(new DetailedTypeComponent(MonsterSoldier3Config.detailedEntityType, MonsterSoldier3Config.campType))
                .bbox(new HitBox(BoundingShape.box(MonsterSoldier3Config.width, MonsterSoldier3Config.height)))
                .with(new ControllableComponent(MonsterSoldier3Config.speed))
                .with(new MeleeAttackComponent(MonsterSoldier3Config.attackAnimationTime, MonsterSoldier3Config.attackBackSwingTime, MonsterSoldier3Config.damage, MonsterSoldier3Config.attackRangeWidth, MonsterSoldier3Config.attackRangeHeight))
                .with(new HealthComponent(MonsterSoldier3Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(MonsterSoldier3Config.animatedIdle, MonsterSoldier3Config.animatedWalk, MonsterSoldier3Config.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, MonsterSoldier3Config.name);
        return entity;
    }

    @Spawns(MonsterSoldier4Config.name)
    public Entity newMonsterSoldier4(SpawnData data) {
        SpawnDataComponent spawnDataComponent = createSpawnData(data);

        var entity = FXGL.entityBuilder(data)
                .type(MonsterSoldier4Config.basicEntityType)
                .with(new NetworkIDComponent(MonsterSoldier4Config.name))
                .with(new DetailedTypeComponent(MonsterSoldier4Config.detailedEntityType, MonsterSoldier4Config.campType))
                .bbox(new HitBox(BoundingShape.box(MonsterSoldier4Config.width, MonsterSoldier4Config.height)))
                .with(new ControllableComponent(MonsterSoldier4Config.speed))
                .with(new MeleeAttackComponent(MonsterSoldier4Config.attackAnimationTime, MonsterSoldier4Config.attackBackSwingTime, MonsterSoldier4Config.damage, MonsterSoldier4Config.attackRangeWidth, MonsterSoldier4Config.attackRangeHeight))
                .with(new HealthComponent(MonsterSoldier4Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(MonsterSoldier4Config.animatedIdle, MonsterSoldier4Config.animatedWalk, MonsterSoldier4Config.animatedAttack))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, MonsterSoldier4Config.name);
        return entity;
    }

    @Spawns("TestCharacter1")
    public Entity newTestCharacter1(SpawnData data) {
        String spawnName = "TestCharacter1-Enemy";
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
}
