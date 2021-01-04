package config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class ErwaConfig {
    public static final String name = "Erwa";

    public static final int width = 50;
    public static final int height = 60;

    public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("erwa.png"), 4, width, height, Duration.seconds(1), 1, 1);
    public static final AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("erwa.png"), 4, width, height, Duration.seconds(1), 0, 3);
    public static final AnimationChannel animatedAttack = new AnimationChannel(FXGL.image("erwa.png"), 4, width, height, Duration.seconds(1), 0, 3);

    public static final BasicEntityTypes basicEntityType = BasicEntityTypes.PLAYER;
    public static final DetailedEntityType detailedEntityType = DetailedEntityType.Erwa;
    public static final CampType campType = CampType.HuluBabyCamp;

    public static final int speed = 300;

    public static final int health = 150;

    public static final double attackAnimationTime = 0.4;
    public static final double attackBackSwingTime = 0.6;
    public static final int damage = 30;

    public static class bullet {
        public static final String name = "ErwaBullet";

        public static final BasicEntityTypes basicEntityType = BasicEntityTypes.BULLET;
        public static final DetailedEntityType detailedEntityType = DetailedEntityType.ErwaBullet;

        public static final int width = 30;
        public static final int height = 30;

        public static final int speed = 600;
        public static final int distance = 600;

        public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("TestCharacter1Bullet.png"), 4, width, height, Duration.seconds(1), 0, 3);
    }
}
