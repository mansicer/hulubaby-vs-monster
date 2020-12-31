package components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;

public class ItemAnimatedComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel animatedIdle;

    public ItemAnimatedComponent(AnimationChannel animatedIdle) {
        this.animatedIdle = animatedIdle;
        texture = new AnimatedTexture(animatedIdle);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(entity.getWidth()/2, entity.getHeight()/2));
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(animatedIdle);
    }

}
