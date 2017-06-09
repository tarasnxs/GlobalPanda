package ua.com.pandasushi.controller.costs.product_purchase;

import javafx.scene.control.*;

import java.util.ArrayList;

/**
 * Created by Тарас on 13.01.2017.
 */
public class PurchaseTabBuilder {
    private TabPane pane;
    protected ArrayList<PurchaseTab> openTabs;
    public ProductsPurchaseTabController controller;




    PurchaseTabBuilder (TabPane pane, ProductsPurchaseTabController controller) {
        this.pane = pane;
        this.controller = controller;
        openTabs = new ArrayList<>();
    }


    public void createNewTab () {
        PurchaseTab result = new PurchaseTab(this);
        openTabs.add(result);
        pane.getTabs().add(result.getTab());
    }

    public void closeTab (Tab tab) {
        openTabs.remove(tab);
        pane.getTabs().remove(tab);
    }


}
