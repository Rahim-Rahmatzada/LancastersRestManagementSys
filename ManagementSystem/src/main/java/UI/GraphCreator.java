package UI;

import javafx.scene.chart.*;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.util.List;

public class GraphCreator {
    private LineChart<String, Number> lineChart;
    private BarChart<String, Number> barChart;

    public GraphCreator() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        xAxis.setTickLabelFill(Color.WHITE);
        xAxis.setStyle("-fx-text-fill: white;");

        NumberAxis yAxis = new NumberAxis();
        NumberAxis yAxisLineChart = new NumberAxis();
        yAxisLineChart.setTickLabelFill(Color.WHITE);
        yAxisLineChart.setStyle("-fx-text-fill: white;");
        yAxis.setTickLabelFill(Color.WHITE);
        yAxis.setStyle("-fx-text-fill: white;");

        lineChart = new LineChart<>(xAxis, yAxisLineChart);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);

        CategoryAxis barXAxis = new CategoryAxis();
        barXAxis.setLabel("Date");
        barXAxis.setTickLabelFill(Color.WHITE);
        barXAxis.setStyle("-fx-text-fill: white;");
        barChart = new BarChart<>(barXAxis, yAxis);
        barChart.setLegendVisible(false);

        lineChart.setVerticalGridLinesVisible(false);
        barChart.setVerticalGridLinesVisible(false);



        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: #1A1A1A;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: #1A1A1A;");
    }

    public BarChart<String, Number> getBarChart() {
        return barChart;
    }

    public LineChart<String, Number> getLineChart() {
        return lineChart;
    }

    public void addSeriesToGraph(List<Double> data, String seriesName, LocalDate startDate, String variableType) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);

        for (int i = 0; i < data.size(); i++) {
            String dateString = startDate.plusDays(i).toString();
            series.getData().add(new XYChart.Data<>(dateString, data.get(i)));
        }

        lineChart.getData().add(series);

        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
        yAxis.setLabel(variableType);
    }

    public void addBarSeriesToGraph(List<Double> data, String seriesName, LocalDate startDate, String yLabel) {
        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);

        for (int i = 0; i < data.size(); i++) {
            String dateString = startDate.plusDays(i).toString();
            series.getData().add(new XYChart.Data<>(dateString, data.get(i)));
        }

        barChart.getData().add(series);

        NumberAxis yAxis = (NumberAxis) barChart.getYAxis();
        yAxis.setLabel(yLabel);
    }

    public void clearGraph() {
        lineChart.getData().clear();
        barChart.getData().clear();

        resetAxisRanges();
    }

    public void resetAxisRanges() {
        NumberAxis yAxisLineChart = (NumberAxis) lineChart.getYAxis();
        yAxisLineChart.setAutoRanging(true);

        NumberAxis yAxisBarChart = (NumberAxis) barChart.getYAxis();
        yAxisBarChart.setAutoRanging(true);
    }
}