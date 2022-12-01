package fi.tuni.compse110.project.Graph;
/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.UIView.UIController;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.chart.fx.ChartViewer;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides ChartViews from data. Idea is that the raw data could be given to this provider class,
 * and the data would be then parsed to wanted form by parameters given with
 * the function calls
 * @author Onni Merilä
 */
public class GraphProvider {

    private GraphProvider singleInstance = null;

    private GraphProvider(){

    }

    public GraphProvider getInstance(){
        if (singleInstance == null)
            singleInstance = new GraphProvider();

        return singleInstance;
    }


    /**
     * @author Onni Merilä
     *
     * @param width int, (px)
     * @param height int, (px)
     * @param sameLocationConditions A list of RoadConditions, preferrably from
     *                               a same location but different time
     * @param plottedData (enum) the wanted data from the roadcondition
     * @param title A title text for the chart
     * @return ChartViewer object containing a JFreeChart of the data
     */
    public static ChartViewer getRoadConditionChart(int width,int height,
                                                    List<RoadCondition> sameLocationConditions,
                                                    UIController.Plottable plottedData,
                                                    String title){
        // Get values
        ArrayList<String> xdata = new ArrayList<>();

        ArrayList<Object> ydata = new ArrayList<>();
        String valueLabel = "";
        switch (plottedData) {
            case ROAD_TEMPERATURE:
                for (RoadCondition r : sameLocationConditions){
                    ydata.add(r.getRoadTemperature());
                }
                valueLabel = "Tien lämpötila";
                break;
            case TEMPERATURE:
                for (RoadCondition r : sameLocationConditions){
                    ydata.add(r.getTemperature());
                }
                valueLabel = "Ilman lämpötila";
                break;
            case WIND_SPEED:
                for (RoadCondition r : sameLocationConditions){
                    ydata.add(r.getWindSpeed());
                }
                valueLabel = "Tuulen nopeus";
                break;
        }

        for (RoadCondition r : sameLocationConditions){
            xdata.add(r.getForecastTime());
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0;i<5;i++){
            dataset.addValue(Double.parseDouble(ydata.get(i).toString()),"Dataset",xdata.get(i));
        }
        JFreeChart forecast = ChartFactory.createLineChart(title,"Tunnit",valueLabel,dataset);

        ChartViewer viewer = new ChartViewer(forecast);
        viewer.setPrefSize(width,height);
        return viewer;

    }

    public static ChartViewer getWeatherChart(int width,int height,
                                              List<RoadCondition> sameLocationConditions,
                                              UIController.Plottable plottedData,
                                              String title){
        return getTestChart(width,height);
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
     * @param width (px) int, the width of the ChartViewer in pixels
     * @param height (px) int, the height of the ChartViewer in pixels
     * @return ChartViewer , a class that behaves lovely with javafx
     */
    public static ChartViewer getTestChart(int width,int height){
        ChartViewer viewer =  new ChartViewer(createTestChart());
        viewer.setPrefSize(width,height);
        return viewer;
    }


}
