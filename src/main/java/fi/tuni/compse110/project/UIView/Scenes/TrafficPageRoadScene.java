package fi.tuni.compse110.project.UIView.Scenes;

import fi.tuni.compse110.project.API.MaintenanceTask;
import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.API.RoadDataProvider;
import fi.tuni.compse110.project.Graph.GraphProvider;
import fi.tuni.compse110.project.UIView.UIController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
public class TrafficPageRoadScene extends Scene {

    VBox vLayout;
    Feed taskFeed;
    Region filler;
    HBox row;
    Pane graph;
    UIController controller;
    int roadNumber;

    public TrafficPageRoadScene(ScrollPane root, double v, double v1, UIController controller) {
        super(root, v, v1);
        this.controller = controller;

        vLayout = new VBox(20);
        // taskFeed = new Feed(new HashMap<>());
        row = new HBox();
        graph = new Pane();
        createContent();
        root.setContent(vLayout);
        roadNumber = 1;

    }

    public void createContent() {
        vLayout.getChildren().clear();
        row.getChildren().clear();
        graph.getChildren().clear();


        // testCase();
        ArrayList<Double> coords = new ArrayList<>(Arrays.asList(25.72088, 62.24147, 25.8, 62.3));

        List<MaintenanceTask> tasks = new ArrayList<>();
        try {
            tasks = RoadDataProvider.getMaintenanceData(coords, new ArrayList<>(), "", "");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (tasks.isEmpty()) {
            System.out.println("no tasks");
            MaintenanceTask t = new MaintenanceTask();
            t.setTasks(new ArrayList<>(Arrays.asList("no tasks")));
            t.setEndTime("2022-01-01T00:00:00Z");
            t.setStartTime("2022-01-01T00:00:00Z");
            tasks.add(t);
        }

        // vertical layout

        vLayout.setMinWidth(1024);
        vLayout.setAlignment(Pos.CENTER);
        vLayout.setId("background");

        Button backButton = new Button("<- back to menu");
        backButton.setPrefSize(120, 40);
        backButton.setOnAction(event -> backToMenuClickHandle());

        VBox mainContent = new VBox(20);
        vLayout.getChildren().add(backButton);

        row.setId("row");
        graph.setId("graph");

        /*
         * Here are some easily modifiable parameters to get different kinds of data,
         * These are useful especially for the begin-phase of the program
         */
        
        int sectionArrayListIndex = 3;
        UIController.Plottable wantedData = UIController.Plottable.ROAD_TEMPERATURE;
        String titleForChart = "Road:" + roadNumber + "  Section:" + sectionArrayListIndex;

        // load condition data for chart and feed
        List<RoadCondition> specificRCData = new ArrayList<>();
        try {
            for(int sectionIndex = 0; sectionIndex < 20 ; sectionIndex++){
                specificRCData.addAll(RoadDataProvider.getSpecificSectionRoadCondition(roadNumber, sectionIndex, coords));
            }
            ChartViewer dataChartViewer = GraphProvider.getRoadConditionChart(634, 500, specificRCData, wantedData,
                    titleForChart);
            graph.getChildren().add(dataChartViewer);
        } catch (Exception e) { // If there occurs any errors while creating the chart
            // from API data, creates a hardcoded chart to act as a placeholder
            // System.out.println("error");
            ChartViewer testChartViewer = GraphProvider.getTestChart(634, 500);
            graph.getChildren().add(testChartViewer);
        }

        // load roadcondition data for the feed
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

        Region filler = new Region();
        filler.setPrefWidth(50);

        VBox feed_window = new VBox(20);

        // feed navigation bar top
        HBox feed_navigation_bar = new HBox();
        Button previous_road = new Button("<--");
        previous_road.setId("title");

        previous_road.setOnAction(event -> {
            if (roadNumber - 1 >= 0){
                roadNumber = roadNumber - 1;
                createContent();
                controller.refresh();
            }
            
        });

        Button next_road = new Button("-->");
        next_road.setId("title");
        next_road.setOnAction(event -> {
            roadNumber = roadNumber + 1;
            createContent();
            controller.refresh();
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

        feed_window.setId("feed-window");
        feed_window.setAlignment(Pos.TOP_RIGHT);
        feed_window.getChildren().addAll(feed_navigation_bar, taskFeed.getElement(), feed_timerange_bar);

        // sidepanel
        VBox sidepanel = new VBox(20);
        sidepanel.setId("sidepanel");
        sidepanel.setPrefWidth(300);

        // text title with text "enter coordinates" followed by textfields for max and
        // min lat and lon coordinates
        VBox coordinate_input = new VBox(10);
        coordinate_input.setId("road-input");
        Text road_input_title = new Text("Enter road");
        road_input_title.setId("title");

        Text road_input = new Text("Road number");
        TextField road_input_field = new TextField();

        coordinate_input.getChildren().addAll(road_input_title, road_input, road_input_field);

        Text params_label = new Text("Choose parameters:");
        params_label.setId("title");

        VBox checkbox_stack = new VBox();
        checkbox_stack.setId("checkbox-stack");

        CheckBox precipitationCheckBox = new CheckBox("Precipitation");
        CheckBox winter_slipperiness_checkbox = new CheckBox("Winter slipperiness");
        CheckBox overall_road_condition_checkbox = new CheckBox("Overall road condition");
        CheckBox additional_info_checkbox = new CheckBox("Additional information");

        checkbox_stack.getChildren().addAll(precipitationCheckBox, winter_slipperiness_checkbox,
                overall_road_condition_checkbox, additional_info_checkbox);

        // big centered search button
        Button search_button = new Button("Search");
        search_button.setId("search-button");
        sidepanel.getChildren().addAll(coordinate_input, params_label, checkbox_stack, search_button);

        mainContent.getChildren().addAll(graph, feed_window);
        row.getChildren().addAll(mainContent, filler, sidepanel);

        vLayout.getChildren().addAll(row);

        // Possible nullPointerException throwing from .toExternalForm()
        this.getStylesheets().add(TrafficPageRoadScene.class.getResource("/stylesheet.css").toExternalForm());

    }

    /**
     * lambda to go handle back button going back to menu
     */
    private void backToMenuClickHandle() {
        // Stuff happening after the "back to menu" button click
        controller.fromAnyPageToMenu();
    }

    /*
     * public static void setRoot(String fxml) throws IOException {
     * scene.setRoot(loadFXML(fxml));
     * }
     */

    /*
     * private static Parent loadFXML(String fxml) throws IOException {
     * FXMLLoader fxmlLoader = new
     * FXMLLoader(TrafficPageScene.class.getResource(fxml + ".fxml"));
     * return fxmlLoader.load();
     * }
     */


}