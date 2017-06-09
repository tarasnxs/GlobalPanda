package ua.com.pandasushi.main;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.hibernate.cfg.Configuration;
import ua.com.pandasushi.Config;
import ua.com.pandasushi.database.common.Employee;
import ua.com.pandasushi.database.common.Kitchens;
import ua.com.pandasushi.database.site.dao.DAOSite;
import ua.com.pandasushi.database.site.dao.NewDAOSiteImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Тарас on 14.03.2017.
 */
public class GlobalPandaApp extends Application {
    private static final String POST_URL = "http://185.25.117.145:8080/pandaserver/Login";

    private static Pane loginLayout;
    private static TextField users;
    private static PasswordField pass;
    private static HashMap<String, Integer> kitchList;
    private static ComboBox<String> kitchen;
    private static Button login;
    public static Stage mainStage;

    public static Configuration cfg;
    public static Config config;
    public static DAOSite site;

    @Override
    public void init() {

    }

    @Override
    public void start(final Stage initStage) throws Exception {
        showLoginStage(initStage);
    }

    private static void showMainStage(Stage stage) throws IOException {
        stage.hide();
        System.out.println(config.getKitchen().getName());
        System.out.println(config.getOperator().getName());
        AnchorPane parent = FXMLLoader.load(MainFX.class.getResource("/view/main/main.fxml"));
        Scene main = new Scene(parent, 1024, 768);
        main.getStylesheets().add(MainFX.class.getResource("/css/styles.css").toExternalForm());
        mainStage = new Stage(StageStyle.DECORATED);
        mainStage.setOnCloseRequest(event -> {
            if ( showAlert(Alert.AlertType.CONFIRMATION, "Вихід", "Підтвердіть вихід") == 1 )
                Platform.exit();
            else
                event.consume();
        });
        mainStage.setTitle("Global Panda - " + config.getKitchen().getName());
        mainStage.setIconified(true);
        mainStage.getIcons().add(new Image(MainFX.class.getResourceAsStream("/media/images/icon16.gif")));
        mainStage.getIcons().add(new Image(MainFX.class.getResourceAsStream("/media/images/icon24.gif")));
        mainStage.getIcons().add(new Image(MainFX.class.getResourceAsStream("/media/images/icon32.gif")));
        mainStage.getIcons().add(new Image(MainFX.class.getResourceAsStream("/media/images/icon48.gif")));
        mainStage.getIcons().add(new Image(MainFX.class.getResourceAsStream("/media/images/icon64.gif")));
        mainStage.getIcons().add(new Image(MainFX.class.getResourceAsStream("/media/images/icon256.gif")));
        mainStage.setScene(main);
        mainStage.show();
        mainStage.setMaximized(true);
        mainStage.toFront();
    }


    @Override
    public void stop(){
        //Config.site.closeSession();
    }

    public static int showAlert(Alert.AlertType a, String title, String msg) {
        Alert alert = new Alert(a);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
            return 1;
        else if(result.get() == ButtonType.CANCEL)
            return 0;
        else
            return 0;
    }

    public static void logOut() {
        config = null;
        showLoginStage(mainStage);
    }


    public static void showLoginStage(Stage initStage) {
        initStage.hide();
        Stage stage = new Stage(StageStyle.UNDECORATED);
        loginLayout = new AnchorPane();
        users = new TextField();
        users.setPromptText("Логін");
        pass = new PasswordField();
        pass.setPromptText("Пароль");
        kitchList = new HashMap<>();
        kitchList.put("Сихів", 0);
        kitchList.put("Варшавська", 1);
        kitchList.put("Списання", 2);
        kitchen = new ComboBox<>();
        kitchen.getItems().addAll(kitchList.keySet());
        kitchen.getSelectionModel().select(0);
        login = new Button("Вхід");
        loginLayout.setPrefWidth(175);
        loginLayout.setPrefHeight(175);

        Label label = new Label("Вхід");
        label.setFont(new Font("Arial Black", 16.0));
        AnchorPane.setTopAnchor(label, 10.0);
        AnchorPane.setLeftAnchor(label, 60.0);

        AnchorPane.setLeftAnchor(users,10.0);
        AnchorPane.setTopAnchor(users,70.0);
        AnchorPane.setLeftAnchor(pass, 10.0);
        AnchorPane.setTopAnchor(pass, 100.0);
        AnchorPane.setLeftAnchor(kitchen, 10.0);
        AnchorPane.setTopAnchor(kitchen, 40.0);
        AnchorPane.setRightAnchor(login, 10.0);
        AnchorPane.setBottomAnchor(login, 10.0);
        users.setPrefWidth(160.0);
        pass.setPrefWidth(160.0);
        kitchen.setPrefWidth(160.0);
        login.setOnAction(event -> {
                    config = getConfig(kitchList.get(kitchen.getSelectionModel().getSelectedItem()), users.getText(), pass.getText());
                    if(config != null && config.getOperator() != null) {
                        site = new NewDAOSiteImpl();
                        try {
                            showMainStage(stage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        pass.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
                        TranslateTransition tt = new TranslateTransition(Duration.millis(100), pass);
                        tt.setByX(1f);
                        tt.setCycleCount(4);
                        tt.setAutoReverse(true);

                        tt.play();
                    }
        });
        pass.textProperty().addListener((observable, oldValue, newValue) -> {
            pass.setStyle("");
        });
        pass.setOnAction(event -> {
            config = getConfig(kitchList.get(kitchen.getSelectionModel().getSelectedItem()), users.getText(), pass.getText());
            if(config != null  && config.getOperator() != null) {
                site = new NewDAOSiteImpl();
                try {
                    showMainStage(stage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                pass.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
                TranslateTransition tt = new TranslateTransition(Duration.millis(100), pass);
                tt.setByX(1f);
                tt.setCycleCount(4);
                tt.setAutoReverse(true);

                tt.play();
            }
        });
        loginLayout.getChildren().addAll(label, kitchen, users, pass, login);
        Scene loginScene = new Scene(loginLayout);
        stage.setResizable(false);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        stage.setX(bounds.getMinX() + bounds.getWidth() / 2 - loginScene.getWidth());
        stage.setY(bounds.getMinY() + bounds.getHeight() / 2 - loginScene.getHeight());
        stage.setScene(loginScene);
        stage.setTitle("Global Panda");
        stage.setAlwaysOnTop(true);
        stage.show();
        stage.centerOnScreen();
    }

    private static Config getConfig (Integer kitch, String login, String pass) {
        Config config = null;
        ObjectInputStream ois = null;

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("kitchen", kitch);
        params.put("login", login);
        params.put("password", pass);

        HttpURLConnection conn = null;
        try {
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");


            URL url = new URL(POST_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.getOutputStream().write(postDataBytes);

            conn.connect();

            ois = new ObjectInputStream(conn.getInputStream());

            config = (Config) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null)
                try {
                    System.out.println(conn.getErrorStream().read());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        } finally {
            if (ois != null)
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return config;
    }
}
