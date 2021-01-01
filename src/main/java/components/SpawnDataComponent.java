package components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.SerializableComponent;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SpawnDataComponent extends Component implements SerializableComponent {
    private Map<String, Serializable> spawnData;
    public SpawnDataComponent(){
        spawnData = new HashMap<>();
    }
    @Override
    public void read(@NotNull Bundle bundle) {
        spawnData.putAll(bundle.getData());
    }

    @Override
    public void write(@NotNull Bundle bundle) {
        spawnData.forEach(bundle::put);
    }
}
