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

    public CampType getCampType() {
        return campType;
    }

    public DetailedEntityType getDetailedEntityType() {
        return detailedEntityType;
    }

    @Override
    public String toString() {
        return "DetailedTypeComponent{" +
                "detailedEntityType=" + detailedEntityType +
                ", campType=" + campType +
                '}';
    }
}
