package graphing;

import interpreter.Interpreter;
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
import ui.JFrameWindow;

import javax.swing.*;

public class CreateGraph {

    public static JFrameWindow appWindow;
    public static JFreeChart currentChart;
    public static ArrayList<String> lineNames;
    public static JFreeChart setData(ArrayList<ArrayList<String>> chartData, int iterations, int xAxisIndex) {
        System.out.print("Test");

        //First get values to compare in chart from UI
        //Now just sets the first variable to X and second to Y, the rest is skipped
        //Now makes standard legend with one line called "Line name", should be made customizable
        XYDataset dataset = reiterateChartData(chartData, iterations, xAxisIndex);
        return(createChart(dataset));
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

    public static JFreeChart createChart(XYDataset dataset){
        return ChartFactory.createXYLineChart(
                "",
                "",
                "",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );
    }



    public static void generateChart(){
        //Check UI for iterations, 1000 is default for now
        int iterations = 1000;

        ArrayList<ArrayList<String>> chartData = interpretData(iterations);
        for (int i = 0; i< chartData.size(); i++)
            lineNames.add(chartData.get(i).get(0));
        //Change variable on x-axis through UI, default = 0 for now
        int xAxisIndex = 0;

        currentChart = setData(chartData,iterations, xAxisIndex);
    }

    public static void setUpUI(){

        lineNames = new ArrayList<String>();
        // following two lines are not important
        String s = UIManager.getSystemLookAndFeelClassName();
        System.out.println(s);

        // boilerplate code needed for setting up the window, don't delete
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                appWindow = new JFrameWindow();
                setGraph();
            }

            public void setGraph()
            {
                appWindow.setGraph(currentChart, lineNames, 0);
            }
        });
    }

    private static ArrayList<ArrayList<String>> interpretData(int iterations){
        String variables = getContentsOfFile("variables");
        String rules = getContentsOfFile("rules");

        return Interpreter.interpret(variables, rules, iterations);
    }

    static String getContentsOfFile(String file) {
        String data = new String();
        try {
            Scanner scanner = new Scanner( new File(file) );
            while (scanner.hasNextLine()) {
                data = data.concat(scanner.nextLine());
                if (scanner.hasNextLine()) {
                    data = data.concat("\n");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not open file");
            e.printStackTrace();
        }
        return data;
    }
}