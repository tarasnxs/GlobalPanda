package ua.com.pandasushi.controller.costs.product_shift;

import javafx.scene.control.TabPane;
import ua.com.pandasushi.database.common.Operations;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Тарас on 13.01.2017.
 */
public class ShiftTabBuilder {
    private TabPane pane;
    protected ArrayList<NewShiftTab> openTabs;
    public ProductsShiftTabController controller;




    ShiftTabBuilder(TabPane pane, ProductsShiftTabController controller) {
        this.pane = pane;
        this.controller = controller;
        openTabs = new ArrayList<>();
    }


    public void createNewTab () {
        NewShiftTab result = new NewShiftTab(this);
        openTabs.add(result);
        pane.getTabs().add(result.getTab());
        System.out.println("Tab added");
    }

    public void createNewTab (ArrayList<Operations> operations) {
        NewShiftTab result = new NewShiftTab(this, operations);
        if (result.getTab() != null) {
            openTabs.add(result);
            pane.getTabs().add(result.getTab());
            System.out.println("Tab added");
        }
    }

    public void reloadTabs () {
        openTabs.clear();
        pane.getTabs().clear();

        HashMap<Integer, ArrayList<Operations>> result = GlobalPandaApp.site.getOpenShifts();
        System.out.println(result.size());
        for (ArrayList<Operations> op : result.values()) {
            createNewTab(op);
        }
    }

}