package ua.com.pandasushi.controller.control.schedule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.spreadsheet.*;
import ua.com.pandasushi.controller.MainController;
import ua.com.pandasushi.controller.TabController;
import ua.com.pandasushi.database.common.Employee;
import ua.com.pandasushi.database.common.KitchProperties;
import ua.com.pandasushi.database.common.Kitchens;
import ua.com.pandasushi.database.common.Schedule;
import ua.com.pandasushi.main.GlobalPandaApp;
import ua.com.pandasushi.main.MainFX;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Taras on 12.06.2017.
 */
public class SchedulePlanController implements TabController {
    private MainController main;

    private static final int COLUMN_COUNT = 10;
    private static final String REGULAR_EXP_FOR_TIME = "^(([0,1][0-9])|(2[0-3]))[0-5][0-9]$";
    private static final String REGULAR_EXP_FOR_TIME_WITH_PLUS = "^(([0,1][0-9])|(2[0-3]))[0-5][0-9][+]$";
    private static final String REGULAR_EXP_FOR_TIME_START_END = "^(([0,1][0-9])|(2[0-3]))[0-5][0-9](([0,1][0-9])|(2[0-3]))[0-5][0-9]$";
    private static final String REGULAR_EXP_FOR_TIME_START_END_WITH_PLUS = "^(([0,1][0-9])|(2[0-3]))[0-5][0-9](([0,1][0-9])|(2[0-3]))[0-5][0-9][+]$";

    private static final String COURIER = "Кур'єр";
    private static final String COOKER = "Кухар";
    private static final String OPERATOR = "Оператор";

    private static final LocalDate TODAY = LocalDate.now();

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM");
    private static DateTimeFormatter dateTimeFormatterWithYear = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    static final HashMap<String, Employee> employeeMap = new HashMap<>();

    private HashMap<LocalDate, HashMap<Integer, Schedule>> scheduleMap = new HashMap<>();

//    private List<Employee> sel = new ArrayList<>();
//    private List<Employee> notSel = new ArrayList<>();
    private List<Kitchens> kitchens = new ArrayList<>();
    private List<String> positions = new ArrayList<>();

    private SpreadsheetCell [] totals = new SpreadsheetCell[7];
    private SpreadsheetCell [] quantityOfShifts;
    private LocalDate [] dates;
    private KitchProperties [] kitchProperties;

    private ArrayList<Employee> inView;

    @FXML
    private ChoiceBox<Kitchens> kitchSelect;

    @FXML
    private ChoiceBox<String> posSelect;

    @FXML
    private DatePicker dateSelect;

    @FXML
    private AnchorPane schedulePlan;

    private SpreadsheetView sheet;

    @Override
    public void init (MainController main) {
        this.main = main;

        dateSelect.setValue(TODAY);
        dates = getPeriod(dateSelect.getValue());

        dateSelect.valueProperty().addListener((observable, oldValue, newValue) -> {
            writeToDB(posSelect.getValue());
            dates = getPeriod(dateSelect.getValue());
            initLists();
            createSpreadSheetView();
        });

        positions.add(0, COURIER);
        positions.add(1, COOKER);
        positions.add(2, OPERATOR);

        posSelect.setItems(FXCollections.observableArrayList(positions));

        if (GlobalPandaApp.config.getOperator().getPosition().equals("Адміністратор")) {
            posSelect.getSelectionModel().select(0);
        } else {
            posSelect.getSelectionModel().select(GlobalPandaApp.config.getOperator().getPosition());
            posSelect.setDisable(true);
        }

        posSelect.valueProperty().addListener((observable, oldValue, newValue) -> {
            writeToDB(oldValue);
            initLists();
            createSpreadSheetView();
        });

        initLists();

        kitchSelect.setItems(FXCollections.observableArrayList(kitchens));
        kitchSelect.getSelectionModel().select(GlobalPandaApp.config.getKitchen());
        if( kitchSelect.getSelectionModel().getSelectedIndex() < 0)
            kitchSelect.getSelectionModel().select(0);
        kitchSelect.valueProperty().addListener((observable, oldValue, newValue) -> {
            writeToDB(posSelect.getValue());
            createSpreadSheetView();
        });

        sortEmployees(inView);
        createSpreadSheetView();

    }

    private void initLists (){
        if(employeeMap.isEmpty()) {

            List<Employee> list = GlobalPandaApp.site.getEmployeesForSchedule().stream().filter(employee -> !employee.getPosition()
                    .equals("Адміністратор") && !employee.getName().contains("Стажер") && !employee.getName().equals("-")
                    && !employee.getName().equals("Тест Тест") && !employee.getName().equals("Самовивіз")).collect(Collectors.toList());

            for (Employee employee : list)
                employeeMap.put(employee.getName(), employee);

        }

        kitchens = GlobalPandaApp.site.getKitchens().stream().filter(kitchens1 -> kitchens1.getType()
                .equals("Кухня")).collect(Collectors.toList());

        scheduleMap = GlobalPandaApp.site.getSceduleMapPlan(dates[0], dates[6], posSelect.getValue());

        inView = new ArrayList<>();

        for (Employee employee : employeeMap.values())
            if (employee.getPosition().equals(posSelect.getValue()))
                inView.add(employee);

    }

    private void createSpreadSheetView () {
        GridBase grid = new GridBase(employeeMap.values().size()+1, COLUMN_COUNT);

        String kitchenName = kitchSelect.getValue().getName();

        int quantityOfEmployees;

//        sel = inView.stream().filter( employee -> employee.getKitchen().equals(kitchenName))
//                .collect(Collectors.toList());

//        for (Employee emp : inView) {
//            if (emp.getKitchen().equals(kitchSelect.getValue().getName())) {
//                sel.add(emp);
//                continue;
//            }
//            for (HashMap<Integer, Schedule> hm : scheduleMap.values()) {
//                if (hm.get(emp.getId()).getKitchId().equals(kitchSelect.getValue().getKitch_id())) {
//                    sel.add(emp);
//                }
//            }
//        }

//        quantityOfEmployees = sel.size();
//
//        notSel = inView.stream().filter(employee -> !employee.getKitchen().equals(kitchenName))
//                .collect(Collectors.toList());
//
//        quantityOfEmployees += notSel.size();

        quantityOfEmployees = inView.size();

        quantityOfShifts = new SpreadsheetCell[quantityOfEmployees];

        buildGrid(grid);

        sheet = new SpreadsheetView(grid);
        sheet.setShowRowHeader(true);
        sheet.setShowColumnHeader(false);
        sheet.getFixedRows().add(0);
        sheet.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        AnchorPane.setTopAnchor(sheet, 40.0);
        AnchorPane.setLeftAnchor(sheet, 10.0);
        AnchorPane.setRightAnchor(sheet, 10.0);
        AnchorPane.setBottomAnchor(sheet, 10.0);

        schedulePlan.getChildren().add(sheet);
        updateTotals(posSelect.getValue());
    }

    private void buildGrid (GridBase grid) {
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();

        int indexOfEmployee = 0;

        int rowIndex = 0;

        kitchProperties = getKitchProperties(kitchSelect.getValue().getKitch_id());

        List<Schedule> schedules = new ArrayList<>();

        for (HashMap<Integer, Schedule> hm : scheduleMap.values())
            schedules.addAll(hm.values());

        rows.add(getHeaderCells(grid, rowIndex++));

        rows.add(getDataRequestCells(grid, rowIndex++));

        sortEmployees(inView);

        for (Employee employee: inView){
            rows.add(getDataCells(schedules, employee, grid, rowIndex++, indexOfEmployee++));
        }

//        for (Employee employee: sel){
//            rows.add(getDataCells(schedules, employee, grid, rowIndex++, indexOfEmployee++));
//        }
//
//        for (Employee employee: notSel){
//            rows.add(getEmployeeFromAnotherKitchenCells(schedules, employee, grid, rowIndex++, indexOfEmployee++));
//        }

        grid.setRows(rows);

    }

    private ObservableList<SpreadsheetCell> getHeaderCells (GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> headerCells = FXCollections.observableArrayList();

        SpreadsheetCell employees = SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, "Працівник");
        employees.setEditable(false);
        employees.getStyleClass().add("header-cell");
        headerCells.add(employees);

        SpreadsheetCell mark = SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, "Мітка");
        mark.setEditable(false);
        mark.getStyleClass().add("header-cell");
        headerCells.add(mark);

        SpreadsheetCell shift = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "Зміни/\nгодини");
        shift.setEditable(false);
        shift.getStyleClass().add("header-cell");
        headerCells.add(shift);

        SpreadsheetCell monday = SpreadsheetCellType.STRING.createCell(row, 3, 1, 1, "ПН \n("
                + dates[0].format(dateTimeFormatter) +")");
        monday.setEditable(false);
        monday.getStyleClass().add("header-cell");
        headerCells.add(monday);

        SpreadsheetCell tuesday = SpreadsheetCellType.STRING.createCell(row, 4, 1, 1, "ВТ \n("
                + dates[1].format(dateTimeFormatter) +")");
        tuesday.setEditable(false);
        tuesday.getStyleClass().add("header-cell");
        headerCells.add(tuesday);

        SpreadsheetCell wednesday = SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, "СР \n("
                + dates[2].format(dateTimeFormatter) +")");
        wednesday.setEditable(false);
        wednesday.getStyleClass().add("header-cell");
        headerCells.add(wednesday);

        SpreadsheetCell thursday = SpreadsheetCellType.STRING.createCell(row, 6, 1, 1, "ЧТ \n("
                + dates[3].format(dateTimeFormatter) +")");
        thursday.setEditable(false);
        thursday.getStyleClass().add("header-cell");
        headerCells.add(thursday);

        SpreadsheetCell friday = SpreadsheetCellType.STRING.createCell(row, 7, 1, 1, "ПТ \n("
                + dates[4].format(dateTimeFormatter) +")");
        friday.setEditable(false);
        friday.getStyleClass().add("header-cell");
        headerCells.add(friday);

        SpreadsheetCell saturday = SpreadsheetCellType.STRING.createCell(row, 8, 1, 1, "СБ \n("
                + dates[5].format(dateTimeFormatter) +")");
        saturday.setEditable(false);
        saturday.getStyleClass().add("header-cell");
        headerCells.add(saturday);

        SpreadsheetCell sunday = SpreadsheetCellType.STRING.createCell(row, 9, 1, 1, "НД \n("
                + dates[6].format(dateTimeFormatter) +")");
        sunday.setEditable(false);
        sunday.getStyleClass().add("header-cell");
        headerCells.add(sunday);

        return headerCells;
    }

    private ObservableList<SpreadsheetCell> getDataRequestCells(GridBase grid, int row){
        final ObservableList<SpreadsheetCell> dataRequestCells = FXCollections.observableArrayList();

        SpreadsheetCell request = SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, "Підсумок: ");
        request.setEditable(false);
        request.getStyleClass().add("schedule-cell");
        dataRequestCells.add(request);

        SpreadsheetCell mark = SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, "");
        mark.setEditable(false);
        mark.getStyleClass().add("schedule-cell");
        dataRequestCells.add(mark);

        SpreadsheetCell shift = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "");
        shift.setEditable(false);
        shift.getStyleClass().add("schedule-cell");
        dataRequestCells.add(shift);

        SpreadsheetCell mondayRequest = SpreadsheetCellType.STRING.createCell(row, 3, 1, 1, "");
        mondayRequest.setEditable(false);
        mondayRequest.getStyleClass().add("schedule-cell");
        dataRequestCells.add(mondayRequest);
        totals[0] = mondayRequest;

        SpreadsheetCell tuesdayRequest = SpreadsheetCellType.STRING.createCell(row, 4, 1, 1, "");
        tuesdayRequest.setEditable(false);
        tuesdayRequest.getStyleClass().add("schedule-cell");
        dataRequestCells.add(tuesdayRequest);
        totals[1] = tuesdayRequest;

        SpreadsheetCell wednesdayRequest = SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, "");
        wednesdayRequest.setEditable(false);
        wednesdayRequest.getStyleClass().add("schedule-cell");
        dataRequestCells.add(wednesdayRequest);
        totals[2] = wednesdayRequest;

        SpreadsheetCell thursdayRequest = SpreadsheetCellType.STRING.createCell(row, 6, 1, 1, "");
        thursdayRequest.setEditable(false);
        thursdayRequest.getStyleClass().add("schedule-cell");
        dataRequestCells.add(thursdayRequest);
        totals[3] = thursdayRequest;

        SpreadsheetCell fridayRequest = SpreadsheetCellType.STRING.createCell(row, 7, 1, 1, "");
        fridayRequest.setEditable(false);
        fridayRequest.getStyleClass().add("schedule-cell");
        dataRequestCells.add(fridayRequest);
        totals[4] = fridayRequest;

        SpreadsheetCell saturdayRequest = SpreadsheetCellType.STRING.createCell(row, 8, 1, 1, "");
        saturdayRequest.setEditable(false);
        saturdayRequest.getStyleClass().add("schedule-cell");
        dataRequestCells.add(saturdayRequest);
        totals[5] = saturdayRequest;

        SpreadsheetCell sundayRequest = SpreadsheetCellType.STRING.createCell(row, 9, 1, 1, "");
        sundayRequest.setEditable(false);
        sundayRequest.getStyleClass().add("schedule-cell");
        dataRequestCells.add(sundayRequest);
        totals[6] = sundayRequest;

        return dataRequestCells;
    }

    private ObservableList<SpreadsheetCell> getDataCells (List <Schedule> schedules, Employee employee, GridBase grid, int row, int indexOfEmployee){
        final ObservableList<SpreadsheetCell> dataCells = FXCollections.observableArrayList();

        int quantityOfEmployees = 0;
        double time = 0;

        SpreadsheetCell employeeName = SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, employee.getName());
        employeeName.setEditable(false);

        if ((int)employee.getKitch_id() == kitchSelect.getValue().getKitch_id()){
            if (employee.getPosition().equals(COURIER)){
                employeeName.getStyleClass().add("schedule-cell-courier");
            }
            if (employee.getPosition().equals(COOKER)){
                employeeName.getStyleClass().add("schedule-cell-cooker");
            }
            if (employee.getPosition().equals(OPERATOR)){
                employeeName.getStyleClass().add("schedule-cell-operator");
            }
        } else {
            employeeName.getStyleClass().add("schedule-cell-anotherKitchen");
        }

        dataCells.add(employeeName);

        SpreadsheetCell mark = SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, employee.getMark());
        mark.setEditable(false);
        mark.getStyleClass().add("schedule-cell");
        dataCells.add(mark);

        SpreadsheetCell shift = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "");
        shift.setEditable(false);
        shift.getStyleClass().add("schedule-cell");
        quantityOfShifts[indexOfEmployee] = shift;
        if (shift.getText().isEmpty()){
            shift.itemProperty().setValue("0 / 0.0");
        }
        dataCells.add(shift);

        SpreadsheetCell monday = SpreadsheetCellType.STRING.createCell(row, 3, 1, 1, "");
        monday.setEditable(true);
        monday.getStyleClass().add("schedule-cell");
        monday.textProperty().addListener((observable, oldValue, newValue)
                -> textPropertiesWithCheck(monday, dates[0], employee, kitchProperties[0], indexOfEmployee));
        setScheduleInTable(schedules, dates[0], employee, monday);
        if (!monday.getText().isEmpty() && !monday.getText().equals("-")){
            quantityOfEmployees++;
            Date [] dates = countTime(monday.getText());
            time += dates[1].getTime() - dates[0].getTime();
        }
        dataCells.add(monday);

        SpreadsheetCell tuesday = SpreadsheetCellType.STRING.createCell(row, 4, 1, 1, "");
        tuesday.setEditable(true);
        tuesday.getStyleClass().add("schedule-cell");
        tuesday.textProperty().addListener((observable, oldValue, newValue)
                -> textPropertiesWithCheck(tuesday, dates[1], employee, kitchProperties[1], indexOfEmployee));
        setScheduleInTable(schedules, dates[1], employee, tuesday);
        if (!tuesday.getText().isEmpty() && !tuesday.getText().equals("-")){
            quantityOfEmployees++;
            Date [] dates = countTime(tuesday.getText());
            time += dates[1].getTime() - dates[0].getTime();
        }
        dataCells.add(tuesday);

        SpreadsheetCell wednesday = SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, "");
        wednesday.setEditable(true);
        wednesday.getStyleClass().add("schedule-cell");
        wednesday.textProperty().addListener((observable, oldValue, newValue)
                -> textPropertiesWithCheck(wednesday, dates[2], employee, kitchProperties[2], indexOfEmployee));
        setScheduleInTable(schedules, dates[2], employee, wednesday);
        if (!wednesday.getText().isEmpty() && !wednesday.getText().equals("-")){
            quantityOfEmployees++;
            Date [] dates = countTime(wednesday.getText());
            time += dates[1].getTime() - dates[0].getTime();
        }
        dataCells.add(wednesday);

        SpreadsheetCell thursday = SpreadsheetCellType.STRING.createCell(row, 6, 1, 1, "");
        thursday.setEditable(true);
        thursday.getStyleClass().add("schedule-cell");
        thursday.textProperty().addListener((observable, oldValue, newValue)
                -> textPropertiesWithCheck(thursday, dates[3], employee, kitchProperties[3], indexOfEmployee));
        setScheduleInTable(schedules, dates[3], employee, thursday);
        if (!thursday.getText().isEmpty() && !thursday.getText().equals("-")){
            quantityOfEmployees++;
            Date [] dates = countTime(thursday.getText());
            time += dates[1].getTime() - dates[0].getTime();
        }
        dataCells.add(thursday);

        SpreadsheetCell friday = SpreadsheetCellType.STRING.createCell(row, 7, 1, 1, "");
        friday.setEditable(true);
        friday.getStyleClass().add("schedule-cell");
        friday.textProperty().addListener((observable, oldValue, newValue)
                -> textPropertiesWithCheck(friday, dates[4], employee, kitchProperties[4], indexOfEmployee));
        setScheduleInTable(schedules, dates[4], employee, friday);
        if (!friday.getText().isEmpty() && !friday.getText().equals("-")){
            quantityOfEmployees++;
            Date [] dates = countTime(friday.getText());
            time += dates[1].getTime() - dates[0].getTime();
        }
        dataCells.add(friday);

        SpreadsheetCell saturday = SpreadsheetCellType.STRING.createCell(row, 8, 1, 1, "");
        saturday.setEditable(true);
        saturday.getStyleClass().add("schedule-cell");
        saturday.textProperty().addListener((observable, oldValue, newValue)
                -> textPropertiesWithCheck(saturday, dates[5], employee, kitchProperties[5], indexOfEmployee));
        setScheduleInTable(schedules, dates[5], employee, saturday);
        if (!saturday.getText().isEmpty() && !saturday.getText().equals("-")){
            quantityOfEmployees++;
            Date [] dates = countTime(saturday.getText());
            time += dates[1].getTime() - dates[0].getTime();
        }
        dataCells.add(saturday);

        SpreadsheetCell sunday = SpreadsheetCellType.STRING.createCell(row, 9, 1, 1, "");
        sunday.setEditable(true);
        sunday.getStyleClass().add("schedule-cell");
        sunday.textProperty().addListener((observable, oldValue, newValue)
                -> textPropertiesWithCheck(sunday, dates[6], employee, kitchProperties[6], indexOfEmployee));
        setScheduleInTable(schedules, dates[6], employee, sunday);
        if (!sunday.getText().isEmpty() && !sunday.getText().equals("-")){
            quantityOfEmployees++;
            Date [] dates = countTime(sunday.getText());
            time += dates[1].getTime() - dates[0].getTime();
        }
        dataCells.add(sunday);

        double hours = time / (60*60*1000);

        shift.itemProperty().setValue(String.valueOf(quantityOfEmployees) + " / " + hours);

        return dataCells;

    }

//    private ObservableList<SpreadsheetCell> getEmployeeFromAnotherKitchenCells (List <Schedule> schedules, Employee employee, GridBase grid, int row, int indexOfEmployee){
//        final ObservableList<SpreadsheetCell> dataCells = FXCollections.observableArrayList();
//
//        int quantityOfEmployees = 0;
//        double time = 0;
//
//        SpreadsheetCell employeeName = SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, employee.getName());
//        employeeName.setEditable(false);
//        employeeName.getStyleClass().add("schedule-cell-anotherKitchen");
//        dataCells.add(employeeName);
//
//        SpreadsheetCell mark = SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, employee.getMark());
//        mark.setEditable(false);
//        mark.getStyleClass().add("schedule-cell");
//        dataCells.add(mark);
//
//        SpreadsheetCell shift = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "");
//        shift.setEditable(false);
//        shift.getStyleClass().add("schedule-cell");
//        quantityOfShifts[indexOfEmployee] = shift;
//        if (shift.getText().isEmpty()){
//            shift.itemProperty().setValue("0 / 0.0");
//        }
//        dataCells.add(shift);
//
//        SpreadsheetCell monday = SpreadsheetCellType.STRING.createCell(row, 3, 1, 1, "");
//        monday.setEditable(true);
//        monday.getStyleClass().add("schedule-cell");
//        monday.textProperty().addListener((observable, oldValue, newValue)
//                -> textPropertiesWithCheck(monday, dates[0], employee, kitchProperties[0], indexOfEmployee));
//        setScheduleInTable(schedules, dates[0], employee, monday);
//        if (!monday.getText().isEmpty() && !monday.getText().equals("-")){
//            quantityOfEmployees++;
//            Date [] dates = countTime(monday.getText());
//            time += dates[1].getTime() - dates[0].getTime();
//        }
//        dataCells.add(monday);
//
//        SpreadsheetCell tuesday = SpreadsheetCellType.STRING.createCell(row, 4, 1, 1, "");
//        tuesday.setEditable(true);
//        tuesday.getStyleClass().add("schedule-cell");
//        tuesday.textProperty().addListener((observable, oldValue, newValue)
//                -> textPropertiesWithCheck(tuesday, dates[1], employee, kitchProperties[1], indexOfEmployee));
//        setScheduleInTable(schedules, dates[1], employee, tuesday);
//        if (!tuesday.getText().isEmpty() && !tuesday.getText().equals("-")){
//            quantityOfEmployees++;
//            Date [] dates = countTime(tuesday.getText());
//            time += dates[1].getTime() - dates[0].getTime();
//        }
//        dataCells.add(tuesday);
//
//        SpreadsheetCell wednesday = SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, "");
//        wednesday.setEditable(true);
//        wednesday.getStyleClass().add("schedule-cell");
//        wednesday.textProperty().addListener((observable, oldValue, newValue)
//                -> textPropertiesWithCheck(wednesday, dates[2], employee, kitchProperties[2], indexOfEmployee));
//        setScheduleInTable(schedules, dates[2], employee, wednesday);
//        if (!wednesday.getText().isEmpty() && !wednesday.getText().equals("-")){
//            quantityOfEmployees++;
//            Date [] dates = countTime(wednesday.getText());
//            time += dates[1].getTime() - dates[0].getTime();
//        }
//        dataCells.add(wednesday);
//
//        SpreadsheetCell thursday = SpreadsheetCellType.STRING.createCell(row, 6, 1, 1, "");
//        thursday.setEditable(true);
//        thursday.getStyleClass().add("schedule-cell");
//        thursday.textProperty().addListener((observable, oldValue, newValue)
//                -> textPropertiesWithCheck(thursday, dates[3], employee, kitchProperties[3], indexOfEmployee));
//        setScheduleInTable(schedules, dates[3], employee, thursday);
//        if (!thursday.getText().isEmpty() && !thursday.getText().equals("-")){
//            quantityOfEmployees++;
//            Date [] dates = countTime(thursday.getText());
//            time += dates[1].getTime() - dates[0].getTime();
//        }
//        dataCells.add(thursday);
//
//        SpreadsheetCell friday = SpreadsheetCellType.STRING.createCell(row, 7, 1, 1, "");
//        friday.setEditable(true);
//        friday.getStyleClass().add("schedule-cell");
//        friday.textProperty().addListener((observable, oldValue, newValue)
//                -> textPropertiesWithCheck(friday, dates[4], employee, kitchProperties[4], indexOfEmployee));
//        setScheduleInTable(schedules, dates[4], employee, friday);
//        if (!friday.getText().isEmpty() && !friday.getText().equals("-")){
//            quantityOfEmployees++;
//            Date [] dates = countTime(friday.getText());
//            time += dates[1].getTime() - dates[0].getTime();
//        }
//        dataCells.add(friday);
//
//        SpreadsheetCell saturday = SpreadsheetCellType.STRING.createCell(row, 8, 1, 1, "");
//        saturday.setEditable(true);
//        saturday.getStyleClass().add("schedule-cell");
//        saturday.textProperty().addListener((observable, oldValue, newValue)
//                -> textPropertiesWithCheck(saturday, dates[5], employee, kitchProperties[5], indexOfEmployee));
//        setScheduleInTable(schedules, dates[5], employee, saturday);
//        if (!saturday.getText().isEmpty() && !saturday.getText().equals("-")){
//            quantityOfEmployees++;
//            Date [] dates = countTime(saturday.getText());
//            time += dates[1].getTime() - dates[0].getTime();
//        }
//        dataCells.add(saturday);
//
//        SpreadsheetCell sunday = SpreadsheetCellType.STRING.createCell(row, 9, 1, 1, "");
//        sunday.setEditable(true);
//        sunday.getStyleClass().add("schedule-cell");
//        sunday.textProperty().addListener((observable, oldValue, newValue)
//                -> textPropertiesWithCheck(sunday, dates[6], employee, kitchProperties[6], indexOfEmployee));
//        setScheduleInTable(schedules, dates[6], employee, sunday);
//        if (!sunday.getText().isEmpty() && !sunday.getText().equals("-")){
//            quantityOfEmployees++;
//            Date [] dates = countTime(sunday.getText());
//            time += dates[1].getTime() - dates[0].getTime();
//        }
//        dataCells.add(sunday);
//
//        double hours = time / (60*60*1000);
//
//        shift.itemProperty().setValue(String.valueOf(quantityOfEmployees) + " / " + hours);
//
//        return dataCells;
//    }

    private LocalDate [] getPeriod (LocalDate date) {
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

    private KitchProperties [] getKitchProperties (int kitchenId){
        KitchProperties [] kitchProperties = new KitchProperties[7];
        kitchProperties [0] = GlobalPandaApp.site.getKitchProperty(dates[0], kitchenId);
        kitchProperties [1] = GlobalPandaApp.site.getKitchProperty(dates[1], kitchenId);
        kitchProperties [2] = GlobalPandaApp.site.getKitchProperty(dates[2], kitchenId);
        kitchProperties [3] = GlobalPandaApp.site.getKitchProperty(dates[3], kitchenId);
        kitchProperties [4] = GlobalPandaApp.site.getKitchProperty(dates[4], kitchenId);
        kitchProperties [5] = GlobalPandaApp.site.getKitchProperty(dates[5], kitchenId);
        kitchProperties [6] = GlobalPandaApp.site.getKitchProperty(dates[6], kitchenId);

        return kitchProperties;
    }

    private void sortEmployees (List<Employee> employees) {
        String regMarks = "^(.[СПАК])|([СПАК].)$";

        employees.sort((o1, o2) -> {
            if ( !o1.getKitchen().equals(o2.getKitchen()) ) {
                return o1.getKitchen().equals(kitchSelect.getValue().getName()) ? -1 : 1;
            } else {
                if (o1.getMark().matches(regMarks) == o2.getMark().matches(regMarks)) {
                    int timeDiff = compareWorkDays(o1, o2);
                    return timeDiff != 0 ? timeDiff : o1.getName().compareTo(o2.getName());
                } else {
                    return o1.getMark().matches(regMarks) ? -1 : 1;
                }
            }
        });
    }

    private int compareWorkDays (Employee e1, Employee e2) {
        ArrayList<Schedule> workDaysE1 = new ArrayList<>();
        ArrayList<Schedule> workDaysE2 = new ArrayList<>();
        for (LocalDate date = LocalDate.of(dates[0].getYear(), dates[0].getMonth(), dates[0].getDayOfMonth()); !date.isAfter(dates[6]); date = date.plusDays(1)) {
            Schedule sch1 = scheduleMap.get(date).get(e1.getId());
            if (sch1.getStart() != null)
                workDaysE1.add(sch1);

            Schedule sch2 = scheduleMap.get(date).get(e2.getId());
            if (sch2.getStart() != null)
                workDaysE2.add(sch2);
        }

        int workTime1 = 0;
        int workTime2 = 0;

        for (Schedule s1 : workDaysE1) {
            workTime1 += (s1.getEnd().getTime() - s1.getStart().getTime());
        }

        for (Schedule s2 : workDaysE2) {
            workTime2 += (s2.getEnd().getTime() - s2.getStart().getTime());
        }

        return workTime2 - workTime1;
    }

    private String [] stringTimeParse (String time, KitchProperties kitchProperties){

        String [] stringTime = {null, null, null, null};

        if (time.equals("+") || time.equals("++")){
            String start = kitchProperties.getStart();
            String end = kitchProperties.getEnd();

            String hourBegin = start.substring(0,2);
            String minutesBegin = start.substring(2,4);
            String hourEnd = end.substring(0,2);
            String minutesEnd = end.substring(2,4);

            return new String[] {hourBegin, minutesBegin, hourEnd, minutesEnd};
        }

        if (time.equals("-")){
            String s = "1000";

            String hourBegin = s.substring(0,2);
            String minutesBegin = s.substring(2,4);
            String hourEnd = s.substring(0,2);
            String minutesEnd = s.substring(2,4);

            return new String[] {hourBegin, minutesBegin, hourEnd, minutesEnd};
        }

        if (time.length() == 4 || (time.length() == 5 && time.contains("+"))){

            String end = kitchProperties.getEnd();

            String hourBegin = time.substring(0,2);
            String minutesBegin = time.substring(2,4);
            String hourEnd = end.substring(0,2);
            String minutesEnd = end.substring(2,4);

            return new String[]{hourBegin, minutesBegin, hourEnd, minutesEnd};
        }

        if (time.length() == 8 || (time.length() == 9 && time.contains("+"))){

            String hourBegin = time.substring(0,2);
            String minutesBegin = time.substring(2,4);
            String hourEnd = time.substring(4,6);
            String minutesEnd = time.substring(6,8);

            return new String[]{hourBegin, minutesBegin, hourEnd, minutesEnd};
        }

        return stringTime;
    }

    private int [] intTimeParse (String text){
        assert text != null;

        int [] intTime = {0, 0, 0, 0};

        if (!text.isEmpty() && text.length() == 13) {
            int hoursStart = Integer.parseInt(text.substring(0, 2));
            int minutesStart = Integer.parseInt(text.substring(3, 5));
            int hoursEnd = Integer.parseInt(text.substring(8, 10));
            int minutesEnd = Integer.parseInt(text.substring(11, 13));

            return new int [] {hoursStart, minutesStart, hoursEnd, minutesEnd};
        }

        if (text.equals("-")){
            int hoursStart = 10;
            int minutesStart = 0;
            int hoursEnd = 10;
            int minutesEnd = 0;

            return new int[] {hoursStart, minutesStart, hoursEnd, minutesEnd};
        }

        return intTime;
    }

    private Date [] countTime (String text){
        int [] time = intTimeParse(text);
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, time[0]);
        start.set(Calendar.MINUTE, time[1]);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        Calendar end = Calendar.getInstance();
        if (time[2] == 0){
            end.add(Calendar.DATE, +1);
        }
        end.set(Calendar.HOUR_OF_DAY, time[2]);
        end.set(Calendar.MINUTE, time[3]);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        return new Date[]{start.getTime(), end.getTime()};
    }

    private String setTextInSchedule (String text, KitchProperties kitchProperties){
        if (text.equals("-")){
            return "-";
        }
        String time [] = stringTimeParse(text, kitchProperties);

        return time[0] + ":" + time[1] + " - " + time[2] + ":" + time[3];

    }

    private void textPropertiesForDataCells (SpreadsheetCell cell, LocalDate localDate, Employee employee, KitchProperties kitchProperties, int indexOfEmployee){

        if (cell.getStyleClass().contains("errorSymbol")){
            cell.getStyleClass().remove("errorSymbol");
        }

        if (cell.getStyleClass().contains("errorOutOfWorkingHours")){
            cell.getStyleClass().remove("errorOutOfWorkingHours");
        }

        if (cell.getStyleClass().contains("errorTime")){
            cell.getStyleClass().remove("errorTime");
        }

        if (cell.getStyleClass().contains("errorStartTimeEqualsEndTime")){
            cell.getStyleClass().remove("errorStartTimeEqualsEndTime");
        }

        if (cell.getText() != null && !cell.getText().isEmpty()){

            String cellText = cell.textProperty().getValue();

            String start = kitchProperties.getStart();
            String end = kitchProperties.getEnd();
            String startEnd = kitchProperties.getStart() + kitchProperties.getEnd();

            if (cellText.matches(REGULAR_EXP_FOR_TIME_WITH_PLUS)){
                start = kitchProperties.getStart() + "+";
            } else if (cellText.matches(REGULAR_EXP_FOR_TIME_START_END_WITH_PLUS)){
                startEnd = kitchProperties.getStart() + kitchProperties.getEnd() + "+";
            }

            if (validateTime(cellText)){

                cell.itemProperty().setValue(setTextInSchedule(cellText, kitchProperties));

                String [] time = stringTimeParse(cellText, kitchProperties);

                LocalDateTime startTime = localDate.atTime(Integer.parseInt(time[0]), Integer.parseInt(time[1]));

                LocalDateTime endTime;

                if (Integer.parseInt(time[2]) < 6){
                    LocalDate newLocalDate = localDate.plusDays(1);
                    endTime = newLocalDate.atTime(Integer.parseInt(time[2]), Integer.parseInt(time[3]));
                } else {
                    endTime = localDate.atTime(Integer.parseInt(time[2]), Integer.parseInt(time[3]));
                }

                LocalDateTime startTimeDefault = localDate.atTime(Integer.parseInt(start.substring(0,2)), Integer.parseInt(start.substring(2,4)));

                LocalDateTime endTimeDefault;

                if (end.equals("0030")){
                    LocalDate newLocalDate = localDate.plusDays(1);
                    endTimeDefault = newLocalDate.atTime(Integer.parseInt(end.substring(0,2)), Integer.parseInt(end.substring(2,4)));
                } else {
                    endTimeDefault = localDate.atTime(Integer.parseInt(end.substring(0,2)), Integer.parseInt(end.substring(2,4)));
                }

                if (startTime.isBefore(startTimeDefault) || endTime.isAfter(endTimeDefault)){
                    cell.getStyleClass().add("errorOutOfWorkingHours");
                } else if (startTime.equals(endTime) && !time[0].equals("10")) {
                    cell.getStyleClass().add("errorStartTimeEqualsEndTime");
                } else if (startTime.isAfter(endTime)){
                    cell.getStyleClass().add("errorTime");
                } else if (cellText.equals("+") || cellText.equals("++") || cellText.equals(start) || cellText.equals(startEnd)){

                    setColorInScheduleFullShift(employee, cell);

                    if (cellText.equals("++") || cellText.length() == 5 && cellText.contains("+") || cellText.length() == 9 && cellText.contains("+")) {
                        activateCorners(cell);
                    } else {
                        deactivateCorners(cell);
                    }

                } else if (cellText.equals("-")){
                    cell.setStyle("-fx-background-color: #ffb3b3;");
                } else {

                    setColorInScheduleOtherShift(employee, cell);

                    if (cellText.length() == 5 && cellText.contains("+") || cellText.length() == 9 && cellText.contains("+")) {
                        activateCorners(cell);
                    } else {
                        deactivateCorners(cell);
                    }
                }

            } if (!validateTime(cellText)){
                cell.getStyleClass().add("errorSymbol");
            }
        } else {
            cell.setStyle("-fx-background-color: white;");

            deactivateCorners(cell);
        }

        if (isCountedForWritingInDB(cell.getText())){
            updateSchedule(localDate, employee, cell);
        }
        updateTotals(posSelect.getValue());
        updateShifts(indexOfEmployee);

    }

    private void textPropertiesWithCheck (SpreadsheetCell cell, LocalDate localDate, Employee employee, KitchProperties kitchProperties, int indexOfEmployee){
        if (cell.getText().length() != 13){
            textPropertiesForDataCells(cell, localDate, employee, kitchProperties, indexOfEmployee);
        }

        if (!cell.getStyleClass().isEmpty()){
            alertDialog(cell.getStyleClass().toString());
        }
    }

    private void updateSchedule(LocalDate localDate, Employee employee, SpreadsheetCell cell) {

            Schedule schedule = scheduleMap.get(localDate).get(employee.getId());

            int [] timeArray = intTimeParse(cell.textProperty().getValue());

            schedule.setKitchId(kitchSelect.getValue().getKitch_id());

            schedule.setEmployeeName(employee.getName());

            LocalDateTime startTime = localDate.atTime(timeArray[0], timeArray[1]);

            Date start = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
            schedule.setStart(start);

            LocalDateTime endTime = localDate.atTime(timeArray[2], timeArray[3]);

            Date end;

            if (timeArray[2] < 6){
                LocalDateTime newEndTime = endTime.plusDays(1);
                end = Date.from(newEndTime.atZone(ZoneId.systemDefault()).toInstant());
            } else {
                end = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());
            }

            schedule.setEnd(end);

            if (cell.isCornerActivated(SpreadsheetCell.CornerPosition.BOTTOM_LEFT)){
                schedule.setMark("Адмін");
            }

            schedule.setStartChange(new Date());

            schedule.setEndChange(new Date());

            schedule.setOperator(GlobalPandaApp.config.getOperator().getName());

            schedule.setPlan(true);


//        System.out.println("Date: " + schedule.getDate() + "\n Kitchen: " + schedule.getKitchId() +
//                "\n Employee: " + schedule.getEmployeeId() +"\n Name: " + schedule.getEmployeeName() + "\n Start: " + schedule.getStart() + "\n End: " + schedule.getEnd() +
//                "\n StartChange: " + schedule.getStartChange() + "\n EndChange: " + schedule.getEndChange() + "\n Operator: " + schedule.getOperator() +
//                "\n Mark: " + schedule.getMark());
    }

    private void updateTotals (String position) {

        int[] totals = new int[7];
            for (ObservableList<SpreadsheetCell> row : sheet.getGrid().getRows()) {
            if (row.get(0).getStyleClass().contains("schedule-cell-operator")
                    || row.get(0).getStyleClass().contains("schedule-cell-cooker")
                    || row.get(0).getStyleClass().contains("schedule-cell-courier")
                    || row.get(0).getStyleClass().contains("schedule-cell-anotherKitchen")) {
                for (int i = 3; i <= 9; i++) {
                    if (isCountedShift(row.get(i).getText()) && row.get(i).isEditable()) {
                        totals[i-3]++;
                    }
                }
            }
        }

        for (int i = 0; i < totals.length; i++) {

            int totalCount = 0;

            if (position != null){
                switch (position){
                    case COURIER:
                        totalCount = kitchProperties[i].getCouriers();
                        break;
                    case COOKER:
                        totalCount = kitchProperties[i].getCooks();
                        break;
                    case OPERATOR:
                        totalCount = kitchProperties[i].getOperators();
                        break;
                }
            }

            int difference = totalCount - totals[i];

            if (totals[i] == 0){
                this.totals[i].itemProperty().setValue(totalCount + "/-/" + difference);
                this.totals[i].setStyle("-fx-text-fill: red; -fx-alignment: center;");
            } else if (difference <= 0){
                this.totals[i].itemProperty().setValue(totalCount + "/" + totals[i] + "/-");
                this.totals[i].setStyle("-fx-text-fill: green; -fx-alignment: center;");
            } else {
                this.totals[i].itemProperty().setValue(totalCount + "/" + totals[i] + "/" + difference);
                this.totals[i].setStyle("-fx-text-fill: #ff6600; -fx-alignment: center;");
            }
        }
    }

    private void updateShifts (int indexOfEmployee){

        int quantity [] = new int[quantityOfShifts.length];
        double hours [] = new double[quantityOfShifts.length];

        for (SpreadsheetCell spreadsheetCell : sheet.getGrid().getRows().get(indexOfEmployee+2)) {
            if (spreadsheetCell.getColumn() != 0 && spreadsheetCell.getColumn() != 1 && spreadsheetCell.getColumn() != 2){
                if (!spreadsheetCell.getText().isEmpty() && isCountedShift(spreadsheetCell.getText())){
                    quantity[indexOfEmployee]++;
                    Date [] dates = countTime(spreadsheetCell.getText());
                    hours[indexOfEmployee] += dates[1].getTime() - dates[0].getTime();
                }
            }
            double time = hours[indexOfEmployee] / (60*60*1000);

            quantityOfShifts[indexOfEmployee].itemProperty().setValue(String.valueOf(quantity[indexOfEmployee]) + " / " +time);
        }
    }

    private void alertDialog (String alert){

        if (alert.contains("errorOutOfWorkingHours")){
            JOptionPane.showMessageDialog(null, "Даний час виходить за межі графіку роботи 'Панда Суші'. \nПеревірте введені дані.");
        }

        if (alert.contains("errorSymbol")){
            JOptionPane.showMessageDialog(null, "Введений текст не відповідає правилам. \nВ ячейку можна вводити: '+', " +
                    "'-', 4 або 8 цифр, які відповідають часу. \nПеревірте введені дані.");
        }

        if (alert.contains("errorTime")){
            JOptionPane.showMessageDialog(null, "Час початку зміни є більшим за час завершення. \nПеревірте введені дані.");
        }

        if (alert.contains("errorStartTimeEqualsEndTime")){
            JOptionPane.showMessageDialog(null, "Час початку зміни співпадає з часом завершення. \nПеревірте введені дані.");
        }
    }

    private boolean isCountedShift (String text) {
        if (text != null && !text.isEmpty() && text.length() == 13) {
            int hoursStart = Integer.parseInt(text.substring(0, 2));
            int minutesStart = Integer.parseInt(text.substring(3, 5));
            int hoursEnd = Integer.parseInt(text.substring(8, 10));
            int minutesEnd = Integer.parseInt(text.substring(11, 13));

            Calendar startShift = Calendar.getInstance();
            startShift.set(Calendar.HOUR_OF_DAY, 10);
            startShift.set(Calendar.MINUTE, 0);

            Calendar endShift = Calendar.getInstance();
            endShift.add(Calendar.DATE, +1);
            endShift.set(Calendar.HOUR_OF_DAY, 0);
            endShift.set(Calendar.MINUTE, 30);

            Calendar startEmployeeWorkingDay = Calendar.getInstance();
            startEmployeeWorkingDay.set(Calendar.HOUR_OF_DAY, hoursStart);
            startEmployeeWorkingDay.set(Calendar.MINUTE, minutesStart);

            Calendar endEmployeeWorkingDay = Calendar.getInstance();
            endEmployeeWorkingDay.set(Calendar.HOUR_OF_DAY, hoursEnd);
            endEmployeeWorkingDay.set(Calendar.MINUTE, minutesEnd);
            if (hoursEnd == 0){
                endEmployeeWorkingDay.add(Calendar.DATE, +1);
            }

            Calendar timeForCountingStart = Calendar.getInstance();
            timeForCountingStart.set(Calendar.HOUR_OF_DAY, 18);
            timeForCountingStart.set(Calendar.MINUTE, 0);

            Calendar timeForCountingEnd = Calendar.getInstance();
            timeForCountingEnd.set(Calendar.HOUR_OF_DAY, 22);
            timeForCountingEnd.set(Calendar.MINUTE, 0);

            if (startEmployeeWorkingDay.after(startShift) || startEmployeeWorkingDay.equals(startShift)){
                if (endEmployeeWorkingDay.before(endShift) || endEmployeeWorkingDay.equals(endShift)){
                    if (startEmployeeWorkingDay.before(timeForCountingStart) || startEmployeeWorkingDay.equals(timeForCountingStart)){
                        if (!endEmployeeWorkingDay.before(timeForCountingEnd) || endEmployeeWorkingDay.equals(timeForCountingEnd)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isCountedForWritingInDB (String text) {
        if (text != null && !text.isEmpty() && text.length() == 13) {
            int hoursStart = Integer.parseInt(text.substring(0, 2));
            int minutesStart = Integer.parseInt(text.substring(3, 5));
            int hoursEnd = Integer.parseInt(text.substring(8, 10));
            int minutesEnd = Integer.parseInt(text.substring(11, 13));

            Calendar startShift = Calendar.getInstance();
            startShift.set(Calendar.HOUR_OF_DAY, 10);
            startShift.set(Calendar.MINUTE, 0);

            Calendar endShift = Calendar.getInstance();
            endShift.add(Calendar.DATE, +1);
            endShift.set(Calendar.HOUR_OF_DAY, 0);
            endShift.set(Calendar.MINUTE, 30);

            Calendar startEmployeeWorkingDay = Calendar.getInstance();
            startEmployeeWorkingDay.set(Calendar.HOUR_OF_DAY, hoursStart);
            startEmployeeWorkingDay.set(Calendar.MINUTE, minutesStart);

            Calendar endEmployeeWorkingDay = Calendar.getInstance();
            endEmployeeWorkingDay.set(Calendar.HOUR_OF_DAY, hoursEnd);
            endEmployeeWorkingDay.set(Calendar.MINUTE, minutesEnd);
            if (hoursEnd == 0){
                endEmployeeWorkingDay.add(Calendar.DATE, +1);
            }

            if (startEmployeeWorkingDay.after(startShift) || startEmployeeWorkingDay.equals(startShift)){
                if (endEmployeeWorkingDay.before(endShift) || endEmployeeWorkingDay.equals(endShift)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validateTime (String timeString) {

        return timeString.equals("+") || timeString.equals("++") || timeString.equals("-") || timeString.length() == 4 && timeString.matches(REGULAR_EXP_FOR_TIME)
                || timeString.length() == 8 && timeString.matches(REGULAR_EXP_FOR_TIME_START_END) || timeString.length() == 5 && timeString.matches(REGULAR_EXP_FOR_TIME_WITH_PLUS)
                || timeString.length() == 9 && timeString.matches(REGULAR_EXP_FOR_TIME_START_END_WITH_PLUS);

    }

    private void setScheduleInTable (List<Schedule> schedules, LocalDate date, Employee employee, SpreadsheetCell cell){

        String strLocalDate = date.format(dateTimeFormatterWithYear);
        String strDate;

        for (Schedule schedule : schedules) {
            if (schedule.getStart() == null)
                continue;
            strDate = simpleDateFormat.format(schedule.getStart());
            if (employee.getId() == schedule.getEmployeeId() && strLocalDate.equals(strDate)) {
                if (schedule.getStart() != null && schedule.getEnd() != null) {
                    Date start = schedule.getStart();
                    Date end = schedule.getEnd();

                    String value = simpleDateFormatTime.format(start) + " - " + simpleDateFormatTime.format(end);

                    if ((int)employee.getKitch_id() == kitchSelect.getValue().getKitch_id()){
                        switch (value) {
                            case "10:00 - 23:30":
                            case "10:00 - 00:30":
                                setColorInScheduleFullShift(employee, cell);
                                if (schedule.getMark() != null && schedule.getMark().equals("Адмін")){
                                    activateCorners(cell);
                                }
                                cell.itemProperty().setValue(value);
                                break;
                            case "10:00 - 10:00":
                                cell.setStyle("-fx-background-color: #ffb3b3;");
                                cell.itemProperty().setValue("-");
                                break;
                            default:
                                setColorInScheduleOtherShift(employee, cell);
                                if (schedule.getMark() != null && schedule.getMark().equals("Адмін")){
                                    activateCorners(cell);
                                }
                                cell.itemProperty().setValue(value);
                                break;
                        }
                    } else {
                        cell.itemProperty().setValue(value);
                        cell.getStyleClass().add("schedule-cell-anotherKitchen");
                        if (schedule.getMark() != null && schedule.getMark().equals("Адмін")){
                            activateCorners(cell);
                        }
                        cell.setEditable(false);
                    }
                }
                return;
            }
        }

    }

    private void setColorInScheduleFullShift (Employee employee, SpreadsheetCell cell){
        switch (employee.getMark()) {
            case "СП":
                cell.setStyle("-fx-background-color: rgba(150,150,255,1);");
                break;
            case "Сп":
            case "С":
                cell.setStyle("-fx-background-color: rgba(170,170,255,0.4);");
                break;
            case "сП":
            case "П":
                cell.setStyle("-fx-background-color: rgba(255,170,0,1);");
                break;
            case "сп":
            case "с":
            case "п":
                cell.setStyle("-fx-background-color: rgba(255,215,0,1);");
                break;
        }
    }

    private void setColorInScheduleOtherShift (Employee employee, SpreadsheetCell cell){
        switch (employee.getMark()) {
            case "СП":
                cell.setStyle("-fx-background-color: rgba(150,150,255,0.7);");
                break;
            case "Сп":
            case "С":
                cell.setStyle("-fx-background-color: rgba(170,170,255,0.2);");
                break;
            case "сП":
            case "П":
                cell.setStyle("-fx-background-color: rgba(255,170,0,0.5);");
                break;
            case "сп":
            case "с":
            case "п":
                cell.setStyle("-fx-background-color: rgba(255,215,0,0.5);");
                break;
        }
    }

    private void activateCorners (SpreadsheetCell cell){
        cell.activateCorner(SpreadsheetCell.CornerPosition.BOTTOM_LEFT);
        cell.activateCorner(SpreadsheetCell.CornerPosition.BOTTOM_RIGHT);
        cell.activateCorner(SpreadsheetCell.CornerPosition.TOP_LEFT);
        cell.activateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
    }

    private void deactivateCorners (SpreadsheetCell cell){
        cell.deactivateCorner(SpreadsheetCell.CornerPosition.BOTTOM_LEFT);
        cell.deactivateCorner(SpreadsheetCell.CornerPosition.BOTTOM_RIGHT);
        cell.deactivateCorner(SpreadsheetCell.CornerPosition.TOP_LEFT);
        cell.deactivateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
    }

    public void writeToDB (ActionEvent actionEvent) {
        writeToDB(posSelect.getValue());
    }

    private void writeToDB (String position){

        for (int i = 0; i <= 6; i++){
            for (Employee employee: employeeMap.values()){
                if (!employee.getPosition().equals(position))
                    continue;
                Schedule schedule = scheduleMap.get(dates[i]).get(employee.getId());

                if (schedule != null && schedule.getStart() != null){
                    GlobalPandaApp.site.saveSchedule(schedule);
                }
            }
        }

        sortEmployees(inView);
        createSpreadSheetView();


    }

    public void print(ActionEvent actionEvent) {
        SnapshotParameters parameters = new SnapshotParameters();
        SpreadsheetView toPrint = new SpreadsheetView(sheet.getGrid());
        toPrint.setShowColumnHeader(false);
        double width = 0.0;
        for (SpreadsheetColumn c : sheet.getColumns())
            width += c.getWidth();
        AnchorPane.setTopAnchor(toPrint, 0.0);
        AnchorPane.setBottomAnchor(toPrint, 0.0);
        AnchorPane.setLeftAnchor(toPrint, 0.0);
        AnchorPane.setRightAnchor(toPrint, 0.0);
        Scene scene = new Scene(new AnchorPane(toPrint), width + 50, (sheet.getGrid().getRows().size() + 2) * sheet.getRowHeight(1));
        scene.getStylesheets().add(MainFX.class.getResource("/css/styles.css").toExternalForm());
        WritableImage image = scene.getRoot().snapshot(parameters, null);

        String position = posSelect.getValue();
        String date = dates[0].format(dateTimeFormatter) + "_" + dates[6].format(dateTimeFormatter);

        File file = new File("Графік_роботи_" + position + "_" + date + ".png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            //TODO
        }

    }
}
