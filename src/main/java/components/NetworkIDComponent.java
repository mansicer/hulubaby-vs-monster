package components;

import com.almasb.fxgl.entity.component.Component;

public class NetworkIDComponent extends Component {
    private static int uniqueID = 0;
    private int id;
    private String spawnName;

    public NetworkIDComponent(String spawnName) {
        this.spawnName = spawnName;
        this.id = uniqueID++;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpawnName(){
        return spawnName;
    }

    public void setSpawnName(String spawnName){
        this.spawnName = spawnName;
    }

    public boolean isComponentInjectionRequired() {
        return false;
    }
}
