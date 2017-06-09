package ua.com.pandasushi.controller.kitchen.rozrobka;

import com.itextpdf.text.DocumentException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.controlsfx.control.spreadsheet.*;
import ua.com.pandasushi.controller.MainController;
import ua.com.pandasushi.controller.TabController;
import ua.com.pandasushi.controller.inputconvert.AutocompleteSpreadsheetCellType;
import ua.com.pandasushi.database.common.Rozrobka;
import ua.com.pandasushi.database.common.menu.PRODUCTS_INGREDIENTS;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Тарас on 07.03.2017.
 */
public class RozrobkaTabController implements TabController, EventHandler<ActionEvent> {
    MainController main;
    protected static ObservableList<String> productNames;
    protected static HashMap<String, PRODUCTS_INGREDIENTS> products;
    protected static ChangeListener[][] listeners = new ChangeListener[41][8];

    @FXML
    Button makeReport;

    @FXML
    AnchorPane rozrobkaPane;

    SpreadsheetView table;

    Button add;


    @Override
    public void init(MainController main) {
        this.main = main;
        add = new Button("Записати в БД");
        add.setFont(new Font("Calibri", 24));
        add.setTextFill(Color.GREEN);
        add.setOnAction(this);
        makeReport.setOnAction(event -> {
            try {
                new PdfMakerRozrobka(GlobalPandaApp.site.getTodayRozrobka()).createPdf();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        AnchorPane.setRightAnchor(add, 7.0);
        AnchorPane.setBottomAnchor(add, 7.0);
        rozrobkaPane.getChildren().add(add);
        createSpreadsheetView();
    }

    public void createSpreadsheetView () {
        GridBase grid = new GridBase(42, 8);
        grid.setRowHeightCallback(new GridBase.MapBasedRowHeightFactory(generateRowHeight()));
        buildGrid(grid);
        table = new SpreadsheetView(grid);
        for (SpreadsheetColumn column : table.getColumns()) {
            column.setPrefWidth(115.0);
        }
        table.getColumns().get(0).setPrefWidth(210.0);
        table.getColumns().get(4).setPrefWidth(210.0);
        table.getColumns().get(7).setPrefWidth(210.0);
        table.setShowRowHeader(true);
        table.setShowColumnHeader(false);
        table.setEditable(true);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getFixedRows().add(0);
        AnchorPane.setLeftAnchor(table, 0.0);
        AnchorPane.setRightAnchor(table, 0.0);
        AnchorPane.setBottomAnchor(table, 55.0);
        AnchorPane.setTopAnchor(table, 35.0);


        rozrobkaPane.getChildren().add(table);
        fillOld(GlobalPandaApp.site.getTodayRozrobka());
    }

    private ObservableList<String> getProducts() {
        if (productNames == null) {
            productNames = FXCollections.observableArrayList();
            products = new HashMap<>();

            ArrayList<PRODUCTS_INGREDIENTS> productsIngList = GlobalPandaApp.site.getProdIngRozrobka();

            for (PRODUCTS_INGREDIENTS product : productsIngList) {
                productNames.add(product.getProductName());
                PRODUCTS_INGREDIENTS test = products.put(product.getProductName(), product);
                if (test != null)
                    System.err.println("DUPLICATE ROZROBKA ON PRODUCT " + test.getProductName() + " -" + test.getIngredientName() + " -" + product.getIngredientName());
            }
        }
        return productNames;
    }

    private ObservableList<SpreadsheetCell> getHeaderCells (GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> headerCells = FXCollections.observableArrayList();

        SpreadsheetCell product = SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, "Продукт");
        product.setEditable(false);
        product.getStyleClass().add("header-cell");
        headerCells.add(product);

        SpreadsheetCell check1 = SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, "Кількість");
        check1.setEditable(false);
        check1.getStyleClass().add("header-cell");
        headerCells.add(check1);

        SpreadsheetCell check2 = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "Розроблено, г");
        check2.setEditable(false);
        check2.getStyleClass().add("header-cell");
        headerCells.add(check2);

        SpreadsheetCell fact = SpreadsheetCellType.STRING.createCell(row, 3, 1, 1, "Отримано, г");
        fact.setEditable(false);
        fact.getStyleClass().add("header-cell");
        headerCells.add(fact);

        SpreadsheetCell cost = SpreadsheetCellType.STRING.createCell(row, 4, 1, 1, "Інгредієнт");
        cost.setEditable(false);
        cost.getStyleClass().add("header-cell");
        headerCells.add(cost);

        SpreadsheetCell fact1 = SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, "Коефіцієнт");
        fact1.setEditable(false);
        fact1.getStyleClass().add("header-cell");
        headerCells.add(fact1);


        SpreadsheetCell costPerUnit = SpreadsheetCellType.STRING.createCell(row, 6, 1, 1, "Середній коеф.");
        costPerUnit.setEditable(false);
        costPerUnit.getStyleClass().add("header-cell");
        headerCells.add(costPerUnit);

        SpreadsheetCell comments = SpreadsheetCellType.STRING.createCell(row, 7, 1, 1, "Коментар");
        comments.setEditable(false);
        comments.getStyleClass().add("header-cell");
        headerCells.add(comments);


        return headerCells;
    }

    private ObservableList<SpreadsheetCell> getDataCells (GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> dataCells = FXCollections.observableArrayList();

        SpreadsheetCell product = new AutocompleteSpreadsheetCellType(getProducts()).createCell(row, 0, 1, 1, "");
        product.setEditable(true);
        product.getStyleClass().add("plain-cell");
        product.textProperty().addListener((observable, oldValue, newValue) -> {
            if(product.getText() != null && !product.getText().isEmpty()) {
                for (SpreadsheetCell cell : grid.getRows().get(product.getRow())) {
                    cell.setEditable(true);
                    cell.getStyleClass().remove("closed-cell");
                    if(cell.getColumn() == 1) {
                        cell.itemProperty().setValue(1);
                        if ( GlobalPandaApp.site.getProduct(products.get(product.getText()).getProductId()).getFirstUnits().equals(" ") )
                            cell.itemProperty().setValue(0);
                        cell.setEditable(false);
                    }

                    if(cell.getColumn() == 2) {
                        cell.setFormat("###,###.##");
                        cell.itemProperty().setValue(0);
                        listeners[product.getRow() - 1][2] = new ChangeListener() {
                            @Override
                            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                cell.setStyle("");
                                int rozrobleno = Integer.parseInt(cell.getText());
                                int otrymano = Integer.parseInt(grid.getRows().get(product.getRow()).get(3).getText());
                                if (rozrobleno > 0 && otrymano > 0) {
                                    grid.getRows().get(product.getRow()).get(5).itemProperty().setValue(rozrobleno / otrymano);
                                }
                            }
                        };
                        cell.textProperty().addListener(listeners[product.getRow() - 1][2]);
                    }

                    if(cell.getColumn() == 4) {
                        cell.itemProperty().setValue(products.get(product.getText()).getIngredientName());
                        cell.setEditable(false);
                        cell.getStyleClass().add("closed-cell");
                    }

                    if(cell.getColumn() == 3) {
                        cell.setFormat("###,###.##");
                        cell.itemProperty().setValue(0);
                        listeners[product.getRow() - 1][4] = new ChangeListener() {
                            @Override
                            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                cell.setStyle("");
                                int rozrobleno = Integer.parseInt(grid.getRows().get(product.getRow()).get(2).getText());
                                int otrymano = Integer.parseInt(cell.getText());
                                if (rozrobleno > 0 && otrymano > 0) {
                                    grid.getRows().get(product.getRow()).get(5).itemProperty().setValue( (double) rozrobleno / (double) otrymano);
                                }
                            }
                        };
                        cell.textProperty().addListener(listeners[product.getRow() - 1][4]);
                    }

                    if(cell.getColumn() == 5) {
                        cell.setFormat("###,###.##");
                        cell.setEditable(false);
                        cell.getStyleClass().add("closed-cell");
                    }

                    if(cell.getColumn() == 6) {
                        cell.setEditable(false);
                        cell.setFormat("###,###.##");
                        cell.itemProperty().setValue( (double) products.get(product.getText()).getAvgCoef() );
                        cell.getStyleClass().add("closed-cell");
                    }
                }
            } else {
                for (SpreadsheetCell cell : grid.getRows().get(product.getRow())) {
                    if(cell.getColumn() > 0) {
                        if(cell.getColumn() != 4 )
                            cell.itemProperty().setValue(0);
                        else
                            cell.itemProperty().setValue("");
                        cell.setFormat("#");
                        if(listeners[product.getRow() - 1][cell.getColumn()] != null)
                            cell.textProperty().removeListener(listeners[product.getRow() - 1][cell.getColumn()]);
                        cell.setEditable(false);
                        cell.getStyleClass().add("closed-cell");
                    }
                }
            }
        });
        dataCells.add(product);

        SpreadsheetCell u1check = SpreadsheetCellType.INTEGER.createCell(row, 1, 1, 1, 0);
        u1check.setEditable(false);
        u1check.getStyleClass().add("plain-cell");
        u1check.getStyleClass().add("number-cell");
        u1check.getStyleClass().add("closed-cell");
        dataCells.add(u1check);

        SpreadsheetCell rozrobleno = SpreadsheetCellType.INTEGER.createCell(row, 2, 1, 1, 0);
        rozrobleno.setFormat("###,###");
        rozrobleno.setEditable(false);
        rozrobleno.getStyleClass().add("plain-cell");
        rozrobleno.getStyleClass().add("number-cell");
        rozrobleno.getStyleClass().add("closed-cell");
        dataCells.add(rozrobleno);

        SpreadsheetCell count = SpreadsheetCellType.INTEGER.createCell(row, 3, 1, 1, 0);
        count.setFormat("###,###");
        count.setEditable(false);
        count.getStyleClass().add("plain-cell");
        count.getStyleClass().add("number-cell");
        count.getStyleClass().add("closed-cell");
        dataCells.add(count);

        SpreadsheetCell ingName = SpreadsheetCellType.STRING.createCell(row, 4, 1, 1, "");
        ingName.setEditable(false);
        ingName.getStyleClass().add("plain-cell");
        ingName.getStyleClass().add("closed-cell");
        dataCells.add(ingName);

        SpreadsheetCell coef = SpreadsheetCellType.DOUBLE.createCell(row, 5, 1, 1, 0.0);
        coef.setEditable(false);
        coef.getStyleClass().add("plain-cell");
        coef.getStyleClass().add("number-cell");
        coef.getStyleClass().add("closed-cell");
        dataCells.add(coef);


        SpreadsheetCell avgCoef = SpreadsheetCellType.DOUBLE.createCell(row, 6, 1, 1, 0.0);
        avgCoef.setEditable(false);
        avgCoef.getStyleClass().add("plain-cell");
        avgCoef.getStyleClass().add("number-cell");
        avgCoef.getStyleClass().add("closed-cell");
        dataCells.add(avgCoef);

        SpreadsheetCell comments = SpreadsheetCellType.STRING.createCell(row, 7, 1, 1, "");
        comments.setEditable(false);
        comments.getStyleClass().add("plain-cell");
        comments.getStyleClass().add("closed-cell");
        dataCells.add(comments);

        return dataCells;
    }

    private void fillOld (ArrayList<Rozrobka> list) {
        for (int i = 0; i < list.size(); i++) {
            Rozrobka rozrobka = list.get(i);
            ObservableList<SpreadsheetCell> row = table.getGrid().getRows().get( i + 1 );
            for (int j = 0; j < row.size(); j++) {
                SpreadsheetCell cell = row.get(j);
                cell.setEditable(false);
                cell.getStyleClass().add("rozrobka-done-cell");
                switch (j) {
                    case 0 :
                        cell.itemProperty().setValue(rozrobka.getProductName());
                        break;
                    case 1 :
                        cell.itemProperty().setValue(rozrobka.getCountU1());
                        break;
                    case 2 :
                        cell.itemProperty().setValue(rozrobka.getCountU2());
                        break;
                    case 3 :
                        cell.itemProperty().setValue( rozrobka.getCountIng() );
                        break;
                    case 4 :
                        cell.itemProperty().setValue( rozrobka.getIngredientName() );
                        break;
                    case 5 :
                        cell.itemProperty().setValue( (double) rozrobka.getCoef() );
                        break;
                    case 6 :
                        cell.itemProperty().setValue( (double) products.get(rozrobka.getProductName()).getAvgCoef() );
                        break;
                    case 7 :
                        cell.itemProperty().setValue(rozrobka.getComment());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void buildGrid (GridBase grid) {
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();

        int rowIndex = 0;
        rows.add(getHeaderCells(grid, rowIndex++));
        while (rowIndex < 42)
            rows.add(getDataCells(grid, rowIndex++));


        grid.setRows(rows);
    }

    private Map<Integer, Double> generateRowHeight() {
        Map<Integer, Double> rowHeight = new HashMap<>();
        rowHeight.put(0, 40.0);
        return rowHeight;
    }

    @Override
    public void handle(ActionEvent event) {
        Date date = new Date();
        for (int i = 1; i < 42; i++) {
            ObservableList<SpreadsheetCell> row = table.getGrid().getRows().get(i);
            if (row.get(0).getStyleClass().contains("rozrobka-done-cell"))
                continue;
            if (row.get(0).getText() == null || row.get(0).getText().isEmpty())
                continue;
            PRODUCTS_INGREDIENTS lib = products.get(row.get(0).getText());

            Rozrobka rozrobka = new Rozrobka();

            //if ( Integer.parseInt(row.get(1).getText()) > 0 )
            rozrobka.setCountU1(Integer.parseInt(row.get(1).getText()));
            //else {
            //    row.get(1).setStyle("-fx-border-color: red; -fx-border-radius: 3px; -fx-border-width: 1px; ");
            //   continue;
            //}

            if ( Integer.parseInt(row.get(2).getText()) > 0 )
                rozrobka.setCountU2(Integer.parseInt(row.get(2).getText()));
            else {
                row.get(2).setStyle("-fx-border-color: red; -fx-border-radius: 3px; -fx-border-width: 1px; ");
                continue;
            }

            if ( Integer.parseInt(row.get(3).getText()) > 0 )
                rozrobka.setCountIng(Integer.parseInt(row.get(3).getText()));
            else {
                row.get(3).setStyle("-fx-border-color: red; -fx-border-radius: 3px; -fx-border-width: 1px; ");
                continue;
            }

            rozrobka.setCoef(fromTableCell(row.get(5).getText()));
            rozrobka.setComment(row.get(7).getText());
            rozrobka.setDate(date);
            rozrobka.setKitchen(GlobalPandaApp.config.getKitchen().getKitch_id());
            rozrobka.setCook(GlobalPandaApp.config.getOperator().getName());
            rozrobka.setProductId(lib.getProductId());
            rozrobka.setProductName(lib.getProductName());
            rozrobka.setIngredientId(lib.getIngredientId());
            rozrobka.setIngredientName(lib.getIngredientName());

            if ( GlobalPandaApp.site.saveRozrobka(rozrobka) ) {
                for (SpreadsheetCell cell : row) {
                    cell.setEditable(false);
                    cell.getStyleClass().add("rozrobka-done-cell");
                }

                lib.setLastCheck(date);
                lib.setLastCoef(rozrobka.getCoef());
                lib.setAvgCoef(GlobalPandaApp.site.getNewAvgCoef(lib.getProductId()));
                GlobalPandaApp.site.update(lib);
            }
        }
    }

    public float fromTableCell (String s) {
        if (!s.matches("\\-?\\d+(\\.\\d{0,})?")) {
            s = s.replaceAll(",", ".");
            s = s.replaceAll("[^\\d\\.]", "");
        }
        float res = Float.parseFloat(s);
        return res;
    }
}
