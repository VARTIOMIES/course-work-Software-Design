package fi.tuni.compse110.project.UIView.Scenes;

import fi.tuni.compse110.project.API.MaintenanceTask;
import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.API.RoadDataProvider;
import fi.tuni.compse110.project.Graph.GraphProvider;
import fi.tuni.compse110.project.UIView.UIController;
import fi.tuni.compse110.project.UIView.components.GraphComponent;
import fi.tuni.compse110.project.UIView.components.SidePanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
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

    private VBox feed_window;

    private int roadNumber;
    private ArrayList<Double> coords;
    private UIController controller;
    private GraphComponent graph;

    private List<RoadCondition> specificRCData;
    private ArrayList<UIController.Plottable> wantedData;
    private TreeMap<Integer,TreeMap<String,ArrayList<RoadCondition>>> data;


    public TrafficPageScene(ScrollPane root,
                            double v,
                            double v1,
                            UIController controller) {
        super(root,v,v1);
        root.setFitToWidth(true);
        root.setPannable(false);
        this.controller = controller;


        //coords = new ArrayList<>(Arrays.asList(25.72088, 62.24147, 25.8, 62.3));

        mainContent = new VBox(20);

        rootVerticalContainer = new VBox(20);
        rootVerticalContainer.setMinWidth(1024);
        rootVerticalContainer.setAlignment(Pos.CENTER);
        rootVerticalContainer.setId("background");

        horizontalrootElementContainer = new HBox();
        horizontalrootElementContainer.setId("row");

        specificRCData = new ArrayList<>();


        Button backButton = new Button("<- back to menu");
        backButton.setPrefSize(120,40);
        backButton.setOnAction(event->backToMenuClickHandle());
        rootVerticalContainer.getChildren().add(backButton);

        graph = new GraphComponent(
                634,
                500,
                UIController.CurrentSceneEnum.TRAFFIC_SCENE);
        graph.setId("graph");

        // TODO:: Here are some differences that should be checked
        wantedData = new ArrayList<>();

        taskFeed = new Feed(new HashMap<>());

        initFeed();

        mainContent.getChildren().addAll(graph, feed_window);

        filler = new Region();
        filler.setPrefWidth(50);
        sidepanel = new SidePanel(20,
                this.controller,
                UIController.CurrentSceneEnum.TRAFFIC_SCENE
        );

        horizontalrootElementContainer.getChildren().addAll(mainContent,filler, sidepanel);

        rootVerticalContainer.getChildren().addAll(horizontalrootElementContainer);

        // Possible nullPointerException throwing from .toExternalForm()
        this.getStylesheets().add(TrafficPageScene.class.getResource("/stylesheet.css").toExternalForm());

        root.setContent(rootVerticalContainer);

    }

    /**
     *
     * Getting the data from the api class and formatting into
     * usable datastructure for plotting. Saves the data locally.
     * Api class may have some restrictions with the
     * coordinates. @see {@link RoadDataProvider#getRoadConditions}
     * @param coords ArrayList<Double> 4 Doubles representing the area
     *               given with the input sliders.
     *
     * @author Onni Merilä
     */
    public void getDataFromApi(ArrayList<Double> coords){

        TreeMap<Integer, LinkedHashMap<String, ArrayList<RoadCondition>>> monster;

        try{
            int empty_road_number = -1;
            monster = RoadDataProvider.getRoadConditions(empty_road_number,coords);

        }
        catch (Exception e){
            System.out.println("Error with getting the data from api");
            return;
        }
        monster.size();


        // Change  monster to be in lists of one specific location
        String[] keys = {"0h","2h","4h","6h","12h"};


        TreeMap<Integer, // Road number
                TreeMap<String, // Section id (instead of the forecastName)
                        ArrayList<RoadCondition> // list (len(5)) containing the
                        // RoadCondition objects of the same section.
                        >
                > road_sections_and_their_forecasts = new TreeMap<>();


        for (Integer road_number : monster.keySet()){

            int section_amount = monster.get(road_number).get("0h").size();
            TreeMap<String,ArrayList<RoadCondition>> section_conditions = new TreeMap<>();


            for (int section_index = 0;section_index<section_amount;section_index++) {
                System.out.println(section_index);
                String section = monster.get(road_number).get("0h").get(section_index).getSection();
                ArrayList<RoadCondition> section_forecast_list = new ArrayList<>();

                for (String key : keys) {
                    RoadCondition iterated_road_condition = monster.get(road_number).get(key).get(section_index);
                    section_forecast_list.add(iterated_road_condition);
                }
                section_conditions.put(section,section_forecast_list);
            }

            road_sections_and_their_forecasts.put(road_number,section_conditions);


        }
        this.data = road_sections_and_their_forecasts;
    }
/*

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
/*

    public void makeNewChartViewer(ArrayList<Double> coords,
                                   ArrayList<UIController.Plottable> selectedPlottables){
        UIController.Plottable wantedData = UIController.Plottable.ROAD_TEMPERATURE;
        this.coords = coords;
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
                    selectedPlottables);
            graph.getChildren().setAll(dataChartViewer);
        }
        catch (Exception e){ // If there occurs any errors while creating the chart
            // from API data, creates a hardcoded chart to act as a placeholder
            //System.out.println("error");
            initChartViewer("Error: code 69");
        }

    }

*/

    private void populateFeed(){
        Map<ArrayList<String>, ArrayList<String>> task_list = new HashMap<>();
        // TODO:: use the data gotten from getDataFromApi
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

    private void initTasks(){
        List<MaintenanceTask> tasks = new ArrayList<>();
        try {
            tasks = RoadDataProvider.getMaintenanceData(this.coords, new ArrayList<>(), "", "");
        } catch (IOException e1) {
            System.out.println(e1);
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
        if(tasks.isEmpty()){
            System.out.println("no tasks");
            MaintenanceTask t = new MaintenanceTask();
            t.setTasks(new ArrayList<>(Arrays.asList("no tasks")));
            t.setEndTime("2022-01-01T00:00:00Z");
            t.setStartTime("2022-01-01T00:00:00Z");
            tasks.add(t);
        }else{
            for (MaintenanceTask t : tasks) {
                task_list.put(t.getTasks(), new ArrayList<String>(Arrays.asList(t.getPrettyTimeRange(), t.getSource())));
            }
        }
        taskFeed = new Feed(task_list);
    }


    private void refreshWithNewData(){
        //makeNewChartViewer(coords,wantedData);
        populateFeed();
        feed_window.getChildren().set(1,taskFeed.getElement());
    }


    /**
     * lambda to go handle back button going back to menu
     */
    private void backToMenuClickHandle(){
        // Stuff happening after the "back to menu" button click
        controller.fromAnyPageToMenu();
    }

    public void handleSearchButtonClick(ArrayList<Double> coords,
                                         ArrayList<UIController.Plottable> selected){
        getDataFromApi(coords);
        this.wantedData = selected;
        graph.give_data(this.data,this.wantedData);
        refreshWithNewData();
    }



}