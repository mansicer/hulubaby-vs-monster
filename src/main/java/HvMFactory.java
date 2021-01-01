import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.net.Server;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimationChannel;
import components.*;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;
import util.NetworkUtils;

import java.io.Serializable;

public class HvMFactory implements EntityFactory {
    private void broadcastSpawnEvent(Entity entity, SpawnData data, String entityName) {
        if (FXGL.getb("isServer")) {
            Server<Bundle> server = FXGL.getWorldProperties().getValue("server");
            server.getConnections().forEach(bundleConnection -> {
                NetworkUtils.getMultiplayerService().spawn(bundleConnection, entity, data, entityName);
            });
        }
    }

    @Spawns("TestCharacter1")
    public Entity newTestCharacter1(SpawnData data) {
        AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("TestCharacter1.png"), 4, 32, 48, Duration.seconds(1), 1, 1);
        AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("TestCharacter1.png"), 4, 32, 48, Duration.seconds(1), 0, 3);
        String spawnName = "TestCharacter1";
        SpawnDataComponent spawnDataComponent = createSpawnData(data);
        var entity = FXGL.entityBuilder(data)
                .type(BasicEntityTypes.PLAYER)
                .with(new NetworkIDComponent(spawnName))
                .with(new DetailedTypeComponent(DetailedEntityType.TestCharacter1, CampType.HuluBabyCamp))
                .bbox(new HitBox(BoundingShape.box(32, 48)))
                .with(new ControllableComponent(200))
                .with(new AnimatedComponent(animatedIdle, animatedWalk))
                .with(new RangedAttackComponent(0.3, 0.3, 10, 500, "TestCharacter1Bullet"))
                .with(new HealthComponent(100))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "TestCharacter1");
        return entity;
    }

    @Spawns("TestCharacter1-Enemy")
    public Entity newTestCharacter1Enemy(SpawnData data) {
        AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("TestCharacter1.png"), 4, 32, 48, Duration.seconds(1), 1, 1);
        AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("TestCharacter1.png"), 4, 32, 48, Duration.seconds(1), 0, 3);
        String spawnName = "TestCharacter1-Enemy";
        SpawnDataComponent spawnDataComponent = createSpawnData(data);
        var entity = FXGL.entityBuilder(data)
                .type(BasicEntityTypes.PLAYER)
                .with(new NetworkIDComponent(spawnName))
                .with(new DetailedTypeComponent(DetailedEntityType.TestCharacter1, CampType.MonsterCamp))
                .bbox(new HitBox(BoundingShape.box(32, 48)))
                .with(new ControllableComponent(200))
                .with(new AnimatedComponent(animatedIdle, animatedWalk))
                .with(new RangedAttackComponent(0.4, 0.3, 25, 300, "TestCharacter1Bullet-Enemy"))
                .with(new HealthComponent(100))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "TestCharacter1-Enemy");
        return entity;
    }

    @Spawns("TestCharacter1Bullet")
    public Entity newTestCharacter1Bullet(SpawnData data) {
        AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("TestCharacter1Bullet.png"), 4, 30, 30, Duration.seconds(0.5), 1, 1);
        AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("TestCharacter1Bullet.png"), 4, 30, 30, Duration.seconds(0.5), 0, 3);
        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        String spawnName = "TestCharacter1Bullet";
        SpawnDataComponent spawnDataComponent = createSpawnData(data);
        var entity = FXGL.entityBuilder(data)
                .type(BasicEntityTypes.BULLET)
                .with(new NetworkIDComponent(spawnName))
                .with(new DetailedTypeComponent(DetailedEntityType.TestCharacter1, CampType.HuluBabyCamp))
                .bbox(new HitBox(BoundingShape.box(30, 30)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID))
                .with(new AnimatedComponent(animatedIdle, animatedWalk))
                .with(spawnDataComponent)
                .collidable()
                .build();

        broadcastSpawnEvent(entity, data, "TestCharacter1Bullet");
        return entity;
    }

    @Spawns("TestCharacter1Bullet-Enemy")
    public Entity newTestCharacter1BulletEnemy(SpawnData data) {
        AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("TestCharacter1Bullet.png"), 4, 30, 30, Duration.seconds(0.5), 1, 1);
        AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("TestCharacter1Bullet.png"), 4, 30, 30, Duration.seconds(0.5), 0, 3);
        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int sourceID = data.get("sourceID");
        String spawnName = "TestCharacter1Bullet-Enemy";
        SpawnDataComponent spawnDataComponent = createSpawnData(data);
        var entity = FXGL.entityBuilder(data)
                .type(BasicEntityTypes.BULLET)
                .with(new NetworkIDComponent(spawnName))
                .with(new DetailedTypeComponent(DetailedEntityType.TestCharacter1, CampType.MonsterCamp))
                .bbox(new HitBox(BoundingShape.box(30, 30)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, sourceID))
                .with(new AnimatedComponent(animatedIdle, animatedWalk))
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
