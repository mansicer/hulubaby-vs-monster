package config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class SanwaConfig {
    public static final String name = "Sanwa";

    public static final int width = 50;
    public static final int height = 60;

    public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("sanwa.png"), 4, width, height, Duration.seconds(1), 0, 0);
    public static final AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("sanwa.png"), 4, width, height, Duration.seconds(1), 0, 3);
    public static final AnimationChannel animatedAttack = new AnimationChannel(FXGL.image("sanwa.png"), 4, width, height, Duration.seconds(1), 0, 3);

    public static final BasicEntityTypes basicEntityType = BasicEntityTypes.PLAYER;
    public static final DetailedEntityType detailedEntityType = DetailedEntityType.Sanwa;
    public static final CampType campType = CampType.HuluBabyCamp;

    public static final int speed = 200;

    public static final int health = 800;

    public static final double attackAnimationTime = 0.6;
    public static final double attackBackSwingTime = 0.8;
    public static final int damage = 25;
    public static final int attackRangeWidth = 50;
    public static final int attackRangeHeight = 60;
}
