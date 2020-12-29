package components;

import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;

public class NetworkIDComponent extends Component {
    private static int uniqueID = 0;
    private int id;

    public NetworkIDComponent() {
        this.id = uniqueID++;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isComponentInjectionRequired() {
        return false;
    }
}
