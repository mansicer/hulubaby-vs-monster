package config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class HuluSoldier2Config {
    public static final String name = "HuluSoldier2";

    public static final int width = 50;
    public static final int height = 50;

    public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("hulusoldier2.png"), 6, width, height, Duration.seconds(1), 1, 1);
    public static final AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("hulusoldier2.png"), 6, width, height, Duration.seconds(1), 0, 5);
    public static final AnimationChannel animatedAttack = new AnimationChannel(FXGL.image("hulusoldier2.png"), 6, width, height, Duration.seconds(1), 0, 5);

    public static final BasicEntityTypes basicEntityType = BasicEntityTypes.PLAYER;
    public static final DetailedEntityType detailedEntityType = DetailedEntityType.HuluSoldier2;
    public static final CampType campType = CampType.HuluBabyCamp;

    public static final int speed = 150;

    public static final int health = 100;

    public static final double attackAnimationTime = 0.5;
    public static final double attackBackSwingTime = 0.5;
    public static final int damage = 15;

    public static class bullet {
        public static final String name = "HuluSoldier2Bullet";

        public static final BasicEntityTypes basicEntityType = BasicEntityTypes.BULLET;
        public static final DetailedEntityType detailedEntityType = DetailedEntityType.HuluSoldier2Bullet;

        public static final int width = 30;
        public static final int height = 25;

        public static final int speed = 400;
        public static final int distance = 500;

        public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("hulusoldier2bullet.png"), 4, width, height, Duration.seconds(1), 0, 3);
    }
}
