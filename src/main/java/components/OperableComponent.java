package components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.SerializableComponent;
import org.jetbrains.annotations.NotNull;

public class OperableComponent extends Component implements SerializableComponent {
    protected boolean isOperable = true;

    protected boolean isOperable() {
        return isOperable;
    }
    public void enableOperation() {
        this.isOperable = true;
        for (Component component : entity.getComponents()) {
            if (component instanceof OperableComponent) {
                ((OperableComponent) component).isOperable = true;
            }
        }
    }
    public void disableOperation() {
        this.isOperable = false;
        for (Component component : entity.getComponents()) {
            if (component instanceof OperableComponent) {
                ((OperableComponent) component).isOperable = false;
            }
        }
    }

    @Override
    public void read(@NotNull Bundle bundle) {
        isOperable = bundle.get("isOperable");
    }

    @Override
    public void write(@NotNull Bundle bundle) {
        bundle.put("isOperable", isOperable);
    }
}
