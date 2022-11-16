package fi.tuni.compse110.project.UIView.Scenes;

import fi.tuni.compse110.project.API.MaintenanceTask;
import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.API.RoadDataProvider;
import fi.tuni.compse110.project.API.Utility;
import fi.tuni.compse110.project.API.WeatherDataProvider;
import fi.tuni.compse110.project.Graph.GraphProvider;
import fi.tuni.compse110.project.UIView.UIController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

import org.jfree.chart.fx.ChartViewer;
import fi.tuni.compse110.project.components.Feed;

/**
 * A SCENE
 */
public class TrafficPageScene extends Scene{

    VBox vLayout;
    Pane searchBar;
    Feed taskFeed;
    Region filler;
    HBox row;
    Pane graph;
    UIController controller;

    public TrafficPageScene(ScrollPane root, double v, double v1,UIController controller) {
        super(root,v,v1);
        this.controller = controller;

        vLayout = new VBox(20);
        searchBar = new Pane();
        //taskFeed = new Feed(new HashMap<>());
        row = new HBox();
        graph = new Pane();
        stuff();
        root.setContent(vLayout);

    }

    public void stuff()  {
        System.out.println("hep");
        //testCase();
        ArrayList<Double> coords = new ArrayList<>(Arrays.asList(25.72088, 62.24147, 25.8, 62.3));

        List<MaintenanceTask> tasks = new ArrayList<>();
        try {
            tasks = RoadDataProvider.getMaintenanceData(coords, new ArrayList<>(), "", "");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Map<ArrayList<String>, ArrayList<String>> task_list = new HashMap<>();
        // get necessary data for feed
        for (MaintenanceTask t : tasks) {
            task_list.put(t.getTasks(), new ArrayList<String>(Arrays.asList(t.getPrettyTimeRange(), t.getSource())));
        }
        taskFeed = new Feed(task_list);

        // vertical layout

        vLayout.setMinWidth(1024);
        vLayout.setAlignment(Pos.CENTER);
        vLayout.setId("background");
        // seachbar (not yet implemented)

        searchBar.setPadding(new Insets(0, 20, 10, 20)); 
        searchBar.setStyle("-fx-background-color: red");
        searchBar.setMinSize(800, 200);
        searchBar.setId("search-bar");
        

        row.setId("row");
        graph.setId("graph");

        /*
            Here are some easily modifiable parameters to get different kinds of data,
            These are useful especially for the begin-phase of the program
         */
        int roadNumber = 5;
        int sectionArrayListIndex = 3;
        GraphProvider.Plottable wantedData = GraphProvider.Plottable.ROAD_TEMPERATURE;
        String titleForChart = "Road:" + roadNumber + "  Section:" + sectionArrayListIndex;

        try {
            List<RoadCondition> specificRCData = RoadDataProvider.getSpecificSectionRoadCondition(roadNumber,sectionArrayListIndex,coords);
            ChartViewer dataChartViewer = GraphProvider.getRoadConditionChart(500,400,specificRCData, wantedData,titleForChart);
            graph.getChildren().add(dataChartViewer);
        }
        catch (Exception e){ // If there occurs any errors while creating the chart
            // from API data, creates a hardcoded chart to act as a placeholder
            //System.out.println("error");
            ChartViewer testChartViewer = GraphProvider.getTestChart(500,400);
            graph.getChildren().add(testChartViewer);
        }

        Button backButton = new Button("<- back to menu");
        backButton.setPrefSize(120,40);
        backButton.setOnAction(event->backToMenuClickHandle());

        Region filler = new Region();
        filler.setPrefWidth(50);
        row.getChildren().addAll(graph,filler, taskFeed.getElement(),backButton);
        
        vLayout.getChildren().addAll(searchBar, row);


        // Possible nullPointerException throwing from .toExternalForm()
        this.getStylesheets().add(TrafficPageScene.class.getResource("/stylesheet.css").toExternalForm());

    }

    /**
     * lambda to go handle back button going back to menu
     */
    private void backToMenuClickHandle(){
        // Stuff happening after the "back to menu" button click
        controller.fromTrafficPageToMenu();
    }

 /*   public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }*/

    /*private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TrafficPageScene.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }*/

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
        RoadDataProvider.getTrafficMessages(1, "TRAFFIC_ANNOUNCEMENT");
        WeatherDataProvider.weatherURLCreator(places, new ArrayList<Double>(), params, stime, etime);
    }

     

}