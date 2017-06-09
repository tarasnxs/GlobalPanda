package ua.com.pandasushi.main;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by Тарас on 14.03.2017.
 */
public class GlobalPandaPreloader extends Preloader {
    private Stage preloaderStage;
    private AnchorPane splashLayout;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage = primaryStage;
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        splashLayout = FXMLLoader.load(MainFX.class.getResource("/view/main/splash.fxml"));
        Scene splashScene = new Scene(splashLayout, 200, 140);
        preloaderStage.setScene(splashScene);
        preloaderStage.setResizable(false);
        preloaderStage.setAlwaysOnTop(true);
        preloaderStage.show();
        preloaderStage.centerOnScreen();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            preloaderStage.hide();
        }
    }
}
