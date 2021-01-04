package config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class MonsterSoldier3Config {
    public static final String name = "MonsterSoldier3";

    public static final int width = 50;
    public static final int height = 50;

    public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("monstersoldier3.png"), 6, width, height, Duration.seconds(1), 0, 0);
    public static final AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("monstersoldier3.png"), 6, width, height, Duration.seconds(1), 0, 5);
    public static final AnimationChannel animatedAttack = new AnimationChannel(FXGL.image("monstersoldier3.png"), 6, width, height, Duration.seconds(1), 0, 5);

    public static final BasicEntityTypes basicEntityType = BasicEntityTypes.PLAYER;
    public static final DetailedEntityType detailedEntityType = DetailedEntityType.MonsterSoldier3;
    public static final CampType campType = CampType.MonsterCamp;

    public static final int speed = 200;

    public static final int health = 400;

    public static final double attackAnimationTime = 0.5;
    public static final double attackBackSwingTime = 0.5;
    public static final int damage = 20;
    public static final int attackRangeWidth = 60;
    public static final int attackRangeHeight = 60;
}
