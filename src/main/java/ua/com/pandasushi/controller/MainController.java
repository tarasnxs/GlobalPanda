package ua.com.pandasushi.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import ua.com.pandasushi.controller.control.employee.EmployeeController;
import ua.com.pandasushi.controller.control.menu.MenuController;
import ua.com.pandasushi.controller.control.reports.ReportsController;
import ua.com.pandasushi.controller.costs.product_purchase.ProductsPurchaseTabController;
import ua.com.pandasushi.controller.costs.product_shift.ProductsShiftTabController;
import ua.com.pandasushi.controller.kitchen.inventory.InventoryTabController;
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
    Tab control;

    @FXML
    Tab viewOrders;

    @FXML
    Tab menuSU;

    @FXML
    Tab employeesTab;

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
    private EmployeeController employee;

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
    private ReportsController reportsTabController;

    @FXML
    public void initialize() {
        employee = new EmployeeController();
        pandaOrder = new OrderController();
        menu = new MenuController();
        System.out.println("Applicaton is started");
        employee.init(this);
        pandaOrder.init(this);
        menu.init(this);
        productsPurchaseTabController.init(this);
        productsShiftTabController.init(this);
        rozrobkaTabController.init(this);
        inventoryTabController.init(this);
        reportsTabController.init(this);
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
        //list.add(inventory);
        list.add(viewOrders);
        list.add(employeesTab);
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
