package ua.com.pandasushi.controller.control.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ua.com.pandasushi.controller.MainController;
import ua.com.pandasushi.controller.TabController;
import ua.com.pandasushi.controller.inputconvert.FloatFieldChangeListener;

public class EmployeeController implements TabController {

    private MainController main;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> id;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private TableColumn<?, ?> position;

    @FXML
    private TableColumn<?, ?> kitchen;

    @FXML
    private TableColumn<?, ?> cost;

    @FXML
    private TableColumn<?, ?> cons;

    @FXML
    private TableColumn<?, ?> stationFuel;

    @FXML
    private TableColumn<?, ?> code;

    @FXML
    private TableColumn<?, ?> active;

    @FXML
    private TextField inputName;

    @FXML
    private ChoiceBox<String> inputPosition;

    @FXML
    private ChoiceBox<String> inputKitchen;

    @FXML
    private TextField inputCost;

    @FXML
    private TextField inputCons;

    @FXML
    private ChoiceBox<?> inputStation;

    @FXML
    private TextField inputCode;

    @Override
    public void init(MainController main) {
        this.main = main;

    }

    private void setFields() {
        inputCost.textProperty().addListener(new FloatFieldChangeListener(inputCost));
        inputCons.textProperty().addListener(new FloatFieldChangeListener(inputCons));
        inputPosition.setTooltip(new Tooltip("Посада"));
        inputKitchen.setTooltip(new Tooltip("Кухня"));
    }

    @FXML
    void addEmployee(ActionEvent event) {

    }
}
