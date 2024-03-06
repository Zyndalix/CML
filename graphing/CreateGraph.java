package graphing;

import interpreter.Interpreter;
import interpreter.Node;
import interpreter.Variable;
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
    public static JFreeChart setData(ArrayList<ArrayList<String>> chartData, int iterations) {
        XYDataset dataset = reiterateChartData(chartData, iterations);
        return(createChart(dataset));
    }

    public static XYDataset reiterateChartData(ArrayList<ArrayList<String>> chartData, int iterations){

        //Add values from chosen variables to dataset
        XYSeriesCollection newData = new XYSeriesCollection();

        //Find a way to add more lines using the app's UI
        ArrayList<XYSeries> lines = new ArrayList<>();

        for(int i = 0; i < chartData.size(); i++){
            lines.add(new XYSeries(chartData.get(i).get(0)));
            if(i == appWindow.xAxisIndex)
                continue;
            for (int j = 1; j < iterations; j++){
                lines.get(i).add(Double.parseDouble(chartData.get(appWindow.xAxisIndex).get(j)), Double.parseDouble(chartData.get(i).get(j)));
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

        int iterations = appWindow.iterationsText == null ? 5 : Integer.parseInt(appWindow.iterationsText.getText());;

        lineNames = new ArrayList<String>();

        ArrayList<ArrayList<String>> chartData = interpretData(iterations);
        System.out.print("Generated chart data: " + chartData);

        for (ArrayList<String> chartDatum : chartData) lineNames.add(chartDatum.get(0));
        currentChart = setData(chartData, iterations);
    }

    public static void setUpUI(){
        // following two lines are not important
        String s = UIManager.getSystemLookAndFeelClassName();
        System.out.print(s);

        if (appWindow == null)
            CreateWindow();
    }

    static void CreateWindow(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                appWindow = new JFrameWindow();
                appWindow.loadSavedText();
                generateChart();
                setGraph();
            }

            public void setGraph()
            {
                appWindow.setGraph(currentChart, lineNames);
            }
        });
    }

    private static ArrayList<ArrayList<String>> interpretData(int iterations){
        String variables = getContentsOfFile("variables");
        System.out.print("\nVariables: "+ variables);
        String rules = getContentsOfFile("rules");
        System.out.print("\nRules: "+ rules);
        Interpreter.data.clear();
        Variable.list.clear();
        Node.list.clear();
        return Interpreter.interpret(variables, rules, iterations);
    }

    public static String getContentsOfFile(String file) {
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