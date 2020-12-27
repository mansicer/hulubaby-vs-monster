package menu;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import config.Config;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import util.NetworkUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class GameMenu extends FXGLMenu {
    private ObjectProperty<menuButton> selectButton;
    public GameMenu() {
        super(MenuType.MAIN_MENU);

        menuButton startGame = new menuButton("开始游戏","创建房间等待对战玩家加入",this::startGame);
        menuButton joinGame = new menuButton("加入游戏","加入他人房间进行对战",this::joinGame);
        menuButton options = new menuButton("设置","",()->{});
        menuButton quitGame = new menuButton("退出游戏","结束程序并返回桌面",()->fireExit());

        selectButton = new SimpleObjectProperty<>(startGame);

        var descriptionText = FXGL.getUIFactoryService().newText("创建房间等待对战玩家加入",Color.WHITE,20);
        descriptionText.textProperty().bind(
                Bindings.createObjectBinding(()->selectButton.get().description,selectButton)
        );

        var box = new VBox(15,
                startGame,
                joinGame,
                options,
                quitGame,
                new LineSeparator(),
                descriptionText);
        box.setTranslateX(FXGL.getAppWidth()/4);
        box.setTranslateY(FXGL.getAppHeight()/3);

        Text text = FXGL.getUIFactoryService().newText("葫芦娃大战妖精",Color.WHITE,60);
        text.setTranslateX(FXGL.getAppWidth()/6);
        text.setTranslateY(FXGL.getAppHeight()/5);

        getContentRoot().getChildren().addAll(box,text);
    }

    @NotNull
    @Override
    protected Button createActionButton(@NotNull StringBinding stringBinding, @NotNull Runnable runnable) {
        return new Button();
    }

    @NotNull
    @Override
    protected Button createActionButton(@NotNull String s, @NotNull Runnable runnable) {
        return new Button();
    }

    @NotNull
    @Override
    protected Node createBackground(double v, double v1) {
        return FXGL.texture("background/background.png",FXGL.getAppWidth(),FXGL.getAppHeight());
    }

    @NotNull
    @Override
    protected Node createProfileView(@NotNull String s) {
        return new Rectangle();
    }

    @NotNull
    @Override
    protected Node createTitleView(@NotNull String s) {
        return new Rectangle();
    }

    @NotNull
    @Override
    protected Node createVersionView(@NotNull String s) {
        return new Rectangle();
    }

    //    private static final Color SELECTED_COLOR = Color.WHITE;
    private static final Color SELECTED_COLOR = Color.WHITE;
    private static final Color NOT_SELECTED_COLOR = Color.LIGHTGRAY;
    private class menuButton extends StackPane {
        private String name;
        private Runnable action;

        private Text text;
        private Polygon selector;
        private String description;

        public menuButton(String name,String description,Runnable action){
            this.name = name;
            this.action = action;
            this.description = description;
            this.text = FXGL.getUIFactoryService().newText(name, Color.WHITE,25);
            this.selector = new Polygon(0,0,15,10,0,20);
            this.selector.setFill(Color.WHITE);
            this.selector.setTranslateX(-25);
            this.selector.setTranslateY(-2);
            this.selector.visibleProperty().bind(focusedProperty());
            this.text.fillProperty().bind(Bindings.when(focusedProperty()).then(SELECTED_COLOR).otherwise(NOT_SELECTED_COLOR));
            this.text.strokeProperty().bind(Bindings.when(focusedProperty()).then(SELECTED_COLOR).otherwise(NOT_SELECTED_COLOR));

            focusedProperty().addListener((observableValue, oldValue, isSelected) -> {
                if(isSelected){
                    selectButton.setValue(this);
                }
            });

            setFocusTraversable(true);
            setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode()==KeyCode.ENTER){
                    this.action.run();
                }
            });
            setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getButton()== MouseButton.PRIMARY){
                    this.action.run();
                }
            });
            setAlignment(Pos.CENTER_LEFT);
            getChildren().addAll(this.selector,this.text);
        }
    }

    private static class LineSeparator extends Parent{
        private Rectangle line = new Rectangle(400,3);

        public LineSeparator(){
            var gradient = new LinearGradient(0,0.5,1,0.5,true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.WHITE),
                    new Stop(0.5,Color.LIGHTGRAY),
                    new Stop(1,Color.TRANSPARENT));
            line.setFill(gradient);
            getChildren().add(line);
        }
    }

    private void startGame(){
        System.out.println("fire before");
        fireNewGame();
        Properties props = new Properties();
        props.setProperty("isServer", "true");
        props.setProperty("isClient", "false");
        try {
            props.store(new FileOutputStream("src/config.properties"),"server or client config");
        }
        catch (IOException e){
            System.out.println("no such file or directory");
        }
    }

    private void joinGame(){
        FXGL.getDialogService().showInputBox("房间IP",GameServerIP -> {
            Properties props = new Properties();
            props.setProperty("isServer","false");
            props.setProperty("isClient","true");
            props.setProperty("ip",GameServerIP);
            try {
                props.store(new FileOutputStream("src/config.properties"),"server or client config");
            }
            catch (IOException e){
                System.out.println("no such file or directory");
            }
            fireNewGame();
        });

    }
}
