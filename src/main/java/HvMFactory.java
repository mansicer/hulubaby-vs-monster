import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimationChannel;
import components.*;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class HvMFactory implements EntityFactory {
    @Spawns("TestCharacter1")
    public Entity newTestCharacter1(SpawnData data) {
        AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("TestCharacter1.png"), 4, 32, 48, Duration.seconds(1), 1, 1);
        AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("TestCharacter1.png"), 4, 32, 48, Duration.seconds(1), 0, 3);
        String name = data.get("name");
        int id = data.get("id");
        return FXGL.entityBuilder(data)
                .type(BasicEntityTypes.PLAYER)
                .with(new DetailedTypeComponent(DetailedEntityType.TestCharacter1, CampType.HuluBabyCamp))
                .bbox(new HitBox(BoundingShape.box(32, 48)))
                .with(new ControllableComponent(200))
                .with(new AnimatedComponent(animatedIdle, animatedWalk))
                .with(new RangedAttackComponent(0.3, 0.3, 10, 500, "TestCharacter1Bullet"))
                .with(new HealthComponent(100))
                .with(new IDComponent(name,id))
                .collidable()
                .build();
    }

    @Spawns("TestCharacter1-Enemy")
    public Entity newTestCharacter1Enemy(SpawnData data) {
        AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("TestCharacter1.png"), 4, 32, 48, Duration.seconds(1), 1, 1);
        AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("TestCharacter1.png"), 4, 32, 48, Duration.seconds(1), 0, 3);
        String name = data.get("name");
        int id = data.get("id");
        return FXGL.entityBuilder(data)
                .type(BasicEntityTypes.PLAYER)
                .with(new DetailedTypeComponent(DetailedEntityType.TestCharacter1, CampType.MonsterCamp))
                .bbox(new HitBox(BoundingShape.box(32, 48)))
                .with(new ControllableComponent(200))
                .with(new AnimatedComponent(animatedIdle, animatedWalk))
                .with(new RangedAttackComponent(0.4, 0.3, 25, 300, "TestCharacter1Bullet-Enemy"))
                .with(new HealthComponent(100))
                .with(new IDComponent(name,id))
                .collidable()
                .build();
    }

    @Spawns("TestCharacter1Bullet")
    public Entity newTestCharacter1Bullet(SpawnData data) {
        AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("TestCharacter1Bullet.png"), 4, 30, 30, Duration.seconds(0.5), 1, 1);
        AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("TestCharacter1Bullet.png"), 4, 30, 30, Duration.seconds(0.5), 0, 3);
        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        int id = data.get("id");
        Entity source = data.get("source");
        return FXGL.entityBuilder(data)
                .type(BasicEntityTypes.BULLET)
                .with(new DetailedTypeComponent(DetailedEntityType.TestCharacter1, CampType.HuluBabyCamp))
                .bbox(new HitBox(BoundingShape.box(30, 30)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, source))
                .with(new AnimatedComponent(animatedIdle, animatedWalk))
                .with(new IDComponent("",id))
                .collidable()
                .build();
    }

    @Spawns("TestCharacter1Bullet-Enemy")
    public Entity newTestCharacter1BulletEnemy(SpawnData data) {
        AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("TestCharacter1Bullet.png"), 4, 30, 30, Duration.seconds(0.5), 1, 1);
        AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("TestCharacter1Bullet.png"), 4, 30, 30, Duration.seconds(0.5), 0, 3);
        int speedX = data.get("speedX");
        int speedY = data.get("speedY");
        int damage = data.get("damage");
        Entity source = data.get("source");
        return FXGL.entityBuilder(data)
                .type(BasicEntityTypes.BULLET)
                .with(new DetailedTypeComponent(DetailedEntityType.TestCharacter1, CampType.MonsterCamp))
                .bbox(new HitBox(BoundingShape.box(30, 30)))
                .with(new MovableComponent(speedX, speedY))
                .with(new BulletComponent(damage, source))
                .with(new AnimatedComponent(animatedIdle, animatedWalk))
                .collidable()
                .build();
    }
}
