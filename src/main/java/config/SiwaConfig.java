package config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class SiwaConfig {
    public static final String name = "Siwa";

    public static final int width = 50;
    public static final int height = 60;

    public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("siwa.png"), 4, width, height, Duration.seconds(1), 1, 1);
    public static final AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("siwa.png"), 4, width, height, Duration.seconds(1), 0, 3);
    public static final AnimationChannel animatedAttack = new AnimationChannel(FXGL.image("siwa.png"), 4, width, height, Duration.seconds(1), 0, 3);

    public static final BasicEntityTypes basicEntityType = BasicEntityTypes.PLAYER;
    public static final DetailedEntityType detailedEntityType = DetailedEntityType.Siwa;
    public static final CampType campType = CampType.HuluBabyCamp;

    public static final int speed = 350;

    public static final int health = 250;

    public static final double attackAnimationTime = 1;
    public static final double attackBackSwingTime = 0.8;
    public static final int damage = 50;

    public static class bullet {
        public static final String name = "SiwaBullet";

        public static final BasicEntityTypes basicEntityType = BasicEntityTypes.BULLET;
        public static final DetailedEntityType detailedEntityType = DetailedEntityType.SiwaBullet;

        public static final int width = 50;
        public static final int height = 50;

        public static final int speed = 400;
        public static final int distance = 1200;

        public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("bigfireball.png"), 49, width, height, Duration.seconds(1), 0, 48);
    }
}
