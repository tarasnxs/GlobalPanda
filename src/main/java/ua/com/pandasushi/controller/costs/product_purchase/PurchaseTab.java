package ua.com.pandasushi.controller.costs.product_purchase;

import com.itextpdf.text.DocumentException;
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
import ua.com.pandasushi.database.common.Operations;
import ua.com.pandasushi.database.common.menu.PRODUCTS;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;


public class PurchaseTab implements EventHandler<ActionEvent> {
    private Tab tab;
    private PurchaseTabBuilder builder;
    private static final int ROW_COUNT = 52;
    private static final int COLUMN_COUNT = 8;

    protected AnchorPane pane;
    protected ComboBox<String> contragentComboBox;
    protected ComboBox<String> currencyComboBox;
    protected ComboBox<String> cookComboBox;
    protected Label finalSum;
    protected Button done;
    protected SpreadsheetView table;

    protected static ObservableList<String> productNames;
    protected static HashMap<String, PRODUCTS> products;
    protected static ChangeListener[][] listeners = new ChangeListener[50][8];



    public PurchaseTab (PurchaseTabBuilder builder) {
        this.builder = builder;

        tab = new Tab("Квитанція");

        pane = new AnchorPane();

        Label contragentLabel = new Label("Контрагент");
        contragentLabel.setPrefWidth(110.0);
        AnchorPane.setLeftAnchor(contragentLabel, 5.0);
        AnchorPane.setTopAnchor(contragentLabel, 5.0);

        Label currencyLabel = new Label("Валюта");
        currencyLabel.setPrefWidth(110.0);
        AnchorPane.setLeftAnchor(currencyLabel, 125.0);
        AnchorPane.setTopAnchor(currencyLabel, 5.0);

        Label cookLabel = new Label("Хто приймав");
        cookLabel.setPrefWidth(110.0);
        AnchorPane.setLeftAnchor(cookLabel, 245.0);
        AnchorPane.setTopAnchor(cookLabel, 5.0);

        contragentComboBox = new ComboBox<>();
        contragentComboBox.setPrefWidth(96.0);
        contragentComboBox.setItems(getContrAgentList());
        contragentComboBox.setOnAction(event -> {
            currencyComboBox.setDisable(false);
            currencyComboBox.getSelectionModel().select("UAH");
            contragentComboBox.setStyle("");
            if (contragentComboBox.getSelectionModel().getSelectedItem().equals("Суші Хаус"))
                currencyComboBox.getSelectionModel().select("USD");
        });
        AnchorPane.setLeftAnchor(contragentComboBox, 5.0);
        AnchorPane.setTopAnchor(contragentComboBox, 23.0);

        Button addContrAgent = new Button("+");
        addContrAgent.setPrefWidth(12.0);
        addContrAgent.setFont(new Font(7.0));
        addContrAgent.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Новий контрагент");
            dialog.setContentText("");
            dialog.setHeaderText("Введіть ім'я контрагента");
            String s = dialog.showAndWait().get();
            if ( s != null && !s.isEmpty() ) {
                contragentComboBox.getItems().add(s);
                contragentComboBox.getSelectionModel().select(s);
            }
        });
        AnchorPane.setLeftAnchor(addContrAgent, 103.0);
        AnchorPane.setTopAnchor(addContrAgent, 23.0);


        currencyComboBox = new ComboBox<>();
        currencyComboBox.setItems(getCurrencys());
        currencyComboBox.setPrefWidth(110.0);
        currencyComboBox.setDisable(true);
        currencyComboBox.setOnAction(event -> currencyComboBox.setStyle(""));
        AnchorPane.setLeftAnchor(currencyComboBox, 125.0);
        AnchorPane.setTopAnchor(currencyComboBox, 23.0);


        cookComboBox = new ComboBox<>();
        cookComboBox.setItems(getCooks());
        cookComboBox.setPrefWidth(110.0);
        cookComboBox.setOnAction(event -> cookComboBox.setStyle(""));
        AnchorPane.setLeftAnchor(cookComboBox, 245.0);
        AnchorPane.setTopAnchor(cookComboBox, 23.0);


        done = new Button("Оформити");
        done.setFont(new Font("Calibri", 15.0));
        done.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        done.setOnAction(this);
        done.setPrefWidth(110.0);
        done.setPrefHeight(38.0);
        AnchorPane.setRightAnchor(done, 5.0);
        AnchorPane.setTopAnchor(done, 5.0);

        /*Button newProduct = new Button("Новий продукт");
        newProduct.setPrefWidth(80.0);
        newProduct.setOnAction(event -> {
            new AddProductDialog("", this);
        });
        AnchorPane.setLeftAnchor(newProduct, 370.0);
        AnchorPane.setTopAnchor(newProduct, 23.0);*/

        finalSum = new Label("0.0");
        finalSum.setAlignment(Pos.CENTER_LEFT);
        finalSum.setTextAlignment(TextAlignment.JUSTIFY);
        finalSum.setFont(new Font("Arial Black", 15.0));
        finalSum.textProperty().addListener((observable, oldValue, newValue) -> {
            finalSum.setStyle("");
        });
        AnchorPane.setRightAnchor(finalSum, 125.0);
        AnchorPane.setTopAnchor(finalSum, 5.0);

        Label sumLabel = new Label("Сума: ");
        sumLabel.setAlignment(Pos.CENTER_LEFT);
        sumLabel.setTextAlignment(TextAlignment.JUSTIFY);
        sumLabel.setFont(new Font("Arial Black", 15.0));
        AnchorPane.setRightAnchor(sumLabel, 210.0);
        AnchorPane.setTopAnchor(sumLabel, 5.0);


        createSpreadsheetView();
        AnchorPane.setLeftAnchor(table, 5.0);
        AnchorPane.setRightAnchor(table, 5.0);
        AnchorPane.setBottomAnchor(table, 5.0);
        AnchorPane.setTopAnchor(table, 55.0);

        pane.getChildren().addAll(contragentLabel, currencyLabel, cookLabel,
                contragentComboBox, addContrAgent, currencyComboBox, cookComboBox, done, finalSum, sumLabel, table);

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
        table.getColumns().get(0).setPrefWidth(270.0);

    }

    private ObservableList<String> getContrAgentList() {
        ObservableList<String> result = FXCollections.observableArrayList();
        result.addAll(GlobalPandaApp.site.getContrAgentsForType(Operations.PRODUCT_PURCHASE));
        return result;
    }

    private ObservableList<String> getCurrencys() {
        ObservableList<String> result = FXCollections.observableArrayList();
        result.addAll("UAH", "USD", "EUR");
        return result;
    }

    private ObservableList<String> getProducts() {
        if (productNames == null) {
            productNames = FXCollections.observableArrayList();
            products = new HashMap<>();

            ArrayList<PRODUCTS> productsList = GlobalPandaApp.site.getProducts();

            for (PRODUCTS product : productsList) {
                productNames.add(product.getProductName());
                products.put(product.getProductName(), product);
            }
        }
        return productNames;
    }

    public void reloadProducts() {
        productNames = FXCollections.observableArrayList();
        products = new HashMap<>();

        ArrayList<PRODUCTS> productsList = GlobalPandaApp.site.getProducts();

        for (PRODUCTS product : productsList) {
            productNames.add(product.getProductName());
            products.put(product.getProductName(), product);
        }

        for (int i = 0; i < ROW_COUNT; i++) {
            try {
                AutocompleteSpreadsheetCellType type = (AutocompleteSpreadsheetCellType) table.getGrid().getRows().get(i).get(0).getCellType();
                type.setItems(productNames);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private ObservableList<String> getCooks() {
        ObservableList<String> cooks = FXCollections.observableArrayList();
        for (Employee cook : GlobalPandaApp.site.getCooks())
            cooks.add(cook.getName());
        return cooks;
    }



    @Override
    public void handle(ActionEvent event) {
        if(contragentComboBox.getSelectionModel().getSelectedIndex() > -1) {
            if(currencyComboBox.getSelectionModel().getSelectedIndex() > -1) {
                if(cookComboBox.getSelectionModel().getSelectedIndex() > -1) {
                    if(fromTableCell(finalSum.getText()) > 0.1 ) {

                        ArrayList<Operations> prodOps = new ArrayList<>();
                        Integer checkId = GlobalPandaApp.site.getNextCheckID(Operations.PRODUCT_PURCHASE);
                        for (int i = 2; i < ROW_COUNT; i++) {
                            ObservableList<SpreadsheetCell> row = table.getGrid().getRows().get(i);
                            if (row.get(0).getText() != null && row.get(0).getText().length() > 0 ) {
                                Operations op = new Operations();

                                op.setFloatparameter1(fromTableCell(row.get(1).getText()));
                                op.setFloatparameter3(fromTableCell(row.get(4).getText()));
                                op.setIntparameter4(fromTableCell(row.get(6).getText()));

                                if( fromTableCell(row.get(2).getText()) > 0.01 )
                                    op.setFloatparameter2(fromTableCell(row.get(2).getText()));
                                else {
                                    row.get(2).setStyle("-fx-border-color: red; -fx-border-radius: 3px; -fx-border-width: 1px; ");
                                    return;
                                }

                                if( fromTableCell(row.get(5).getText()) > 0.01 )
                                    op.setFloatparameter4(fromTableCell(row.get(5).getText()));
                                else {
                                    row.get(5).setStyle("-fx-border-color: red; -fx-border-radius: 3px; -fx-border-width: 1px; ");
                                    return;
                                }

                                if( fromTableCell(row.get(3).getText()) > 0.01 )
                                    op.setSum(fromTableCell(row.get(3).getText()));
                                else {
                                    row.get(3).setStyle("-fx-border-color: red; -fx-border-radius: 3px; -fx-border-width: 1px; ");
                                    return;
                                }

                                op.setKitchen(GlobalPandaApp.config.getKitchen().getKitch_id());
                                op.setType(Operations.PRODUCT_PURCHASE);
                                Date date = new Date();
                                op.setDate(date);
                                op.setStartPeriod(date);
                                op.setEndPeriod(date);
                                op.setCurrency(currencyComboBox.getSelectionModel().getSelectedItem());
                                op.setOperator(GlobalPandaApp.config.getOperator().getName());
                                op.setContrAgent(contragentComboBox.getSelectionModel().getSelectedItem());
                                op.setDescription1(cookComboBox.getSelectionModel().getSelectedItem());
                                op.setDescription2(row.get(7).getText());
                                op.setStrparameter1(row.get(0).getText());
                                op.setIntparameter1( (float) products.get(row.get(0).getText()).getProductId());
                                op.setStrparameter2(products.get(row.get(0).getText()).getFirstUnits());
                                op.setStrparameter3(products.get(row.get(0).getText()).getSecondUnits());
                                op.setCheckId(checkId);
                                prodOps.add(op);
                            }
                        }
                        makePDF(prodOps);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.initModality(Modality.APPLICATION_MODAL);
                        alert.setTitle("Квитанція №" + checkId);
                        alert.setHeaderText("Перевірте правильність введених даних");
                        alert.setContentText("Все вірно?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK){
                            closeCheck(prodOps, fromTableCell(finalSum.getText()), currencyComboBox.getSelectionModel().getSelectedItem());
                        } else {

                        }
                    } else
                        finalSum.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px; ");
                } else
                    cookComboBox.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px; ");
            } else
                currencyComboBox.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px; ");
        } else
            contragentComboBox.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 2px; ");
    }

    public float fromTableCell (String s) {
        if (!s.matches("\\-?\\d+(\\.\\d{0,})?")) {
            s = s.replaceAll(",", ".");
        }
        s = s.replaceAll("[^\\d\\.]", "");
        float res = Float.parseFloat(s);
        return res;
    }



    public void closeCheck (ArrayList<Operations> productList, double sum, String cur) {
        final Double[] baseSum = {sum};
        final Double[] totalPaySum = {sum};

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
        field.setPrefWidth(80.0);

        Button ok = new Button("Оформити");
        ok.requestFocus();
        Button cancel = new Button("Відміна");
        cancel.setOnAction(event -> stage.close());

        Label delivery = new Label("Вартість доставки");
        delivery.setFont(font);
        TextField deliveryCost = new TextField();
        deliveryCost.setFont(font);
        deliveryCost.textProperty().addListener(new FloatFieldChangeListener(deliveryCost));
        deliveryCost.textProperty().addListener((observable, oldValue, newValue) -> {
            field.setStyle("");
            try {
                Double d = Double.parseDouble(newValue);
                if (d != null) {
                    totalPaySum[0] = baseSum[0] + d;
                    doOplaty.setText("Сума до оплати : " + totalPaySum[0] + " " + cur);
                    field.setText(totalPaySum[0] + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        deliveryCost.setText(0+"");
        deliveryCost.setPrefWidth(80.0);



        EventHandler<ActionEvent> eventHandler = event -> {
            float paySum = Float.parseFloat(field.getText());
            float deliverySum = Float.parseFloat(deliveryCost.getText());
            if (paySum <= totalPaySum[0]) {
                if (deliverySum > 0) {
                    double totalWeight = productList.stream().mapToDouble(o -> o.getFloatparameter4()).sum();
                    double priceForUnit = deliverySum / totalWeight;
                    for (Operations op : productList) {
                        op.setIntparameter2((float)Math.round(op.getFloatparameter4() * priceForUnit));
                        op.setSum(op.getSum() +
                                op.getIntparameter2());
                    }
                }

                Integer checkId = null;
                while (checkId == null) {
                    try {
                        checkId = GlobalPandaApp.site.getNextCheckID(Operations.PRODUCT_PURCHASE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                for (Operations op : productList) {
                    op.setCheckId(checkId);
                }
                GlobalPandaApp.site.saveOperations(productList);
                for (Operations op : productList) {
                    GlobalPandaApp.site.updateAverageProducts(op.getIntparameter1().intValue());
                }

                Operations borg = new Operations();
                Operations op = productList.get(0);
                borg.setKitchen(GlobalPandaApp.config.getKitchen().getKitch_id());
                borg.setType(Operations.DEBT_PURCHASE);
                borg.setDate(op.getDate());
                borg.setStartPeriod(op.getDate());
                borg.setEndPeriod(op.getDate());
                borg.setSum(paySum * -1);
                borg.setCurrency(op.getCurrency());
                borg.setOperator(GlobalPandaApp.config.getOperator().getName());
                borg.setContrAgent(op.getContrAgent());
                borg.setCheckId(op.getCheckId());
                if (deliverySum > 0) {
                    borg.setIntparameter2(deliverySum);
                }

                GlobalPandaApp.site.saveOperation(borg);
                stage.close();
                ArrayList<Float> allSum = GlobalPandaApp.site.getSumForType(Operations.PRODUCT_PURCHASE);
                String s = "Сума за день : " + ( allSum.get(0) > 0 ? allSum.get(0) + " UAH; " : "" )
                        + ( allSum.get(1) > 0 ? allSum.get(1) + " USD; " : "" )
                        + ( allSum.get(2) > 0 ? allSum.get(2) + " EUR; " : "" );
                builder.controller.sumByDay.setText(s);
                builder.closeTab(tab);
            } else {
                field.setStyle("-fx-border-color: red; -fx-border-radius: 5px; -fx-border-width: 1px; ");
                TranslateTransition tt = new TranslateTransition(Duration.millis(100), field);
                tt.setByX(1f);
                tt.setCycleCount(4);
                tt.setAutoReverse(true);
                tt.play();
            }
        };

        field.setOnAction(eventHandler);
        ok.setOnAction(eventHandler);


        AnchorPane.setLeftAnchor(delivery, 10.0);
        AnchorPane.setTopAnchor(delivery, 10.0);
        AnchorPane.setLeftAnchor(doOplaty, 10.0);
        AnchorPane.setTopAnchor(doOplaty, 35.0);
        AnchorPane.setLeftAnchor(oplacheno, 10.0);
        AnchorPane.setTopAnchor(oplacheno, 60.0);
        AnchorPane.setLeftAnchor(deliveryCost, 130.0);
        AnchorPane.setTopAnchor(deliveryCost, 10.0);
        AnchorPane.setLeftAnchor(field, 90.0);
        AnchorPane.setTopAnchor(field, 60.0);
        AnchorPane.setRightAnchor(ok, 15.0);
        AnchorPane.setBottomAnchor(ok, 10.0);
        AnchorPane.setLeftAnchor(cancel, 15.0);
        AnchorPane.setBottomAnchor(cancel, 10.0);

        pane.getChildren().addAll(doOplaty, oplacheno, field, ok, cancel, delivery, deliveryCost);
        Scene scene = new Scene(pane, 250, 160);
        stage.setTitle("Оплата");
        stage.setScene(scene);
        stage.show();
    }

    private Map<Integer, Double> generateRowHeight() {
        Map<Integer, Double> rowHeight = new HashMap<>();
        rowHeight.put(1, 50.0);
        return rowHeight;
    }

    private ObservableList<SpreadsheetCell> getHeaderCells (GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> headerCells = FXCollections.observableArrayList();

        SpreadsheetCell product = SpreadsheetCellType.STRING.createCell(row, 0, 2, 1, "Продукт");
        product.setEditable(false);
        product.getStyleClass().add("header-cell");
        headerCells.add(product);

        SpreadsheetCell check = SpreadsheetCellType.STRING.createCell(row, 1, 1, 2, "По чеку");
        check.setEditable(false);
        check.getStyleClass().add("header-cell");
        headerCells.add(check);

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, ""));

        SpreadsheetCell cost = SpreadsheetCellType.STRING.createCell(row, 3, 2, 1, "Вартість");
        cost.setEditable(false);
        cost.getStyleClass().add("header-cell");
        headerCells.add(cost);

        SpreadsheetCell fact = SpreadsheetCellType.STRING.createCell(row, 4, 1, 2, "Факт");
        fact.setEditable(false);
        fact.getStyleClass().add("header-cell");
        headerCells.add(fact);

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, ""));


        SpreadsheetCell costPerUnit = SpreadsheetCellType.STRING.createCell(row, 6, 2, 1, "Вартість/кг");
        costPerUnit.setEditable(false);
        costPerUnit.getStyleClass().add("header-cell");
        headerCells.add(costPerUnit);

        SpreadsheetCell comments = SpreadsheetCellType.STRING.createCell(row, 7, 2, 1, "Коментар");
        comments.setEditable(false);
        comments.getStyleClass().add("header-cell");
        headerCells.add(comments);


        return headerCells;
    }

    private ObservableList<SpreadsheetCell> getSecondHeaderCells (GridBase grid, int row) {
        final ObservableList<SpreadsheetCell> headerCells = FXCollections.observableArrayList();

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 0, 1, 1, ""));

        SpreadsheetCell u1check = SpreadsheetCellType.STRING.createCell(row, 1, 1, 1, "U1");
        u1check.setEditable(false);
        u1check.getStyleClass().add("header-cell");
        headerCells.add(u1check);

        SpreadsheetCell u2check = SpreadsheetCellType.STRING.createCell(row, 2, 1, 1, "U2");
        u2check.setEditable(false);
        u2check.getStyleClass().add("header-cell");
        headerCells.add(u2check);

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 3, 1, 1, ""));

        SpreadsheetCell u1fact = SpreadsheetCellType.STRING.createCell(row, 4, 1, 1, "U1");
        u1fact.setEditable(false);
        u1fact.getStyleClass().add("header-cell");
        headerCells.add(u1fact);

        SpreadsheetCell u2fact = SpreadsheetCellType.STRING.createCell(row, 5, 1, 1, "U2");
        u2fact.setEditable(false);
        u2fact.getStyleClass().add("header-cell");
        headerCells.add(u2fact);

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 6, 1, 1, ""));

        headerCells.add(SpreadsheetCellType.STRING.createCell(row, 7, 1, 1, ""));


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
                        PRODUCTS prod = products.get(product.getText());

                        if( prod.getUnitsRelation() > 0 ) {
                            cell.setFormat("#,###.## " + prod.getFirstUnits());
                            listeners[product.getRow() - 2][1] = new ChangeListener() {
                                @Override
                                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                    String cellText = cell.getText();
                                    if (!cellText.matches("\\-?\\d+(\\.\\d{0,})?")) {
                                        cellText = cellText.replaceAll(",", ".");
                                        cellText = cellText.replaceAll("[^\\d\\.]", "");
                                    }
                                    if(cellText.isEmpty()) cellText = "0";
                                    double res = Double.parseDouble(cellText);
                                    table.getGrid().getRows().get(product.getRow()).get(2).setFormat( "#,###.## " + prod.getSecondUnits());
                                    table.getGrid().getRows().get(product.getRow()).get(2).itemProperty().setValue(prod.getUnitsRelation() * res);
                                    table.getGrid().getRows().get(product.getRow()).get(4).setFormat( "#,###.## " + prod.getFirstUnits());
                                    table.getGrid().getRows().get(product.getRow()).get(4).itemProperty().setValue(res);
                                    table.getGrid().getRows().get(product.getRow()).get(5).setFormat( "#,###.## " + prod.getSecondUnits());
                                    table.getGrid().getRows().get(product.getRow()).get(5).itemProperty().setValue(prod.getUnitsRelation() * res);
                                }
                            };
                            cell.textProperty().addListener(listeners[product.getRow() - 2][1]);
                        } else if (prod.getFirstUnits().isEmpty() || prod.getFirstUnits().equals(" ")) {
                            cell.setEditable(false);
                            cell.getStyleClass().add("closed-cell");
                        } else {
                            cell.setFormat("#,###.## " + prod.getFirstUnits());
                            listeners[product.getRow() - 2][1] = new ChangeListener() {
                                @Override
                                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                    String cellText = cell.getText();
                                    if (!cellText.matches("\\-?\\d+(\\.\\d{0,})?")) {
                                        cellText = cellText.replaceAll(",", ".");
                                        cellText = cellText.replaceAll("[^\\d\\.]", "");
                                    }
                                    if(cellText.isEmpty()) cellText = "0";
                                    double res = Double.parseDouble(cellText);
                                    table.getGrid().getRows().get(product.getRow()).get(4).setFormat( "#,###.## " + prod.getFirstUnits());
                                    table.getGrid().getRows().get(product.getRow()).get(4).itemProperty().setValue(res);
                                }
                            };
                            cell.textProperty().addListener(listeners[product.getRow() - 2][1]);
                        }
                    }

                    if(cell.getColumn() == 2) {
                        PRODUCTS prod = products.get(product.getText().toString());
                        cell.setFormat("#,###.## " + prod.getSecondUnits());
                        if( prod.getUnitsRelation() > 0 ) {
                            cell.setEditable(false);
                            cell.getStyleClass().add("closed-cell");
                        }
                        listeners[product.getRow() - 2][2] = new ChangeListener() {
                            @Override
                            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                cell.setStyle("");
                                String cellText = cell.getText();
                                if (!cellText.matches("\\-?\\d+(\\.\\d{0,})?")) {
                                    cellText = cellText.replaceAll(",", ".");
                                    cellText = cellText.replaceAll("[^\\d\\.]", "");
                                }
                                if(cellText.isEmpty()) cellText = "0";
                                double res = Double.parseDouble(cellText);
                                table.getGrid().getRows().get(product.getRow()).get(5).setFormat( "#,###.## " + prod.getSecondUnits());
                                table.getGrid().getRows().get(product.getRow()).get(5).itemProperty().setValue(res);
                            }
                        };
                        cell.textProperty().addListener(listeners[product.getRow() - 2][2]);
                    }

                    if(cell.getColumn() == 3) {
                        cell.setFormat("#,###.##");

                        listeners[product.getRow() - 2][3] = new ChangeListener() {
                            @Override
                            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                cell.setStyle("");

                                String s = table.getGrid().getRows().get(product.getRow()).get(3).getText();
                                if (!s.matches("\\-?\\d+(\\.\\d{0,})?")) {
                                    s = s.replaceAll(",", ".");
                                    s = s.replaceAll("[^\\d\\.]", "");
                                }
                                if (s.isEmpty())
                                    s = "0";
                                double cost = Double.parseDouble(s);

                                String c = table.getGrid().getRows().get(product.getRow()).get(5).getText();
                                if (!c.matches("\\-?\\d+(\\.\\d{0,})?")) {
                                    c = c.replaceAll(",", ".");
                                    c = c.replaceAll("[^\\d\\.]", "");
                                }
                                if(c.isEmpty())
                                    c = "0";
                                double u2 = Double.parseDouble(c);

                                if( cost > 0.1 && u2 > 0.1 ) {
                                    double res = (cost*1000)/u2;
                                    //table.getGrid().getRows().get(product.getRow()).get(6).setFormat( NumberFormat.getNumberInstance().format(res/100));
                                    table.getGrid().getRows().get(product.getRow()).get(6).setFormat("#,###.##");
                                    table.getGrid().getRows().get(product.getRow()).get(6).itemProperty().setValue( res );
                                }
                            }
                        };

                        cell.textProperty().addListener(listeners[product.getRow() - 2][3]);
                    }

                    if(cell.getColumn() == 4) {
                        PRODUCTS prod = products.get(product.getText().toString());
                        if( prod.getUnitsRelation() > 0 ) {
                            cell.setFormat("#,###.## " + prod.getFirstUnits());

                            listeners[product.getRow() - 2][4] = new ChangeListener() {
                                @Override
                                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                                    String cellText = cell.getText();
                                    if (!cellText.matches("\\-?\\d+(\\.\\d{0,})?")) {
                                        cellText = cellText.replaceAll(",", ".");
                                        cellText = cellText.replaceAll("[^\\d\\.]", "");
                                    }
                                    if ( cellText.isEmpty() ) cellText = "0";
                                    double res = Double.parseDouble(cellText);
                                    //table.getGrid().getRows().get(product.getRow()).get(5).setFormat( NumberFormat.getNumberInstance().format((int) (prod.getUnitsRelation() * res)) + " " + prod.getSecondUnits());
                                    table.getGrid().getRows().get(product.getRow()).get(5).setFormat( "#,###.## " + prod.getSecondUnits());
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
                    }

                    if(cell.getColumn() == 5) {
                        PRODUCTS prod = products.get(product.getText().toString());
                        cell.setFormat("#,###.## " + prod.getSecondUnits());
                        if( prod.getUnitsRelation() > 0 ) {
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
                                if(s.isEmpty()) s = "0";
                                double cost = Double.parseDouble(s);

                                String c = table.getGrid().getRows().get(product.getRow()).get(5).getText();
                                if (!c.matches("\\-?\\d+(\\.\\d{0,})?")) {
                                    c = c.replaceAll(",", ".");
                                    c = c.replaceAll("[^\\d\\.]", "");
                                }
                                if(c.isEmpty()) c = "0";
                                double u2 = Double.parseDouble(c);

                                if( cost > 0.1 && u2 > 0.1 ) {
                                    double res = (cost*1000)/u2;
                                    //table.getGrid().getRows().get(product.getRow()).get(6).setFormat( NumberFormat.getNumberInstance().format(res/100));
                                    table.getGrid().getRows().get(product.getRow()).get(6).setFormat("#,###.##");
                                    table.getGrid().getRows().get(product.getRow()).get(6).itemProperty().setValue( res );
                                }
                            }
                        };

                        cell.textProperty().addListener(listeners[product.getRow() - 2][5]);
                    }

                    if(cell.getColumn() == 6) {
                        cell.setEditable(false);
                        cell.setFormat("#,###.##");
                        cell.getStyleClass().add("closed-cell");
                    }
                }
            } else {
                for (SpreadsheetCell cell : grid.getRows().get(product.getRow())) {
                    if(cell.getColumn() > 0) {
                        if(cell.getColumn() < 7)
                            cell.setItem(0.0);
                        else
                            cell.setItem("");
                        cell.setFormat("#");
                        if(listeners[product.getRow() - 2][cell.getColumn()] != null)
                            cell.textProperty().removeListener(listeners[product.getRow() - 2][cell.getColumn()]);
                        cell.setEditable(false);
                        cell.getStyleClass().add("closed-cell");
                    }
                }
            }
        });
        dataCells.add(product);

        SpreadsheetCell u1check = SpreadsheetCellType.DOUBLE.createCell(row, 1, 1, 1, 0.0);
        u1check.setEditable(false);
        u1check.getStyleClass().add("plain-cell");
        u1check.getStyleClass().add("number-cell");
        u1check.getStyleClass().add("closed-cell");
        dataCells.add(u1check);

        SpreadsheetCell u2check = SpreadsheetCellType.DOUBLE.createCell(row, 2, 1, 1, 0.0);
        u2check.setEditable(false);
        u2check.getStyleClass().add("plain-cell");
        u2check.getStyleClass().add("number-cell");
        u2check.getStyleClass().add("closed-cell");

        dataCells.add(u2check);

        SpreadsheetCell cost = SpreadsheetCellType.DOUBLE.createCell(row, 3, 1, 1, 0.0);
        cost.setEditable(false);
        cost.getStyleClass().add("plain-cell");
        cost.getStyleClass().add("number-cell");
        cost.getStyleClass().add("closed-cell");

        cost.textProperty().addListener((observable, oldValue, newValue) -> {
            if (fromTableCell(newValue) < 0)
                cost.itemProperty().setValue(fromTableCell(newValue)*-1);
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
        dataCells.add(u1fact);

        SpreadsheetCell u2fact = SpreadsheetCellType.DOUBLE.createCell(row, 5, 1, 1, 0.0);
        u2fact.setEditable(false);
        u2fact.getStyleClass().add("plain-cell");
        u2fact.getStyleClass().add("number-cell");
        u2fact.getStyleClass().add("closed-cell");
        dataCells.add(u2fact);


        SpreadsheetCell costPerUnit = SpreadsheetCellType.DOUBLE.createCell(row, 6, 1, 1, 0.0);
        costPerUnit.setEditable(false);
        costPerUnit.getStyleClass().add("plain-cell");
        costPerUnit.getStyleClass().add("number-cell");
        costPerUnit.getStyleClass().add("closed-cell");
        dataCells.add(costPerUnit);

        SpreadsheetCell comments = SpreadsheetCellType.STRING.createCell(row, 7, 1, 1, "");
        comments.setEditable(false);
        comments.getStyleClass().add("plain-cell");
        comments.getStyleClass().add("closed-cell");
        dataCells.add(comments);


        return dataCells;
    }

    private void buildGrid(GridBase grid) {
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();

        int rowIndex = 0;
        rows.add(getHeaderCells(grid, rowIndex++));
        rows.add(getSecondHeaderCells(grid, rowIndex++));
        while (rowIndex < ROW_COUNT)
            rows.add(getDataCells(grid, rowIndex++));


        grid.setRows(rows);


        grid.spanRow(2, 0, 0);
        grid.spanColumn(2 , 0, 1);
        grid.spanRow(2, 0, 3);
        grid.spanColumn(2, 0, 4);
        grid.spanRow(2, 0, 6);
        grid.spanRow(2, 0, 7);
    }

    private void makePDF(ArrayList<Operations> products) {
        if (products != null) {
            if(products.size() > 0)
                try {
                    new PdfMaker(products, fromTableCell(finalSum.getText())).createPdf();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public Tab getTab() {
        return tab;
    }

}
