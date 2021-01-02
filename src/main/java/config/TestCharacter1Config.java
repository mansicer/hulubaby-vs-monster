package config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class TestCharacter1Config {
    public static final int width = 32;
    public static final int height = 48;

    public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("TestCharacter1.png"), 4, width, height, Duration.seconds(1), 1, 1);
    public static final AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("TestCharacter1.png"), 4, width, height, Duration.seconds(1), 0, 3);

    public static final BasicEntityTypes basicEntityType = BasicEntityTypes.PLAYER;
    public static final DetailedEntityType detailedEntityType = DetailedEntityType.TestCharacter1;
    public static final CampType campType = CampType.HuluBabyCamp;

    public static final int speed = 200;

    public static final int health = 100;

    public static final double attackAnimationTime = 0.3;
    public static final double attackBackSwingTime = 0.3;
    public static final int damage = 100;

    public static class bullet {
        public static final String name = "TestCharacter1Bullet";

        public static final BasicEntityTypes basicEntityType = BasicEntityTypes.BULLET;
        public static final DetailedEntityType detailedEntityType = DetailedEntityType.TestCharacter1Bullet;

        public static final int width = 30;
        public static final int height = 30;

        public static final int speed = 500;
        public static final int distance = 300;

        public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("TestCharacter1Bullet.png"), 4, width, height, Duration.seconds(1), 0, 3);
    }
}
