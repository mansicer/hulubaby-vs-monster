package util;

import com.almasb.fxgl.dsl.FXGL;

public class PropertyUtils {
    public static int getCurrentPlayerID() {
        return FXGL.getWorldProperties().getInt("CurrentPlayerID");
    }
    public static void setCurrentPlayerID(int id) {
        FXGL.getWorldProperties().setValue("CurrentPlayerID", id);
    }
}
