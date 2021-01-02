package util;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.ui.MDIWindow;
import components.DetailedTypeComponent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class GameUtils {
    public static boolean detectGameOver(){
        boolean isEnd=true;
        ArrayList<Entity> entities = FXGL.getGameWorld().getEntities();
        AtomicReference<Entity> last = new AtomicReference<>(null);
        if(entities.size()==0){
            return false;
        }
        for (int i = 0; i < entities.size(); i++) {
            if(entities.get(i).isActive()){
                if(last.get() == null){
                    last.set(entities.get(i));
                }
                else{
                    boolean b = last.get().getComponent(DetailedTypeComponent.class).getCampType() == entities.get(i).getComponent(DetailedTypeComponent.class).getCampType();
//                    System.out.println(b);
                    isEnd &= b;
                    last.set(entities.get(i));
                }
            }
        }
        return isEnd;
    }

    public static boolean isWin(){
        ArrayList<Entity> entities = FXGL.getGameWorld().getEntities();
        Optional<Entity> first = entities.stream().findFirst();
        if(first.isEmpty()){
            return false;
        }
        else{
            if(FXGL.getb("isServer")) {
                return EntityUtils.getCampType(first.get()) == FXGL.geto("campType");
            }
            else if (first.isPresent()) {
                return EntityUtils.getCampType(first.get()) == FXGL.geto("opponentCampType");
            }
        }
        return false;
    }

    public static void recordUI(){
        MDIWindow mdiWindow = FXGL.getUIFactoryService().newWindow();
        mdiWindow.setPrefSize(350,250);
        mdiWindow.setTranslateX(FXGL.getAppWidth()/3);
        mdiWindow.setTranslateY(FXGL.getAppHeight()/3);
        mdiWindow.setTitle("存档");
        mdiWindow.setCanResize(false);
        mdiWindow.setCanClose(false);
        Button button = FXGL.getUIFactoryService().newButton("test");
        button.setOnAction(e->{
            FXGL.getGameScene().getRoot().getChildren().remove(mdiWindow);
            FXGL.getGameController().resumeEngine();
        });

        TextField textField = new TextField();
        textField.setOnAction(actionEvent -> {
            if(textField.getText().isBlank()){

            }
            else{
                File file = new File("./temp_record_hulubrother");
                if(file.exists()&&file.listFiles().length>0){
                    ZipUtils.zip("./temp_record_hulubrother",textField.getText());
                    FileUtils.deleteQuietly(new File("./temp_record_hulubrother"));
                    FXGL.getGameScene().getRoot().getChildren().remove(mdiWindow);
                    FXGL.getGameController().resumeEngine();
                }
                else{
                    FXGL.getDialogService().showErrorBox("无存档",()->{});
                }
            }
        });
        Text text = FXGL.getUIFactoryService().newText("存档名字", Color.WHITE,40);
        Rectangle rec = new Rectangle();
        rec.setWidth(100);
        rec.setHeight(40);
        rec.setArcWidth(20);
        rec.setArcHeight(20);

        Button confirm = FXGL.getUIFactoryService().newButton("确定");
        confirm.setShape(rec);
        confirm.setMaxWidth(rec.getWidth());
        confirm.setMaxHeight(rec.getHeight());
        confirm.setOnAction(actionEvent -> {
            if(!textField.getText().isBlank()){
                ZipUtils.zip("./temp_record_hulubrother",textField.getText());
                FileUtils.deleteQuietly(new File("./temp_record_hulubrother"));
                FXGL.getGameScene().getRoot().getChildren().remove(mdiWindow);
                FXGL.getGameController().resumeEngine();
            }
        });

        Button cancel = FXGL.getUIFactoryService().newButton("取消");
        cancel.setShape(rec);
        cancel.setMaxWidth(rec.getWidth());
        cancel.setMaxHeight(rec.getHeight());
        cancel.setOnAction(actionEvent -> {
            FXGL.getGameScene().getRoot().getChildren().remove(mdiWindow);
            FXGL.getGameController().resumeEngine();
        });

        HBox buttons = new HBox(15,
                confirm,
                cancel);

        VBox vBox = new VBox(15,
                text,
                textField,
                buttons
        );
        vBox.setAlignment(Pos.CENTER);
        vBox.setTranslateX(60);
        vBox.setTranslateY(20);
        Pane pane = new Pane();
        pane.getStylesheets().add("style/textField.css");
        pane.getChildren().addAll(vBox);
        pane.setStyle("-fx-alignment: center");
        mdiWindow.setContentPane(pane);
        FXGL.getGameScene().getRoot().getStylesheets().add("style/textField.css");
        FXGL.getGameScene().getRoot().getChildren().add(mdiWindow);
        FXGL.getGameController().pauseEngine();
    }
}
