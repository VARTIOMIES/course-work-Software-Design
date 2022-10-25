package fi.tuni.compse110.project;

import fi.tuni.compse110.project.API.MaintenanceTask;
import fi.tuni.compse110.project.API.RoadDataProvider;
import fi.tuni.compse110.project.API.Utility;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jfree.chart.fx.ChartViewer;
import org.json.JSONArray;
import org.json.JSONObject;
import fi.tuni.compse110.project.components.Feed;

/**
 * JavaFX App
 */
public class UITest extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        testCase();
        ArrayList<Double> coords = new ArrayList<>(Arrays.asList(25.72088, 62.24147, 25.8, 62.3));

        List<MaintenanceTask> tasks = RoadDataProvider.getMaintenanceData(coords, new ArrayList<>(), "", "");
        Map<ArrayList<String>, ArrayList<String>> task_list = new HashMap<ArrayList<String>, ArrayList<String>>();
        // get necessary data for feed
        for (MaintenanceTask t : tasks) {
            task_list.put(t.getTasks(), new ArrayList<String>(Arrays.asList(t.getStartTime(), t.getEndTime())));
        }
        Feed feed = new Feed(task_list);

        scene = new Scene(feed.getElement(), 640, 480);

        /*
         *
         * EXAMPLE ON HOW TO GET AND USE THE CHART
         *
         *
         */
        /*
         * ChartViewer viewer = Graph.getChart();
         * scene = new Scene(viewer);
         */

        // Possible nullPointerException throwing from .toExternalForm()
        scene.getStylesheets().add(UITest.class.getResource("/stylesheet.css").toExternalForm());

        stage.setScene(scene);
        stage.show();

    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UITest.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // For test usage of RoadDataProvider's and WeatherDataProvider's functions
    private static void testCase() throws IOException {
        ArrayList<String> places = new ArrayList<>();
        places.add("Tampere");
        places.add("Rovaniemi");

        ArrayList<String> params = new ArrayList<>();
        params.add("temperature");
        params.add("windspeedms");
        // params.add("winddirection");
        // params.add("pressure");
        // params.add("humidity");
        // params.add("windgust");
        // params.add("totalcloudcover");

        String stime = Utility.dateFormatter(2022, 10, 12, 0, 0); // starttime
        String etime = Utility.dateFormatter(2022, 10, 13, 0, 0); // endtime

        double minLong = 24;
        double minLat = 61;
        double maxLong = 24.5;
        double maxLat = 61.4;
        ArrayList<Double> coord = new ArrayList<>();
        coord.add(minLong);
        coord.add(minLat);
        coord.add(maxLong);
        coord.add(maxLat);

        ArrayList<String> taskIds = new ArrayList<>();
        taskIds.add("ROAD_STATE_CHECKING");
        taskIds.add("DITCHING");

        RoadDataProvider.getMaintenanceData(coord, taskIds, stime, etime);
        RoadDataProvider.getTrafficMessages(3, "TRAFFIC_ANNOUNCEMENT");
    }

    /*
     * public static void main(String[] args) {
     * launch();
     * }
     */

}