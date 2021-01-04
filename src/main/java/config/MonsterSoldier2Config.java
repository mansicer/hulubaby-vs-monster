package config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class MonsterSoldier2Config {
    public static final String name = "MonsterSoldier2";

    public static final int width = 50;
    public static final int height = 50;

    public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("monstersoldier2.png"), 6, width, height, Duration.seconds(1), 1, 1);
    public static final AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("monstersoldier2.png"), 6, width, height, Duration.seconds(1), 0, 5);
    public static final AnimationChannel animatedAttack = new AnimationChannel(FXGL.image("monstersoldier2.png"), 6, width, height, Duration.seconds(1), 0, 5);

    public static final BasicEntityTypes basicEntityType = BasicEntityTypes.PLAYER;
    public static final DetailedEntityType detailedEntityType = DetailedEntityType.MonsterSoldier2;
    public static final CampType campType = CampType.MonsterCamp;

    public static final int speed = 150;

    public static final int health = 100;

    public static final double attackAnimationTime = 0.5;
    public static final double attackBackSwingTime = 0.5;
    public static final int damage = 30;

    public static class bullet {
        public static final String name = "MonsterSoldier2Bullet";

        public static final BasicEntityTypes basicEntityType = BasicEntityTypes.BULLET;
        public static final DetailedEntityType detailedEntityType = DetailedEntityType.MonsterSoldier2Bullet;

        public static final int width = 30;
        public static final int height = 20;

        public static final int speed = 400;
        public static final int distance = 500;

        public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("bullet.png"), 1, width, height, Duration.seconds(1), 0, 0);
    }
}
