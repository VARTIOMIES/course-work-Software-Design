package fi.tuni.compse110.project.UIView.Scenes;

import fi.tuni.compse110.project.API.MaintenanceTask;
import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.API.RoadDataProvider;
import fi.tuni.compse110.project.Graph.GraphProvider;
import fi.tuni.compse110.project.UIView.UIController;
import fi.tuni.compse110.project.UIView.components.GraphComponent;
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


    private VBox vLayout;

    private HBox horizontalrootElementContainer;

    private VBox mainContent;

    private Feed taskFeed;
    private VBox feed_window;

    private UIController controller;
    private int roadNumber;
    private SidePanel sidepanel;
    private GraphComponent graph;
    private final Region filler;

    private List<RoadCondition> specificRCData;
    private ArrayList<UIController.Plottable> wantedData;
    private TreeMap<Integer,TreeMap<String,ArrayList<RoadCondition>>> data;

    public TrafficPageRoadScene(ScrollPane root,
                                double v,
                                double v1,
                                UIController controller) {
        super(root, v, v1);
        root.setFitToWidth(true);
        root.setPannable(false);
        this.controller = controller;

        //testCoords = new ArrayList<>(Arrays.asList(25.72088, 62.24147, 25.8, 62.3));

        mainContent = new VBox(20);

        vLayout = new VBox(20);
        vLayout.setMinWidth(1024);
        vLayout.setAlignment(Pos.CENTER);
        vLayout.setId("background");

        horizontalrootElementContainer = new HBox();
        horizontalrootElementContainer.setId("row");

        // TODO:: this should be removed after feed populating
        //  is done with the new datastructure
        specificRCData = new ArrayList<>();


        Button backButton = new Button("<- back to menu");
        backButton.setPrefSize(120, 40);
        backButton.setOnAction(event -> backToMenuClickHandle());
        vLayout.getChildren().add(backButton);


        graph = new GraphComponent(
            750,
                500,
                UIController.CurrentSceneEnum.TRAFFIC_SCENE_ROAD);
        graph.setId("graph");
        wantedData = new ArrayList<>();
        wantedData.add(UIController.Plottable.ROAD_TEMPERATURE);
        //initChartViewer("");

        initFeed();

        mainContent.getChildren().addAll(graph, feed_window);

        filler = new Region();
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

    public void getDataFromApi(ArrayList<Integer> road){

        TreeMap<Integer, LinkedHashMap<String, ArrayList<RoadCondition>>> monster;
        int road_number = road.get(0);
        try{
            ArrayList<Double> empty_coords = new ArrayList<>();
            monster = RoadDataProvider.getRoadConditions(road_number,empty_coords);

        }
        catch (Exception e){
            System.out.println("Error with getting the data from api");
            return;
        }
        monster.size();


        //baby_monster.size();
        int section_amount = monster.get(road_number).get("0h").size();
        // Change baby monster to be in lists of one specific location
        String[] keys = {"0h","2h","4h","6h","12h"};

        // String is the id of the section
        TreeMap<Integer,TreeMap<String,ArrayList<RoadCondition>>> road_sections_and_their_forecasts = new TreeMap<>();

        TreeMap<String,ArrayList<RoadCondition>> section_conditions = new TreeMap<>();
        for (int i = 0;i<section_amount;i++) {
            System.out.println(i);
            String section = monster.get(road_number).get("0h").get(i).getSection();
            ArrayList<RoadCondition> section_forecast_list = new ArrayList<>();

            for (String key : keys) {
                RoadCondition iterated_road_condition = monster.get(road_number).get(key).get(i);
                section_forecast_list.add(iterated_road_condition);
            }
            section_conditions.put(section,section_forecast_list);
        }
        road_sections_and_their_forecasts.put(road_number,section_conditions);
        this.data = road_sections_and_their_forecasts;

    }


    /**
     * Assumes that this.roadNumber,this.controller are already initiated
     */
    private void initFeed(){
        

        // feed navigation bar top
        HBox feed_navigation_bar = new HBox();

        Text road_number_text = new Text("Road " + roadNumber);
        road_number_text.setId("title");
        feed_navigation_bar.setSpacing(30);

        Region fill_top = new Region();
        fill_top.setPrefWidth(90);

        feed_navigation_bar.getChildren().addAll(fill_top, road_number_text);

        // feed navigation bottom
        HBox feed_timerange_bar = new HBox();

       

        Region fill_bottom = new Region();
        fill_bottom.setPrefWidth(100);
        feed_timerange_bar.setSpacing(30);
        feed_timerange_bar.getChildren().addAll(fill_bottom);

        populateFeed();

        feed_window = new VBox(20);
        feed_window.setId("feed-window");
        feed_window.setAlignment(Pos.TOP_RIGHT);
        feed_window.getChildren().addAll(feed_navigation_bar, taskFeed.getElement(), feed_timerange_bar);

    }


    private void populateFeed(){
        specificRCData.clear();
        try{
            // iterate "data" treemap
            for(var x: data.keySet()){
                for(var y: data.get(x).keySet()){
                    for(var z: data.get(x).get(y)){
                        var task = data.get(x).get(y).get(0);
                        specificRCData.add(task);
                    }
                }
            }

            
        }catch (Exception e){
            System.out.println("Error with getting the data from api");
            System.out.println(e);

        }
        

        Map<ArrayList<String>, ArrayList<String>> task_list = new HashMap<>();

        if (!specificRCData.isEmpty()) {
            for (var condition : specificRCData) {
                task_list.put(new ArrayList<String>(Arrays.asList("Road: " + condition.getRoadNumber() +" Section " + condition.getSection())),
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

        //makeNewChartViewer(road,wantedData);
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

    public void handleSearchButtonClick(ArrayList<Integer> road,
                                        ArrayList<UIController.Plottable> selected){
        getDataFromApi(road);
        this.wantedData = selected;
        graph.give_data(this.data,this.wantedData);
        refreshWithNewData();
    }


}