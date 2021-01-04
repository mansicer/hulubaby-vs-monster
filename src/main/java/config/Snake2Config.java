package config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class Snake2Config {
    public static final String name = "Snake2";

    public static final int width = 50;
    public static final int height = 60;

    public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("snake2.png"), 4, width, height, Duration.seconds(1), 0, 0);
    public static final AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("snake2.png"), 4, width, height, Duration.seconds(1), 0, 3);
    public static final AnimationChannel animatedAttack = new AnimationChannel(FXGL.image("snake2.png"), 4, width, height, Duration.seconds(1), 0, 3);

    public static final BasicEntityTypes basicEntityType = BasicEntityTypes.PLAYER;
    public static final DetailedEntityType detailedEntityType = DetailedEntityType.Snake2;
    public static final CampType campType = CampType.MonsterCamp;

    public static final int speed = 300;

    public static final int health = 400;

    public static final double attackAnimationTime = 0.4;
    public static final double attackBackSwingTime = 0.6;
    public static final int damage = 45;

    public static class bullet {
        public static final String name = "Snake2Bullet";

        public static final BasicEntityTypes basicEntityType = BasicEntityTypes.BULLET;
        public static final DetailedEntityType detailedEntityType = DetailedEntityType.Snake2Bullet;

        public static final int width = 48;
        public static final int height = 48;

        public static final int speed = 400;
        public static final int distance = 1600;

        public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("spike.png"), 8, width, height, Duration.seconds(1), 0, 7);
    }
}
