package fi.tuni.compse110.project.Graph;
/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.chart.fx.ChartViewer;

/**
 * Provides ChartViews from data. Idea is that the raw data could be given to this provider class,
 * and the data would be then parsed to wanted form by parameters given with
 * the function calls
 */
public class GraphProvider {

    public GraphProvider(){

    }

    public static JFreeChart createTestChart() {

        double[] values = { 95, 49, 14, 59, 50, 66, 47, 40, 1, 67,
                12, 58, 28, 63, 14, 9, 31, 17, 94, 71,
                49, 64, 73, 97, 15, 63, 10, 12, 31, 62,
                93, 49, 74, 90, 59, 14, 15, 88, 26, 57,
                77, 44, 58, 91, 10, 67, 57, 19, 88, 84
        };


        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("key", values, 20);

        JFreeChart histogram = ChartFactory.createHistogram("JFreeChart Histogram",
                "y values", "x values", dataset);
        return histogram;
    }

    /**
     * Use this function in testing! Returns a hardcoded example of a
     * (Node) Chart to be used in UI. Size can be controlled by parameters.
     *
     * @param width
     * @param height
     * @return ChartViewer , a class that behaves lovely with javafx
     */
    public static ChartViewer getTestChart(int width,int height){
        ChartViewer viewer =  new ChartViewer(createTestChart());
        viewer.setPrefSize(width,height);
        return viewer;
    }


}
