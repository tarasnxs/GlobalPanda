package ua.com.pandasushi.controller.control.menu;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import ua.com.pandasushi.controller.MainController;
import ua.com.pandasushi.controller.TabController;
import ua.com.pandasushi.controller.services.ExcelService;
import ua.com.pandasushi.main.GlobalPandaApp;

/**
 * Created by Тарас on 10.01.2017.
 */
public class MenuController implements TabController {
    MainController main;

    @FXML
    Label fileName;

    @FXML
    Button loader;

    File menuFile;

    @Override
    public void init(MainController main) {
        this.main = main;
    }


    public void loadMenu(ActionEvent actionEvent) {
        if(menuFile != null) {
            ExcelService.parse(menuFile);
            menuFile = null;
            fileName.setText("Файл не вибрано...");
            loader.setDisable(true);
        }
    }

    public void openDialog(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file...");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(GlobalPandaApp.mainStage);
        if(file != null) {
            menuFile = file;
            fileName.setText(file.getName());
            loader.setDisable(false);
        } else {
            menuFile = null;
            fileName.setText("Файл не вибрано...");
            loader.setDisable(true);
        }
        GlobalPandaApp.showAlert(Alert.AlertType.INFORMATION, "Готово!", "Файл завантажено!");
    }

    public void saveFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file...");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(GlobalPandaApp.mainStage);
        if(file != null) {
            System.out.println(file.getName());
            ExcelService.writeToFile(file);
            GlobalPandaApp.showAlert(Alert.AlertType.INFORMATION, "Готово!", "Файл збережено!");
        }
    }
}
