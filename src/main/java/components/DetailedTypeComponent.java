package components;

import com.almasb.fxgl.entity.component.Component;
import types.CampType;
import types.DetailedEntityType;

public class DetailedTypeComponent extends Component {
    protected DetailedEntityType detailedEntityType;
    protected CampType campType;

    public DetailedTypeComponent(DetailedEntityType detailedEntityType, CampType campType) {
        this.detailedEntityType = detailedEntityType;
        this.campType = campType;
    }

    public boolean isEnemy(DetailedTypeComponent other) {
        return !this.campType.equals(other.campType);
    }

    @Override
    public String toString() {
        return "DetailedTypeComponent{" +
                "detailedEntityType=" + detailedEntityType +
                ", campType=" + campType +
                '}';
    }
}
