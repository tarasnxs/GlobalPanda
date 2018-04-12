package ua.com.pandasushi.controller.costs.product_shift;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import ua.com.pandasushi.controller.MainController;
import ua.com.pandasushi.controller.TabController;
import ua.com.pandasushi.controller.services.ExcelService;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Тарас on 12.01.2017.
 */
public class ProductsShiftTabController implements TabController {
    MainController main;
    ShiftTabBuilder builder;

    @FXML
    TabPane productShiftTabPane;

    @FXML
    Button addCheck;

    @FXML
    Button makeReport;

    @FXML
    Button refresh;

    @Override
    public void init(MainController main) {
        System.out.println("initialized");
        this.main = main;
        this.builder = new ShiftTabBuilder(productShiftTabPane, this);

        refresh.setOnAction(event -> builder.reloadTabs() );

        addCheck.setOnAction(event -> builder.createNewTab() );

        makeReport.setOnAction(event -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            if(cal.get(Calendar.HOUR_OF_DAY) < 6)
                cal.add(Calendar.DAY_OF_MONTH, -1);
            String kitchChar;
            switch (GlobalPandaApp.config.getKitchen().getKitch_id()) {
                case 0: kitchChar = "S";
                    break;
                case 1: kitchChar = "V";
                    break;
                case 5: kitchChar = "L";
                    break;
                default: kitchChar = "TEST";
                    break;
            }
            String path = kitchChar + cal.get(Calendar.DAY_OF_MONTH) + "." + (new SimpleDateFormat("MM").format(cal.getTime())) + "."
                    + cal.get(Calendar.YEAR) + " Закупка продуктів" + ".xlsx";
            File file = new File(path);
            ExcelService.createReportProductPurchase(file, cal.getTime());
        });

        builder.reloadTabs();
    }
}
