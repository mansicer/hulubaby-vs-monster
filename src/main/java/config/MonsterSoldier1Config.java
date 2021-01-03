package config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class MonsterSoldier1Config {
    public static final String name = "MonsterSoldier1";

    public static final int width = 60;
    public static final int height = 60;

    public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("MonsterSoldier1.png"), 6, width, height, Duration.seconds(1), 0, 0);
    public static final AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("MonsterSoldier1.png"), 6, width, height, Duration.seconds(1), 0, 5);
    public static final AnimationChannel animatedAttack = new AnimationChannel(FXGL.image("MonsterSoldier1.png"), 6, width, height, Duration.seconds(1), 0, 5);

    public static final BasicEntityTypes basicEntityType = BasicEntityTypes.PLAYER;
    public static final DetailedEntityType detailedEntityType = DetailedEntityType.MonsterSoldier1;
    public static final CampType campType = CampType.MonsterCamp;

    public static final int speed = 150;

    public static final int health = 150;

    public static final double attackAnimationTime = 0.5;
    public static final double attackBackSwingTime = 0.5;
    public static final int damage = 10;
    public static final int attackRangeWidth = 60;
    public static final int attackRangeHeight = 60;
}
