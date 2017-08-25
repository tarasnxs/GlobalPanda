package ua.com.pandasushi.controller.control.employee;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import org.hibernate.mapping.Collection;
import ua.com.pandasushi.controller.MainController;
import ua.com.pandasushi.controller.TabController;
import ua.com.pandasushi.controller.inputconvert.FloatFieldChangeListener;
import ua.com.pandasushi.database.common.Employee;
import ua.com.pandasushi.database.common.Kitchens;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EmployeeTabController implements TabController {

    private MainController main;

    @FXML
    private TableView<Employee> table;

    @FXML
    private TableColumn<Employee, Integer> id;

    @FXML
    private TableColumn<Employee, String> name;

    @FXML
    private TableColumn<Employee, String> position;

    @FXML
    private TableColumn<Employee, String> mark;

    @FXML
    private TableColumn<Employee, Kitchens> kitchen;

    @FXML
    private TableColumn<Employee, Float> cost;

    @FXML
    private TableColumn<Employee, Float> cons;

    @FXML
    private TableColumn<Employee, String> stationFuel;

    @FXML
    private TableColumn<Employee, Boolean> active;

    @FXML
    private TextField inputName;

    @FXML
    private ChoiceBox<String> inputPosition;

    @FXML
    private ChoiceBox<Kitchens> inputKitchen;

    @FXML
    private TextField inputCost;

    @FXML
    private TextField inputCons;

    @FXML
    private ChoiceBox<String> inputStation;

    @FXML
    private TextField inputCode;

    @Override
    public void init(MainController main) {
        this.main = main;
        setFields();

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        position.setCellValueFactory(new PropertyValueFactory<>("position"));
        position.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), inputPosition.getItems()));
        position.setEditable(true);
        mark.setCellValueFactory(new PropertyValueFactory<>("mark"));
        kitchen.setCellValueFactory(new PropertyValueFactory<>("kitchen"));
        kitchen.setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter() {

            @Override
            public String toString(Object object) {
                return object.toString();
            }

            @Override
            public Object fromString(String string) {
                return inputKitchen.getItems().stream().filter(kitchens -> kitchens.getName().equals(string)).findAny().get();
            }

        }, inputKitchen.getItems()));

        stationFuel.setCellValueFactory(new PropertyValueFactory<>("fuelStation"));
        cons.setCellValueFactory(new PropertyValueFactory<>("fuelCons"));
        cons.setEditable(true);
        cost.setCellValueFactory(new PropertyValueFactory<>("hourlyPrice"));
        cost.setEditable(true);

        active.setCellFactory(param -> new BooleanCell());
        active.setEditable(true);
        active.setCellValueFactory(new PropertyValueFactory<>("active"));


        table.setRowFactory(param -> new TableRow<Employee>() {
            @Override
            public void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (!item.isActive()) {
                    this.setStyle("");
                } else {
                    this.setStyle("-fx-font-weight: bold;");
                }
            }
        });

        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        refreshEmployees();
    }

    private void refreshEmployees () {
        ArrayList < Employee > employees = GlobalPandaApp.site.getAllEmployees();

        employees.sort((o1, o2) -> {
            if (Boolean.compare(o2.isActive(), o1.isActive()) != 0)
                return Boolean.compare(o2.isActive(), o1.isActive());
            else if (o1.getPosition().compareTo(o2.getPosition()) != 0)
                return o1.getPosition().compareTo(o2.getPosition());
            else if (o1.getKitch_id().compareTo(o2.getKitch_id()) != 0)
                return o1.getKitch_id().compareTo(o2.getKitch_id());
            else
                return o1.getName().compareTo(o2.getName());
        });


        table.setItems(FXCollections.observableArrayList(employees));
    }

    private void setFields() {
        inputCost.textProperty().addListener(new FloatFieldChangeListener(inputCost));
        inputCons.textProperty().addListener(new FloatFieldChangeListener(inputCons));
        inputPosition.setTooltip(new Tooltip("Посада"));
        inputPosition.getItems().addAll("Адміністратор", "Оператор", "Кухар", "Кур'єр");
        inputKitchen.setTooltip(new Tooltip("Кухня"));
        inputKitchen.setItems(FXCollections.observableArrayList(GlobalPandaApp.site.getKitchens()));
        inputStation.setTooltip(new Tooltip("Заправка"));
        inputStation.getItems().addAll("ОККО А-95", "ОККО А-92");
    }

    @FXML
    void addEmployee(ActionEvent event) {
        if(inputName.getText() != null && !inputName.getText().isEmpty()) {
            if(inputKitchen.getSelectionModel().getSelectedIndex() >= 0) {
                if(inputPosition.getSelectionModel().getSelectedIndex() >= 0) {
                    Employee employee = new Employee();
                    employee.setName(inputName.getText());
                    inputName.setText("");
                    employee.setKitchen(inputKitchen.getValue().getName());
                    employee.setKitch_id(inputKitchen.getValue().getKitch_id());
                    inputKitchen.getSelectionModel().select(-1);
                    employee.setPosition(inputPosition.getValue());
                    inputPosition.getSelectionModel().select(-1);
                    employee.setActive(true);
                    employee.setFuelCons(Float.parseFloat(inputCons.getText()));
                    inputCons.setText("0.0");
                    employee.setHourlyPrice(Float.parseFloat(inputCost.getText()));
                    inputCost.setText("0.0");
                    employee.setCharcode("");
                    employee.setGpAccess(false);
                    employee.setPassword("1111");
                    employee.setFuelStation(inputStation.getValue());
                    inputStation.getSelectionModel().select(-1);
                    employee.setMark("");
                    employee.setLogin("default_login_11");
                    GlobalPandaApp.site.save(employee);
                    refreshEmployees();
                } else {
                    inputPosition.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
                    TranslateTransition tt = new TranslateTransition(Duration.millis(100), inputPosition);
                    tt.setByX(1f);
                    tt.setCycleCount(4);
                    tt.setAutoReverse(true);

                    tt.play();
                }
            } else {
                inputKitchen.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
                TranslateTransition tt = new TranslateTransition(Duration.millis(100), inputKitchen);
                tt.setByX(1f);
                tt.setCycleCount(4);
                tt.setAutoReverse(true);

                tt.play();
            }
        } else {
            inputName.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
            TranslateTransition tt = new TranslateTransition(Duration.millis(100), inputName);
            tt.setByX(1f);
            tt.setCycleCount(4);
            tt.setAutoReverse(true);

            tt.play();
        }
    }

    class BooleanCell extends TableCell<Employee, Boolean> {
        private CheckBox checkBox;
        public BooleanCell() {
            checkBox = new CheckBox();
            checkBox.setDisable(false);
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if(isEditing())
                    commitEdit(newValue == null ? false : newValue);
            });
            this.setGraphic(checkBox);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.setEditable(true);
        }
        @Override
        public void startEdit() {
            super.startEdit();
            if (isEmpty()) {
                return;
            }
            checkBox.setDisable(false);
            checkBox.requestFocus();
        }
        @Override
        public void cancelEdit() {
            super.cancelEdit();
            checkBox.setDisable(true);
        }
        public void commitEdit(Boolean value) {
            super.commitEdit(value);
            checkBox.setDisable(true);
        }
        @Override
        public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (!isEmpty()) {
                checkBox.setSelected(item);
            }
        }
    }
}
