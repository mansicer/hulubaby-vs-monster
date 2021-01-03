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
import util.NetworkUtils;

public class HvMFactory implements EntityFactory {
    private void broadcastSpawnEvent(Entity entity, SpawnData data, String entityName) {
        if (NetworkUtils.isServer()) {
            Server<Bundle> server = FXGL.getWorldProperties().getValue("server");
            server.getConnections().forEach(bundleConnection -> {
                NetworkUtils.getMultiplayerService().spawn(bundleConnection, entity, data, entityName);
            });
        }
    }

    @Spawns("Dawa")
    public Entity newDawa(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(DawaConfig.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(DawaConfig.detailedEntityType, DawaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(DawaConfig.width, DawaConfig.height)))
                .with(new ControllableComponent(DawaConfig.speed))
                .with(new MeleeAttackComponent(DawaConfig.attackAnimationTime, DawaConfig.attackBackSwingTime, DawaConfig.damage, DawaConfig.attackRangeWidth, DawaConfig.attackRangeHeight))
                .with(new HealthComponent(DawaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(DawaConfig.animatedIdle, DawaConfig.animatedWalk, DawaConfig.animatedAttack))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "Dawa");
        return entity;
    }

    @Spawns("Erwa")
    public Entity newErwa(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(ErwaConfig.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(ErwaConfig.detailedEntityType, ErwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(ErwaConfig.width, ErwaConfig.height)))
                .with(new ControllableComponent(ErwaConfig.speed))
                .with(new RangedAttackComponent(ErwaConfig.attackAnimationTime, ErwaConfig.attackBackSwingTime, ErwaConfig.damage, ErwaConfig.bullet.speed, ErwaConfig.bullet.name, ErwaConfig.bullet.height, ErwaConfig.bullet.distance))
                .with(new HealthComponent(ErwaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(ErwaConfig.animatedIdle, ErwaConfig.animatedWalk, ErwaConfig.animatedAttack))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "Erwa");
        return entity;
    }

    @Spawns("ErwaBullet")
    public Entity newErwaBullet(SpawnData data) {
        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        var entity = FXGL.entityBuilder(data)
                .type(ErwaConfig.bullet.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(ErwaConfig.bullet.detailedEntityType, ErwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(ErwaConfig.bullet.width, ErwaConfig.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, ErwaConfig.bullet.distance))
                .with(new ItemAnimatedComponent(ErwaConfig.bullet.animatedIdle))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "ErwaBullet");
        return entity;
    }

    @Spawns("Sanwa")
    public Entity newSanwa(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(SanwaConfig.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(SanwaConfig.detailedEntityType, SanwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(SanwaConfig.width, SanwaConfig.height)))
                .with(new ControllableComponent(SanwaConfig.speed))
                .with(new MeleeAttackComponent(SanwaConfig.attackAnimationTime, SanwaConfig.attackBackSwingTime, SanwaConfig.damage, SanwaConfig.attackRangeWidth, SanwaConfig.attackRangeHeight))
                .with(new HealthComponent(SanwaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(SanwaConfig.animatedIdle, SanwaConfig.animatedWalk, SanwaConfig.animatedAttack))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "Sanwa");
        return entity;
    }

    @Spawns("Siwa")
    public Entity newSiwa(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(SiwaConfig.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(SiwaConfig.detailedEntityType, SiwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(SiwaConfig.width, SiwaConfig.height)))
                .with(new ControllableComponent(SiwaConfig.speed))
                .with(new RangedAttackComponent(SiwaConfig.attackAnimationTime, SiwaConfig.attackBackSwingTime, SiwaConfig.damage, SiwaConfig.bullet.speed, SiwaConfig.bullet.name, SiwaConfig.bullet.height, SiwaConfig.bullet.distance))
                .with(new HealthComponent(SiwaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(SiwaConfig.animatedIdle, SiwaConfig.animatedWalk, SiwaConfig.animatedAttack))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "Siwa");
        return entity;
    }

    @Spawns("SiwaBullet")
    public Entity newSiwaBullet(SpawnData data) {
        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        var entity = FXGL.entityBuilder(data)
                .type(SiwaConfig.bullet.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(SiwaConfig.bullet.detailedEntityType, SiwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(SiwaConfig.bullet.width, SiwaConfig.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, SiwaConfig.bullet.distance))
                .with(new ItemAnimatedComponent(SiwaConfig.bullet.animatedIdle))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "SiwaBullet");
        return entity;
    }


    @Spawns("Wuwa")
    public Entity newWuwa(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(WuwaConfig.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(WuwaConfig.detailedEntityType, WuwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(WuwaConfig.width, WuwaConfig.height)))
                .with(new ControllableComponent(WuwaConfig.speed))
                .with(new RangedAttackComponent(WuwaConfig.attackAnimationTime, WuwaConfig.attackBackSwingTime, WuwaConfig.damage, WuwaConfig.bullet.speed, WuwaConfig.bullet.name, WuwaConfig.bullet.height, WuwaConfig.bullet.distance))
                .with(new HealthComponent(WuwaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(WuwaConfig.animatedIdle, WuwaConfig.animatedWalk, WuwaConfig.animatedAttack))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "Wuwa");
        return entity;
    }

    @Spawns("WuwaBullet")
    public Entity newWuwaBullet(SpawnData data) {
        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        var entity = FXGL.entityBuilder(data)
                .type(WuwaConfig.bullet.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(WuwaConfig.bullet.detailedEntityType, WuwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(WuwaConfig.bullet.width, WuwaConfig.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, WuwaConfig.bullet.distance))
                .with(new ItemAnimatedComponent(WuwaConfig.bullet.animatedIdle))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "WuwaBullet");
        return entity;
    }

    @Spawns("Liuwa")
    public Entity newLiuwa(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(LiuwaConfig.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(LiuwaConfig.detailedEntityType, LiuwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(LiuwaConfig.width, LiuwaConfig.height)))
                .with(new ControllableComponent(LiuwaConfig.speed))
                .with(new MeleeAttackComponent(LiuwaConfig.attackAnimationTime, LiuwaConfig.attackBackSwingTime, LiuwaConfig.damage, LiuwaConfig.attackRangeWidth, LiuwaConfig.attackRangeHeight))
                .with(new HealthComponent(LiuwaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(LiuwaConfig.animatedIdle, LiuwaConfig.animatedWalk, LiuwaConfig.animatedAttack))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "Liuwa");
        return entity;
    }

    @Spawns("Qiwa")
    public Entity newQiwa(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(QiwaConfig.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(QiwaConfig.detailedEntityType, QiwaConfig.campType))
                .bbox(new HitBox(BoundingShape.box(QiwaConfig.width, QiwaConfig.height)))
                .with(new ControllableComponent(QiwaConfig.speed))
                .with(new MeleeAttackComponent(QiwaConfig.attackAnimationTime, QiwaConfig.attackBackSwingTime, QiwaConfig.damage, QiwaConfig.attackRangeWidth, QiwaConfig.attackRangeHeight))
                .with(new HealthComponent(QiwaConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(QiwaConfig.animatedIdle, QiwaConfig.animatedWalk, QiwaConfig.animatedAttack))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "Qiwa");
        return entity;
    }

    @Spawns("Snake1")
    public Entity newSnake1(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(Snake1Config.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(Snake1Config.detailedEntityType, Snake1Config.campType))
                .bbox(new HitBox(BoundingShape.box(Snake1Config.width, Snake1Config.height)))
                .with(new ControllableComponent(Snake1Config.speed))
                .with(new MeleeAttackComponent(Snake1Config.attackAnimationTime, Snake1Config.attackBackSwingTime, Snake1Config.damage, Snake1Config.attackRangeWidth, Snake1Config.attackRangeHeight))
                .with(new HealthComponent(Snake1Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(Snake1Config.animatedIdle, Snake1Config.animatedWalk, Snake1Config.animatedAttack))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "Snake1");
        return entity;
    }

    @Spawns("HuluSoldier1")
    public Entity newHuluSoldier1(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(HuluSoldier1Config.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(HuluSoldier1Config.detailedEntityType, HuluSoldier1Config.campType))
                .bbox(new HitBox(BoundingShape.box(HuluSoldier1Config.width, HuluSoldier1Config.height)))
                .with(new ControllableComponent(HuluSoldier1Config.speed))
                .with(new MeleeAttackComponent(HuluSoldier1Config.attackAnimationTime, HuluSoldier1Config.attackBackSwingTime, HuluSoldier1Config.damage, HuluSoldier1Config.attackRangeWidth, HuluSoldier1Config.attackRangeHeight))
                .with(new HealthComponent(HuluSoldier1Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(HuluSoldier1Config.animatedIdle, HuluSoldier1Config.animatedWalk, HuluSoldier1Config.animatedAttack))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "HuluSoldier1");
        return entity;
    }

    @Spawns("HuluSoldier2")
    public Entity newHuluSoldier2(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(HuluSoldier2Config.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(HuluSoldier2Config.detailedEntityType, HuluSoldier2Config.campType))
                .bbox(new HitBox(BoundingShape.box(HuluSoldier2Config.width, HuluSoldier2Config.height)))
                .with(new ControllableComponent(HuluSoldier2Config.speed))
                .with(new RangedAttackComponent(HuluSoldier2Config.attackAnimationTime, HuluSoldier2Config.attackBackSwingTime, HuluSoldier2Config.damage, HuluSoldier2Config.bullet.speed, HuluSoldier2Config.bullet.name, HuluSoldier2Config.bullet.height, HuluSoldier2Config.bullet.distance))
                .with(new HealthComponent(HuluSoldier2Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(HuluSoldier2Config.animatedIdle, HuluSoldier2Config.animatedWalk, HuluSoldier2Config.animatedAttack))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "HuluSoldier2");
        return entity;
    }

    @Spawns("HuluSoldier2Bullet")
    public Entity newHuluSoldier2Bullet(SpawnData data) {
        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        var entity = FXGL.entityBuilder(data)
                .type(HuluSoldier2Config.bullet.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(HuluSoldier2Config.bullet.detailedEntityType, HuluSoldier2Config.campType))
                .bbox(new HitBox(BoundingShape.box(HuluSoldier2Config.bullet.width, HuluSoldier2Config.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, HuluSoldier2Config.bullet.distance))
                .with(new ItemAnimatedComponent(HuluSoldier2Config.bullet.animatedIdle))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "TestCharacter1Bullet");
        return entity;
    }

    @Spawns("MonsterSoldier1")
    public Entity newMonsterSoldier1(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(MonsterSoldier1Config.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(MonsterSoldier1Config.detailedEntityType, MonsterSoldier1Config.campType))
                .bbox(new HitBox(BoundingShape.box(MonsterSoldier1Config.width, MonsterSoldier1Config.height)))
                .with(new ControllableComponent(MonsterSoldier1Config.speed))
                .with(new MeleeAttackComponent(MonsterSoldier1Config.attackAnimationTime, MonsterSoldier1Config.attackBackSwingTime, MonsterSoldier1Config.damage, MonsterSoldier1Config.attackRangeWidth, MonsterSoldier1Config.attackRangeHeight))
                .with(new HealthComponent(MonsterSoldier1Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(MonsterSoldier1Config.animatedIdle, MonsterSoldier1Config.animatedWalk, MonsterSoldier1Config.animatedAttack))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "MonsterSoldier1");
        return entity;
    }

    @Spawns("MonsterSoldier2")
    public Entity newMonsterSoldier2(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(MonsterSoldier2Config.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(MonsterSoldier2Config.detailedEntityType, MonsterSoldier2Config.campType))
                .bbox(new HitBox(BoundingShape.box(MonsterSoldier2Config.width, MonsterSoldier2Config.height)))
                .with(new ControllableComponent(MonsterSoldier2Config.speed))
                .with(new RangedAttackComponent(MonsterSoldier2Config.attackAnimationTime, MonsterSoldier2Config.attackBackSwingTime, MonsterSoldier2Config.damage, MonsterSoldier2Config.bullet.speed, MonsterSoldier2Config.bullet.name, MonsterSoldier2Config.bullet.height, MonsterSoldier2Config.bullet.distance))
                .with(new HealthComponent(MonsterSoldier2Config.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(MonsterSoldier2Config.animatedIdle, MonsterSoldier2Config.animatedWalk, MonsterSoldier2Config.animatedAttack))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "MonsterSoldier2");
        return entity;
    }

    @Spawns("MonsterSoldier2Bullet")
    public Entity newMonsterSoldier2Bullet(SpawnData data) {
        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        var entity = FXGL.entityBuilder(data)
                .type(MonsterSoldier2Config.bullet.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(MonsterSoldier2Config.bullet.detailedEntityType, MonsterSoldier2Config.campType))
                .bbox(new HitBox(BoundingShape.box(MonsterSoldier2Config.bullet.width, MonsterSoldier2Config.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, MonsterSoldier2Config.bullet.distance))
                .with(new ItemAnimatedComponent(MonsterSoldier2Config.bullet.animatedIdle))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "TestCharacter1Bullet");
        return entity;
    }

    @Spawns("TestCharacter1")
    public Entity newTestCharacter1(SpawnData data) {
        Entity entity = FXGL.entityBuilder(data)
                .type(TestCharacter1Config.basicEntityType)
                .with(new NetworkIDComponent())
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
                .with(new PlayerAnimatedComponent(TestCharacter1Config.animatedIdle, TestCharacter1Config.animatedWalk, TestCharacter1Config.animatedIdle))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "TestCharacter1");
        return entity;
    }

    @Spawns("TestCharacter1-Enemy")
    public Entity newTestCharacter1Enemy(SpawnData data) {
        var entity = FXGL.entityBuilder(data)
                .type(TestCharacter1EnemyConfig.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(TestCharacter1EnemyConfig.detailedEntityType, TestCharacter1EnemyConfig.campType))
                .bbox(new HitBox(BoundingShape.box(TestCharacter1EnemyConfig.width, TestCharacter1EnemyConfig.height)))
                .with(new ControllableComponent(TestCharacter1EnemyConfig.speed))
                .with(new RangedAttackComponent(TestCharacter1EnemyConfig.attackAnimationTime, TestCharacter1EnemyConfig.attackBackSwingTime, TestCharacter1EnemyConfig.damage, TestCharacter1EnemyConfig.bullet.speed, "TestCharacter1Bullet-Enemy", TestCharacter1EnemyConfig.bullet.height, TestCharacter1EnemyConfig.bullet.distance))
                .with(new HealthComponent(TestCharacter1EnemyConfig.health))
                .with(new AIComponent())
                .with(new PlayerAnimatedComponent(TestCharacter1EnemyConfig.animatedIdle, TestCharacter1EnemyConfig.animatedWalk, TestCharacter1EnemyConfig.animatedIdle))
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
        var entity = FXGL.entityBuilder(data)
                .type(TestCharacter1Config.bullet.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(TestCharacter1Config.bullet.detailedEntityType, TestCharacter1Config.campType))
                .bbox(new HitBox(BoundingShape.box(TestCharacter1Config.bullet.width, TestCharacter1Config.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, TestCharacter1Config.bullet.distance))
                .with(new ItemAnimatedComponent(TestCharacter1Config.bullet.animatedIdle))
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
        var entity = FXGL.entityBuilder(data)
                .type(TestCharacter1EnemyConfig.bullet.basicEntityType)
                .with(new NetworkIDComponent())
                .with(new DetailedTypeComponent(TestCharacter1EnemyConfig.bullet.detailedEntityType, TestCharacter1EnemyConfig.campType))
                .bbox(new HitBox(BoundingShape.box(TestCharacter1EnemyConfig.bullet.width, TestCharacter1EnemyConfig.bullet.height)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID, TestCharacter1EnemyConfig.bullet.distance))
                .with(new ItemAnimatedComponent(TestCharacter1EnemyConfig.bullet.animatedIdle))
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "TestCharacter1Bullet-Enemy");
        return entity;
    }
}
