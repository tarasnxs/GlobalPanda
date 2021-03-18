package ua.com.pandasushi.controller.control.graphs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ToggleButton;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class GraphsController {
/*
    @FXML
    private LineChart<?, ?> graph;

    @FXML
    private ToggleButton mode;

    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<CustomersLocal> list = MainApp.local.getAllByKitchen(0);
        TreeMap<LocalDate, Integer> count = new TreeMap<>();
        TreeMap<LocalDate, Integer> sum = new TreeMap<>();
        for(CustomersLocal cl : list) {
            LocalDate date = cl.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (count.containsKey(date)) {
                count.put(date,count.get(date)+1);
                sum.put(date,sum.get(date)+cl.getFianlCost());
            } else {
                count.put(date, 1);
                sum.put(date, cl.getFianlCost());
            }
        }
        XYChart.Series<String, Integer> countChart = new XYChart.Series<>();
        countChart.setName("К-ть замовлень Сихів");
        XYChart.Series<String, Integer> sumChart = new XYChart.Series<>();
        sumChart.setName("Сума замовлень Сихів");
        for (Map.Entry entry : count.entrySet()) {
            XYChart.Data<String, Integer> countData = new XYChart.Data<>();
            countData.setXValue(((LocalDate) entry.getKey()).toString());
            countData.setYValue((Integer) entry.getValue());
            countChart.getData().add(countData);
        }
        for (Map.Entry entry : sum.entrySet()) {
            XYChart.Data<String, Integer> sumData = new XYChart.Data<>();
            sumData.setXValue(((LocalDate) entry.getKey()).toString());
            sumData.setYValue((Integer) entry.getValue());
            sumChart.getData().add(sumData);
        }

        ArrayList<CustomersLocal> listV = MainApp.local.getAllByKitchen(1);
        TreeMap<LocalDate, Integer> countV = new TreeMap<>();
        TreeMap<LocalDate, Integer> sumV = new TreeMap<>();
        for(CustomersLocal cl : listV) {
            LocalDate date = cl.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (countV.containsKey(date)) {
                countV.put(date,countV.get(date)+1);
                sumV.put(date,sumV.get(date)+cl.getFianlCost());
            } else {
                countV.put(date, 1);
                sumV.put(date, cl.getFianlCost());
            }
        }
        XYChart.Series<String, Integer> countChartV = new XYChart.Series<>();
        countChartV.setName("К-ть замовлень Варшавська");
        XYChart.Series<String, Integer> sumChartV = new XYChart.Series<>();
        sumChartV.setName("Сума замовлень Варшавська");
        for (Map.Entry entry : countV.entrySet()) {
            XYChart.Data<String, Integer> countData = new XYChart.Data<>();
            countData.setXValue(((LocalDate) entry.getKey()).toString());
            countData.setYValue((Integer) entry.getValue());
            countChartV.getData().add(countData);
        }
        for (Map.Entry entry : sumV.entrySet()) {
            XYChart.Data<String, Integer> sumData = new XYChart.Data<>();
            sumData.setXValue(((LocalDate) entry.getKey()).toString());
            sumData.setYValue((Integer) entry.getValue());
            sumChartV.getData().add(sumData);
        }

        ArrayList<CustomersLocal> listA = MainApp.local.getAll();
        TreeMap<LocalDate, Integer> countA = new TreeMap<>();
        TreeMap<LocalDate, Integer> sumA = new TreeMap<>();
        for(CustomersLocal cl : listA) {
            LocalDate date = cl.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (countA.containsKey(date)) {
                countA.put(date,countA.get(date)+1);
                sumA.put(date,sumA.get(date)+cl.getFianlCost());
            } else {
                countA.put(date, 1);
                sumA.put(date, cl.getFianlCost());
            }
        }

        StackPane hoverPane = new StackPane();
        XYChart.Series<String, Integer> countChartA = new XYChart.Series<>();
        countChartA.setName("К-ть всіх замовлень");
        XYChart.Series<String, Integer> sumChartA = new XYChart.Series<>();
        sumChartA.setName("Сума всіх замовлень");
        for (Map.Entry entry : countA.entrySet()) {
            XYChart.Data<String, Integer> countData = new XYChart.Data<>();
            countData.setXValue(((LocalDate) entry.getKey()).toString());
            countData.setYValue((Integer) entry.getValue());
            countData.setNode(new HoveredThresholdNode((LocalDate) entry.getKey(),(Integer) entry.getValue()));
            countChartA.getData().add(countData);
        }
        for (Map.Entry entry : sumA.entrySet()) {
            XYChart.Data<String, Integer> sumData = new XYChart.Data<>();
            sumData.setXValue(((LocalDate) entry.getKey()).toString());
            sumData.setYValue((Integer) entry.getValue());
            sumData.setNode(new HoveredThresholdNode((LocalDate) entry.getKey(),(Integer) entry.getValue()));
            sumChartA.getData().add(sumData);
        }


        graph.getData().add(countChart);
        //graph.getData().add(sumChart);
        graph.getData().add(countChartV);
        //graph.getData().add(sumChartV);
        graph.getData().add(countChartA);
        //graph.getData().add(sumChartA);

    }

    class HoveredThresholdNode extends StackPane {
        HoveredThresholdNode(LocalDate date, int value) {
            setPrefSize(8, 8);

            final Label label = createDataThresholdLabel(date, value);

            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    getChildren().setAll(label);
                    setCursor(Cursor.NONE);
                    toFront();
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    getChildren().clear();
                    setCursor(Cursor.CROSSHAIR);
                }
            });
        }

        private Label createDataThresholdLabel(LocalDate date, int value) {
            final Label label = new Label(value + "\n" + date.toString());
            label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 12;");

            label.setTextFill(Color.FORESTGREEN);

            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            return label;
        }
    }

    @FXML
    void switchMode(ActionEvent event) {
        graph.setData(FXCollections.observableArrayList());
        if(mode.isSelected()) {
            mode.setText("Кількість");
            graph.getData().add(countChart);
            graph.getData().add(countChartV);
            graph.getData().add(countChartA);
        } else {
            mode.setText("Сума");
            graph.getData().add(sumChart);
            graph.getData().add(sumChartV);
            graph.getData().add(sumChartA);
        }
    }

    @FXML
    void switchMode(ActionEvent event) {

    }
    */
}
