package ua.com.pandasushi.controller.kitchen.inventorycafe;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.controlsfx.control.spreadsheet.*;
import ua.com.pandasushi.controller.MainController;
import ua.com.pandasushi.controller.TabController;
import ua.com.pandasushi.controller.kitchen.inventory.InventoryPdf;
import ua.com.pandasushi.controller.kitchen.inventory.InventoryReport;
import ua.com.pandasushi.controller.kitchen.inventory.InventoryReportTotal;
import ua.com.pandasushi.controller.kitchen.inventory.InventoryTabController;
import ua.com.pandasushi.database.common.Inventory;
import ua.com.pandasushi.database.common.InventoryCafe;
import ua.com.pandasushi.database.common.inventory.CalculatedNetto;
import ua.com.pandasushi.database.common.inventory.InvCafeSelect;
import ua.com.pandasushi.database.common.inventory.InvSelect;
import ua.com.pandasushi.database.common.menu.INGREDIENTS;
import ua.com.pandasushi.database.common.menu.PRODUCTS;
import ua.com.pandasushi.database.common.menu.PRODUCTS_INGREDIENTS;
import ua.com.pandasushi.database.common.menu.TEHCARDS;
import ua.com.pandasushi.database.common.menu.cafe.TEHCARDS_CAFE;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Тарас on 07.03.2017.
 */
public class InventoryCafeTabController implements TabController, EventHandler<ActionEvent> {
    Calendar customDateInventory;
    boolean total = false;

    private static final int RED_ZONE = 300;
    private static final int ORANGE_ZONE = 100;


    private ArrayList<INGREDIENTS> selectedIngs;

    private static final int COLUMN_COUNT = 6;
    private int ROW_COUNT = 0;
    MainController main;

    private LinkedHashMap<Integer, ArrayList<InventoryCafe>> inventoryMap;

    private SpreadsheetView table;

    private static ObservableList<String> productIngredientsNames;
    private static HashMap<String, PRODUCTS> products;
    private static HashMap<String, INGREDIENTS> ingredients;

    @FXML
    AnchorPane inventoryPane;

    @FXML
    Button makeInventory;

    @FXML
    Button selectProducts;

    @FXML
    Button report;

    @FXML
    ProgressIndicator progressToDb;

    @Override
    public void init(MainController main) {
        this.main = main;
        System.out.println(makeInventory.toString());
        makeInventory.setOnAction(this);

        if ( !GlobalPandaApp.config.getOperator().getPosition().equals("Адміністратор") ) {
            selectProducts.setDisable(true);
            report.setDisable(true);
        }

        /*customDateInventory = Calendar.getInstance();
        customDateInventory.set(Calendar.YEAR, 2019);
        customDateInventory.set(Calendar.MONTH, 2);
        customDateInventory.set(Calendar.DATE, 28);
        customDateInventory.set(Calendar.HOUR_OF_DAY, 8);
        customDateInventory.set(Calendar.MINUTE, 0);*/
    }



    private ObservableList<SpreadsheetCell> getHeaderCells (GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> headerCells = FXCollections.observableArrayList();

        SpreadsheetCell product = SpreadsheetCellType.STRING.createCell(row, 0, 2, 1, "Продукт/Інгредієнт/НФ");
        product.setEditable(false);
        product.getStyleClass().add("header-cell");
        headerCells.add(product);

        SpreadsheetCell check = SpreadsheetCellType.STRING.createCell(row, 1, 1, 2, "Спроба 1");
        check.setEditable(false);
        check.getStyleClass().add("header-cell");
        headerCells.add(check);

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, ""));


        SpreadsheetCell fact = SpreadsheetCellType.STRING.createCell(row, 3, 1, 2, "Спроба 2");
        fact.setEditable(false);
        fact.getStyleClass().add("header-cell");
        headerCells.add(fact);

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 4, 1, 1, ""));


        SpreadsheetCell costPerUnit = SpreadsheetCellType.STRING.createCell(row, 5, 2, 1, "Коментар");
        costPerUnit.setEditable(false);
        costPerUnit.getStyleClass().add("header-cell");
        headerCells.add(costPerUnit);


        return headerCells;
    }

    private ObservableList<SpreadsheetCell> getSecondHeaderCells (GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> headerCells = FXCollections.observableArrayList();

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, ""));

        SpreadsheetCell u1check = SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, "U1");
        u1check.setEditable(false);
        u1check.getStyleClass().add("header-cell");
        headerCells.add(u1check);

        SpreadsheetCell u2check = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "U2, гр/мл");
        u2check.setEditable(false);
        u2check.getStyleClass().add("header-cell");
        headerCells.add(u2check);


        SpreadsheetCell u1fact = SpreadsheetCellType.STRING.createCell(row, 3, 1, 1, "U1");
        u1fact.setEditable(false);
        u1fact.getStyleClass().add("header-cell");
        headerCells.add(u1fact);

        SpreadsheetCell u2fact = SpreadsheetCellType.STRING.createCell(row, 4, 1, 1, "U2, гр/мл");
        u2fact.setEditable(false);
        u2fact.getStyleClass().add("header-cell");
        headerCells.add(u2fact);

        SpreadsheetCell comment = SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, "");
        comment.setEditable(false);
        comment.getStyleClass().add("header-cell");
        headerCells.add(comment);

        return headerCells;
    }

    private ObservableList<SpreadsheetCell> getDataCells (GridBase grid, int row, InventoryCafe inv) {
        final ObservableList<SpreadsheetCell> dataCells = FXCollections.observableArrayList();
        SpreadsheetCell item = SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, inv.getProdIngName());

        SpreadsheetCell s1u1 = SpreadsheetCellType.INTEGER.createCell(row, 1, 1, 1, null);
        SpreadsheetCell s1u2 = SpreadsheetCellType.INTEGER.createCell(row, 2, 1, 1, null);
        SpreadsheetCell s2u1 = SpreadsheetCellType.INTEGER.createCell(row, 3, 1, 1, null);
        SpreadsheetCell s2u2 = SpreadsheetCellType.INTEGER.createCell(row, 4, 1, 1, null);

        SpreadsheetCell comment = SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, inv.getComment());

        item.setEditable(false);

        s1u1.getStyleClass().add("number-cell");
        s1u2.getStyleClass().add("number-cell");
        s2u1.getStyleClass().add("number-cell");
        s2u2.getStyleClass().add("number-cell");

        String formatU1 = "";
        String formatU2 = "";

        if (inv.getProdIngId() < 1700 || (inv.getProdIngId() >= 1900 && inv.getProdIngId() < 3000)) {
            item.getStyleClass().add("inv-ing");
            formatU1 = "#,###";
            System.out.println(inv.getProdIngName());
            formatU2 = "#,### " + ingredients.get(inv.getProdIngName()).getUnits();
            s1u1.setFormat(formatU1);
            s1u1.itemProperty().setValue(0);
            s1u1.getStyleClass().add("closed-cell");
            s1u1.setEditable(false);
            s2u1.setFormat(formatU1);
            s2u1.itemProperty().setValue(0);
            s2u1.getStyleClass().add("closed-cell");
            s2u1.setEditable(false);

            s1u2.setFormat(formatU2);
            s1u2.setEditable(true);
            s2u2.setFormat(formatU2);
            s2u2.setEditable(true);
        } else if (inv.getProdIngId() > 1700 && inv.getProdIngId() < 1900) {
            item.getStyleClass().add("inv-nf");
            formatU1 = "#,###";
            System.out.println("->" + inv.getProdIngName());
            formatU2 = "#,### " + ingredients.get(inv.getProdIngName()).getUnits();
            s1u1.setFormat(formatU1);
            s1u1.itemProperty().setValue(0);
            s1u1.getStyleClass().add("closed-cell");
            s1u1.setEditable(false);
            s2u1.setFormat(formatU1);
            s2u1.itemProperty().setValue(0);
            s2u1.getStyleClass().add("closed-cell");
            s2u1.setEditable(false);

            s1u2.setFormat(formatU2);
            s1u2.setEditable(true);
            s2u2.setFormat(formatU2);
            s2u2.setEditable(true);
        } else if (inv.getProdIngId() > 3000) {
            item.getStyleClass().add("inv-prod");
            System.out.println(inv.getProdIngName());
            PRODUCTS prod = products.get(inv.getProdIngName());
            System.out.println(prod.getProductName());
            if (prod.getFirstUnits() == null || prod.getFirstUnits().isEmpty() || prod.getFirstUnits().equals(" ")) {
                formatU1 = "#,###";
                s1u1.setEditable(false);
                s1u1.getStyleClass().add("closed-cell");
                s2u1.setEditable(false);
                s2u1.getStyleClass().add("closed-cell");
                s1u1.itemProperty().setValue(0);
                s2u1.itemProperty().setValue(0);
            } else {
                formatU1 = "#,### " + prod.getFirstUnits();
                s1u1.setEditable(true);
                s2u1.setEditable(true);
            }

            if (prod.getUnitsRelation() > 0) {
                s1u1.itemProperty().addListener((observable, oldValue, newValue) -> {
                    int value = fromTableCell(s1u1.getText()).intValue() * prod.getUnitsRelation();
                    s1u2.itemProperty().setValue(value);
                });
                s2u1.itemProperty().addListener((observable, oldValue, newValue) -> {
                    int value = fromTableCell(s2u1.getText()).intValue() * prod.getUnitsRelation();
                    s2u2.itemProperty().setValue(value);
                });
                s1u2.setEditable(false);
                s1u2.getStyleClass().add("closed-cell");
                s2u2.setEditable(false);
                s2u2.getStyleClass().add("closed-cell");
            }

            s1u1.setFormat(formatU1);
            s2u1.setFormat(formatU1);

            formatU2 = "#,### " + prod.getSecondUnits();

            s1u2.setFormat(formatU2);
            s2u2.setFormat(formatU2);

            if (inv.getProdIngId().intValue() == 3050) {
                s1u1.itemProperty().setValue(0);
                s1u2.itemProperty().setValue(0);
                s2u1.itemProperty().setValue(0);
                s2u2.itemProperty().setValue(0);
                s1u1.setEditable(false);
                s1u2.setEditable(false);
                s2u1.setEditable(false);
                s2u2.setEditable(false);
                "sdfd".toUpperCase();
            }
        }


        //BLOCK CELLS
        if (inv.getFirstAttempt() == null) {
            s2u1.setEditable(false);
            s2u1.getStyleClass().add("closed-cell");
            s2u2.setEditable(false);
            s2u2.getStyleClass().add("closed-cell");
        } else if (inv.getFirstAttempt()) {
            s1u1.setEditable(false);
            s1u1.getStyleClass().add("closed-cell");
            s1u1.itemProperty().setValue(inv.getAttemptU1());
            s1u2.setEditable(false);
            s1u2.getStyleClass().add("closed-cell");
            s1u2.itemProperty().setValue(inv.getAttemptU2());
            s2u1.setEditable(false);
            s2u1.getStyleClass().add("closed-cell");
            s2u1.itemProperty().setValue(inv.getFactU1());
            s2u2.setEditable(false);
            s2u2.getStyleClass().add("closed-cell");
            s2u2.itemProperty().setValue(inv.getFactU2());
        } else {
            int diffUAH = Math.abs(inv.getDiffCompensation());
            String c = "";
            if (inv.getFactU2() == null) {
                if (diffUAH >= RED_ZONE)
                    c = "-red";
                else if (diffUAH >= ORANGE_ZONE)
                    c = "-orange";
                else if (diffUAH > 0)
                    c = "-yellow";
                else
                    c = "";
            }
            s1u1.setEditable(false);
            s1u1.getStyleClass().add("closed-cell" + c);
            s1u1.itemProperty().setValue(inv.getAttemptU1());
            s1u2.setEditable(false);
            s1u2.getStyleClass().add("closed-cell" + c);
            s1u2.itemProperty().setValue(inv.getAttemptU2());
            if (inv.getFactU1() != null) {
                s2u1.setEditable(false);
                s2u1.getStyleClass().add("closed-cell");
                s2u1.itemProperty().setValue(inv.getFactU1());
                s2u2.setEditable(false);
                s2u2.getStyleClass().add("closed-cell");
                s2u2.itemProperty().setValue(inv.getFactU2());
            }
        }

        dataCells.addAll(item, s1u1, s1u2, s2u1, s2u2, comment);
        return dataCells;
    }

    private ObservableList<SpreadsheetCell> getClosedCells (GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> closedCells = FXCollections.observableArrayList();
        for (int i = 0; i < COLUMN_COUNT; i++) {
            SpreadsheetCell closed = SpreadsheetCellType.STRING.createCell(row, i, 1, 1, "");
            closed.setEditable(false);
            closed.getStyleClass().add("inv-closed");
            closedCells.add(closed);
        }
        return closedCells;
    }


    private void buildGrid (GridBase grid) {
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();

        int rowIndex = 0;
        rows.add(getHeaderCells(grid, rowIndex++));
        rows.add(getSecondHeaderCells(grid, rowIndex++));

        for (Integer i : inventoryMap.keySet()) {
            for (InventoryCafe inv : inventoryMap.get(i))
                rows.add(getDataCells(grid, rowIndex++, inv));
            if (rowIndex < ROW_COUNT - 1)
                rows.add(getClosedCells(grid, rowIndex++));
        }

        grid.setRows(rows);

        grid.spanRow(2, 0, 0);
        grid.spanColumn(2 , 0, 1);
        grid.spanColumn(2, 0, 3);
        grid.spanRow(2, 0, 5);
    }

    private void createSpreadSheetView () {
        setRowCount();
        getProducts();
        GridBase grid = new GridBase(ROW_COUNT, COLUMN_COUNT);
        grid.setRowHeightCallback(new GridBase.MapBasedRowHeightFactory(generateRowHeight()));

        buildGrid(grid);

        table = new SpreadsheetView(grid);
        table.setShowRowHeader(true);
        table.setShowColumnHeader(false);
        table.setEditable(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getFixedRows().addAll(0, 1);
        for (SpreadsheetColumn column : table.getColumns()) {
            column.setPrefWidth(90.0);
        }
        table.getColumns().get(0).setPrefWidth(310.0);
        table.getColumns().get(5).setPrefWidth(180.0);

        AnchorPane.setLeftAnchor(table, 5.0);
        AnchorPane.setRightAnchor(table, 5.0);
        AnchorPane.setBottomAnchor(table, 5.0);
        AnchorPane.setTopAnchor(table, 65.0);

        inventoryPane.getChildren().add(table);

        for (ObservableList<SpreadsheetCell> row : table.getGrid().getRows()) {
            System.out.println(row.get(0).getText());
            for (SpreadsheetCell cell : row) {
                System.out.print(cell.getFormat() + " - ");
            }
            System.out.println();
        }
    }


    private ObservableList<String> getProducts() {
        if (productIngredientsNames == null) {
            productIngredientsNames = FXCollections.observableArrayList();
            products = new HashMap<>();
            ingredients = new HashMap<>();

            for (PRODUCTS product : GlobalPandaApp.site.getProducts()) {
                productIngredientsNames.add(product.getProductName());
                products.put(product.getProductName(), product);
            }

            for (INGREDIENTS ingredient : GlobalPandaApp.site.getIngredients()) {
                productIngredientsNames.add(ingredient.getIngredientName());
                ingredients.put(ingredient.getIngredientName(), ingredient);
                System.out.println(ingredient.getIngredientName());
            }
        }
        return productIngredientsNames;
    }

    @Override
    public void handle(ActionEvent event) {
        System.out.println("Click handled");
        if (GlobalPandaApp.site.alreadyCafeInventory(customDateInventory == null ? Calendar.getInstance() : customDateInventory)) {
            if (table == null) {
                inventoryMap = GlobalPandaApp.site.getTodayCafeInventory(customDateInventory == null ? Calendar.getInstance() : customDateInventory);
                try {
                    new InventoryCafePdf(inventoryMap).createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                createSpreadSheetView();
            }
            return;
        }
        getProducts();


        fillInventoryMap();

        for (Integer i : inventoryMap.keySet()) {
            ArrayList<InventoryCafe> invList = inventoryMap.get(i);
            CalculatedNetto calcNetto = GlobalPandaApp.site.getCalculatedNettoCafeOnDate(GlobalPandaApp.config.getKitchen().getKitch_id(), i, (customDateInventory == null ? Calendar.getInstance() : customDateInventory));
            for (InventoryCafe inventory : invList) {
                inventory.setPrevious(calcNetto.getLastInventoryDate().getTime());
                inventory.setPreviousNetto(calcNetto.getLastInventoryNetto());
                inventory.setProductPurchase(calcNetto.getProductPurchaseNetto());
                inventory.setProductShift(calcNetto.getShiftNetto());
                inventory.setWriteOffNetto(calcNetto.getWriteOffNetto());
                inventory.setRozrobka(calcNetto.getDiffProcessing());
                inventory.setRozhid(calcNetto.getConsumptionNetto());
                inventory.setCalculatedNetto(calcNetto.getCalculatedNetto());
                inventory.setIngPrice(GlobalPandaApp.site.getPrice(i, Calendar.getInstance()));
                if (inventory.getIngPrice() == null) {
                    inventory.setIngPrice(GlobalPandaApp.site.getIngCafeCost(invList));
                }
            }
            invList.sort((o1, o2) -> {
                if (o1.getProdIngId() < 1700)
                    return 1;
                else if (o2.getProdIngId() < 1700)
                    return -1;
                else if (o1.getProdIngId() < 1900)
                    return 1;
                else if (o2.getProdIngId() < 1900)
                    return -1;
                else
                    return o1.getProdIngName().compareTo(o2.getProdIngName());
            });
        }


        if (GlobalPandaApp.site.saveCafeInventory(inventoryMap)) {
            inventoryMap = GlobalPandaApp.site.getTodayCafeInventory(customDateInventory == null ? Calendar.getInstance() : customDateInventory);
            createSpreadSheetView();
            try {
                new InventoryCafePdf(inventoryMap).createPdf();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkProduct (Integer productId, ArrayList<InventoryCafe> prev) {
        if (prev.isEmpty())
            return true;
        for (InventoryCafe inv : prev)
            if (inv.getProdIngId().equals(productId) && inv.getFactU2() > 0)
                return true;
        if (GlobalPandaApp.site.getSumProdPurchase(prev.get(0).getBegin(), (customDateInventory == null ? new Date() : customDateInventory.getTime()) , productId, GlobalPandaApp.config.getKitchen().getKitch_id()) > 0)
            return true;
        if (GlobalPandaApp.site.getSumProdShift(prev.get(0).getBegin(), (customDateInventory == null ? new Date() : customDateInventory.getTime()), productId, GlobalPandaApp.config.getKitchen().getKitch_id()) > 0)
            return true;
        return false;
    }

    private void fillInventoryMap () {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 9);
        Calendar yearAgo = Calendar.getInstance();
        yearAgo.add(Calendar.YEAR, -1);
        Date date = (customDateInventory == null ? cal.getTime() : customDateInventory.getTime());
        Date timeStart = (customDateInventory == null ? new Date() : customDateInventory.getTime());
        Integer checkId = GlobalPandaApp.site.getNextCafeInventoryCheckId();
        inventoryMap = new LinkedHashMap<>();

        for (INGREDIENTS ing : getIngForInventory(GlobalPandaApp.site.getIngredients())) {
            System.out.println(ing == null ? "NULL ING" : "ING ID = " + ing.getIngredientId() + " - " + ing.getIngredientName());
            ArrayList<InventoryCafe> previous =
                    GlobalPandaApp.site.getLastCafeInventory(
                            ing.getIngredientId(),
                            GlobalPandaApp.config.getKitchen().getKitch_id()
                    );
            Date prevDate = previous.isEmpty() ? yearAgo.getTime() : previous.get(0).getBegin();

            inventoryMap.put(ing.getIngredientId(), new ArrayList<>());
            InventoryCafe inv = new InventoryCafe();
            inv.setPrevious(prevDate);
            inv.setBegin(date);
            inv.setTimeStart(timeStart);
            inv.setKitchen(GlobalPandaApp.config.getKitchen().getKitch_id());
            inv.setCheckId(checkId);
            inv.setCook(GlobalPandaApp.config.getOperator().getName());
            inv.setBasicIng(ing.getIngredientId());
            inv.setProdIngName(ing.getIngredientName());
            inv.setProdIngId(ing.getIngredientId());
            inventoryMap.get(ing.getIngredientId()).add(inv);
            for (PRODUCTS_INGREDIENTS prodIng : GlobalPandaApp.site.getProductsInventory(ing.getIngredientId())) {
                if (!checkProduct(prodIng.getProductId(), previous))
                    continue;
                InventoryCafe prod = new InventoryCafe();
                prod.setPrevious(prevDate);
                prod.setBegin(date);
                prod.setTimeStart(timeStart);
                prod.setKitchen(GlobalPandaApp.config.getKitchen().getKitch_id());
                prod.setCheckId(checkId);
                prod.setCook(GlobalPandaApp.config.getOperator().getName());
                prod.setBasicIng(ing.getIngredientId());
                prod.setProdIngName(prodIng.getProductName());
                prod.setProdIngId(prodIng.getProductId());
                prod.setUnit1(products.get(prodIng.getProductName()).getFirstUnits());
                prod.setProdRelation(products.get(prodIng.getProductName()).getUnitsRelation());
                inventoryMap.get(ing.getIngredientId()).add(prod);
            }
            for (TEHCARDS_CAFE teh : GlobalPandaApp.site.getCafeNF(ing.getIngredientId())) {
                InventoryCafe nf = new InventoryCafe();
                nf.setPrevious(prevDate);
                nf.setBegin(date);
                nf.setTimeStart(timeStart);
                nf.setKitchen(GlobalPandaApp.config.getKitchen().getKitch_id());
                nf.setCheckId(checkId);
                nf.setCook(GlobalPandaApp.config.getOperator().getName());
                nf.setBasicIng(ing.getIngredientId());
                nf.setProdIngName(teh.getDishName());
                nf.setProdIngId(teh.getDishId());
                inventoryMap.get(ing.getIngredientId()).add(nf);
            }
        }
    }

    public ArrayList<INGREDIENTS> getIngForInventory (ArrayList<INGREDIENTS> list) {
        list.sort(Comparator.comparing(INGREDIENTS::getIngredientName));
        if (total && selectedIngs != null)
            return selectedIngs;

        ArrayList<INGREDIENTS> result = new ArrayList<>();

        HashMap<Integer, INGREDIENTS> ingMap = new HashMap<>();
        for (INGREDIENTS ing : list) {
            ingMap.put(ing.getIngredientId(), ing);
        }

        for (InvCafeSelect is : GlobalPandaApp.site.fillTodayCafeInventory())
            result.add(ingMap.get(is.getIngId()));

        return result;
    }

    private void setRowCount () {
        if ( inventoryMap != null ) {
            int invCount = 0;
            for (ArrayList<InventoryCafe> list : inventoryMap.values())
                invCount += list.size();
            ROW_COUNT = inventoryMap.keySet().size() + invCount + 1;
            System.out.println("ROW_COUNT = " + ROW_COUNT);
        }
    }

    private Map<Integer, Double> generateRowHeight() {
        Map<Integer, Double> rowHeight = new HashMap<>();
        rowHeight.put(1, 50.0);
        return rowHeight;
    }

    public Float fromTableCell (String s) {
        if (!s.matches("\\-?\\d+(\\.\\d{0,})?")) {
            s = s.replaceAll(",", ".");
            s = s.replaceAll("[^\\d\\.]", "");
        }
        if (s.isEmpty())
            s = "0";
        float res = Float.parseFloat(s);
        return res;
    }



    public void writeToDB(ActionEvent event) {
        //Check if inventoryMap is present
        if (inventoryMap == null)
            return;

        //Show progress label
        progressToDb.setVisible(true);

        //Check for PDF print
        boolean checked = true;

        Date date = (customDateInventory == null ? new Date() : customDateInventory.getTime());

        //for each basic ingredient
        for (Integer i : inventoryMap.keySet()) {
            //for each proding of basic
            for (InventoryCafe inv : inventoryMap.get(i)) {
                //looking for row contains proding
                for (ObservableList<SpreadsheetCell> row : table.getGrid().getRows()) {
                    if (row.get(0).getText().equals(inv.getProdIngName())) {
                        if (inv.getCalculatedNetto() == 0) {
                            //FOR FIRST INVENTORY
                            if (row.get(1).getItem() != null && row.get(2).getItem() != null) {
                                row.get(1).deactivateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
                                row.get(2).deactivateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
                                inv.setAttemptTime(date);
                                inv.setEnd(date);
                                inv.setAttemptU1(fromTableCell(row.get(1).getText()).intValue());
                                inv.setFactU1(inv.getAttemptU1());
                                inv.setAttemptU2(fromTableCell(row.get(2).getText()).intValue());
                                inv.setFactU2(inv.getAttemptU2());
                                inv.setFirstAttempt(true);
                                inv.setComment(row.get(5).getText());
                                inv.setDiffCompensation(0);
                                inv.setDiffNetto(0);
                                inv.setDiffPercent(0.0f);
                                inv.setDiffUah(0);
                            } else {
                                row.get(1).activateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
                                row.get(2).activateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
                                table.getSelectionModel().select(row.get(1).getRow(), table.getColumns().get(row.get(1).getColumn()));
                                table.getSelectionModel().focus(row.get(1).getRow(), table.getColumns().get(row.get(1).getColumn()));
                                return;
                            }
                        } else {
                            if (inv.getFirstAttempt() == null) {
                                if (row.get(1).getItem() != null && row.get(2).getItem() != null) {
                                    row.get(1).deactivateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
                                    row.get(2).deactivateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
                                    inv.setAttemptTime(date);
                                    inv.setAttemptU1(fromTableCell(row.get(1).getText()).intValue());
                                    inv.setAttemptU2(fromTableCell(row.get(2).getText()).intValue());
                                    inv.setComment(row.get(5).getText());
                                } else {
                                    row.get(1).activateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
                                    row.get(2).activateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
                                    table.getSelectionModel().select(row.get(1).getRow(), table.getColumns().get(row.get(1).getColumn()));
                                    table.getSelectionModel().focus(row.get(1).getRow(), table.getColumns().get(row.get(1).getColumn()));
                                    return;
                                }
                            } else if (!inv.getFirstAttempt() && inv.getFactU2() == null) {
                                if (row.get(3).getItem() != null && row.get(4).getItem() != null) {
                                    row.get(3).deactivateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
                                    row.get(4).deactivateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
                                    inv.setEnd(date);
                                    inv.setFactU1(fromTableCell(row.get(3).getText()).intValue());
                                    inv.setFactU2(fromTableCell(row.get(4).getText()).intValue());
                                    inv.setComment(row.get(5).getText());
                                } else {
                                    row.get(3).activateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
                                    row.get(4).activateCorner(SpreadsheetCell.CornerPosition.TOP_RIGHT);
                                    table.getSelectionModel().select(row.get(3).getRow(), table.getColumns().get(row.get(3).getColumn()));
                                    table.getSelectionModel().focus(row.get(3).getRow(), table.getColumns().get(row.get(3).getColumn()));
                                    return;
                                }
                            }
                        }
                        break;
                    }
                }

            }

            Float inputNetto = getInputNetto(inventoryMap.get(i));

            float calculatedNetto = inventoryMap.get(i).get(0).getCalculatedNetto().floatValue();
            float calculatedCons = inventoryMap.get(i).get(0).getRozhid().floatValue();
            float diffNetto = inputNetto - calculatedNetto;
            float diffPercent;
            if (calculatedCons != 0.0f)
                diffPercent = 100 * diffNetto / calculatedCons;
            else {
                diffPercent = 100 * diffNetto / ( inventoryMap.get(i).get(0).getAttemptU2().floatValue() != 0 ? inventoryMap.get(i).get(0).getAttemptU2().floatValue() : 1);
            }

            float cpu = inventoryMap.get(i).get(0).getIngPrice();

            float diffUAH = diffNetto * cpu;

            InventoryCafe temp = inventoryMap.get(i).get(0);
            if (temp.getFirstAttempt() == null && Math.abs(diffPercent) <= 5) {
                GlobalPandaApp.site.updateImportance(temp.getBasicIng(), temp.getKitchen(), -1);
            } else if (temp.getFirstAttempt() != null && !temp.getFirstAttempt() && Math.abs(diffPercent) > 5) {
                GlobalPandaApp.site.updateImportance(temp.getBasicIng(), temp.getKitchen(), 3);
            }

            for (InventoryCafe inv : inventoryMap.get(i)) {
                inv.setInputNetto(inputNetto.intValue());
                if ( inv.getFirstAttempt() == null ) {
                    if (Math.abs(diffPercent) <= 5) {
                        inv.setDiffUah( (int) diffUAH);
                        inv.setDiffCompensation(0);
                        inv.setDiffNetto( (int) diffNetto);
                        inv.setDiffPercent(diffPercent);
                        inv.setEnd(inv.getAttemptTime());
                        inv.setFirstAttempt(true);
                        inv.setFactU1(inv.getAttemptU1());
                        inv.setFactU2(inv.getAttemptU2());
                    } else {
                        checked = false;
                        float diffComp = ( diffUAH / Math.abs(diffPercent) ) * (Math.abs(diffPercent) - 5);
                        inv.setDiffUah( Math.round(diffUAH) );
                        inv.setDiffCompensation( Math.round(diffComp) );
                        inv.setDiffNetto( Math.round(diffNetto) );
                        inv.setDiffPercent(diffPercent);
                        inv.setFirstAttempt(false);
                    }
                } else {
                    if (Math.abs(diffPercent) <= 5) {
                        inv.setDiffUah( (int) diffUAH);
                        inv.setDiffCompensation(0);
                        inv.setDiffNetto( (int) diffNetto);
                        inv.setDiffPercent(diffPercent);
                        inv.setEnd(date);
                    } else {
                        float diffComp = ( diffUAH / Math.abs(diffPercent) ) * (Math.abs(diffPercent) - 5);
                        inv.setDiffUah( (int) diffUAH);
                        inv.setDiffCompensation( (int) diffComp);
                        inv.setDiffNetto( (int) diffNetto);
                        inv.setDiffPercent(diffPercent);
                        inv.setEnd(date);
                    }
                }
            }
        }
        if (GlobalPandaApp.site.saveCafeInventory(inventoryMap)) {
            inventoryMap = GlobalPandaApp.site.getTodayCafeInventory(customDateInventory == null ? Calendar.getInstance() : customDateInventory);
            inventoryPane.getChildren().remove(table);
            createSpreadSheetView();
            try {
                if(checked)
                    new InventoryCafePdf(inventoryMap).createPdf();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        progressToDb.setVisible(false);
    }

    private float getInputNetto (ArrayList<InventoryCafe> list) {
        float inputNetto = 0.0f;

        for (InventoryCafe inv : list) {
            if (inv.getFirstAttempt() == null) {
                if (inv.getProdIngId() < 1700) {
                    inputNetto += inv.getAttemptU2().floatValue();
                } else if (inv.getProdIngId() < 1900) {
                    float nfWeight = inv.getAttemptU2();
                    float coef = GlobalPandaApp.site.getCoefCafeNf(inv.getBasicIng(),inv.getProdIngId());
                    inputNetto += nfWeight / coef ;
                } else if (inv.getProdIngId() > 3000) {
                    float prodWeight = inv.getAttemptU2().floatValue();
                    float coef = GlobalPandaApp.site.getCoefToNettoOnDate(inv.getProdIngId(), Calendar.getInstance());
                    inputNetto += prodWeight / coef;
                }
            } else {
                if (inv.getProdIngId() < 1700) {
                    inputNetto += inv.getFactU2().floatValue();
                } else if (inv.getProdIngId() < 1900) {
                    float nfWeight = inv.getFactU2();
                    float coef = GlobalPandaApp.site.getCoefCafeNf(inv.getBasicIng(),inv.getProdIngId());
                    inputNetto += nfWeight / coef;
                } else if (inv.getProdIngId() > 3000) {
                    float prodWeight = inv.getFactU2().floatValue();
                    float coef = GlobalPandaApp.site.getCoefToNettoOnDate(inv.getProdIngId(), Calendar.getInstance());
                    inputNetto += prodWeight / coef ;
                }
            }
        }

        return inputNetto;
    }

    public void makeReport(ActionEvent event) {
        Font font = new Font("Calibri", 16.0);
        Stage selectInv = new Stage(StageStyle.UTILITY);
        selectInv.initModality(Modality.APPLICATION_MODAL);
        AnchorPane root = new AnchorPane();
        root.setPrefWidth(190);
        root.setPrefHeight(70);
        root.setMaxHeight(70);
        Label label = new Label("Оберіть інвентаризацію: ");
        label.setFont(font);
        AnchorPane.setLeftAnchor(label, 5.0);
        AnchorPane.setTopAnchor(label, 5.0);
        ProgressBar progress = new ProgressBar();
        progress.setVisible(false);
        AnchorPane.setTopAnchor(progress, 40.0);
        AnchorPane.setRightAnchor(progress, 5.0);
        AnchorPane.setLeftAnchor(progress, 5.0);

        ComboBox<InventoryCafe> invents = new ComboBox<>();
        HashSet<Integer> checkIds = new HashSet<>();
        for (InventoryCafe inv : GlobalPandaApp.site.getAllCafeInventory()) {
            if (checkIds.add(inv.getCheckId()))
                invents.getItems().add(inv);
        }
        AnchorPane.setRightAnchor(invents, 5.0);
        AnchorPane.setTopAnchor(invents, 5.0);
        Button total = new Button("Загальний");
        total.setPrefWidth(130);
        AnchorPane.setBottomAnchor(total, 5.0);
        AnchorPane.setRightAnchor(total, 10.0);
        Button make = new Button("Сформувати");
        make.setPrefWidth(130);
        AnchorPane.setBottomAnchor(make, 5.0);
        AnchorPane.setLeftAnchor(make, 10.0);
        make.setFont(new Font("Calibri", 18.0));
        total.setFont(new Font("Calibri", 18.0));
        make.setTextFill(new Color(0.0,0.4,0.0,1.0));
        total.setTextFill(new Color(0.3, 0.0, 0.0, 1.0));
        InventoryCafeReport ir = new InventoryCafeReport();
        ir.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                File file = saveFile(invents.getValue().toString());
                try {
                    newValue.write(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Desktop desktop = null;

                if(Desktop.isDesktopSupported())
                    desktop = Desktop.getDesktop();

                if( desktop != null )
                    try {
                        desktop.open(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                selectInv.hide();
            }
        });
        make.setOnAction(event1 -> {
            progress.setVisible(true);
            InventoryCafe inv = invents.getValue();
            ir.setInv(inv);
            progress.progressProperty().bind(ir.progressBar);
            make.setDisable(true);
            Thread thread = new Thread(ir);
            thread.setDaemon(true);
            try {
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        root.getChildren().addAll(label, invents, make, progress, total);
        Scene scene = new Scene(root, 400, 120);
        selectInv.setScene(scene);
        selectInv.show();
    }

    public File saveFile(String initFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(initFileName);
        String userDir = System.getProperty("user.home");
        fileChooser.setInitialDirectory(new File(userDir));
        fileChooser.setTitle("Зберегти звіт...");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(GlobalPandaApp.mainStage);
        if(file != null) {
            return file;
        } else {
            return new File("temp.xlsx");
        }
    }

    public void selectProducts(ActionEvent event) {
        total = true;
        ArrayList<INGREDIENTS> ings = GlobalPandaApp.site.getCafeIngredients();
        ings.sort(Comparator.comparing(INGREDIENTS::getIngredientName));
        Stage selectStage = new Stage(StageStyle.UTILITY);
        selectStage.initModality(Modality.APPLICATION_MODAL);
        AnchorPane root = new AnchorPane();
        root.setPrefWidth(300);
        root.setPrefHeight(600);
        ListView<InventoryCafeTabController.ListItem> list = new ListView<>();
        for (INGREDIENTS ing : ings) {
            InventoryCafeTabController.ListItem item = new InventoryCafeTabController.ListItem(ing.getIngredientName(), false);
            item.onProperty().addListener((observable, oldValue, newValue) -> {

            });
            list.getItems().add(item);
        }
        list.setCellFactory(CheckBoxListCell.forListView(new Callback<InventoryCafeTabController.ListItem, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(InventoryCafeTabController.ListItem item) {
                return item.onProperty();
            }
        }));
        Button selectAll = new Button("Вибрати всі");
        selectAll.setOnAction(event1 -> {
            if (selectAll.getText().equals("Вибрати всі")) {
                for (InventoryCafeTabController.ListItem li : list.getItems()) {
                    li.onProperty().set(true);
                }
                selectAll.setText("Забрати всі");
            } else {
                for (InventoryCafeTabController.ListItem li : list.getItems()) {
                    li.onProperty().set(false);
                }
                selectAll.setText("Вибрати всі");
            }
        });
        AnchorPane.setLeftAnchor(selectAll, 5.0);
        AnchorPane.setBottomAnchor(selectAll, 5.0);

        Button done = new Button("Готово");
        done.setOnAction(event1 -> {
            for (InventoryCafeTabController.ListItem item : list.getItems()) {
                if (item.on.get()) {
                    System.out.println("Selected : " + item.getName());
                } else {
                    for (Iterator<INGREDIENTS> it = ings.iterator(); it.hasNext(); ) {
                        if (it.next().getIngredientName().equals(item.getName())) {
                            it.remove();
                            break;
                        }
                    }
                }
            }
            for (INGREDIENTS ing : ings) {
                System.out.println(" - Selected : " + ing.getIngredientName());
            }
            selectedIngs = ings;

            if (selectedIngs.size() < 5) {
                int left = 5 - selectedIngs.size();

            }

            selectStage.hide();
        });
        AnchorPane.setTopAnchor(list, 5.0);
        AnchorPane.setLeftAnchor(list, 5.0);
        AnchorPane.setRightAnchor(list, 5.0);
        AnchorPane.setBottomAnchor(list, 30.0);
        AnchorPane.setRightAnchor(done, 5.0);
        AnchorPane.setBottomAnchor(done, 5.0);
        root.getChildren().addAll(list, done, selectAll);
        Scene scene = new Scene(root, 300, 600);
        selectStage.setScene(scene);
        selectStage.show();
    }


    private class ListItem {
        private final StringProperty name = new SimpleStringProperty();
        private final BooleanProperty on = new SimpleBooleanProperty();

        public ListItem(String name, boolean on) {
            setName(name);
            setOn(on);
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public boolean isOn() {
            return on.get();
        }

        public BooleanProperty onProperty() {
            return on;
        }

        public void setOn(boolean on) {
            this.on.set(on);
        }

        @Override
        public String toString() {
            return getName();
        }
    }

}
