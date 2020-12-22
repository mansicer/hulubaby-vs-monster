import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import components.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import types.BasicEntityTypes;
import util.ComponentUtils;

import java.util.Optional;

public class Main extends GameApplication {
    private static final String GameTitle = "Hulubabies vs Monsters";
    private static final int GameWidth = 1600;
    private static final int GameHeight = 900;
    private static final String GameVersion = "0.0.1";

    private Optional<Entity> currentPlayer;
    private Optional<Polygon> playerIcon;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle(GameTitle);
        gameSettings.setWidth(GameWidth);
        gameSettings.setHeight(GameHeight);
        gameSettings.setVersion(GameVersion);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new HvMFactory());
        FXGL.getGameScene().getViewport().setBounds(0, 0, GameWidth, GameHeight);
        currentPlayer = Optional.of(FXGL.spawn("TestCharacter1", GameWidth/3, GameHeight/2));
        geneartePlayerIcon(currentPlayer.get());
        FXGL.spawn("TestCharacter1-Enemy", 2 * GameWidth/3, GameHeight/2);
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();
        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                currentPlayer.ifPresent(player -> {
                    player.getComponent(ControllableComponent.class).moveUp();
                });
            }
            @Override
            protected void onActionEnd() {
                currentPlayer.ifPresent(player -> {
                    player.getComponent(ControllableComponent.class).stopY();
                });
            }
        }, KeyCode.UP);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                currentPlayer.ifPresent(player -> {
                    player.getComponent(ControllableComponent.class).moveDown();
                });
            }
            @Override
            protected void onActionEnd() {
                currentPlayer.ifPresent(player -> {
                    player.getComponent(ControllableComponent.class).stopY();
                });
            }
        }, KeyCode.DOWN);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                currentPlayer.ifPresent(player -> {
                    player.getComponent(ControllableComponent.class).moveLeft();
                });
            }
            @Override
            protected void onActionEnd() {
                currentPlayer.ifPresent(player -> {
                    player.getComponent(ControllableComponent.class).stopX();
                });
            }
        }, KeyCode.LEFT);

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                currentPlayer.ifPresent(player -> {
                    player.getComponent(ControllableComponent.class).moveRight();
                });
            }
            @Override
            protected void onActionEnd() {
                currentPlayer.ifPresent(player -> {
                    player.getComponent(ControllableComponent.class).stopX();
                });
            }
        }, KeyCode.RIGHT);

        input.addAction(new UserAction("Attack") {
            @Override
            protected void onActionBegin() {
                currentPlayer.ifPresent(player -> {
                    ComponentUtils.getAttackComponent(player).ifPresent(c -> c.attack());
                });
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Choose Player") {
            @Override
            protected void onActionBegin() {
                var position = input.getMousePositionWorld();
                var range = new Rectangle2D(position.getX() - 6, position.getY() - 10, 12, 20);
                var entities = FXGL.getGameWorld().getEntitiesInRange(range);
                if (entities.size() > 0) {
                    if (currentPlayer.isPresent() && currentPlayer.get().isActive()) {
                        playerIcon.ifPresent(polygon -> currentPlayer.get().getViewComponent().removeChild(polygon));
                    }

                    // TODO: choose the closest one
                    currentPlayer = Optional.of(entities.get(0));
                    geneartePlayerIcon(currentPlayer.get());
                }
            }
        }, MouseButton.PRIMARY);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(BasicEntityTypes.PLAYER, BasicEntityTypes.PLAYER) {
            @Override
            protected void onCollision(Entity a, Entity b) {
                // TODO: add collision on x and y separately
                ComponentUtils.getControllableComponent(a).ifPresent(o -> o.resignLastMove());
                ComponentUtils.getControllableComponent(b).ifPresent(o -> o.resignLastMove());
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(BasicEntityTypes.BULLET, BasicEntityTypes.PLAYER) {
            @Override
            protected void onCollisionBegin(Entity a, Entity b) {
                var typeA = a.getComponent(DetailedTypeComponent.class);
                var typeB = b.getComponent(DetailedTypeComponent.class);
                if (typeA.isEnemy(typeB)) {
                    int damage = a.getComponent(BulletComponent.class).getDamage();
                    b.getComponent(HealthComponent.class).decreaseHealth(damage);
                    a.removeFromWorld();
                    checkCurrentPlayerAlive();
                }
            }
        });
    }

    protected void checkCurrentPlayerAlive() {
        if (!currentPlayer.get().isActive()) {
            currentPlayer = Optional.empty();
        }
    }

    protected void geneartePlayerIcon(Entity player) {
        int ICON_HEIGHT = 12, ICON_WIDTH = 10;
        var icon = new Polygon(0, 0, ICON_WIDTH, 0, ICON_WIDTH / 2, ICON_HEIGHT);
        icon.setLayoutY(-10 - ICON_HEIGHT - 3);
        icon.setLayoutX(player.getWidth() / 2 - ICON_WIDTH / 2);
        icon.setFill(Color.BLUE);
        player.getViewComponent().addChild(icon);
        playerIcon = Optional.of(icon);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
