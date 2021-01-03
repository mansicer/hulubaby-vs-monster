import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Server;
import com.almasb.fxgl.physics.CollisionHandler;
import components.AIComponent;
import components.BulletComponent;
import components.ControllableComponent;
import components.HealthComponent;
import input.BehaviorControl;
import input.DirectionControl;
import input.GameControl;
import input.MouseControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import service.MultiplayerConnectionService;
import types.BasicEntityTypes;
import util.EntityUtils;
import util.NetworkUtils;
import util.PropertyUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main extends GameApplication {
    private static final String GameTitle = "Hulubabies vs Monsters";
    private static final int GameWidth = 1600;
    private static final int GameHeight = 900;
    private static final String GameVersion = "0.4.0";
    private static final int GameNetworkPort = 6657;
    private static final String GameServerIP = "localhost";

    private int currentPlayerID = -1;
    private Polygon playerIcon;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle(GameTitle);
        gameSettings.setWidth(GameWidth);
        gameSettings.setHeight(GameHeight);
        gameSettings.setVersion(GameVersion);
        gameSettings.addEngineService(MultiplayerConnectionService.class);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new HvMFactory());
        FXGL.getGameScene().getViewport().setBounds(0, 0, GameWidth, GameHeight);

        FXGL.runOnce(()-> {
            FXGL.getDialogService().showConfirmationBox("Is Server?", aBoolean -> {
                boolean isServer = aBoolean;
                boolean isClient = !aBoolean;
                FXGL.getWorldProperties().setValue("isServer", isServer);
                FXGL.getWorldProperties().setValue("isClient", isClient);
                if (isServer) {
                    Server<Bundle> server = FXGL.getNetService().newUDPServer(GameNetworkPort);
                    server.startAsync();
                    FXGL.getWorldProperties().setValue("server", server);
                    server.setOnConnected(bundleConnection -> {
                        NetworkUtils.getMultiplayerService().addInputReplicationReceiver(bundleConnection);
                    });
                }
                if (isClient) {
                    Client<Bundle> client = FXGL.getNetService().newUDPClient(GameServerIP, GameNetworkPort);
                    client.connectAsync();
                    FXGL.getWorldProperties().setValue("client", client);
                    client.setOnConnected(bundleConnection -> {
                        NetworkUtils.getMultiplayerService().addEntityReplicationReceiver(bundleConnection, FXGL.getGameWorld());
                        NetworkUtils.getMultiplayerService().addInputReplicationSender(bundleConnection, FXGL.getGameWorld());
                    });
                }
            });
        }, Duration.seconds(0.1));
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("server", Optional.empty());
        vars.put("client", Optional.empty());
        vars.put("isServer", false);
        vars.put("isClient", false);
        vars.put("CurrentPlayerID", -1);
        vars.put("OpponentPlayerID", -1);
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();
        input.addAction(new DirectionControl.MoveUp(), KeyCode.UP);
        input.addAction(new DirectionControl.MoveDown(), KeyCode.DOWN);
        input.addAction(new DirectionControl.MoveLeft(), KeyCode.LEFT);
        input.addAction(new DirectionControl.MoveRight(), KeyCode.RIGHT);
        input.addAction(new GameControl.StartGame(), KeyCode.SPACE);
        input.addAction(new BehaviorControl.Attack(), KeyCode.A);
        input.addAction(new MouseControl.ChoosePlayer(), MouseButton.PRIMARY);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(BasicEntityTypes.BULLET, BasicEntityTypes.PLAYER) {
            @Override
            protected void onCollision(Entity a, Entity b) {
                if (NetworkUtils.isServer()) {
                    if (EntityUtils.isEnemy(a, b)) {
                        int damage = a.getComponent(BulletComponent.class).getDamage();
                        b.getComponent(HealthComponent.class).decreaseHealth(damage);
                        a.removeFromWorld();
                    }
                }
            }
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        checkCurrentPlayer();
        if (NetworkUtils.isServer()) {
            checkAI();
        }
    }

    protected void checkAI() {
        List<Entity> controllableEntites = FXGL.getGameWorld().getEntitiesByComponent(ControllableComponent.class);
        for (Entity entity : controllableEntites) {
            int id = EntityUtils.getNetworkID(entity);
            if (id != PropertyUtils.getCurrentPlayerID() && id != PropertyUtils.getOpponentPlayerID()) {
                entity.getComponent(AIComponent.class).setAIActive(true);
            }
            else {
                entity.getComponent(AIComponent.class).setAIActive(false);
            }
        }
    }

    protected void checkCurrentPlayer() {
        int playerID = PropertyUtils.getCurrentPlayerID();
        if (playerID < 0) {
            // current player died
            currentPlayerID = -1;
        }
        else if (currentPlayerID != playerID) {
            if (currentPlayerID >= 0) {
                // remove icons
                EntityUtils.getEntityByNetworkID(currentPlayerID).ifPresent(entity -> {
                    entity.getViewComponent().removeChild(playerIcon);
                });
            }
            currentPlayerID = playerID;
            geneartePlayerIcon(EntityUtils.getEntityByNetworkID(playerID).get());
        }
    }

    protected void geneartePlayerIcon(Entity player) {
        int ICON_HEIGHT = 12, ICON_WIDTH = 10;
        var icon = new Polygon(0, 0, ICON_WIDTH, 0, ICON_WIDTH / 2, ICON_HEIGHT);
        icon.setLayoutY(-10 - ICON_HEIGHT - 3);
        icon.setLayoutX(player.getWidth() / 2 - ICON_WIDTH / 2);
        icon.setFill(Color.BLUE);
        player.getViewComponent().addChild(icon);
        playerIcon = icon;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
