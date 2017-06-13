package ua.com.pandasushi.controller.control.schedule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import ua.com.pandasushi.database.common.Employee;
import ua.com.pandasushi.database.common.Kitchens;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Taras on 12.06.2017.
 */
public class ScheduleController implements TabController {
    MainController main;

    private static final int COLUMN_COUNT = 9;
    private static final String REGULAR_EXP_FOR_TIME = "^(([0,1][0-9])|(2[0-3]))[0-5][0-9]$";

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM");

    private List<Employee> employees = new ArrayList<>();
    private List<Kitchens> kitchens = new ArrayList<>();

    private LocalDate today = LocalDate.now();

    @FXML
    ChoiceBox<Kitchens> kitchSelect;

    @FXML
    DatePicker dateSelect;

    @FXML
    AnchorPane planTab;

    private SpreadsheetView sheet;

    @Override
    public void init(MainController main) {
        this.main = main;

        initLists();

        dateSelect.setValue(today);
        dateSelect.valueProperty().addListener((observable, oldValue, newValue) -> {
            createSpreadSheetView(newValue);
        });

        kitchSelect.setItems(FXCollections.observableArrayList(kitchens));
//        kitchSelect.getSelectionModel().select(GlobalPandaApp.config.getKitchen());
//        kitchSelect.valueProperty().addListener((observable, oldValue, newValue) -> {
//            createSpreadSheetView(dateSelect.getValue());
//        });

        createSpreadSheetView(today);

        for (Employee employee: employees){
            System.out.println(employee.getName() + " - " + employee.getKitchen() + " - " + employee.getPosition());
        }

    }

    private ObservableList<SpreadsheetCell> getHeaderCells (LocalDate date, GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> headerCells = FXCollections.observableArrayList();

        LocalDate [] period = getPeriod(date);

        SpreadsheetCell employees = SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, "Працівник");
        employees.setEditable(false);
        employees.getStyleClass().add("header-cell");
        headerCells.add(employees);

        SpreadsheetCell mark = SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, "Мітка");
        mark.setEditable(false);
        mark.getStyleClass().add("header-cell");
        headerCells.add(mark);

        SpreadsheetCell monday = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "ПН ("
                + period[0].format(dateTimeFormatter) +")");
        monday.setEditable(false);
        monday.getStyleClass().add("header-cell");
        headerCells.add(monday);

        SpreadsheetCell tuesday = SpreadsheetCellType.STRING.createCell(row, 3, 1, 1, "ВТ ("
                + period[1].format(dateTimeFormatter) +")");
        tuesday.setEditable(false);
        tuesday.getStyleClass().add("header-cell");
        headerCells.add(tuesday);

        SpreadsheetCell wednesday = SpreadsheetCellType.STRING.createCell(row, 4, 1, 1, "СР ("
                + period[2].format(dateTimeFormatter) +")");
        wednesday.setEditable(false);
        wednesday.getStyleClass().add("header-cell");
        headerCells.add(wednesday);

        SpreadsheetCell thursday = SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, "ЧТ ("
                + period[3].format(dateTimeFormatter) +")");
        thursday.setEditable(false);
        thursday.getStyleClass().add("header-cell");
        headerCells.add(thursday);

        SpreadsheetCell friday = SpreadsheetCellType.STRING.createCell(row, 6, 1, 1, "ПТ ("
                + period[4].format(dateTimeFormatter) +")");
        friday.setEditable(false);
        friday.getStyleClass().add("header-cell");
        headerCells.add(friday);

        SpreadsheetCell saturday = SpreadsheetCellType.STRING.createCell(row, 7, 1, 1, "СБ ("
                + period[5].format(dateTimeFormatter) +")");
        saturday.setEditable(false);
        saturday.getStyleClass().add("header-cell");
        headerCells.add(saturday);

        SpreadsheetCell sunday = SpreadsheetCellType.STRING.createCell(row, 8, 1, 1, "НД ("
                + period[6].format(dateTimeFormatter) +")");
        sunday.setEditable(false);
        sunday.getStyleClass().add("header-cell");
        headerCells.add(sunday);

        return headerCells;
    }

    private ObservableList<SpreadsheetCell> getDataCells (Employee employee, GridBase grid, int row){
        final ObservableList<SpreadsheetCell> dataCells = FXCollections.observableArrayList();

            SpreadsheetCell employeeName = SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, employee.getName());
            employeeName.setEditable(false);
            if (employee.getKitchen().equals("Варшавська")){
                if (employee.getPosition().equals("Кухар")){
                    employeeName.getStyleClass().add("schedule-cell-cooker");
                }
                if (employee.getPosition().equals("Оператор")){
                    employeeName.getStyleClass().add("schedule-cell-operator");
                }
            } else {
                employeeName.getStyleClass().add("schedule-cell-another-kitchen");
            }
            dataCells.add(employeeName);

            SpreadsheetCell mark = SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, "");
            mark.setEditable(false);
            mark.getStyleClass().add("schedule-cell");
            dataCells.add(mark);

            SpreadsheetCell monday = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "");
            monday.setEditable(true);
            monday.getStyleClass().add("schedule-cell");
            dataCells.add(monday);

            SpreadsheetCell tuesday = SpreadsheetCellType.STRING.createCell(row, 3, 1, 1, "");
            tuesday.setEditable(true);
            tuesday.getStyleClass().add("schedule-cell");
            dataCells.add(tuesday);

            SpreadsheetCell wednesday = SpreadsheetCellType.STRING.createCell(row, 4, 1, 1, "");
            wednesday.setEditable(true);
            wednesday.getStyleClass().add("schedule-cell");
            dataCells.add(wednesday);

            SpreadsheetCell thursday = SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, "");
            thursday.setEditable(true);
            thursday.getStyleClass().add("schedule-cell");
            dataCells.add(thursday);

            SpreadsheetCell friday = SpreadsheetCellType.STRING.createCell(row, 6, 1, 1, "");
            friday.setEditable(true);
            friday.getStyleClass().add("schedule-cell");
            dataCells.add(friday);

            SpreadsheetCell saturday = SpreadsheetCellType.STRING.createCell(row, 7, 1, 1, "");
            saturday.setEditable(true);
            saturday.getStyleClass().add("schedule-cell");
            dataCells.add(saturday);

            SpreadsheetCell sunday = SpreadsheetCellType.STRING.createCell(row, 8, 1, 1, "");
            sunday.setEditable(true);
            sunday.getStyleClass().add("schedule-cell");
            dataCells.add(sunday);

        return dataCells;
    }

    private void buildGrid (LocalDate date, GridBase grid) {
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();

        int rowIndex = 0;
        rows.add(getHeaderCells(date, grid, rowIndex++));

        sortEmployees(employees);

        for (Employee employee: employees) {
            rows.add(getDataCells(employee, grid, rowIndex++));

        }

        grid.setRows(rows);
    }

    private void createSpreadSheetView (LocalDate date) {
        GridBase grid = new GridBase(employees.size()+1, COLUMN_COUNT);

        buildGrid(date, grid);

        sheet = new SpreadsheetView(grid);
        sheet.setShowRowHeader(true);
        sheet.setShowColumnHeader(false);

        AnchorPane.setTopAnchor(sheet, 40.0);
        AnchorPane.setLeftAnchor(sheet, 10.0);
        AnchorPane.setRightAnchor(sheet, 10.0);
        AnchorPane.setBottomAnchor(sheet, 10.0);

        planTab.getChildren().add(sheet);

    }

    private LocalDate [] getPeriod(LocalDate date) {
        LocalDate [] period = new LocalDate[7];
        period[0] = date.minusDays(date.getDayOfWeek().getValue() - 1);
        period[1] = date.plusDays(2 - date.getDayOfWeek().getValue());
        period[2] = date.plusDays(3 - date.getDayOfWeek().getValue());
        period[3] = date.plusDays(4 - date.getDayOfWeek().getValue());
        period[4] = date.plusDays(5 - date.getDayOfWeek().getValue());
        period[5] = date.plusDays(6 - date.getDayOfWeek().getValue());
        period[6] = date.plusDays(7 - date.getDayOfWeek().getValue());
        return period;
    }

    private void sortEmployees (List<Employee> employees){
//
//        List<Employee> kitchenList;
//
//        if (kitchSelect.getSelectionModel().isSelected(SYHIV)){
//            kitchenList = employees;
//            kitchenList.stream().filter(employee -> employee.getKitchen().equals("Сихів")).collect(Collectors.toList());
//            kitchenList.sort(Comparator.comparing(Employee::getPosition).thenComparing(Employee::getName));
//        }

        employees.sort(Comparator.comparing(Employee::getKitchen).thenComparing(Employee::getPosition).thenComparing(Employee::getName));

    }

    private void initLists(){
        employees = GlobalPandaApp.site.getEmployeesForLogin().stream().filter(employee -> !employee.getPosition()
                .equals("Адміністратор")).collect(Collectors.toList());

        kitchens = GlobalPandaApp.site.getKitchens().stream().filter(kitchens1 -> kitchens1.getType()
                .equals("Кухня")).collect(Collectors.toList());
    }

    private int [] stringTimeParse (String time){

        int [] intTime = null;

        if (time.length() == 4){

            int hourBegin = Integer.parseInt(time.substring(0,1));
            int minutesBegin = Integer.parseInt(time.substring(2));

            int [] intTimeBegin = {hourBegin, minutesBegin};

            return intTimeBegin;
        } if (time.length() == 8){

            int hourBegin = Integer.parseInt(time.substring(0,1));
            int minutesBegin = Integer.parseInt(time.substring(2,3));
            int hourEnd = Integer.parseInt(time.substring(4,5));
            int minutesEnd = Integer.parseInt(time.substring(6));

            int [] intTimeBeginAndEnd = {hourBegin, minutesBegin, hourEnd, minutesEnd};

            return  intTimeBeginAndEnd;
        }

        return intTime;
    }

    private boolean checkWithRegExp(String time){
        boolean bool = false;

        if (time.length() == 4){
            Pattern p = Pattern.compile(REGULAR_EXP_FOR_TIME);
            Matcher m = p.matcher(time);
            return m.matches();
        }

        if (time.length() == 8){
            Pattern p = Pattern.compile(REGULAR_EXP_FOR_TIME);
            Matcher m = p.matcher(time);
            return m.matches();
        }

        return bool;
    }
}
