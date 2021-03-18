package ua.com.pandasushi.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import ua.com.pandasushi.controller.control.employee.EmployeeTabController;
import ua.com.pandasushi.controller.control.menu.MenuController;
import ua.com.pandasushi.controller.control.reports.ReportsController;
import ua.com.pandasushi.controller.control.schedule.ScheduleFactController;
import ua.com.pandasushi.controller.control.schedule.SchedulePlanController;
import ua.com.pandasushi.controller.costs.product_purchase.ProductsPurchaseTabController;
import ua.com.pandasushi.controller.costs.product_shift.ProductsShiftTabController;
import ua.com.pandasushi.controller.kitchen.inventory.InventoryTabController;
import ua.com.pandasushi.controller.kitchen.inventorycafe.InventoryCafeTabController;
import ua.com.pandasushi.controller.kitchen.rozrobka.RozrobkaTabController;
import ua.com.pandasushi.controller.order.OrderController;
import ua.com.pandasushi.database.common.Employee;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.util.ArrayList;

/**
 * Created by Тарас on 26.10.2016.
 */
public class MainController {

    @FXML
    Tab orders;

    @FXML
    Tab costs;

    @FXML
    Tab courierFuel;

    @FXML
    Tab courierRozm;

    @FXML
    Tab pays;

    @FXML
    Tab delivers;

    @FXML
    Tab prodPurch;

    @FXML
    Tab prodShift;

    @FXML
    Tab containerPurch;

    @FXML
    Tab converts;

    @FXML
    Tab kitchen;

    @FXML
    Tab rozrobka;

    @FXML
    Tab inventory;

    @FXML
    Tab inventoryCafe;

    @FXML
    Tab control;

    @FXML
    Tab viewOrders;

    @FXML
    Tab menuSU;

    @FXML
    Tab employee;

    @FXML
    Tab schedule;

    @FXML
    Tab gasStations;

    @FXML
    Tab balance;

    @FXML
    Tab graphs;

    @FXML
    Tab viber;

    @FXML
    Tab reports;

    @FXML
    private EmployeeTabController employeeTabController;

    @FXML
    private OrderController pandaOrder;

    @FXML
    private MenuController menu;

    @FXML
    private AnchorPane productsPurchaseTab;

    @FXML
    private ProductsPurchaseTabController productsPurchaseTabController;

    @FXML
    private ProductsShiftTabController productsShiftTabController;

    @FXML
    private RozrobkaTabController rozrobkaTabController;

    @FXML
    private InventoryTabController inventoryTabController;

    @FXML
    private InventoryCafeTabController inventoryCafeTabController;

    @FXML
    private ReportsController reportsTabController;

    @FXML
    private SchedulePlanController schedulePlanController;

    @FXML
    private ScheduleFactController scheduleFactController;

    @FXML
    private MenuBar menuBar;

    @FXML
    public void initialize() {
        pandaOrder = new OrderController();
        menu = new MenuController();
        System.out.println("Applicaton is started");
        employeeTabController.init(this);
        pandaOrder.init(this);
        menu.init(this);
        productsPurchaseTabController.init(this);
        productsShiftTabController.init(this);
        rozrobkaTabController.init(this);
        if (GlobalPandaApp.config.getKitchen().getKitch_id().intValue() == 7)
            inventoryCafeTabController.init(this);
        else
            inventoryTabController.init(this);
        reportsTabController.init(this);
        schedulePlanController.init(this);
        scheduleFactController.init(this);

        if(GlobalPandaApp.config.getKitchen().getKitch_id().intValue() == 0) {
            menuBar.setStyle("-fx-background-color: rgb(50,255,50);");
            Menu menu = new Menu("Сихів");
            menu.setStyle("-fx-font-weight: 800; -fx-font-size: 15px;");
            menuBar.getMenus().add(menu);
        }
        if(GlobalPandaApp.config.getKitchen().getKitch_id().intValue() == 1) {
            menuBar.setStyle("-fx-background-color: rgb(222,184,135);");
            Menu menu = new Menu("Варшавська");
            menu.setStyle("-fx-font-weight: 800; -fx-font-size: 15px;");
            menuBar.getMenus().add(menu);
        }
        if(GlobalPandaApp.config.getKitchen().getKitch_id().intValue() == 5) {
            menuBar.setStyle("-fx-background-color: rgb(135,184,222);");
            Menu menu = new Menu("Садова");
            menu.setStyle("-fx-font-weight: 800; -fx-font-size: 15px;");
            menuBar.getMenus().add(menu);
        }

        if(GlobalPandaApp.config.getKitchen().getKitch_id().intValue() == 7) {
            menuBar.setStyle("-fx-background-color: rgb(255,120,120);");
            Menu menu = new Menu("Кафе");
            menu.setStyle("-fx-font-weight: 800; -fx-font-size: 15px;");
            menuBar.getMenus().add(menu);

        }
        setTabAccess(GlobalPandaApp.config.getOperator());


    }

    private void setTabAccess (Employee e) {
        ArrayList<Tab> list = new ArrayList<>();
        //close empty tabs
        list.add(orders);
        list.add(courierFuel);
        list.add(courierRozm);
        list.add(pays);
        list.add(delivers);
        list.add(containerPurch);
        list.add(converts);
        if(GlobalPandaApp.config.getKitchen().getKitch_id().intValue() == 7) {
            list.add(inventory);
        } else {
            list.add(inventoryCafe);
        }
        list.add(viewOrders);
        //list.add(employeesTab);
        //list.add(schedule);
        list.add(gasStations);
        list.add(balance);
        list.add(graphs);
        list.add(viber);

        if (e.getPosition().equals("Оператор")) {
            list.add(kitchen);
            list.add(control);
        } else if (e.getPosition().equals("Кухар")) {
            list.add(prodPurch);
            list.add(control);
        }

        closeTabs(list);
    }

    private void closeTabs (ArrayList<Tab> list) {
        for (Tab tab : list)
            tab.getTabPane().getTabs().remove(tab);
    }

    public void createReport(ActionEvent event) {
    }

    public void switchSync(ActionEvent event) {
    }

    public void switchTwoPC(ActionEvent event) {
    }

    public void syncWeek(ActionEvent event) {
    }

    public void showAbout(ActionEvent event) {
    }

    public void logOut(ActionEvent event) {
        GlobalPandaApp.logOut();
    }
}
