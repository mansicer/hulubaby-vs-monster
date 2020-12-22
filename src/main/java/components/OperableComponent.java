package components;

import com.almasb.fxgl.entity.component.Component;

public class OperableComponent extends Component {
    protected boolean isOperable = true;

    protected boolean isOperable() {
        return isOperable;
    }
    public void enableOperation() {
        this.isOperable = true;
    }
    public void disableOperation() {
        this.isOperable = false;
    }
}
