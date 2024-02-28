package graphing;

import org.jfree.chart.*;
import java.io.*;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class CreateGraph {
    public static JFreeChart setData(ArrayList<ArrayList<String>> chartData, int xAxisIndex, int yAxisIndex) {
        System.out.print("Test");

        //First get values to compare in chart from UI
        //Now just sets the first variable to X and second to Y, the rest is skipped
        //Now makes standard legend with one line called "Line name", should be made customizable
        DefaultCategoryDataset dataset = reiterateChartData(chartData, xAxisIndex, yAxisIndex, "Line name");
        return(generateChart(dataset));
    }

    public static DefaultCategoryDataset reiterateChartData(ArrayList<ArrayList<String>> chartData, int xAxisIndex, int yAxisIndex, String currentLine){
        //Add values from chosen variables to dataset
        DefaultCategoryDataset newData = new DefaultCategoryDataset();
        for (int i = 1; i < chartData.get(0).size(); i++){
            newData.addValue(Float.parseFloat(chartData.get(yAxisIndex).get(i)), currentLine, chartData.get(xAxisIndex).get(i));
        }
        return newData;
    }

    public static JFreeChart generateChart(DefaultCategoryDataset dataset){
        return ChartFactory.createLineChart(
                "",
                "X-axis",
                "Y-axis",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );
    }

    //Make button for saving chart in files
    public static void saveChart(JFreeChart lineChartObject){
        try {
            int width = 320;    // Width of the image
            int height = 240;   // Height of the image
            File lineChart = new File( "LineChart.jpeg" );
            ChartUtils.saveChartAsJPEG(lineChart ,lineChartObject, width ,height);
        } catch (IOException ex) {
            System.out.println("Failed to save chart");
        }
    }
}