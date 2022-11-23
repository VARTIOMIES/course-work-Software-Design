package fi.tuni.compse110.project.UIView.Scenes;

import fi.tuni.compse110.project.API.MaintenanceTask;
import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.API.RoadDataProvider;
import fi.tuni.compse110.project.Graph.GraphProvider;
import fi.tuni.compse110.project.UIView.UIController;
import fi.tuni.compse110.project.UIView.components.SidePanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

import org.jfree.chart.fx.ChartViewer;
import fi.tuni.compse110.project.UIView.components.Feed;

/**
 * A SCENE
 */
public class TrafficPageScene extends Scene{

    // The main container
    private VBox rootVerticalContainer;
    // Is inside the VBox
    private HBox horizontalrootElementContainer;

    private SidePanel sidepanel;
    private VBox mainContent;

    private Feed taskFeed;
    private Region filler;

    private Pane graph;
    private VBox feed_window;


    private ArrayList<Double> testCoords;
    private UIController controller;

    public TrafficPageScene(ScrollPane root, double v, double v1,UIController controller) {
        super(root,v,v1);
        this.controller = controller;

        testCoords = new ArrayList<>(Arrays.asList(25.72088, 62.24147, 25.8, 62.3));

        rootVerticalContainer = new VBox(20);
        taskFeed = new Feed(new HashMap<>());
        horizontalrootElementContainer = new HBox();

        mainContent = new VBox(20);
        sidepanel = new SidePanel(20,this.controller,UIController.CurrentSceneEnum.TRAFFIC_SCENE);
        graph = new Pane();

        feed_window = new VBox(20);

        horizontalrootElementContainer.setId("row");
        graph.setId("graph");

        //initSidePanel();
        initChartViewer();
        initFeed();
        createContent();
        root.setContent(rootVerticalContainer);

    }
    public void makeNewChartViewer(ArrayList<Double> coords, ArrayList<UIController.Plottable> selectedPlottables){
        UIController.Plottable wantedData = UIController.Plottable.ROAD_TEMPERATURE;
        int roadNumber = 1;
        int sectionArrayListIndex = 1;
        String titleForChart = "helo";
        try {
            List<RoadCondition> specificRCData = RoadDataProvider.getSpecificSectionRoadCondition(
                    roadNumber,
                    sectionArrayListIndex,
                    coords
            );
            ChartViewer dataChartViewer = GraphProvider.getRoadConditionChart(
                    634,
                    500,
                    specificRCData,
                    wantedData,
                    titleForChart
            );
            graph.getChildren().setAll(dataChartViewer);
        }
        catch (Exception e){ // If there occurs any errors while creating the chart
            // from API data, creates a hardcoded chart to act as a placeholder
            //System.out.println("error");
            ChartViewer testChartViewer = GraphProvider.getTestChart(500,400);
            graph.getChildren().setAll(testChartViewer);
        }

    }

    public void initChartViewer(){
        /*
            Here are some easily modifiable parameters to get different kinds of data,
            These are useful especially for the begin-phase of the program
         */
        int roadNumber = 5;
        int sectionArrayListIndex = 3;
        UIController.Plottable wantedData = UIController.Plottable.ROAD_TEMPERATURE;
        String titleForChart = "Road:" + roadNumber + "  Section:" + sectionArrayListIndex;

        try {
            List<RoadCondition> specificRCData = RoadDataProvider.getSpecificSectionRoadCondition(roadNumber,sectionArrayListIndex,this.testCoords);
            ChartViewer dataChartViewer = GraphProvider.getRoadConditionChart(634,500,specificRCData, wantedData,titleForChart);
            graph.getChildren().add(dataChartViewer);
        }
        catch (Exception e){ // If there occurs any errors while creating the chart
            // from API data, creates a hardcoded chart to act as a placeholder
            //System.out.println("error");
            ChartViewer testChartViewer = GraphProvider.getTestChart(500,400);
            graph.getChildren().add(testChartViewer);
        }
    }

    private void initFeed(){

        // feed navigation bar top
        HBox feed_navigation_bar = new HBox();
        Button previous_road = new Button("<--");
        previous_road.setId("title");
        Button next_road = new Button("-->");
        next_road.setId("title");
        Text road_number_text = new Text("Road X");
        road_number_text.setId("title");

        Region fill_top = new Region();
        fill_top.setPrefWidth(90);
        feed_navigation_bar.setSpacing(30);
        feed_navigation_bar.getChildren().addAll(fill_top, previous_road, road_number_text, next_road);


        // feed navigation bottom
        HBox feed_timerange_bar = new HBox();

        Button previus_timerange = new Button("<--");
        previus_timerange.setId("title");
        Button next_timerange = new Button("-->");
        next_timerange.setId("title");
        Text current_time_text = new Text("2h");
        current_time_text.setId("title");

        Region fill_bottom = new Region();
        fill_bottom.setPrefWidth(100);
        feed_timerange_bar.setSpacing(30);
        feed_timerange_bar.getChildren().addAll(fill_bottom,previus_timerange, current_time_text, next_timerange);

        feed_window.setId("feed-window");
        feed_window.setAlignment(Pos.TOP_RIGHT);
        feed_window.getChildren().addAll(feed_navigation_bar, taskFeed.getElement(), feed_timerange_bar);

    }

    private void createContent()  {
        System.out.println("hep");
        //testCase();


        List<MaintenanceTask> tasks = new ArrayList<>();
        try {
            tasks = RoadDataProvider.getMaintenanceData(this.testCoords, new ArrayList<>(), "", "");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if(tasks.isEmpty()){
            System.out.println("no tasks");
            MaintenanceTask t = new MaintenanceTask();
            t.setTasks(new ArrayList<>(Arrays.asList("no tasks")));
            t.setEndTime("2022-01-01T00:00:00Z");
            t.setStartTime("2022-01-01T00:00:00Z");
            tasks.add(t);
        }
        Map<ArrayList<String>, ArrayList<String>> task_list = new HashMap<>();
        // get necessary data for feed
        for (MaintenanceTask t : tasks) {
            task_list.put(t.getTasks(), new ArrayList<String>(Arrays.asList(t.getPrettyTimeRange(), t.getSource())));
        }
        taskFeed = new Feed(task_list);

        // vertical layout

        rootVerticalContainer.setMinWidth(1024);
        rootVerticalContainer.setAlignment(Pos.CENTER);
        rootVerticalContainer.setId("background");

        Button backButton = new Button("<- back to menu");
        backButton.setPrefSize(120,40);
        backButton.setOnAction(event->backToMenuClickHandle());
        rootVerticalContainer.getChildren().add(backButton);

        filler = new Region();
        filler.setPrefWidth(50);



        mainContent.getChildren().addAll(graph, feed_window);
        horizontalrootElementContainer.getChildren().addAll(mainContent,filler, sidepanel);
        
        rootVerticalContainer.getChildren().addAll(horizontalrootElementContainer);


        // Possible nullPointerException throwing from .toExternalForm()
        this.getStylesheets().add(TrafficPageScene.class.getResource("/stylesheet.css").toExternalForm());

    }



    /**
     * lambda to go handle back button going back to menu
     */
    private void backToMenuClickHandle(){
        // Stuff happening after the "back to menu" button click
        controller.fromAnyPageToMenu();
    }

    // For test usage of RoadDataProvider's and WeatherDataProvider's functions
    /*private static void testCase() throws IOException {
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
*/
     

}