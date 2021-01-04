package config;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;
import types.BasicEntityTypes;
import types.CampType;
import types.DetailedEntityType;

public class DawaConfig {
    public static final String name = "Dawa";

    public static final int width = 60;
    public static final int height = 60;

    public static final AnimationChannel animatedIdle = new AnimationChannel(FXGL.image("DawaWalk.png"), 4, width, height, Duration.seconds(1), 0, 0);
    public static final AnimationChannel animatedWalk = new AnimationChannel(FXGL.image("DawaWalk.png"), 4, width, height, Duration.seconds(1), 0, 3);
    public static final AnimationChannel animatedAttack = new AnimationChannel(FXGL.image("DawaAttack.png"), 4, width, height, Duration.seconds(1), 0, 3);
    public static final AnimationChannel attackEffect = new AnimationChannel(FXGL.image("Attack.png"),4, 42, 24, Duration.seconds(1), 0, 5);

    public static final BasicEntityTypes basicEntityType = BasicEntityTypes.PLAYER;
    public static final DetailedEntityType detailedEntityType = DetailedEntityType.Dawa;
    public static final CampType campType = CampType.HuluBabyCamp;

    public static final int speed = 250;

    public static final int health = 400;

    public static final double attackAnimationTime = 0.3;
    public static final double attackBackSwingTime = 0.4;
    public static final int damage = 45;
    public static final int attackRangeWidth = 40;
    public static final int attackRangeHeight = 100;
}
