package ua.com.pandasushi.controller.control.schedule;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import ua.com.pandasushi.controller.MainController;
import ua.com.pandasushi.controller.TabController;
import ua.com.pandasushi.database.common.Kitchens;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Taras on 12.06.2017.
 */
public class ScheduleController implements TabController {
    MainController main;

    @FXML
    ChoiceBox<Kitchens> kitchSelect;

    @FXML
    DatePicker dateSelect;

    private SpreadsheetView sheet;

    @Override
    public void init(MainController main) {
        this.main = main;
        //TODO Коли вибираєш дату- вибрати початок і кінець тижня
        dateSelect.setValue(LocalDate.now());
        dateSelect.valueProperty().addListener((observable, oldValue, newValue) -> {
            LocalDate[] period = getPeriod(newValue);
            System.out.println(period[0].toString() + " - " + period[1].toString());
        });
        //TODO Filter by type
        kitchSelect.setItems(FXCollections.observableArrayList(GlobalPandaApp.site.getKitchens()));
        ArrayList list = new ArrayList();


    }

    private LocalDate[] getPeriod(LocalDate date) {
        LocalDate[] period = new LocalDate[2];
        period[0] = date.minusDays(date.getDayOfWeek().getValue() - 1);
        period[1] = date.plusDays(7 - date.getDayOfWeek().getValue());
        return period;
    }

}
