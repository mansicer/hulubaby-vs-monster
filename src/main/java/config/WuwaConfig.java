package config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class WuwaConfig {
    public static final String name = "Wuwa";

    public static final int width = 60;
    public static final int height = 60;

    public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("wuwa.png"), 4, width, height, Duration.seconds(1), 1, 1);
    public static final AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("wuwa.png"), 4, width, height, Duration.seconds(1), 0, 3);
    public static final AnimationChannel animatedAttack = new AnimationChannel(FXGL.image("wuwa.png"), 4, width, height, Duration.seconds(1), 0, 3);

    public static final BasicEntityTypes basicEntityType = BasicEntityTypes.PLAYER;
    public static final DetailedEntityType detailedEntityType = DetailedEntityType.Wuwa;
    public static final CampType campType = CampType.HuluBabyCamp;

    public static final int speed = 300;

    public static final int health = 400;

    public static final double attackAnimationTime = 0.5;
    public static final double attackBackSwingTime = 0.8;
    public static final int damage = 25;

    public static class bullet {
        public static final String name = "WuwaBullet";

        public static final BasicEntityTypes basicEntityType = BasicEntityTypes.BULLET;
        public static final DetailedEntityType detailedEntityType = DetailedEntityType.WuwaBullet;

        public static final int width = 67;
        public static final int height = 60;

        public static final int speed = 350;
        public static final int distance = 600;

        public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("water.png"), 10, width, height, Duration.seconds(1), 0, 9);
    }
}
