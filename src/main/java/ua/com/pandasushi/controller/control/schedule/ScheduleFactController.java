package ua.com.pandasushi.controller.control.schedule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import ua.com.pandasushi.controller.MainController;
import ua.com.pandasushi.controller.TabController;
import ua.com.pandasushi.controller.inputconvert.AutocompleteSpreadsheetCellType;
import ua.com.pandasushi.database.common.Employee;
import ua.com.pandasushi.database.common.Kitchens;
import ua.com.pandasushi.database.common.Schedule;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by postp on 14.08.2017.
 */
public class ScheduleFactController implements TabController {

    private MainController main;

    private static final int COLUMN_COUNT = 6;

    private static SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm");

    private static final String COURIER = "Кур'єр";
    private static final String COOKER = "Кухар";
    private static final String OPERATOR = "Оператор";

    private List<Employee> employees = new ArrayList<>();
    private List<Kitchens> kitchens = new ArrayList<>();
    private HashMap <String, Employee> employeeHashMapKeyByName = new HashMap<>();
    private HashMap <Integer, Employee> employeeHashMapKeyByID = new HashMap<>();
    private HashMap <Long, Schedule> factMap = new HashMap<>();

    private static final LocalDate TODAY = LocalDate.now();

    @FXML
    private ChoiceBox<Kitchens> kitchSelectFact;

    @FXML
    private DatePicker dateSelectFact;

    @FXML
    private AnchorPane scheduleFact;

    private SpreadsheetView sheet;

    @Override
    public void init(MainController main) {
//        this.main = main;
//
//        initLists();
//
//        dateSelectFact.setValue(TODAY);
//        dateSelectFact.valueProperty().addListener((observable, oldValue, newValue) -> {
//            initLists();
//            createSpreadSheetView();
//        });
//        dateSelectFact.setDisable(true);
//
//        kitchSelectFact.setItems(FXCollections.observableArrayList(kitchens));
//        kitchSelectFact.getSelectionModel().select(GlobalPandaApp.config.getKitchen().getKitch_id());
//        kitchSelectFact.valueProperty().addListener((observable, oldValue, newValue) -> {
//            initLists();
//            createSpreadSheetView();
//        });
//        kitchSelectFact.setDisable(true);
//
//        createSpreadSheetView();

    }

//    private void initLists() {
//        employees = GlobalPandaApp.site.getEmployeesForSchedule().stream().filter(employee -> !employee.getPosition()
//                .equals("Адміністратор") && !employee.getName().contains("Стажер") && !employee.getName().equals("-")
//                && !employee.getName().equals("Тест Тест") && !employee.getName().equals("Самовивіз")).collect(Collectors.toList());
//
//        kitchens = GlobalPandaApp.site.getKitchens().stream().filter(kitchens1 -> kitchens1.getType()
//                .equals("Кухня")).collect(Collectors.toList());
//
//        for (Schedule schedule : GlobalPandaApp.site.getFactSchedule(dateSelectFact.getValue(), kitchSelectFact.getSelectionModel().getSelectedItem().getKitch_id())) {
//            factMap = new HashMap<>();
//            factMap.put(schedule.getId(), schedule);
//        }
//
//        for (Employee employee: employees){
//            employeeHashMapKeyByID.put(employee.getId(), employee);
//            employeeHashMapKeyByName.put(employee.getName(), employee);
//        }
//    }
//
//    private void createSpreadSheetView () {
//        GridBase grid = new GridBase(50, COLUMN_COUNT);
//
//        buildGrid(grid);
//
//        sheet = new SpreadsheetView(grid);
//        sheet.setShowRowHeader(true);
//        sheet.setShowColumnHeader(false);
//        sheet.getFixedRows().add(0);
//        sheet.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//
//        AnchorPane.setTopAnchor(sheet, 40.0);
//        AnchorPane.setLeftAnchor(sheet, 10.0);
//        AnchorPane.setRightAnchor(sheet, 10.0);
//        AnchorPane.setBottomAnchor(sheet, 10.0);
//
//        scheduleFact.getChildren().add(sheet);
//
//    }
//
//    private void buildGrid(GridBase grid) {
//        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
//
//        int rowIndex = 0;
//
//        rows.add(getHeaderCells(grid, rowIndex++));
//
//        ArrayList<Schedule> listSchedule = new ArrayList<>(factMap.values());
//
//        //TODO sort
//
//        Iterator<Schedule> iterator = listSchedule.iterator();
//        while (rowIndex < grid.getRowCount()) {
//            rows.add(getDataCells(grid, rowIndex++, iterator.hasNext() ? iterator.next() : null));
//        }
//
//        grid.setRows(rows);
//    }
//
//    private ObservableList<SpreadsheetCell> getHeaderCells(GridBase grid, int row) {
//        final ObservableList<SpreadsheetCell> headerCells = FXCollections.observableArrayList();
//
//        SpreadsheetCell id = SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, "ID");
//        id.setEditable(false);
//        id.getStyleClass().add("header-cell");
//        headerCells.add(id);
//
//        SpreadsheetCell employees = SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, "Працівник");
//        employees.setEditable(false);
//        employees.getStyleClass().add("header-cell");
//        headerCells.add(employees);
//
//        SpreadsheetCell mark = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "Мітка");
//        mark.setEditable(false);
//        mark.getStyleClass().add("header-cell");
//        headerCells.add(mark);
//
//        SpreadsheetCell start = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "Початок");
//        start.setEditable(false);
//        start.getStyleClass().add("header-cell");
//        headerCells.add(start);
//
//        SpreadsheetCell end = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "Кінець");
//        end.setEditable(false);
//        end.getStyleClass().add("header-cell");
//        headerCells.add(end);
//
//        SpreadsheetCell comment = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "Коментар");
//        comment.setEditable(false);
//        comment.getStyleClass().add("header-cell");
//        headerCells.add(comment);
//
//        return headerCells;
//    }
//
//    private ObservableList<SpreadsheetCell> getDataCells(GridBase grid, int row, Schedule schedule) {
//        final ObservableList<SpreadsheetCell> dataCells = FXCollections.observableArrayList();
//
//        if (schedule != null) {
//
//            SpreadsheetCell id = SpreadsheetCellType.INTEGER.createCell(row, 0, 1, 1, (int) schedule.getId());
//            id.setEditable(false);
//            id.getStyleClass().add("schedule-cell");
//            dataCells.add(id);
//
//            SpreadsheetCell employeeName = SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, employeeHashMapKeyByID.get(schedule.getEmployeeId()).getName());
//            employeeName.setEditable(false);
//            if (employeeHashMapKeyByID.get(schedule.getEmployeeId()).getPosition().equals(COURIER)){
//                employeeName.getStyleClass().add("schedule-cell-courier");
//            }
//            if (employeeHashMapKeyByID.get(schedule.getEmployeeId()).getPosition().equals(COOKER)){
//                employeeName.getStyleClass().add("schedule-cell-cooker");
//            }
//            if (employeeHashMapKeyByID.get(schedule.getEmployeeId()).getPosition().equals(OPERATOR)){
//                employeeName.getStyleClass().add("schedule-cell-operator");
//            }
//            dataCells.add(employeeName);
//
//            SpreadsheetCell mark = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, employeeHashMapKeyByID.get(schedule.getEmployeeId()).getMark());
//            mark.setEditable(false);
//            mark.getStyleClass().add("schedule-cell");
//            dataCells.add(mark);
//
//            SpreadsheetCell start = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, simpleDateFormatTime.format(schedule.getStart()));
//            start.setEditable(true);
//            start.getStyleClass().add("nonActive");
//            start.textProperty().addListener((observable, oldValue, newValue) -> {
//                start.getStyleClass().add("active");
//            });
//            dataCells.add(start);
//
//            SpreadsheetCell end = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, simpleDateFormatTime.format(schedule.getEnd()));
//            end.setEditable(true);
//            end.getStyleClass().add("nonActive");
//            end.textProperty().addListener((observable, oldValue, newValue) -> {
//                end.getStyleClass().add("active");
//            });
//            dataCells.add(end);
//
//            SpreadsheetCell comment = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "");
//            comment.setEditable(true);
//            comment.getStyleClass().add("schedule-cell");
//            dataCells.add(comment);
//
//        } else {
//
//            SpreadsheetCell id = SpreadsheetCellType.INTEGER.createCell(row, 0, 1, 1, 0);
//            id.setEditable(false);
//            id.getStyleClass().add("schedule-cell");
//            dataCells.add(id);
//
//            SpreadsheetCell employeeName = new AutocompleteSpreadsheetCellType(getEmployeesName()).createCell(row, 1, 1, 1, "");
//            employeeName.setEditable(true);
//            employeeName.getStyleClass().add("schedule-cell");
//            dataCells.add(employeeName);
//
//            SpreadsheetCell mark = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "");
//            mark.setEditable(false);
//            mark.getStyleClass().add("schedule-cell");
//            dataCells.add(mark);
//
//            SpreadsheetCell start = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "");
//            start.setEditable(false);
//            start.getStyleClass().add("schedule-cell");
//            dataCells.add(start);
//
//            SpreadsheetCell end = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "");
//            end.setEditable(false);
//            end.getStyleClass().add("schedule-cell");
//            dataCells.add(end);
//
//            SpreadsheetCell comment = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "");
//            comment.setEditable(false);
//            comment.getStyleClass().add("schedule-cell");
//            dataCells.add(comment);
//
//            if (employeeName.getText() != null){
//                if (employeeHashMapKeyByName.get(employeeName.getText()).getPosition().equals(COURIER)){
//                    employeeName.getStyleClass().add("schedule-cell-courier");
//                }
//                if (employeeHashMapKeyByName.get(employeeName.getText()).getPosition().equals(COOKER)){
//                    employeeName.getStyleClass().add("schedule-cell-cooker");
//                }
//                if (employeeHashMapKeyByName.get(employeeName.getText()).getPosition().equals(OPERATOR)){
//                    employeeName.getStyleClass().add("schedule-cell-operator");
//                }
//                mark.itemProperty().setValue(employeeHashMapKeyByName.get(employeeName.getText()).getMark());
//                start.setEditable(true);
//                end.setEditable(true);
//                comment.setEditable(true);
//            }
//
//        }
//
//        return dataCells;
//    }
//
//    private ObservableList<String> getEmployeesName(){
//        ObservableList<String> employeesName = FXCollections.observableArrayList();
//
//        for (Employee employee: employees){
//            employeesName.add(employee.getName());
//        }
//
//        return employeesName;
//    }
//
//    private int[] intTimeParse(String text) {
//        assert text != null;
//
//        int [] intTime = {0, 0};
//
//        if (!text.isEmpty() && text.length() == 5) {
//            int hours = Integer.parseInt(text.substring(0, 2));
//            int minutes = Integer.parseInt(text.substring(3, 5));
//
//            return new int [] {hours, minutes};
//        }
//
//        return intTime;
//    }

    public void writeToDBFact(ActionEvent actionEvent) {
//        initLists();
//
//        for (ObservableList<SpreadsheetCell> row : sheet.getGrid().getRows()) {
//            if (row.get(3).getText() != null && !row.get(3).getText().isEmpty()) {
//                if (row.get(3).getStyleClass().contains("active")){
//                    int [] time = intTimeParse(row.get(3).getText());
//
//                    Schedule schedule = factMap.get((long) Integer.parseInt(row.get(0).getText()));
//
//                    LocalDate startTime = LocalDate.now();
//                    startTime.atTime(time[0], time[1]);
//                    Date start = Date.from(startTime.atStartOfDay(ZoneId.systemDefault()).toInstant());
//                    schedule.setStart(start);
//
//                    schedule.setPlan(false);
//
////                    GlobalPandaApp.site.saveSchedule(schedule);
//                }
//            }
//
//            if (row.get(4).getText() != null && !row.get(4).getText().isEmpty()) {
//                if (row.get(4).getStyleClass().contains("active")){
//                    int [] time = intTimeParse(row.get(4).getText());
//
//                    Schedule schedule = factMap.get((long) Integer.parseInt(row.get(0).getText()));
//
//                    LocalDate endTime = LocalDate.now();
//                    endTime.atTime(time[0], time[1]);
//                    if (time[0] == 0){
//                        endTime.plusDays(1);
//                    }
//                    Date end = Date.from(endTime.atStartOfDay(ZoneId.systemDefault()).toInstant());
//                    schedule.setStart(end);
//
//                    schedule.setPlan(false);
//
////                    GlobalPandaApp.site.saveSchedule(schedule);
//                }
//            }
//
//            if (row.get(5).getText() != null && !row.get(5).getText().isEmpty()) {
//                Schedule schedule = factMap.get((long) Integer.parseInt(row.get(0).getText()));
//
////                schedule.setComment(row.get(5).getText());
//
////                GlobalPandaApp.site.saveSchedule(schedule);
//            }
//        }
    }

}
