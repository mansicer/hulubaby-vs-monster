package components;

import com.almasb.fxgl.entity.component.Component;

public class OperableComponent extends Component {
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
}
