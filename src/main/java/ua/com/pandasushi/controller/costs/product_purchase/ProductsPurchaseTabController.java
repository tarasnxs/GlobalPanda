package ua.com.pandasushi.controller.costs.product_purchase;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import ua.com.pandasushi.controller.MainController;
import ua.com.pandasushi.controller.TabController;
import ua.com.pandasushi.controller.services.ExcelService;
import ua.com.pandasushi.database.common.Operations;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Тарас on 12.01.2017.
 */
public class ProductsPurchaseTabController implements TabController {
    MainController main;
    PurchaseTabBuilder tabBuilder;

    @FXML
    TabPane productPurchaseTabPane;

    @FXML
    Label sumByDay;

    @FXML
    Button addCheck;

    @FXML
    Button closeCheck;

    @FXML
    Button makeReport;

    @FXML
    DatePicker reportDate;

    @FXML
    Button addProduct;

    @Override
    public void init(MainController main) {
        this.main = main;
        tabBuilder = new PurchaseTabBuilder(productPurchaseTabPane, this);

        ArrayList<Float> sum = GlobalPandaApp.site.getSumForType(Operations.PRODUCT_PURCHASE);
        String s = "Сума за день : " + ( sum.get(0) > 0 ? sum.get(0) + " UAH; " : "" )
                                        + ( sum.get(1) > 0 ? sum.get(1) + " USD; " : "" )
                                        + ( sum.get(2) > 0 ? sum.get(2) + " EUR; " : "" );
        sumByDay.setText(s);

        addCheck.setOnAction(event -> tabBuilder.createNewTab());

        closeCheck.setOnAction(event -> tabBuilder.closeTab(productPurchaseTabPane.getSelectionModel().getSelectedItem()));

        reportDate.setValue(LocalDate.now());
        if (!GlobalPandaApp.config.getOperator().getPosition().equals("Адміністратор")) {
            reportDate.setDisable(true);
        }

        makeReport.setOnAction(event -> {
            Calendar cal = Calendar.getInstance();
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

        addProduct.setOnAction(event -> {
            new AddProductDialog("", tabBuilder.openTabs);
        });

        tabBuilder.createNewTab();
    }
}