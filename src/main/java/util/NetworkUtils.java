package util;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Server;
import service.MultiplayerConnectionService;

public class NetworkUtils {
    public static boolean isServer() {
        return FXGL.getWorldProperties().getBoolean("isServer");
    }
    public static boolean isClient() {
        return FXGL.getWorldProperties().getBoolean("isClient");
    }
    public static Server<Bundle> getServer() {
        return FXGL.getWorldProperties().getValue("server");
    }
    public static Client<Bundle> getClient() {
        return FXGL.getWorldProperties().getValue("client");
    }
    public static MultiplayerConnectionService getMultiplayerService() {
        return FXGL.getService(MultiplayerConnectionService.class);
    }
}
