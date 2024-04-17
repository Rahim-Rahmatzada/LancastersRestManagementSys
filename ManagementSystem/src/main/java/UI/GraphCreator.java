package UI;

import javafx.scene.chart.*;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.util.List;

/**
 * This class is responsible for creating and managing line charts and bar charts.
 */
public class GraphCreator {
    private LineChart<String, Number> lineChart;
    private BarChart<String, Number> barChart;

    /**
     * Constructs a new GraphCreator and initializes lineChart and barChart.
     */
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
        lineChart.setLegendVisible(true);


        CategoryAxis barXAxis = new CategoryAxis();
        barXAxis.setLabel("Date");
        barXAxis.setTickLabelFill(Color.WHITE);
        barXAxis.setStyle("-fx-text-fill: white;");
        barChart = new BarChart<>(barXAxis, yAxis);
        barChart.setLegendVisible(true);


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

    /**
     * Adds a series to the line chart.
     *
     * @param data         The data points for the series.
     * @param seriesName   The name of the series.
     * @param startDate    The start date for the x-axis.
     * @param variableType The type of variable for the y-axis.
     */
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

    /**
     * Adds a series to the bar chart.
     *
     * @param data    The data points for the series.
     * @param seriesName The name of the series.
     * @param startDate The start date for the x-axis.
     * @param yLabel  The label for the y-axis.
     */
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

    /**
     * Clears both the line chart and the bar chart.
     */
    public void clearGraph() {
        lineChart.getData().clear();
        barChart.getData().clear();

        resetAxisRanges();
    }

    /**
     * Resets the axis ranges for both charts to auto-ranging.
     */
    public void resetAxisRanges() {
        NumberAxis yAxisLineChart = (NumberAxis) lineChart.getYAxis();
        yAxisLineChart.setAutoRanging(true);

        NumberAxis yAxisBarChart = (NumberAxis) barChart.getYAxis();
        yAxisBarChart.setAutoRanging(true);
    }
}