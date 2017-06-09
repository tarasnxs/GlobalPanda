package ua.com.pandasushi.controller.kitchen.inventory;

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
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import ua.com.pandasushi.database.common.Inventory;
import ua.com.pandasushi.database.common.menu.INGREDIENTS;
import ua.com.pandasushi.database.common.menu.PRODUCTS;
import ua.com.pandasushi.database.common.menu.PRODUCTS_INGREDIENTS;
import ua.com.pandasushi.database.common.menu.TEHCARDS;
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
public class InventoryTabController implements TabController, EventHandler<ActionEvent> {
    boolean total = false;


    private ArrayList<INGREDIENTS> selectedIngs;

    private static final int COLUMN_COUNT = 6;
    private int ROW_COUNT = 0;
    MainController main;

    private LinkedHashMap<Integer, ArrayList<Inventory>> inventoryMap;

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
        makeInventory.setOnAction(this);

        if ( !GlobalPandaApp.config.getOperator().getPosition().equals("Адміністратор") ) {
            selectProducts.setDisable(true);
            report.setDisable(true);
        }

        if ( GlobalPandaApp.config.getOperator().getName().equals("Роман Скрип") ) {
            selectProducts.setDisable(false);
            report.setDisable(false);
        }
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

    private ObservableList<SpreadsheetCell> getDataCells (GridBase grid, int row, Inventory inv) {
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

        if (inv.getProdIngId() < 1700) {
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
            PRODUCTS prod = products.get(inv.getProdIngName());
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
            s1u1.setEditable(false);
            s1u1.getStyleClass().add("closed-cell");
            s1u1.itemProperty().setValue(inv.getAttemptU1());
            s1u2.setEditable(false);
            s1u2.getStyleClass().add("closed-cell");
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
            for (Inventory inv : inventoryMap.get(i))
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
                if (ingredient.getIngredientId() >= 1900)
                    continue;
                productIngredientsNames.add(ingredient.getIngredientName());
                ingredients.put(ingredient.getIngredientName(), ingredient);
                System.out.println(ingredient.getIngredientName());
            }
        }
        return productIngredientsNames;
    }

    @Override
    public void handle(ActionEvent event) {
        if (GlobalPandaApp.site.alreadyInventory()) {
            if (table == null) {
                inventoryMap = GlobalPandaApp.site.getTodayInventory();
                try {
                    new InventoryPdf(inventoryMap).createPdf();
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
            int calcWeight = 0;
            int calcCons = 0;
            ArrayList<Inventory> list = inventoryMap.get(i);
            ArrayList<Inventory> previous = GlobalPandaApp.site.getLastInventory(i, GlobalPandaApp.config.getKitchen().getKitch_id());

            System.out.println("\n##############\n" + i + " - " + list.get(0));

            if (!previous.isEmpty()) {

                HashSet<Integer> founded = new HashSet<>();
                HashMap<Integer, Inventory> curInv = new HashMap<>();

                for (Inventory n : list) {
                    for (Inventory p : previous) {
                        if (p.getProdIngId().equals(n.getProdIngId())) {
                            founded.add(n.getProdIngId());
                            break;
                        }
                    }

                    if (!founded.contains(n.getProdIngId()))
                        previous.add(n);

                    curInv.put(n.getProdIngId(), n);
                }


                for (Inventory inv : previous) {
                    System.out.println( "\n--- Рахуємо : " + inv.getProdIngName() );
                    Inventory cur = curInv.get(inv.getProdIngId());

                    if (inv.getProdIngId() < 1700) {
                        //INGREDIENT
                        int ingShift = GlobalPandaApp.site.getSumProdShift(inv.getBegin(), list.get(0).getBegin(), inv.getProdIngId(), GlobalPandaApp.config.getKitchen().getKitch_id());
                        int ingCons = GlobalPandaApp.site.getIngredientConsumption(inv.getBegin(), list.get(0).getBegin(), inv.getProdIngId(), GlobalPandaApp.config.getKitchen().getKitch_id());

                        calcWeight += inv.getFactU2() != null ? inv.getFactU2() : 0;
                        System.out.println("------Додаємо " + ( inv.getFactU2() != null ? inv.getFactU2() : 0 ) + " з попередньої інвентаризації");
                        calcWeight += ingShift;
                        System.out.println("------Додаємо " + ingShift + " з переміщення");
                        calcWeight -= ingCons;
                        System.out.println("------Віднімаємо " + ingCons + " з розходу");
                        calcCons += ingCons;

                        cur.setRozhid(ingCons * -1);
                        cur.setProductShift(ingShift);
                    } else if (inv.getProdIngId() > 1700 && inv.getProdIngId() < 1900) {
                        //NF
                        int nfShift = GlobalPandaApp.site.getSumProdShift(inv.getBegin(), list.get(0).getBegin(), inv.getProdIngId(), GlobalPandaApp.config.getKitchen().getKitch_id());
                        int nfCons = GlobalPandaApp.site.getIngredientConsumption(inv.getBegin(), list.get(0).getBegin(), inv.getProdIngId(), GlobalPandaApp.config.getKitchen().getKitch_id());

                        int nfWeight = inv.getFactU2() != null ? inv.getFactU2() : 0;
                        System.out.println("------Додаємо " + ( inv.getFactU2() != null ? inv.getFactU2() : 0 ) + " з попередньої інвентаризації");
                        nfWeight += nfShift;
                        System.out.println("------Додаємо " + nfShift + " з переміщення");
                        nfWeight -= nfCons;
                        System.out.println("------Віднімаємо " + nfCons + " з розходу");
                        ArrayList<TEHCARDS> tehcard = GlobalPandaApp.site.getTehcardsForDish(inv.getProdIngId());
                        int countIng = 0;
                        int totalWeight = 0;
                        for ( TEHCARDS tc : tehcard ) {
                            totalWeight += tc.getFinalWeight();
                            if (tc.getIngredientId().equals(i))
                                countIng = tc.getCount().intValue();
                        }
                        System.out.println("------З коефіцієнтом " + (double) countIng / (double) totalWeight + " переводимо в інг. Рез : " + (int) ( (double) nfWeight * (double) countIng / (double) totalWeight ) );
                        calcWeight += (int) ( (double) nfWeight * (double) countIng / (double) totalWeight );
                        calcCons += (int) ( (double) nfCons * (double) countIng / (double) totalWeight );
                        cur.setRozhid(nfCons * -1);
                        cur.setProductShift(nfShift);
                    } else if (inv.getProdIngId() > 3000) {
                        //PRODUCT
                        int prodPurch = GlobalPandaApp.site.getSumProdPurchase(inv.getBegin(), list.get(0).getBegin(), inv.getProdIngId(), GlobalPandaApp.config.getKitchen().getKitch_id());
                        int prodShift = GlobalPandaApp.site.getSumProdShift(inv.getBegin(), list.get(0).getBegin(), inv.getProdIngId(), GlobalPandaApp.config.getKitchen().getKitch_id());
                        ArrayList<Integer> rozrobka = GlobalPandaApp.site.getRozrobka(inv.getBegin(), list.get(0).getBegin(), inv.getProdIngId(), GlobalPandaApp.config.getKitchen().getKitch_id());

                        int prodWeight = inv.getFactU2() != null ? inv.getFactU2() : 0;
                        System.out.println("------Додаємо " + ( inv.getFactU2() != null ? inv.getFactU2() : 0 ) + " з попередньої інвентаризації");

                        //TODO int tushkaCount = inv.getFactU1();
                        prodWeight += prodPurch;
                        System.out.println("------Додаємо " + prodPurch + " з приходу");


                        prodWeight += prodShift;
                        System.out.println("------Додаємо " + prodShift + " з переміщення");


                        prodWeight -= rozrobka.get(0);
                        calcWeight += rozrobka.get(1);

                        System.out.println("------Розроблено " + rozrobka.get(0) + " отримано " + rozrobka.get(1));
                        PRODUCTS_INGREDIENTS prodIng = GlobalPandaApp.site.getProdIngForProd(inv.getProdIngName());

                        System.out.println("------Вага продукту: " + prodWeight);

                        if (prodIng != null) {
                            System.out.println("------Переводимо з коеф " + prodIng.getAvgCoef() + " отримуємо " + (int) ((double) prodWeight / (double) prodIng.getAvgCoef()));
                            calcWeight += (int) ((double) prodWeight / (double) prodIng.getAvgCoef());

                        }

                        if (cur != null) {
                            cur.setProductPurchase(prodPurch);
                            cur.setProductShift(prodShift);
                            cur.setRozrobka(rozrobka.get(0));
                            cur.setRozrobkaOut(rozrobka.get(1));
                        }
                    }
                }
            }

            System.out.println("\n ------Порахована вага : " + calcWeight + " ------");

            for (Inventory inv : list) {
                inv.setCalculatedNetto(calcWeight);
                inv.setCalculatedCons(calcCons);
            }

            list.sort((o1, o2) -> {
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


        if (GlobalPandaApp.site.saveInventory(inventoryMap)) {
            inventoryMap = GlobalPandaApp.site.getTodayInventory();
            createSpreadSheetView();
            try {
                new InventoryPdf(inventoryMap).createPdf();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkProduct (Integer productId, ArrayList<Inventory> prev) {
        if (prev.isEmpty())
            return true;
        for (Inventory inv : prev)
            if (inv.getProdIngId().equals(productId) && inv.getFactU2() > 0)
                return true;
        if (GlobalPandaApp.site.getSumProdPurchase(prev.get(0).getBegin(), new Date(), productId, GlobalPandaApp.config.getKitchen().getKitch_id()) > 0)
            return true;
        if (GlobalPandaApp.site.getSumProdShift(prev.get(0).getBegin(), new Date(), productId, GlobalPandaApp.config.getKitchen().getKitch_id()) > 0)
            return true;
        return false;
    }

    private void fillInventoryMap () {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 9);
        Calendar yearAgo = Calendar.getInstance();
        yearAgo.add(Calendar.YEAR, -1);
        Date date = cal.getTime();
        Date timeStart = new Date();
        Integer checkId = GlobalPandaApp.site.getNextInventoryCheckId();
        inventoryMap = new LinkedHashMap<>();
        for (INGREDIENTS ing : getIngForInventory(GlobalPandaApp.site.getIngredientsInventory())) {
            ArrayList<Inventory> previous = GlobalPandaApp.site.getLastInventory(ing.getIngredientId(), GlobalPandaApp.config.getKitchen().getKitch_id());
            Date prevDate = previous.isEmpty() ? yearAgo.getTime() : previous.get(0).getBegin();

            inventoryMap.put(ing.getIngredientId(), new ArrayList<>());
            Inventory inv = new Inventory();
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
                Inventory prod = new Inventory();
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
            for (TEHCARDS teh : GlobalPandaApp.site.getNF(ing.getIngredientId())) {
                Inventory nf = new Inventory();
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
        list.sort((o1, o2) -> o1.getIngredientName().compareTo(o2.getIngredientName()));
        if (total && selectedIngs != null)
            return selectedIngs;

        ArrayList<INGREDIENTS> result = new ArrayList<>();
        HashSet<INGREDIENTS> setResult = new HashSet<>();

        Random rnd = new Random();

        HashMap<Integer, INGREDIENTS> ingImp = new HashMap<>();
        int totalImp = 0;
        for (INGREDIENTS ing : list) {
            switch (GlobalPandaApp.config.getKitchen().getKitch_id()) {
                case 0:
                    totalImp += ing.getSyhivImportance();
                    ingImp.put(totalImp, ing);
                    break;

                case 1:
                    totalImp += ing.getVarshavImportance();
                    ingImp.put(totalImp, ing);
                    break;

                default:
                    totalImp += ing.getSyhivImportance();
                    ingImp.put(totalImp, ing);
                    break;
            }
        }

        double avgImp = totalImp / list.size();
        int count = avgImp > 40 ? 5 : 4;

        while (setResult.size() < count) {
            int i = rnd.nextInt(totalImp);
            int r = totalImp;
            for (Integer imp : ingImp.keySet()) {
                if ( imp > i && imp < r )
                    r = imp;
            }
            setResult.add(ingImp.get(r));
        }

        result.addAll(setResult);
        return result;
    }

    private void setRowCount () {
        if ( inventoryMap != null ) {
            int invCount = 0;
            for (ArrayList<Inventory> list : inventoryMap.values())
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

    public void selectProducts(ActionEvent event) {
        total = true;
        ArrayList<INGREDIENTS> ings = GlobalPandaApp.site.getIngredientsInventory();
        ings.sort(Comparator.comparing(INGREDIENTS::getIngredientName));
        Stage selectStage = new Stage(StageStyle.UTILITY);
        selectStage.initModality(Modality.APPLICATION_MODAL);
        AnchorPane root = new AnchorPane();
        root.setPrefWidth(300);
        root.setPrefHeight(600);
        ListView<ListItem> list = new ListView<>();
        for (INGREDIENTS ing : ings) {
            ListItem item = new ListItem(ing.getIngredientName(), false);
            item.onProperty().addListener((observable, oldValue, newValue) -> {

            });
            list.getItems().add(item);
        }
        list.setCellFactory(CheckBoxListCell.forListView(new Callback<ListItem, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(ListItem item) {
                return item.onProperty();
            }
        }));
        Button selectAll = new Button("Вибрати всі");
        selectAll.setOnAction(event1 -> {
            if (selectAll.getText().equals("Вибрати всі")) {
                for (ListItem li : list.getItems()) {
                    li.onProperty().set(true);
                }
                selectAll.setText("Забрати всі");
            } else {
                for (ListItem li : list.getItems()) {
                    li.onProperty().set(false);
                }
                selectAll.setText("Вибрати всі");
            }
        });
        AnchorPane.setLeftAnchor(selectAll, 5.0);
        AnchorPane.setBottomAnchor(selectAll, 5.0);

        Button done = new Button("Готово");
        done.setOnAction(event1 -> {
            for (ListItem item : list.getItems()) {
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

    public void writeToDB(ActionEvent event) {
        //Check if inventoryMap is present
        if (inventoryMap == null)
            return;

        //Show progress label
        progressToDb.setVisible(true);

        //Check for PDF print
        boolean checked = true;

        Date date = new Date();

        //for each basic ingredient
        for (Integer i : inventoryMap.keySet()) {
            //for each proding of basic
            for (Inventory inv : inventoryMap.get(i)) {
                //looking for row contains proding
                for (ObservableList<SpreadsheetCell> row : table.getGrid().getRows()) {
                    if (row.get(0).getText().equals(inv.getProdIngName())) {


                        if (inv.getCalculatedNetto() == 0) {
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

            float inputNetto = 0;
            for (Inventory inv : inventoryMap.get(i)) {
                System.out.println(" ---- Рахуємо введенне нето для " + inv.getProdIngName());

                if (inv.getFirstAttempt() == null) {
                    if (inv.getProdIngId() < 1700) {
                        inputNetto += inv.getAttemptU2().floatValue();
                        System.out.println("--------------Додаємо " + inv.getAttemptU2().floatValue() + " грам");
                    } else if (inv.getProdIngId() < 1900) {
                        float nfWeight = inv.getAttemptU2();
                        ArrayList<TEHCARDS> tehcard = GlobalPandaApp.site.getTehcardsForDish(inv.getProdIngId());
                        float countIng = 0;
                        float totalWeight = 0;
                        for ( TEHCARDS tc : tehcard ) {
                            totalWeight += tc.getFinalWeight();
                            if (tc.getIngredientId().equals(i))
                                countIng = tc.getCount().intValue();
                        }
                        double coef = (double) totalWeight / (double) countIng;
                        inputNetto += (double) nfWeight / coef ;
                    } else if (inv.getProdIngId() > 3000) {
                        float prodWeight = inv.getAttemptU2().floatValue();
                        PRODUCTS_INGREDIENTS prodIng = GlobalPandaApp.site.getProdIngForProd(inv.getProdIngName());
                        inputNetto += prodWeight / prodIng.getAvgCoef().floatValue() ;
                    }
                } else if (!inv.getFirstAttempt()) {
                    if (inv.getProdIngId() < 1700) {
                        inputNetto += inv.getFactU2().floatValue();
                    } else if (inv.getProdIngId() < 1900) {
                        int nfWeight = inv.getFactU2();
                        ArrayList<TEHCARDS> tehcard = GlobalPandaApp.site.getTehcardsForDish(inv.getProdIngId());
                        int countIng = 0;
                        int totalWeight = 0;
                        for ( TEHCARDS tc : tehcard ) {
                            totalWeight += tc.getFinalWeight();
                            if (tc.getIngredientId().equals(i))
                                countIng = tc.getCount().intValue();
                        }
                        inputNetto += (double) nfWeight * (double) countIng / (double) totalWeight;
                    } else if (inv.getProdIngId() > 3000) {
                        float prodWeight = inv.getFactU2().floatValue();
                        PRODUCTS_INGREDIENTS prodIng = GlobalPandaApp.site.getProdIngForProd(inv.getProdIngName());
                        inputNetto += prodWeight / prodIng.getAvgCoef().floatValue() ;
                    }
                }
            }

            float calculatedNetto = inventoryMap.get(i).get(0).getCalculatedNetto().floatValue();
            float calculatedCons = inventoryMap.get(i).get(0).getCalculatedCons().floatValue();
            float diffNetto = inputNetto - calculatedNetto;
            float diffPercent;
            if (calculatedCons != 0.0f)
                diffPercent = 100 * diffNetto / calculatedCons;
            else {
                diffPercent = 100 * diffNetto / ( inventoryMap.get(i).get(0).getAttemptU2().floatValue() != 0 ? inventoryMap.get(i).get(0).getAttemptU2().floatValue() : 1);
            }


            float avgCoef = 0.0f;
            float avgCPU = 0.0f;

            ArrayList<PRODUCTS_INGREDIENTS> prodIngs = GlobalPandaApp.site.getProdForIng(inventoryMap.get(i).get(0).getBasicIng());

            for (PRODUCTS_INGREDIENTS prodIng : prodIngs ) {
                avgCoef += prodIng.getAvgCoef().floatValue();
                avgCPU += prodIng.getAvgPrice().floatValue();
            }
            avgCoef /= (float) prodIngs.size();
            avgCPU /= (float) prodIngs.size();

            float diffUAH = (diffNetto / avgCoef) * (avgCPU / 1000);

            Inventory temp = inventoryMap.get(i).get(0);
            if (temp.getFirstAttempt() == null && Math.abs(diffPercent) <= 5) {
                GlobalPandaApp.site.updateImportance(temp.getBasicIng(), temp.getKitchen(), -1);
            } else if (temp.getFirstAttempt() != null && !temp.getFirstAttempt() && Math.abs(diffPercent) > 5) {
                GlobalPandaApp.site.updateImportance(temp.getBasicIng(), temp.getKitchen(), 3);
            }

            for (Inventory inv : inventoryMap.get(i)) {
                if ( inv.getFirstAttempt() == null ) {
                    if (Math.abs(diffPercent) < 5) {
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
                        float diffComp = ( Math.abs(diffUAH) / diffPercent ) * (Math.abs(diffPercent) - 5);
                        inv.setDiffUah( (int) diffUAH);
                        inv.setDiffCompensation( (int) diffComp);
                        inv.setDiffNetto( (int) diffNetto);
                        inv.setDiffPercent(diffPercent);
                        inv.setFirstAttempt(false);
                    }
                } else {
                    if (Math.abs(diffPercent) < 5) {
                        inv.setDiffUah( (int) diffUAH);
                        inv.setDiffCompensation(0);
                        inv.setDiffNetto( (int) diffNetto);
                        inv.setDiffPercent(diffPercent);
                        inv.setEnd(date);
                    } else {
                        float diffComp = ( Math.abs(diffUAH) / diffPercent ) * (Math.abs(diffPercent) - 5);
                        inv.setDiffUah( (int) diffUAH);
                        inv.setDiffCompensation( (int) diffComp);
                        inv.setDiffNetto( (int) diffNetto);
                        inv.setDiffPercent(diffPercent);
                        inv.setEnd(date);
                    }
                }
            }
        }
        if (GlobalPandaApp.site.saveInventory(inventoryMap)) {
            inventoryMap = GlobalPandaApp.site.getTodayInventory();
            inventoryPane.getChildren().remove(table);
            createSpreadSheetView();
            try {
                if(checked)
                    new InventoryPdf(inventoryMap).createPdf();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        progressToDb.setVisible(false);
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

        ComboBox<Inventory> invents = new ComboBox<>();
        HashSet<Integer> checkIds = new HashSet<>();
        for (Inventory inv : GlobalPandaApp.site.getAllInventory()) {
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
        InventoryReport ir = new InventoryReport();
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
        InventoryReportTotal irt = new InventoryReportTotal();
        irt.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                File file = saveFile("From " + invents.getValue().toString());
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
            Inventory inv = invents.getValue();
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

        total.setOnAction(event1 -> {
            progress.setVisible(true);
            Inventory inv = invents.getValue();
            ArrayList<Inventory> list = new ArrayList<>();
            list.add(inv);
            for (Inventory inventory : invents.getItems()) {
                if (inventory.getCheckId() > inv.getCheckId())
                    list.add(inventory);
            }
            irt.setInv(list);
            progress.progressProperty().bind(irt.progressBar);
            total.setDisable(true);
            Thread thread = new Thread(irt);
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
        fileChooser.setInitialDirectory(new File(userDir +"/Desktop"));
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
