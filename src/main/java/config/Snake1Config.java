package config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class Snake1Config {
    public static final int width = 40;
    public static final int height = 60;

    public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("snake1.png"), 4, width, height, Duration.seconds(1), 0, 0);
    public static final AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("snake1.png"), 4, width, height, Duration.seconds(1), 0, 3);
    public static final AnimationChannel animatedAttack = new AnimationChannel(FXGL.image("snake1attack.png"), 4, width, height, Duration.seconds(1), 0, 3);

    public static final BasicEntityTypes basicEntityType = BasicEntityTypes.PLAYER;
    public static final DetailedEntityType detailedEntityType = DetailedEntityType.Snake1;
    public static final CampType campType = CampType.MonsterCamp;

    public static final int speed = 150;

    public static final int health = 400;

    public static final double attackAnimationTime = 0.4;
    public static final double attackBackSwingTime = 0.4;
    public static final int damage = 30;
    public static final int attackRangeWidth = 60;
    public static final int attackRangeHeight = 60;
}
