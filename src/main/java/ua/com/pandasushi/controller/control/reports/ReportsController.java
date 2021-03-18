package ua.com.pandasushi.controller.control.reports;

import com.sun.javafx.scene.control.skin.DatePickerContent;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.LocalDatePicker;
import jfxtras.scene.control.LocalDateTimePicker;
import ua.com.pandasushi.controller.MainController;
import ua.com.pandasushi.controller.TabController;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Taras on 06.06.2017.
 */
public class ReportsController implements TabController {
    MainController main;

    @FXML
    private HBox datesBox;

    @FXML
    private Button ordersReport;

    @FXML
    private Button purchaseReport;

    @FXML
    private Button inventoryReport;

    @FXML
    private Button balanceReport;

    @FXML
    private CheckBox fullReport;

    private LocalDatePicker fromPicker;

    @Override
    public void init(MainController main) {
        this.main = main;

        fromPicker = new LocalDatePicker();
        Locale locale = Locale.forLanguageTag("uk-UA");
        fromPicker.setLocale(locale);
        fromPicker.modeProperty().setValue(LocalDatePicker.Mode.RANGE);


        ordersReport.setOnAction(event -> {
            Calendar[] res = getSelectedDates();
            System.out.println(res[0].getTime().toString() + " - " + res[1].getTime().toString());
            OrdersReport.createReport(res[0], res[1], fullReport.isSelected() ? -1 : GlobalPandaApp.config.getKitchen().getKitch_id());
        });

        purchaseReport.setOnAction(event -> {
            Calendar[] res = getSelectedDates();
            PurchaseReport.createReport(res[0], res[1]);
        });

        balanceReport.setOnAction(event -> {
            Calendar[] res = getSelectedDates();
            BalanceReport.createReport(res[0], res[1], fullReport.isSelected());
        });

        datesBox.getChildren().addAll(fromPicker);
    }

    private Calendar[] getSelectedDates() {
        Calendar[] result = new Calendar[2];

        LocalDate start = LocalDate.MAX;
        LocalDate end = LocalDate.MIN;
        for (LocalDate date : fromPicker.localDates()) {
            if (date.isBefore(start))
                start = date;
            if (date.isAfter(end))
                end = date;
        }

        ZonedDateTime st = start.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime en = end.atStartOfDay(ZoneId.systemDefault());

        Calendar calStart = GregorianCalendar.from(st);
        calStart.set(Calendar.HOUR_OF_DAY, 6);

        Calendar calEnd = GregorianCalendar.from(en);
        calEnd.set(Calendar.HOUR_OF_DAY, 6);
        calEnd.add(Calendar.DATE, 1);

        result[0] = calStart;
        result[1] = calEnd;

        return result;
    }
}
