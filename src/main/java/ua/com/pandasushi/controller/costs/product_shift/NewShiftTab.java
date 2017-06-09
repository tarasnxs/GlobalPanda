package ua.com.pandasushi.controller.costs.product_shift;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.spreadsheet.*;
import ua.com.pandasushi.controller.inputconvert.AutocompleteSpreadsheetCellType;
import ua.com.pandasushi.controller.inputconvert.FloatFieldChangeListener;
import ua.com.pandasushi.database.common.Employee;
import ua.com.pandasushi.database.common.Kitchens;
import ua.com.pandasushi.database.common.Operations;
import ua.com.pandasushi.database.common.Rozrobka;
import ua.com.pandasushi.database.common.menu.INGREDIENTS;
import ua.com.pandasushi.database.common.menu.PRODUCTS;
import ua.com.pandasushi.database.common.menu.PRODUCTS_INGREDIENTS;
import ua.com.pandasushi.database.common.menu.TEHCARDS;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.text.NumberFormat;
import java.util.*;


public class NewShiftTab implements EventHandler<ActionEvent> {

    private static MODE mode;
    private static final  int COLUMN_COUNT = 8;
    private int ROW_COUNT = 52;

    private Tab tab;
    private ShiftTabBuilder builder;
    private ArrayList<Operations> operations;

    private AnchorPane pane;
    private ComboBox<String> kitchenComboBox;
    private ComboBox<String> cookComboBox;
    private ComboBox<String> courierComboBox;
    private Label finalSum;
    private Label status;
    private Button done;
    private SpreadsheetView table;

    private static ObservableList<String> cooks;
    private static ObservableList<String> productIngredientsNames;
    private static HashMap<String, PRODUCTS> products;
    private static HashMap<String, INGREDIENTS> ingredients;
    private static ChangeListener[][] listeners = new ChangeListener[50][8];


    public NewShiftTab (ShiftTabBuilder builder) {
        this.mode = MODE.NEW_SEND;
        this.builder = builder;
        this.tab = new Tab("Нове відправлення");
        pane = new AnchorPane();
        setHeader();

        createSpreadsheetView();
        AnchorPane.setLeftAnchor(table, 5.0);
        AnchorPane.setRightAnchor(table, 5.0);
        AnchorPane.setBottomAnchor(table, 5.0);
        AnchorPane.setTopAnchor(table, 55.0);

        pane.getChildren().add(table);
        tab.setContent(pane);
    }

    public NewShiftTab (ShiftTabBuilder builder, ArrayList<Operations> operations) {
        if(operations.isEmpty())
            return;
        else if ( !operations.get(0).getKitchen().equals(GlobalPandaApp.config.getKitchen().getKitch_id()) && operations.get(0).getIntparameter2().intValue() != GlobalPandaApp.config.getKitchen().getKitch_id().intValue() ) {
            return;
        }

        ROW_COUNT = operations.size() + 2;
        this.builder = builder;
        this.operations = operations;
        this.tab = new Tab(setMode(operations.get(0)));
        pane = new AnchorPane();
        setHeader();

        createSpreadsheetView();
        AnchorPane.setLeftAnchor(table, 5.0);
        AnchorPane.setRightAnchor(table, 5.0);
        AnchorPane.setBottomAnchor(table, 5.0);
        AnchorPane.setTopAnchor(table, 55.0);

        pane.getChildren().add(table);
        tab.setContent(pane);
    }

    private void createSpreadsheetView () {
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
        table.getColumns().get(7).setPrefWidth(180.0);
    }

    private void buildGrid(GridBase grid) {
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();

        int rowIndex = 0;
        rows.add(getHeaderCells(grid, rowIndex++));
        rows.add(getSecondHeaderCells(grid, rowIndex++));
        while (rowIndex < ROW_COUNT)
            rows.add(getDataCells(grid, rowIndex++, operations == null ? null : operations.get(rowIndex - 3)));


        grid.setRows(rows);


        grid.spanRow(2, 0, 0);
        grid.spanColumn(2 , 0, 1);
        grid.spanRow(2, 0, 3);
        grid.spanColumn(2, 0, 4);
        grid.spanRow(2, 0, 6);
        grid.spanRow(2, 0, 7);
    }

    private ObservableList<SpreadsheetCell> getHeaderCells (GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> headerCells = FXCollections.observableArrayList();

        SpreadsheetCell product = SpreadsheetCellType.STRING.createCell(row, 0, 2, 1, "Продукт/Інгредієнт");
        product.setEditable(false);
        product.getStyleClass().add("header-cell");
        headerCells.add(product);

        SpreadsheetCell check = SpreadsheetCellType.STRING.createCell(row, 1, 1, 2, "Відправлено");
        check.setEditable(false);
        check.getStyleClass().add("header-cell");
        headerCells.add(check);

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, ""));

        SpreadsheetCell cost = SpreadsheetCellType.STRING.createCell(row, 3, 2, 1, "Вартість");
        cost.setEditable(false);
        cost.getStyleClass().add("header-cell");
        headerCells.add(cost);

        SpreadsheetCell fact = SpreadsheetCellType.STRING.createCell(row, 4, 1, 2, "Отримано");
        fact.setEditable(false);
        fact.getStyleClass().add("header-cell");
        headerCells.add(fact);

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, ""));


        SpreadsheetCell costPerUnit = SpreadsheetCellType.STRING.createCell(row, 6, 2, 1, "Вартість/кг");
        costPerUnit.setEditable(false);
        costPerUnit.getStyleClass().add("header-cell");
        headerCells.add(costPerUnit);

        SpreadsheetCell receiver = SpreadsheetCellType.STRING.createCell(row, 7, 2, 1, "Прийняв");
        receiver.setEditable(false);
        receiver.getStyleClass().add("header-cell");
        headerCells.add(receiver);

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

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 3, 1, 1, ""));

        SpreadsheetCell u1fact = SpreadsheetCellType.STRING.createCell(row, 4, 1, 1, "U1");
        u1fact.setEditable(false);
        u1fact.getStyleClass().add("header-cell");
        headerCells.add(u1fact);

        SpreadsheetCell u2fact = SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, "U2, гр/мл");
        u2fact.setEditable(false);
        u2fact.getStyleClass().add("header-cell");
        headerCells.add(u2fact);

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 6, 1, 1, ""));

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 7, 1, 1, ""));


        return headerCells;
    }

    private ObservableList<SpreadsheetCell> getDataCells (GridBase grid, int row, Operations op) {
        final ObservableList<SpreadsheetCell> dataCells = FXCollections.observableArrayList();

        SpreadsheetCell product = new AutocompleteSpreadsheetCellType(getProducts()).createCell(row, 0, 1, 1, "");
        if ( op != null) {
            product.itemProperty().setValue(op.getStrparameter1());
            if (op.getIntparameter1() < 3000)
                product.getStyleClass().add("ingredient-cell");
        }

        if (mode == MODE.NEW_SEND) {
            product.setEditable(true);
            product.getStyleClass().add("plain-cell");
        } else {
            product.setEditable(false);
            product.getStyleClass().add("plain-cell");
            product.getStyleClass().add("closed-cell");
        }

        if( mode == MODE.NEW_SEND ) {
            product.textProperty().addListener((observable, oldValue, newValue) -> {
                if (product.getText() != null && !product.getText().isEmpty()) {
                    for (SpreadsheetCell cell : grid.getRows().get(product.getRow())) {
                        cell.setEditable(true);
                        cell.getStyleClass().remove("closed-cell");
                        if (cell.getColumn() == 1) {
                            PRODUCTS prod = products.get(product.getText().toString());
                            if (prod != null) {
                                product.getStyleClass().remove("ingredient-cell");
                                if (prod.getUnitsRelation() > 0) {
                                    cell.setFormat("#,###.## " + prod.getFirstUnits());
                                    listeners[product.getRow() - 2][1] = new ChangeListener() {
                                        @Override
                                        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                            String cellText = cell.getText();
                                            if (!cellText.matches("\\-?\\d+(\\.\\d{0,})?")) {
                                                cellText = cellText.replaceAll(",", ".");
                                                cellText = cellText.replaceAll("[^\\d\\.]", "");
                                            }
                                            if (cellText.isEmpty()) cellText = "0";
                                            double res = Double.parseDouble(cellText);
                                            table.getGrid().getRows().get(product.getRow()).get(2).setFormat("#,###.## " + prod.getSecondUnits());
                                            table.getGrid().getRows().get(product.getRow()).get(2).itemProperty().setValue(prod.getUnitsRelation() * res);
                                        }
                                    };
                                    cell.textProperty().addListener(listeners[product.getRow() - 2][1]);
                                } else if (prod.getFirstUnits().isEmpty() || prod.getFirstUnits().equals(" ")) {
                                    cell.setEditable(false);
                                    cell.getStyleClass().add("closed-cell");
                                } else {
                                    cell.setFormat("#,###.## " + prod.getFirstUnits());
                                }
                            } else {
                                INGREDIENTS ing = ingredients.get(product.getText().toString());
                                cell.setEditable(false);
                                cell.getStyleClass().add("closed-cell");
                                product.getStyleClass().add("ingredient-cell");
                            }
                        }

                        if (cell.getColumn() == 2) {
                            PRODUCTS prod = products.get(product.getText().toString());
                            if (prod != null) {
                                cell.setFormat("#,###.## " + prod.getSecondUnits());
                                if (prod.getUnitsRelation() > 0) {
                                    cell.setEditable(false);
                                    cell.getStyleClass().add("closed-cell");
                                }
                                listeners[product.getRow() - 2][2] = new ChangeListener() {
                                    @Override
                                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                        cell.setStyle("");
                                        Operations op = GlobalPandaApp.site.getLastProductPurchase(prod.getProductId());
                                        float count = (float) (fromTableCell(cell.getText()) / 1000) ;
                                        if ( op != null ) {
                                            table.getGrid().getRows().get(product.getRow()).get(3).itemProperty().setValue( (double) (op.getIntparameter4() * count) );
                                        } else {
                                            table.getGrid().getRows().get(product.getRow()).get(3).itemProperty().setValue( (double) (prod.getAvgPriceUah() * count) );
                                        }
                                    }
                                };
                                cell.textProperty().addListener(listeners[product.getRow() - 2][2]);
                            } else {
                                INGREDIENTS ing = ingredients.get(product.getText().toString());
                                cell.setFormat("#,###.## " + ing.getUnits());
                                listeners[product.getRow() - 2][2] = new ChangeListener() {
                                    @Override
                                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                        cell.setStyle("");
                                        if ( ing.getIngredientId() < 1700 ) {
                                            ArrayList<Rozrobka> rozrobkas = GlobalPandaApp.site.getLastRozrobka(ing.getIngredientId());
                                            if ( !rozrobkas.isEmpty() ) {
                                                float countCoef = 0.0f;
                                                float countCPU = 0.0f;
                                                for ( Rozrobka rozrobka : rozrobkas ) {
                                                    countCoef += rozrobka.getCoef();
                                                    Operations op = GlobalPandaApp.site.getLastProductPurchase(rozrobka.getProductId());
                                                    if ( op != null )
                                                        countCPU += op.getIntparameter4();
                                                    else
                                                        countCPU += products.get(rozrobka.getProductName()).getAvgPriceUah();
                                                }
                                                float avgCoef = countCoef / rozrobkas.size();
                                                float avgCPU = countCPU / rozrobkas.size();
                                                float count = (float) (fromTableCell(cell.getText()) / 1000) ;
                                                table.getGrid().getRows().get(product.getRow()).get(3).itemProperty().setValue( (double) ( count * avgCoef * avgCPU ) );
                                            } else {
                                                ArrayList<PRODUCTS_INGREDIENTS> products_ingredientses = GlobalPandaApp.site.getProdIng();
                                                for (PRODUCTS_INGREDIENTS prodIng : products_ingredientses) {
                                                    if (prodIng.getIngredientId().equals(ing.getIngredientId())) {
                                                        float avgCoef = prodIng.getAvgCoef();
                                                        float avgCPU = prodIng.getAvgPrice();
                                                        float count = (float) (fromTableCell(cell.getText()) / 1000) ;
                                                        table.getGrid().getRows().get(product.getRow()).get(3).itemProperty().setValue( (double) ( count * avgCoef * avgCPU  ));
                                                        break;
                                                    }
                                                }
                                            }
                                        } else if ( ing.getIngredientId() < 1900 ) {
                                            ArrayList<TEHCARDS> tehcard = GlobalPandaApp.site.getTehcardsForDish(ing.getIngredientId());
                                            ArrayList<PRODUCTS_INGREDIENTS> products_ingredientses = GlobalPandaApp.site.getProdIng();
                                            float costPerUnit = 0.0f;
                                            for ( TEHCARDS ing : tehcard ) {
                                                if ( ing.getIngredientId() < 1700 ) {
                                                    Integer ingredientId = ing.getIngredientId();
                                                    float count = ing.getCount() / 1000;
                                                    for (PRODUCTS_INGREDIENTS prodIng : products_ingredientses) {
                                                        if (prodIng.getIngredientId().equals(ingredientId)) {
                                                            float avgCoef = prodIng.getAvgCoef();
                                                            float avgCPU = prodIng.getAvgPrice();
                                                            costPerUnit += count * avgCoef * avgCPU;
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    ArrayList<TEHCARDS> cTeh = GlobalPandaApp.site.getTehcardsForDish(ing.getIngredientId());
                                                    for ( TEHCARDS tf : cTeh ) {
                                                        Integer ingredientId = tf.getIngredientId();
                                                        float count = tf.getCount() / 1000;
                                                        for (PRODUCTS_INGREDIENTS prodIng : products_ingredientses) {
                                                            if (prodIng.getIngredientId().equals(ingredientId)) {
                                                                float avgCoef = prodIng.getAvgCoef();
                                                                float avgCPU = prodIng.getAvgPrice();
                                                                costPerUnit += count * avgCoef * avgCPU;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            float count = (float) (fromTableCell(cell.getText()) / 1000) ;
                                            table.getGrid().getRows().get(product.getRow()).get(3).itemProperty().setValue( (double) costPerUnit * count );
                                        }
                                    }
                                };
                                cell.textProperty().addListener(listeners[product.getRow() - 2][2]);
                            }


                        }

                        if (cell.getColumn() == 3) {
                            cell.setFormat("#,###");
                            cell.setEditable(false);
                            cell.getStyleClass().add("closed-cell");
                            cell.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                                float count = fromTableCell(table.getGrid().getRows().get(product.getRow()).get(2).getText()) / 1000;
                                float cpu = fromTableCell(cell.getText()) / count;
                                table.getGrid().getRows().get(product.getRow()).get(6).itemProperty().setValue( (double) cpu);
                            });
                        }

                        if (cell.getColumn() == 4) {
                            PRODUCTS prod = products.get(product.getText().toString());
                            if (prod != null) {
                                if (prod.getUnitsRelation() > 0) {
                                    cell.setFormat("#,###.## " + prod.getFirstUnits());

                                    listeners[product.getRow() - 2][4] = new ChangeListener() {
                                        @Override
                                        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                            String cellText = cell.getText();
                                            if (!cellText.matches("\\-?\\d+(\\.\\d{0,})?")) {
                                                cellText = cellText.replaceAll(",", ".");
                                                cellText = cellText.replaceAll("[^\\d\\.]", "");
                                            }
                                            if (cellText.isEmpty()) cellText = "0";
                                            double res = Double.parseDouble(cellText);
                                            //table.getGrid().getRows().get(product.getRow()).get(5).setFormat( NumberFormat.getNumberInstance().format((int) (prod.getUnitsRelation() * res)) + " " + prod.getSecondUnits());
                                            table.getGrid().getRows().get(product.getRow()).get(5).setFormat("#,###.## " + prod.getSecondUnits());
                                            table.getGrid().getRows().get(product.getRow()).get(5).itemProperty().setValue(prod.getUnitsRelation() * res);
                                        }
                                    };

                                    cell.textProperty().addListener(listeners[product.getRow() - 2][4]);
                                } else if (prod.getFirstUnits().isEmpty() || prod.getFirstUnits().equals(" ")) {
                                    cell.setEditable(false);
                                    cell.getStyleClass().add("closed-cell");
                                } else {
                                    cell.setFormat("#,###.## " + prod.getFirstUnits());
                                }
                            } else {
                                INGREDIENTS ing = ingredients.get(product.getText().toString());
                                cell.setEditable(false);
                                cell.getStyleClass().add("closed-cell");
                            }

                            cell.setEditable(false);
                            cell.getStyleClass().add("closed-cell");
                        }

                        if (cell.getColumn() == 5) {
                            PRODUCTS prod = products.get(product.getText().toString());
                            if (prod != null) {
                                cell.setFormat("#,###.## " + prod.getSecondUnits());
                                if (prod.getUnitsRelation() > 0) {
                                    cell.setEditable(false);
                                    cell.getStyleClass().add("closed-cell");
                                }

                                listeners[product.getRow() - 2][5] = new ChangeListener() {
                                    @Override
                                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                        cell.setStyle("");
                                        String s = table.getGrid().getRows().get(product.getRow()).get(3).getText();
                                        if (!s.matches("\\-?\\d+(\\.\\d{0,})?")) {
                                            s = s.replaceAll(",", ".");
                                            s = s.replaceAll("[^\\d\\.]", "");
                                        }
                                        if (s.isEmpty()) s = "0";
                                        double cost = Double.parseDouble(s);

                                        String c = table.getGrid().getRows().get(product.getRow()).get(5).getText();
                                        if (!c.matches("\\-?\\d+(\\.\\d{0,})?")) {
                                            c = c.replaceAll(",", ".");
                                            c = c.replaceAll("[^\\d\\.]", "");
                                        }
                                        if (c.isEmpty()) c = "0";
                                        double u2 = Double.parseDouble(c);

                                        if (cost > 0.1 && u2 > 0.1) {
                                            double res = (cost * 1000) / u2;
                                            //table.getGrid().getRows().get(product.getRow()).get(6).setFormat( NumberFormat.getNumberInstance().format(res/100));
                                            table.getGrid().getRows().get(product.getRow()).get(6).setFormat("#,###.##");
                                            table.getGrid().getRows().get(product.getRow()).get(6).itemProperty().setValue(res);
                                        }
                                    }
                                };

                                cell.textProperty().addListener(listeners[product.getRow() - 2][5]);
                            } else {
                                INGREDIENTS ing = ingredients.get(product.getText().toString());
                                cell.setEditable(false);
                                cell.getStyleClass().add("closed-cell");
                            }

                            cell.setEditable(false);
                            cell.getStyleClass().add("closed-cell");
                        }

                        if (cell.getColumn() == 6) {
                            cell.setEditable(false);
                            cell.setFormat("#,###.##");
                            cell.getStyleClass().add("closed-cell");
                        }

                        if (cell.getColumn() == 7) {
                            cell.setEditable(false);
                            cell.getStyleClass().add("closed-cell");
                        }
                    }
                } else {
                    for (SpreadsheetCell cell : grid.getRows().get(product.getRow())) {
                        if (cell.getColumn() > 0) {
                            if (cell.getColumn() < 7)
                                cell.setItem(0.0);
                            else
                                cell.setItem("");
                            cell.setFormat("#");
                            if (listeners[product.getRow() - 2][cell.getColumn()] != null)
                                cell.textProperty().removeListener(listeners[product.getRow() - 2][cell.getColumn()]);
                            cell.setEditable(false);
                            cell.getStyleClass().add("closed-cell");
                        }
                    }
                }
            });
        }
        dataCells.add(product);

        SpreadsheetCell u1check = SpreadsheetCellType.DOUBLE.createCell(row, 1, 1, 1, 0.0);
        u1check.setEditable(false);
        u1check.getStyleClass().add("plain-cell");
        u1check.getStyleClass().add("number-cell");
        u1check.getStyleClass().add("closed-cell");
        if ( op != null ) {
            if ( op.getFloatparameter1() != null ) {
                u1check.setFormat("#,###.## " + op.getStrparameter2());
                u1check.itemProperty().setValue( op.getFloatparameter1().doubleValue() );
            }
        }
        dataCells.add(u1check);

        SpreadsheetCell u2check = SpreadsheetCellType.DOUBLE.createCell(row, 2, 1, 1, 0.0);
        u2check.setEditable(false);
        u2check.getStyleClass().add("plain-cell");
        u2check.getStyleClass().add("number-cell");
        u2check.getStyleClass().add("closed-cell");
        if ( op != null ) {
            if ( op.getFloatparameter2() != null ) {
                u2check.setFormat("#,###");
                u2check.itemProperty().setValue( op.getFloatparameter2().doubleValue() );
            }
        }

        dataCells.add(u2check);

        SpreadsheetCell cost = SpreadsheetCellType.DOUBLE.createCell(row, 3, 1, 1, 0.0);
        cost.setEditable(false);
        cost.getStyleClass().add("plain-cell");
        cost.getStyleClass().add("number-cell");
        cost.getStyleClass().add("closed-cell");
        cost.setFormat("#,###");
        if ( op != null ) {
            if ( op.getSum() != null ) {
                cost.setFormat("#,###");
                cost.itemProperty().setValue( op.getSum().doubleValue() );
            }
        }

        cost.textProperty().addListener((observable, oldValue, newValue) -> {
            double finalSum = 0.0;
            for (int i = 2; i < ROW_COUNT; i++) {
                try {
                    String cellText = table.getGrid().getRows().get(i).get(3).getText();
                    if( cellText == null || cellText.length() < 1)
                        continue;
                    if (!cellText.matches("\\-?\\d+(\\.\\d{0,})?")) {
                        cellText = cellText.replaceAll(",", ".");
                        cellText = cellText.replaceAll("[^\\d\\.]", "");
                    }
                    if(cellText.isEmpty()) cellText = "0";
                    double res = Double.parseDouble(cellText);
                    finalSum += Double.valueOf(res);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            int n = (int) (finalSum * 100);
            this.finalSum.setText(NumberFormat.getInstance().format((double) n / 100));
        });

        dataCells.add(cost);


        SpreadsheetCell u1fact = SpreadsheetCellType.DOUBLE.createCell(row, 4, 1, 1, 0.0);
        u1fact.setEditable(false);
        u1fact.getStyleClass().add("plain-cell");
        u1fact.getStyleClass().add("number-cell");
        u1fact.getStyleClass().add("closed-cell");
        if ( op != null ) {
            u1fact.setFormat("#,###.## " + op.getStrparameter2());
            if (op.getFloatparameter3() != null) {
                u1fact.itemProperty().setValue( op.getFloatparameter3().doubleValue() );
            } else if ( op.getKitchen() != GlobalPandaApp.config.getKitchen().getKitch_id() ) {
                u1fact.setEditable(true);
                u1fact.getStyleClass().remove("closed-cell");
            }
        }
        dataCells.add(u1fact);

        SpreadsheetCell u2fact = SpreadsheetCellType.DOUBLE.createCell(row, 5, 1, 1, 0.0);
        u2fact.setEditable(false);
        u2fact.getStyleClass().add("plain-cell");
        u2fact.getStyleClass().add("number-cell");
        u2fact.getStyleClass().add("closed-cell");
        if ( op != null ) {
            u2fact.setFormat("#,###.##");
            if (op.getFloatparameter4() != null) {
                u2fact.itemProperty().setValue( op.getFloatparameter4().doubleValue() );
            } else if ( op.getKitchen() != GlobalPandaApp.config.getKitchen().getKitch_id() ) {
                u2fact.setEditable(true);
                u2fact.getStyleClass().remove("closed-cell");
            }
        }
        dataCells.add(u2fact);


        SpreadsheetCell costPerUnit = SpreadsheetCellType.DOUBLE.createCell(row, 6, 1, 1, 0.0);
        costPerUnit.setEditable(false);
        costPerUnit.getStyleClass().add("plain-cell");
        costPerUnit.getStyleClass().add("number-cell");
        costPerUnit.getStyleClass().add("closed-cell");
        if ( op != null && op.getIntparameter4() != null ) {
            costPerUnit.setFormat("#,###.##");
            costPerUnit.itemProperty().setValue( op.getIntparameter4().doubleValue() );
        }
        dataCells.add(costPerUnit);


        SpreadsheetCell cook = new AutocompleteSpreadsheetCellType(getCooks()).createCell(row, 7, 1, 1, "");
        cook.setEditable(false);
        cook.getStyleClass().add("plain-cell");
        cook.getStyleClass().add("closed-cell");
        if ( op != null ) {
            if (op.getDescription3() == null && op.getKitchen() != GlobalPandaApp.config.getKitchen().getKitch_id() ) {
                cook.setEditable(true);
                cook.getStyleClass().remove("closed-cell");
                cook.textProperty().addListener((observable, oldValue, newValue) -> {
                    for (ObservableList<SpreadsheetCell> r : table.getGrid().getRows()) {
                        if(r.get(7).getText() == null || r.get(7).getText().isEmpty())
                            r.get(7).itemProperty().setValue(newValue);
                    }
                });
            } else {
                cook.itemProperty().setValue(op.getDescription3());
            }
        }
        dataCells.add(cook);

        return dataCells;
    }

    private Map<Integer, Double> generateRowHeight() {
        Map<Integer, Double> rowHeight = new HashMap<>();
        rowHeight.put(1, 50.0);
        return rowHeight;
    }

    private void setHeader () {

        Label kitchenLabel = new Label("Кухня");
        kitchenLabel.setPrefWidth(110.0);
        AnchorPane.setLeftAnchor(kitchenLabel, 5.0);
        AnchorPane.setTopAnchor(kitchenLabel, 5.0);

        Label cookLabel = new Label("Кухар");
        cookLabel.setPrefWidth(110.0);
        AnchorPane.setLeftAnchor(cookLabel, 125.0);
        AnchorPane.setTopAnchor(cookLabel, 5.0);

        Label courierLabel = new Label("Кур'єр");
        courierLabel.setPrefWidth(110.0);
        AnchorPane.setLeftAnchor(courierLabel, 245.0);
        AnchorPane.setTopAnchor(courierLabel, 5.0);

        Label statusLabel = new Label("Статус");
        statusLabel.setPrefWidth(110.0);
        AnchorPane.setLeftAnchor(statusLabel, 405.0);
        AnchorPane.setTopAnchor(statusLabel, 5.0);

        kitchenComboBox = new ComboBox<>();
        kitchenComboBox.setPrefWidth(110.0);
        kitchenComboBox.setItems(getKitchenList());
        kitchenComboBox.setOnAction(event -> {
            kitchenComboBox.setStyle("");
        });
        AnchorPane.setLeftAnchor(kitchenComboBox, 5.0);
        AnchorPane.setTopAnchor(kitchenComboBox, 23.0);


        cookComboBox = new ComboBox<>();
        cookComboBox.setItems(getCooks());
        cookComboBox.setPrefWidth(110.0);
        cookComboBox.setOnAction(event -> cookComboBox.setStyle(""));
        AnchorPane.setLeftAnchor(cookComboBox, 125.0);
        AnchorPane.setTopAnchor(cookComboBox, 23.0);


        courierComboBox = new ComboBox<>();
        courierComboBox.setItems(getCouriers());
        courierComboBox.setPrefWidth(110.0);
        courierComboBox.setOnAction(event -> courierComboBox.setStyle(""));
        AnchorPane.setLeftAnchor(courierComboBox, 245.0);
        AnchorPane.setTopAnchor(courierComboBox, 23.0);

        status = new Label("");
        status.setPrefWidth(270.0);
        AnchorPane.setLeftAnchor(status, 405.0);
        AnchorPane.setTopAnchor(status, 23.0);
        float totalSum = 0.0f;

        if (GlobalPandaApp.config.getOperator().getPosition().equals("Кухар")) {
            kitchenComboBox.getSelectionModel().select("Списання");
            kitchenComboBox.setDisable(true);
            cookComboBox.getSelectionModel().select(GlobalPandaApp.config.getOperator().getName());
            cookComboBox.setDisable(true);
            courierComboBox.getSelectionModel().select("-");
            courierComboBox.setDisable(true);
        }

        if (mode != MODE.NEW_SEND) {
            Operations temp = operations.get(0);

            if ( temp.getKitchen().intValue() == GlobalPandaApp.config.getKitchen().getKitch_id().intValue() ) {

                kitchenComboBox.getSelectionModel().select(temp.getContrAgent());
                if (temp.getBoolparameter1()) {
                    status.setText("Продукти доставлено. Гроші в дорозі.");
                    tab.setStyle("-fx-background-color: #ddf0dd;");
                } else {
                    tab.setStyle("-fx-background-color: #ccf0cc;");
                    status.setText("Продукти в дорозі.");
                }

                if (temp.getBoolparameter2()) {
                    tab.setStyle("-fx-background-color: #eef0ee;");
                    status.setText("Оплачено. Закрито.");
                }

            } else {
                GlobalPandaApp.site.getKitchens().stream().filter(kitch -> temp.getKitchen().intValue() == kitch.getKitch_id().intValue()).forEach(kitch -> kitchenComboBox.getSelectionModel().select(kitch.getName()));
                if (temp.getBoolparameter1()) {
                    tab.setStyle("-fx-background-color: #f0dfdd;");
                    status.setText("Продукти отримано. Гроші в дорозі.");
                } else {
                    tab.setStyle("-fx-background-color: #f0cfcc");
                    status.setText("Продукти доставляються.");
                }

                if (temp.getBoolparameter2()) {
                    tab.setStyle("-fx-background-color: #f0efee;");
                    status.setText("Оплачено. Закрито.");
                }
            }


            for (Operations op : operations)
                totalSum += op.getSum();


            cookComboBox.getSelectionModel().select(operations.get(0).getDescription1());
            cookComboBox.setEditable(false);
            cookComboBox.setDisable(true);
            courierComboBox.getSelectionModel().select(operations.get(0).getDescription2());
            courierComboBox.setEditable(false);
            courierComboBox.setDisable(true);
            kitchenComboBox.setEditable(false);
            kitchenComboBox.setDisable(true);
        }

        String title;

        switch (mode) {
            case NEW_SEND:
                title = "Відправити";
                break;

            case OPEN_SEND:
                title = "Прийняти оплату";
                break;

            case CLOSED_SEND:
                title = "";
                break;

            case OPEN_RECEIVE:
                title = "Прийняти";
                break;

            case WAITING_RECEIVE:
                title = "Оплатити";
                break;

            case CLOSED_RECEIVE:
                title = "";
                break;

            default:
                title = "error";
                break;
        }

        done = new Button(title);
        done.setFont(new Font("Calibri", 15.0));
        done.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        done.setOnAction(this);
        done.setPrefWidth(140.0);
        done.setPrefHeight(38.0);

        if ( mode != MODE.NEW_SEND && ((GlobalPandaApp.config.getKitchen().getKitch_id() == operations.get(0).getKitchen() && !operations.get(0).getBoolparameter1()) || title.isEmpty() ) )
            done.setDisable(true);


        AnchorPane.setRightAnchor(done, 5.0);
        AnchorPane.setTopAnchor(done, 5.0);

        finalSum = new Label(NumberFormat.getInstance().format((double) totalSum));
        finalSum.setAlignment(Pos.CENTER_LEFT);
        finalSum.setTextAlignment(TextAlignment.JUSTIFY);
        finalSum.setFont(new Font("Arial Black", 15.0));
        finalSum.textProperty().addListener((observable, oldValue, newValue) -> finalSum.setStyle(""));
        AnchorPane.setRightAnchor(finalSum, 155.0);
        AnchorPane.setTopAnchor(finalSum, 5.0);

        Label sumLabel = new Label("Сума: ");
        sumLabel.setAlignment(Pos.CENTER_LEFT);
        sumLabel.setTextAlignment(TextAlignment.JUSTIFY);
        sumLabel.setFont(new Font("Arial Black", 15.0));
        AnchorPane.setRightAnchor(sumLabel, 240.0);
        AnchorPane.setTopAnchor(sumLabel, 5.0);

        pane.getChildren().addAll(kitchenLabel, cookLabel, courierLabel, statusLabel,
                kitchenComboBox, cookComboBox, courierComboBox, status, finalSum, sumLabel);

        if ( mode != MODE.CLOSED_RECEIVE && mode != MODE.CLOSED_SEND )
            pane.getChildren().add(done);
    }

    public float fromTableCell (String s) {
        if (!s.matches("\\-?\\d+(\\.\\d{0,})?")) {
            s = s.replaceAll(",", ".");
            s = s.replaceAll("[^\\d\\.]", "");
        }
        if (s.isEmpty())
            s = "0";
        float res = Float.parseFloat(s);
        return res;
    }

    @Override
    public void handle(ActionEvent event) {

        switch (mode) {
            case NEW_SEND:
                sendProducts();
                break;

            case OPEN_RECEIVE:
                takeProducts();
                break;

            case OPEN_SEND:
                takePurchase();

            default:
                break;
        }
    }

    private void takePurchase () {
        float sum = 0.0f;
        String cur = "UAH";

        for (int i = 2; i < ROW_COUNT; i++) {
            sum += operations.get(i-2).getSum();
        }

        Font font = new Font("Calibri", 15.0);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        AnchorPane pane = new AnchorPane();
        Label doOplaty = new Label("Сума до оплати : " + sum + " " + cur);
        doOplaty.setFont(font);
        Label oplacheno = new Label("Оплачено : ");
        oplacheno.setFont(font);
        TextField field = new TextField();
        field.setFont(font);
        field.textProperty().addListener(new FloatFieldChangeListener(field));
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            field.setStyle("");
        });
        field.setText(sum + "");
        field.setEditable(false);
        field.setDisable(true);
        field.setPrefWidth(80.0);
        float finalSum1 = sum;
        field.setOnAction(event -> {
            float paySum = Float.parseFloat(field.getText());
            if (paySum <= finalSum1) {
                if (paySum > 0.0) {
                    Operations borg = new Operations();
                    borg.setKitchen(GlobalPandaApp.config.getKitchen().getKitch_id());
                    borg.setType(Operations.DEBT_PURCHASE);
                    borg.setDate(new Date());
                    borg.setStartPeriod(new Date());
                    borg.setEndPeriod(new Date());
                    borg.setSum(paySum);
                    borg.setCurrency("UAH");
                    borg.setOperator(GlobalPandaApp.config.getOperator().getName());
                    borg.setContrAgent(GlobalPandaApp.site.getKitchens().stream().filter(kitch -> operations.get(0).getKitchen() == kitch.getKitch_id()).findFirst().get().getName());
                    borg.setCheckId(operations.get(0).getCheckId());
                    GlobalPandaApp.site.saveOperation(borg);
                }

                if ((int) paySum == (int) finalSum1)
                    for (Operations op : operations)
                        op.setBoolparameter2(true);

                if (GlobalPandaApp.site.saveOperations(operations)) {
                    stage.close();
                    builder.reloadTabs();
                }
            } else {
                field.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
                TranslateTransition tt = new TranslateTransition(Duration.millis(100), field);
                tt.setByX(1f);
                tt.setCycleCount(4);
                tt.setAutoReverse(true);
                tt.play();
            }
        });
        Button ok = new Button("Прийняти");
        ok.requestFocus();
        Button cancel = new Button("Відміна");
        float finalSum2 = sum;
        ok.setOnAction(event -> {
            float paySum = Float.parseFloat(field.getText());
            if (paySum <= finalSum2) {
                if (paySum > 0.0) {
                    Operations borg = new Operations();
                    borg.setKitchen(GlobalPandaApp.config.getKitchen().getKitch_id());
                    borg.setType(Operations.DEBT_PURCHASE);
                    borg.setDate(new Date());
                    borg.setStartPeriod(new Date());
                    borg.setEndPeriod(new Date());
                    borg.setSum(paySum);
                    borg.setCurrency("UAH");
                    borg.setOperator(GlobalPandaApp.config.getOperator().getName());
                    borg.setContrAgent(GlobalPandaApp.site.getKitchens().stream().filter(kitch -> operations.get(0).getKitchen() == kitch.getKitch_id()).findFirst().get().getName());
                    borg.setCheckId(operations.get(0).getCheckId());
                    GlobalPandaApp.site.saveOperation(borg);
                }

                if ((int) paySum == (int) finalSum2)
                    for (Operations op : operations)
                        op.setBoolparameter2(true);

                if (GlobalPandaApp.site.saveOperations(operations)) {
                    stage.close();
                    builder.reloadTabs();
                }
            } else {
                field.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
                TranslateTransition tt = new TranslateTransition(Duration.millis(100), field);
                tt.setByX(1f);
                tt.setCycleCount(4);
                tt.setAutoReverse(true);
                tt.play();
            }
        });
        cancel.setOnAction(event -> {
            stage.close();
        });

        AnchorPane.setLeftAnchor(doOplaty, 10.0);
        AnchorPane.setTopAnchor(doOplaty, 10.0);
        AnchorPane.setLeftAnchor(oplacheno, 10.0);
        AnchorPane.setTopAnchor(oplacheno, 35.0);
        AnchorPane.setLeftAnchor(field, 90.0);
        AnchorPane.setTopAnchor(field, 35.0);
        AnchorPane.setRightAnchor(ok, 15.0);
        AnchorPane.setBottomAnchor(ok, 10.0);
        AnchorPane.setLeftAnchor(cancel, 15.0);
        AnchorPane.setBottomAnchor(cancel, 10.0);

        pane.getChildren().addAll(doOplaty, oplacheno, field, ok, cancel);
        Scene scene = new Scene(pane, 250, 130);
        stage.setTitle("Оплата");
        stage.setScene(scene);
        stage.show();
    }

    private void takeProducts () {
        float sum = 0.0f;
        String cur = "UAH";

        for (int i = 2; i < ROW_COUNT; i++) {
            ObservableList<SpreadsheetCell> row = table.getGrid().getRows().get(i);
            Operations op = operations.get( i-2 );
            float u1 = fromTableCell(row.get(4).getText());
            float u2 = fromTableCell(row.get(5).getText());
            String cook = row.get(7).getText();
            if ( u2 < 1 ) {
                row.get(5).setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
                return;
            }
            if (cook.isEmpty()) {
                row.get(7).setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
                return;
            }
            op.setFloatparameter3(u1);
            op.setFloatparameter4(u2);
            op.setDescription3(cook);
            op.setEndPeriod(new Date());
            op.setStrparameter4(GlobalPandaApp.config.getOperator().getName());
            op.setBoolparameter1(true);
            sum += op.getSum();
        }

        Font font = new Font("Calibri", 15.0);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        AnchorPane pane = new AnchorPane();
        Label doOplaty = new Label("Сума до оплати : " + sum + " " + cur);
        doOplaty.setFont(font);
        Label oplacheno = new Label("Оплачено : ");
        oplacheno.setFont(font);
        TextField field = new TextField();
        field.setFont(font);
        field.textProperty().addListener(new FloatFieldChangeListener(field));
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            field.setStyle("");
        });
        field.setText(sum + "");
        field.setEditable(false);
        field.setDisable(true);
        field.setPrefWidth(80.0);
        float finalSum1 = sum;
        field.setOnAction(event -> {
            float paySum = Float.parseFloat(field.getText());
            if (paySum <= finalSum1) {
                if (paySum > 0.0) {
                    Operations borg = new Operations();
                    borg.setKitchen(GlobalPandaApp.config.getKitchen().getKitch_id());
                    borg.setType(Operations.DEBT_PURCHASE);
                    borg.setDate(new Date());
                    borg.setStartPeriod(new Date());
                    borg.setEndPeriod(new Date());
                    borg.setSum(paySum * -1);
                    borg.setCurrency("UAH");
                    borg.setOperator(GlobalPandaApp.config.getOperator().getName());
                    borg.setContrAgent(GlobalPandaApp.site.getKitchens().stream().filter(kitch -> operations.get(0).getKitchen() == kitch.getKitch_id()).findFirst().get().getName());
                    borg.setCheckId(operations.get(0).getCheckId());
                    GlobalPandaApp.site.saveOperation(borg);
                }

                if ((int) paySum == (int) finalSum1)
                    for (Operations op : operations)
                        op.setBoolparameter3(true);

                if (GlobalPandaApp.site.saveOperations(operations)) {
                    stage.close();
                    builder.reloadTabs();
                }
            } else {
                field.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
                TranslateTransition tt = new TranslateTransition(Duration.millis(100), field);
                tt.setByX(1f);
                tt.setCycleCount(4);
                tt.setAutoReverse(true);
                tt.play();
            }
        });
        Button ok = new Button("Оформити");
        ok.requestFocus();
        Button cancel = new Button("Відміна");
        float finalSum2 = sum;
        ok.setOnAction(event -> {
            float paySum = Float.parseFloat(field.getText());
            if (paySum <= finalSum2) {
                if (paySum > 0.0) {
                    Operations borg = new Operations();
                    borg.setKitchen(GlobalPandaApp.config.getKitchen().getKitch_id());
                    borg.setType(Operations.DEBT_PURCHASE);
                    borg.setDate(new Date());
                    borg.setStartPeriod(new Date());
                    borg.setEndPeriod(new Date());
                    borg.setSum(paySum * -1);
                    borg.setCurrency("UAH");
                    borg.setOperator(GlobalPandaApp.config.getOperator().getName());
                    borg.setContrAgent(GlobalPandaApp.site.getKitchens().stream().filter(kitch -> operations.get(0).getKitchen() == kitch.getKitch_id()).findFirst().get().getName());
                    borg.setCheckId(operations.get(0).getCheckId());
                    GlobalPandaApp.site.saveOperation(borg);
                }

                if ((int) paySum == (int) finalSum2)
                    for (Operations op : operations)
                        op.setBoolparameter3(true);

                if (GlobalPandaApp.site.saveOperations(operations)) {
                    stage.close();
                    builder.reloadTabs();
                }
            } else {
                field.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
                TranslateTransition tt = new TranslateTransition(Duration.millis(100), field);
                tt.setByX(1f);
                tt.setCycleCount(4);
                tt.setAutoReverse(true);
                tt.play();
            }
        });
        cancel.setOnAction(event -> {
            stage.close();
        });

        AnchorPane.setLeftAnchor(doOplaty, 10.0);
        AnchorPane.setTopAnchor(doOplaty, 10.0);
        AnchorPane.setLeftAnchor(oplacheno, 10.0);
        AnchorPane.setTopAnchor(oplacheno, 35.0);
        AnchorPane.setLeftAnchor(field, 90.0);
        AnchorPane.setTopAnchor(field, 35.0);
        AnchorPane.setRightAnchor(ok, 15.0);
        AnchorPane.setBottomAnchor(ok, 10.0);
        AnchorPane.setLeftAnchor(cancel, 15.0);
        AnchorPane.setBottomAnchor(cancel, 10.0);

        pane.getChildren().addAll(doOplaty, oplacheno, field, ok, cancel);
        Scene scene = new Scene(pane, 250, 130);
        stage.setTitle("Оплата");
        stage.setScene(scene);
        stage.show();
    }

    private void sendProducts () {
        ArrayList<Operations> list = new ArrayList<>();
        String kitchen;
        String cook;
        String courier;
        Date date = new Date();
        Integer checkID = GlobalPandaApp.site.getNextCheckID(Operations.PRODUCT_SHIFT);
        if (kitchenComboBox.getSelectionModel().getSelectedIndex() >= 0 && !kitchenComboBox.getSelectionModel().getSelectedItem().equals(GlobalPandaApp.config.getKitchen().getName()))
            kitchen = kitchenComboBox.getSelectionModel().getSelectedItem();
        else {
            kitchenComboBox.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
            return;
        }

        if (cookComboBox.getSelectionModel().getSelectedIndex() >= 0)
            cook = cookComboBox.getSelectionModel().getSelectedItem();
        else {
            cookComboBox.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
            return;
        }

        if (courierComboBox.getSelectionModel().getSelectedIndex() >= 0)
            courier = courierComboBox.getSelectionModel().getSelectedItem();
        else {
            courierComboBox.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
            return;
        }

        for (int i = 2; i < ROW_COUNT; i++) {
            ObservableList<SpreadsheetCell> row = table.getGrid().getRows().get(i);
            if ( row.get(0).getText() != null && !row.get(0).getText().isEmpty()) {
                Operations op = new Operations();
                op.setStrparameter1(row.get(0).getText());
                PRODUCTS prod = products.get(op.getStrparameter1());
                INGREDIENTS ing = ingredients.get(op.getStrparameter1());
                if (prod != null) {
                    op.setIntparameter1(prod.getProductId().floatValue());
                    op.setStrparameter2(prod.getFirstUnits());
                    op.setStrparameter3(prod.getSecondUnits());
                } else {
                    op.setIntparameter1(ing.getIngredientId().floatValue());
                    op.setStrparameter2("");
                    op.setStrparameter3(ing.getUnits());
                }
                op.setFloatparameter1(fromTableCell(row.get(1).getText()));
                op.setFloatparameter2(fromTableCell(row.get(2).getText()));
                if (op.getFloatparameter2() < 1) {
                    row.get(2).setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
                    return;
                }
                op.setSum(fromTableCell(row.get(3).getText()));
                op.setOperator(GlobalPandaApp.config.getOperator().getName());
                op.setCurrency("UAH");
                op.setType(Operations.PRODUCT_SHIFT);
                op.setKitchen(GlobalPandaApp.config.getKitchen().getKitch_id());
                op.setDate(date);
                op.setStartPeriod(date);
                op.setContrAgent(kitchen);
                for ( Kitchens kitch : GlobalPandaApp.site.getKitchens() )
                    if (kitch.getName().equals(kitchen))
                        op.setIntparameter2(kitch.getKitch_id().floatValue());
                op.setDescription1(cook);
                op.setDescription2(courier);
                op.setIntparameter4(fromTableCell(row.get(6).getText()));
                op.setCheckId(checkID);
                op.setBoolparameter1(false);
                op.setBoolparameter2(false);
                op.setBoolparameter3(false);
                if (kitchen.equals("Списання")) {
                    op.setBoolparameter1(true);
                    op.setBoolparameter2(true);
                    op.setBoolparameter3(true);
                    op.setEndPeriod(date);
                    op.setDescription3(cook);
                    op.setFloatparameter3(op.getFloatparameter1());
                    op.setFloatparameter4(op.getFloatparameter2());
                    op.setStrparameter4(op.getOperator());
                }
                list.add(op);
            }
        }

        if (list.isEmpty()) {
            finalSum.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
            return;
        }

        if (GlobalPandaApp.site.saveOperations(list)) {
            //TODO MAKE PDF
            builder.reloadTabs();
        }

    }

    private ObservableList<String> getKitchenList() {
        ObservableList<String> result = FXCollections.observableArrayList();
        ArrayList<Kitchens> kitchens = GlobalPandaApp.site.getKitchens();
        for (Kitchens kitch : kitchens)
            result.addAll(kitch.getName());
        return result;
    }

    private ObservableList<String> getCouriers() {
        ObservableList<String> result = FXCollections.observableArrayList();
        for (Employee employee : GlobalPandaApp.site.getCouriers())
            result.add(employee.getName());
        return result;
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
            }
        }
        return productIngredientsNames;
    }

    private ObservableList<String> getCooks() {
        if (cooks == null || cooks.isEmpty()) {
            cooks = FXCollections.observableArrayList();
            for (Employee cook : GlobalPandaApp.site.getCooks())
                cooks.add(cook.getName());
        }
        return cooks;
    }

    public Tab getTab() {
        return tab;
    }

    private String setMode (Operations op) {
        if (op.getKitchen() == GlobalPandaApp.config.getKitchen().getKitch_id()) {
            if (op.getBoolparameter1() != null && op.getBoolparameter1())
                if (op.getBoolparameter2() != null && op.getBoolparameter2())
                    mode = MODE.CLOSED_SEND;
                else
                    mode = MODE.OPEN_SEND;
            else
                mode = MODE.OPEN_SEND;
        } else {
            if (op.getBoolparameter1() != null && op.getBoolparameter1())
                if (op.getBoolparameter3() != null && op.getBoolparameter3())
                    mode = MODE.CLOSED_RECEIVE;
                else
                    mode = MODE.WAITING_RECEIVE;
            else
                mode = MODE.OPEN_RECEIVE;
        }

        String title;

        switch (mode) {
            case OPEN_SEND:
                title = "Відправлення (оплата)";
                break;

            case CLOSED_SEND:
                title = "Відправлення (закрите)";
                break;

            case OPEN_RECEIVE:
                title = "Прийом";
                break;

            case WAITING_RECEIVE:
                title = "Прийом (оплата)";
                break;

            case CLOSED_RECEIVE:
                title = "Прийом (закрите)";
                break;

            default:
                title = "error";
                break;
        }

        return title;
    }

    private enum MODE {
        NEW_SEND,
        OPEN_SEND,
        CLOSED_SEND,
        OPEN_RECEIVE,
        WAITING_RECEIVE,
        CLOSED_RECEIVE
    }
}
