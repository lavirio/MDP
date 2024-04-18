import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;
import java.util.List;

public class PlotGraph {
    public static void plotConvergenceTime(List<Double> times) {
        XYSeries series = new XYSeries("Iteration Times");
        for (int i = 0; i < times.size(); i++) {
            series.add(i+1, times.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Value Iteration Convergence Time",
                "Iteration",
                "Time (seconds)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public static void plotConvergenceUtility(List<Double> totalDifferences) {
        XYSeries series = new XYSeries("Total Differences");
        for (int i = 0; i < totalDifferences.size(); i++) {
            series.add(i + 1, totalDifferences.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Convergence of Value Iteration",
                "Iteration Number",
                "Total Difference in Utility",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void plotActionsChanged(List<Integer> actionsChanged) {
        XYSeries series = new XYSeries("Actions Changed");
        for (int i = 0; i < actionsChanged.size(); i++) {
            series.add(i + 1, actionsChanged.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Actions Changed Per Iteration",
                "Iteration Number",
                "Number of Actions Changed",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chartPanel);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public static void plotPolicyChanges(List<Integer> policyChanges) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < policyChanges.size(); i++) {
            dataset.addValue(policyChanges.get(i), "Policy Changes", "" + i);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Policy Changes Per Iteration",
                "Iteration",
                "Number of Changes",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        JFrame frame = new JFrame();
        frame.add(chartPanel);
        frame.setTitle("RTDP Policy Change Visualization");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
