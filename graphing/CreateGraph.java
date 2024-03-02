package graphing;

import org.jfree.chart.*;
import java.io.*;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.DomainOrder;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class CreateGraph {
    public static JFreeChart setData(ArrayList<ArrayList<String>> chartData, int iterations, int xAxisIndex) {
        System.out.print("Test");

        //First get values to compare in chart from UI
        //Now just sets the first variable to X and second to Y, the rest is skipped
        //Now makes standard legend with one line called "Line name", should be made customizable
        XYDataset dataset = reiterateChartData(chartData, iterations, xAxisIndex);
        return(generateChart(dataset));
    }

    public static XYDataset reiterateChartData(ArrayList<ArrayList<String>> chartData, int iterations, int xAxisIndex){

        //Add values from chosen variables to dataset
        XYSeriesCollection newData = new XYSeriesCollection();

        //Find a way to add more lines using the app's UI
        ArrayList<XYSeries> lines = new ArrayList<>();

        for(int i = 0; i < chartData.size(); i++){
            lines.add(new XYSeries(chartData.get(i).get(0)));
            if(i == xAxisIndex)
                continue;
            for (int j = 1; j < iterations; j++){
                lines.get(i).add(Double.parseDouble(chartData.get(xAxisIndex).get(j)), Double.parseDouble(chartData.get(i).get(j)));
            }
            newData.addSeries(lines.get(i));
        }
        return newData;
    }

    public static JFreeChart generateChart(XYDataset dataset){
        return ChartFactory.createXYLineChart(
                "",
                "X-axis",
                "Y-axis",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );
    }
}