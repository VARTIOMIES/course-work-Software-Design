package fi.tuni.compse110.project.UIView.Scenes;

import fi.tuni.compse110.project.API.MaintenanceTask;
import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.API.RoadDataProvider;
import fi.tuni.compse110.project.Graph.GraphProvider;
import fi.tuni.compse110.project.UIView.UIController;
import fi.tuni.compse110.project.UIView.components.SidePanel;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.*;

import javafx.scene.text.TextAlignment;
import org.jfree.chart.fx.ChartViewer;
import fi.tuni.compse110.project.UIView.components.Feed;

/**
 * A SCENE
 */
public class TrafficPageRoadScene extends Scene {


    private VBox mainContent;
    VBox vLayout;
    Feed taskFeed;
    private VBox feed_window;
    Region filler;
    HBox horizontalrootElementContainer;
    Pane graph;
    UIController controller;
    private int roadNumber;
    private ArrayList<Double> testCoords;
    private SidePanel sidepanel;

    private List<RoadCondition> specificRCData;
    private ArrayList<UIController.Plottable> wantedData;

    public TrafficPageRoadScene(ScrollPane root,
                                double v,
                                double v1,
                                UIController controller) {
        super(root, v, v1);
        this.controller = controller;

        testCoords = new ArrayList<>(Arrays.asList(25.72088, 62.24147, 25.8, 62.3));

        mainContent = new VBox(20);

        vLayout = new VBox(20);
        vLayout.setMinWidth(1024);
        vLayout.setAlignment(Pos.CENTER);
        vLayout.setId("background");

        horizontalrootElementContainer = new HBox();
        horizontalrootElementContainer.setId("row");

        specificRCData = new ArrayList<>();

        roadNumber = 1;


        //createContent();

        Button backButton = new Button("<- back to menu");
        backButton.setPrefSize(120, 40);
        backButton.setOnAction(event -> backToMenuClickHandle());

        vLayout.getChildren().add(backButton);


        graph = new Pane();
        graph.setId("graph");
        wantedData = new ArrayList<>();
        wantedData.add(UIController.Plottable.ROAD_TEMPERATURE);
        initChartViewer("");

        initFeed();


        mainContent.getChildren().addAll(graph, feed_window);

        Region filler = new Region();
        filler.setPrefWidth(50);
        sidepanel = new SidePanel(20,
                this.controller,
                UIController.CurrentSceneEnum.TRAFFIC_SCENE_ROAD
        );

        horizontalrootElementContainer.getChildren().addAll(mainContent, filler, sidepanel);

        vLayout.getChildren().addAll(horizontalrootElementContainer);

        // Possible nullPointerException throwing from .toExternalForm()
        this.getStylesheets().add(TrafficPageRoadScene.class.getResource("/stylesheet.css").toExternalForm());

        root.setContent(vLayout);



    }

    public void getDataFromApi(){
        int roadNumber = 1;
        int sectionArrayListIndex = 1;
        try{
            specificRCData = RoadDataProvider.getSpecificSectionRoadCondition(
                    roadNumber,
                    sectionArrayListIndex,
                    testCoords
            );
        }
        catch (Exception e){
            System.out.println("Error with getting the data from api");
        }

    }

    public void initChartViewer(String info_text){
        if (info_text.isEmpty()){
            info_text = "Nothing to show yet :(";
        }
        graph.setPrefSize(634,500);

        Label emptytext = new Label(info_text);
        emptytext.setAlignment(Pos.CENTER);
        emptytext.minWidthProperty().bind(graph.widthProperty());
        emptytext.minHeightProperty().bind(graph.heightProperty());

        graph.getChildren().setAll(emptytext);
    }

    /**
     * Assumes that this.roadNumber,this.controller are already initiated
     */
    private void initFeed(){

        // feed navigation bar top
        HBox feed_navigation_bar = new HBox();
        Button previous_road = new Button("<--");
        previous_road.setId("title");

        previous_road.setOnAction(event -> {
            if (roadNumber - 1 >= 0){
                roadNumber = roadNumber - 1;
                refreshWithNewData();
            }

        });

        Button next_road = new Button("-->");
        next_road.setId("title");
        next_road.setOnAction(event -> {
            roadNumber = roadNumber + 1;
            refreshWithNewData();
        });

        Text road_number_text = new Text("Road " + roadNumber);
        road_number_text.setId("title");
        feed_navigation_bar.setSpacing(30);

        Region fill_top = new Region();
        fill_top.setPrefWidth(90);

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
        feed_timerange_bar.getChildren().addAll(fill_bottom, previus_timerange, current_time_text, next_timerange);

        populateFeed();

        feed_window = new VBox(20);
        feed_window.setId("feed-window");
        feed_window.setAlignment(Pos.TOP_RIGHT);
        feed_window.getChildren().addAll(feed_navigation_bar, taskFeed.getElement(), feed_timerange_bar);

    }


    public void makeNewChartViewer(ArrayList<Double> coords,
                                   ArrayList<UIController.Plottable> selectedPlottables){
        UIController.Plottable wantedData = UIController.Plottable.ROAD_TEMPERATURE;
        int roadNumber = 1;
        int sectionArrayListIndex = 1;
        String titleForChart = "helo";
        try {
            getDataFromApi();
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
            initChartViewer("Error: code 69");
        }

    }



    private void populateFeed(){
        Map<ArrayList<String>, ArrayList<String>> task_list = new HashMap<>();
        if (!specificRCData.isEmpty()) {
            for (var condition : specificRCData) {
                task_list.put(new ArrayList<String>(Arrays.asList("Section " + condition.getSection())),
                        new ArrayList<String>(Arrays.asList(
                                "Precipitation: " + condition.getPrecipitationCondition(),
                                "Overall road condition: " + condition.getOverallRoadCondition(),
                                "Road condition: " + condition.getRoadCondition()
                        )));
            }
        } else {
            task_list.put(new ArrayList<String>(Arrays.asList("no data for this road")),
                    new ArrayList<String>(Arrays.asList(
                            "")));
        }
        taskFeed = new Feed(task_list);
    }


    private void refreshWithNewData(){

        makeNewChartViewer(testCoords,wantedData);
        populateFeed();
        feed_window.getChildren().set(1,taskFeed.getElement());
    }


    /**
     * lambda to go handle back button going back to menu
     */
    private void backToMenuClickHandle() {
        // Stuff happening after the "back to menu" button click
        controller.fromAnyPageToMenu();
    }

    private void handleSearchButtonClick(){
        getDataFromApi();
        refreshWithNewData();
    }


}