package menu;

import com.almasb.fxgl.app.FXGLApplication;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.Scene;
import com.almasb.fxgl.ui.InGamePanel;
import com.almasb.fxgl.ui.MDIWindow;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.ZipUtils;

import java.io.*;
import java.net.Inet4Address;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMenu extends FXGLMenu {
    private ObjectProperty<menuButton> selectButton;

    public GameMenu() {
        super(MenuType.MAIN_MENU);

        menuButton startGame = new menuButton("开始游戏","创建房间等待对战玩家加入",this::startGame);
        menuButton joinGame = new menuButton("加入游戏","加入他人房间进行对战",this::joinGame);
        menuButton options = new menuButton("存档回放","选择存档并观看回放",this::replayGame);
        menuButton quitGame = new menuButton("退出游戏","结束程序并返回桌面",()->{
            File f = new File("src/config.properties");
            if(f.exists()){
                f.delete();
            }
            fireExit();});

        selectButton = new SimpleObjectProperty<>(startGame);

        var descriptionText = FXGL.getUIFactoryService().newText("创建房间等待对战玩家加入",Color.WHITE,20);
        descriptionText.textProperty().bind(
                Bindings.createObjectBinding(()->selectButton.get().description,selectButton)
        );
        Text IPContent = null;
        try {
            IPContent = FXGL.getUIFactoryService().newText("@IP:" + Inet4Address.getLocalHost().getHostAddress());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        var box = new VBox(15,
                startGame,
                joinGame,
                options,
                quitGame,
                new LineSeparator(),
                descriptionText,
                IPContent);
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
            this.text = FXGL.getUIFactoryService().newText(name, Color.LIGHTGRAY,25);
            this.selector = new Polygon(0,0,15,10,0,20);
            this.selector.setFill(Color.WHITE);
            this.selector.setTranslateX(-25);
            this.selector.setTranslateY(-2);
            this.selector.visibleProperty().bind(focusedProperty());
            this.text.fillProperty().bind(Bindings.when(focusedProperty()).then(SELECTED_COLOR).otherwise(NOT_SELECTED_COLOR));
            this.text.strokeProperty().bind(Bindings.when(focusedProperty()).then(SELECTED_COLOR).otherwise(NOT_SELECTED_COLOR));

            setOnMouseEntered(mouseEvent -> {
                setFocused(true);
            });
            setOnMouseExited(mouseEvent -> {
                setFocused(false);
            });

            focusedProperty().addListener((observableValue, oldValue, isSelected) -> {
                if(isSelected){
                    selectButton.setValue(this);
                }
            });

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

    private class mdiButton extends StackPane {
        private String name;
        private Runnable action;
        private Rectangle button;
        private Text text;

        public mdiButton(String name,Runnable action){
            this.name = name;
            this.action = action;
            text = FXGL.getUIFactoryService().newText(this.name,Color.WHITE,15);
            button = new Rectangle();
            button.setAccessibleText(name);
            button.setHeight(40);
            button.setWidth(100);
            button.setArcHeight(20);
            button.setArcWidth(20);
            getChildren().addAll(button);
        }

    }

    private void startGame(){
//        System.out.println("fire before");

        Properties props = new Properties();
        props.setProperty("isServer", "true");
        props.setProperty("isClient", "false");
        try {
            props.store(new FileOutputStream("src/config.properties"),"server or client config");
        }
        catch (IOException e){
            System.out.println("no such file or directory");
        }
        fireNewGame();
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

    private void replayGame() {
        Properties props = new Properties();
        props.setProperty("replay","true");
        props.setProperty("isServer","false");
        props.setProperty("isClient","false");
        try {
            props.store(new FileOutputStream("src/config.properties"),"server or client config");
        }
        catch (IOException e){
            System.out.println("no such file or directory");
        }
        MDIWindow mdiWindow = FXGL.getUIFactoryService().newWindow();
        mdiWindow.setCanMove(false);
        mdiWindow.setTitle("存档回放");
        mdiWindow.setPrefSize(getAppWidth()*3/5,getAppHeight()*3/5);
        mdiWindow.setTranslateX(getAppWidth()/5);
        mdiWindow.setTranslateY(getAppHeight()/5);
        mdiWindow.setCanClose(false);
        mdiWindow.setCanMinimize(false);
        mdiWindow.setCanResize(false);
        getContentRoot().getStylesheets().add("style/tableview.css");

        Pane pane = new Pane();

        TableView tableView = new TableView();
        TableColumn<String,String> choiceColumn = new TableColumn<String, String>("存档");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        choiceColumn.setCellValueFactory(cellData->{
            return new ReadOnlyStringWrapper(cellData.getValue());
        });
        tableView.getColumns().add(choiceColumn);
        File f = new File("./hulu_record_video");
        ObservableList<String> videoFiles = FXCollections.observableArrayList();
        if(f.exists()) {
            File[] files = f.listFiles();
            String pattern = "hulu_record_video/(.*?)_hulu_recordVideo\\.zip";
            for (int i = 0; i < files.length; i++) {
                Matcher matcher = Pattern.compile(pattern).matcher(files[i].toString().replace("\\","/"));
                if (matcher.find()) {
                    videoFiles.add(matcher.group(1));
                }
            }
        }
        else{
            f.mkdir();
        }
        tableView.setItems(videoFiles);
        tableView.setMaxHeight(mdiWindow.getPrefHeight()*4/5);
        tableView.setEditable(false);

        Rectangle rec = new Rectangle();
        rec.setWidth(100);
        rec.setHeight(40);
        rec.setArcWidth(20);
        rec.setArcHeight(20);

        Button isLoad = FXGL.getUIFactoryService().newButton("加载");
        isLoad.setMaxHeight(rec.getHeight());
        isLoad.setMaxWidth(rec.getWidth());
        isLoad.setShape(rec);
        isLoad.setTextAlignment(TextAlignment.CENTER);
        tableView.getSelectionModel().select(0,choiceColumn);
        isLoad.setOnAction(actionEvent -> {
            String selectedItem = (String) tableView.getSelectionModel().getSelectedItem();
            if(selectedItem!=null) {
                System.out.println(selectedItem);
                ZipUtils.unzip("./hulu_record_video/" + selectedItem + "_hulu_recordVideo.zip", "./temp_replay_hulubrother");
                fireNewGame();
            }
        });

        Button cancel = FXGL.getUIFactoryService().newButton("关闭");
        cancel.setOnAction(e->{
            getContentRoot().getChildren().removeAll(mdiWindow);
        });
        cancel.setMaxHeight(rec.getHeight());
        cancel.setMaxWidth(rec.getWidth());
        cancel.setShape(rec);
        cancel.setTextAlignment(TextAlignment.CENTER);

        Button remove = FXGL.getUIFactoryService().newButton("删除");
        remove.setMaxHeight(rec.getHeight());
        remove.setMaxWidth(rec.getWidth());
        remove.setShape(rec);
        remove.setTextAlignment(TextAlignment.CENTER);
        remove.setOnAction(actionEvent -> {
            String selectedItem = (String) tableView.getSelectionModel().getSelectedItem();
            if(selectedItem!=null) {
                File file = new File("./hulu_record_video/" + selectedItem + "_hulu_recordVideo.zip");
                if (file.exists()) {
                    file.delete();
                }
                videoFiles.remove(selectedItem);
            }
        });

        Button recordAdd = FXGL.getUIFactoryService().newButton("导入");
        recordAdd.setMaxHeight(rec.getHeight());
        recordAdd.setMaxWidth(rec.getWidth());
        recordAdd.setShape(rec);
        recordAdd.setTextAlignment(TextAlignment.CENTER);
        recordAdd.setOnAction(actionEvent -> {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
//            System.out.println(file.getName());
            if(file!=null&&file.exists()&&file.getName().endsWith("_hulu_recordVideo.zip")){
                try {
                    int byteread = 0;
                    InputStream inStream = new FileInputStream(file.toString()); //读入原文件
                    FileOutputStream fs = new FileOutputStream("./hulu_record_video/"+file.getName());
                    byte[] buffer = new byte[1444];
                    while ( (byteread = inStream.read(buffer)) != -1) {
                        fs.write(buffer, 0, byteread);
                    }
                    inStream.close();
                    fs.close();
                    videoFiles.add(file.getName().replace("_hulu_recordVideo.zip",""));
                }
                catch (Exception e) {
                    System.out.println("复制单个文件操作出错");
                    e.printStackTrace();
                }
            }
            else if(file!=null&&!file.getName().endsWith("_hulu_recordVideo.zip")){
                FXGL.getDialogService().showErrorBox("错误的文件命名或格式",()->{});
            }
        });

        VBox funcBox = new VBox(20,
                isLoad,
                recordAdd,
                remove,
                cancel);
        funcBox.setScaleX(0.8);
        funcBox.setScaleY(0.8);
        funcBox.setTranslateY(mdiWindow.getPrefHeight()*4/20);
        funcBox.setTranslateX(mdiWindow.getPrefWidth()/25);

        HBox hBox = new HBox();
        hBox.setTranslateY(mdiWindow.getPrefHeight()/20);
        hBox.setTranslateX(mdiWindow.getPrefWidth()*2/20);
        hBox.getChildren().addAll(tableView,funcBox);
        pane.getChildren().addAll(hBox);
        mdiWindow.setBackground(new Background(new BackgroundImage(
                new Image("assets/textures/background/recordChoice.jpg"),
                null,null,new BackgroundPosition(null,0.1,true,null,0,true), null)));
        mdiWindow.setContentPane(pane);
        getContentRoot().getChildren().add(mdiWindow);
    }
}
