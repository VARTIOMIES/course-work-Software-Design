package fi.tuni.compse110.project.UIView.Scenes;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.fx.ChartViewer;

import fi.tuni.compse110.project.UIView.UIController;
import fi.tuni.compse110.project.UIView.components.Feed;
import fi.tuni.compse110.project.API.MaintenanceTask;
import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.API.RoadDataProvider;
import fi.tuni.compse110.project.API.Utility;
import fi.tuni.compse110.project.Graph.GraphProvider;
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

public class CombinedScene extends Scene {

    VBox vLayout;
    Pane searchBar;
    HBox row;
    Pane graph;
    UIController controller;
    int timerange;

    public CombinedScene(ScrollPane root, double v, double v1, UIController controller) {
        super(root, v, v1);
        this.controller = controller;


        vLayout = new VBox(20);
        searchBar = new Pane();
        row = new HBox(20);
        graph = new Pane();
        createContent();
        root.setContent(vLayout);
        timerange = 0;
    }

    public void createContent() {
        vLayout.getChildren().clear();
        row.getChildren().clear();
        graph.getChildren().clear();
        searchBar.getChildren().clear();

        ArrayList<Double> coords = new ArrayList<>(Arrays.asList(25.72088, 62.24147, 25.8, 62.3));

        // vertical layout
        vLayout.setMinWidth(1024);
        vLayout.setAlignment(Pos.CENTER);
        vLayout.setId("background");

        Button backButton = new Button("<- back to menu");
        backButton.setPrefSize(120, 40);
        backButton.setOnAction(event -> backToMenuClickHandle());

        row.setId("row");

        /*
         * Here are some easily modifiable parameters to get different kinds of data,
         * These are useful especially for the begin-phase of the program
         */
        int roadNumber = 2;
        int sectionArrayListIndex = 3;
        ArrayList<UIController.Plottable> wantedData = new ArrayList<>();
        wantedData.add(UIController.Plottable.ROAD_TEMPERATURE);
        String titleForChart = "Road:" + roadNumber + "  Section:" + sectionArrayListIndex;

        // graph
        graph.setId("graph");
        graph.setMaxHeight(500);

        /*
         * Here are some easily modifiable parameters to get different kinds of data,
         * These are useful especially for the begin-phase of the program
         */
        List<RoadCondition> specificRCData = new ArrayList<>();
        try {
            // TODO: weather data to graph
            throw new Exception("");
            /*for (int sectionIndex = 0; sectionIndex < 20; sectionIndex++) {
                specificRCData
                        .addAll(RoadDataProvider.getSpecificSectionRoadCondition(roadNumber, sectionIndex, coords));
            }
            ChartViewer dataChartViewer = GraphProvider.getRoadConditionChart(664, 500, specificRCData, wantedData);
            graph.getChildren().add(dataChartViewer);*/
        } catch (Exception e) { // If there occurs any errors while creating the chart
            // from API data, creates a hardcoded chart to act as a placeholder
            System.out.println("test data");
            ChartViewer testChartViewer = GraphProvider.getTestChart(664, 500);
            graph.getChildren().add(testChartViewer);
        }

        VBox sidepanel = new VBox(20);
        sidepanel.setId("sidepanel");
        sidepanel.setPrefWidth(300);

        // text title with text "enter coordinates" followed by textfields for max and
        // min lat and lon coordinates
        VBox coordinate_input = new VBox(10);
        coordinate_input.setId("coordinate-input");
        Text coordinate_input_title = new Text("Enter coordinates");
        coordinate_input_title.setId("title");
        VBox coordinate_input_fields = new VBox(10);
        coordinate_input_fields.setId("coordinate-input-fields");
        Text coordinate_input_max_lat = new Text("Max lat");
        coordinate_input_max_lat.setId("title");
        Text coordinate_input_min_lat = new Text("Min lat");
        coordinate_input_min_lat.setId("title");
        Text coordinate_input_max_lon = new Text("Max lon");
        coordinate_input_max_lon.setId("title");
        Text coordinate_input_min_lon = new Text("Min lon");

        coordinate_input_min_lon.setId("title");
        TextField coordinate_input_max_lat_field = new TextField();
        coordinate_input_max_lat_field.setId("coordinate-input-field");
        TextField coordinate_input_min_lat_field = new TextField();
        coordinate_input_min_lat_field.setId("coordinate-input-field");
        TextField coordinate_input_max_lon_field = new TextField();
        coordinate_input_max_lon_field.setId("coordinate-input-field");
        TextField coordinate_input_min_lon_field = new TextField();
        coordinate_input_min_lon_field.setId("coordinate-input-field");
        coordinate_input_fields.getChildren().addAll(coordinate_input_max_lat, coordinate_input_max_lat_field,
                coordinate_input_min_lat, coordinate_input_min_lat_field, coordinate_input_max_lon,
                coordinate_input_max_lon_field, coordinate_input_min_lon, coordinate_input_min_lon_field);
        coordinate_input.getChildren().addAll(coordinate_input_title, coordinate_input_fields);

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

        Button search_button = new Button("Search");
        search_button.setId("search-button");
        sidepanel.getChildren().addAll(coordinate_input, params_label, checkbox_stack, search_button);

        // maintenance feed
        Map<ArrayList<String>, ArrayList<String>> task_list = new HashMap<>();
        // get necessary data for feed
        try {

            // parse date from timerange
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(new java.util.Date());

            // set hour to selected timerange and set minutes to 0
            cal.set(java.util.Calendar.HOUR_OF_DAY, timerange);
            cal.set(java.util.Calendar.MINUTE, 0);

            var begin = Utility.dateFormatter(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH),
                    cal.get(java.util.Calendar.DAY_OF_MONTH),
                    cal.get(java.util.Calendar.HOUR_OF_DAY), cal.get(java.util.Calendar.MINUTE));
            cal.set(java.util.Calendar.HOUR_OF_DAY, timerange + 1);
            var end = Utility.dateFormatter(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH),
                    cal.get(java.util.Calendar.DAY_OF_MONTH),
                    cal.get(java.util.Calendar.HOUR_OF_DAY), cal.get(java.util.Calendar.MINUTE));

            // get road data
            List<MaintenanceTask> maintenanceTasks = RoadDataProvider.getMaintenanceData(coords, new ArrayList<>(),
                    begin, end);

            // parse data for feed
            for (var task : maintenanceTasks) {
                task_list.put(task.getTasks(), new ArrayList<String>(Arrays.asList(
                        task.getStartTime() + " - " + task.getEndTime(),
                        task.getSource())));
            }

        } catch (IOException e) {
            task_list.put(new ArrayList<String>(Arrays.asList("No data for selected timerange")),
                    new ArrayList<String>(Arrays.asList("")));
            System.out.println(e);

        }

        Text maintenance_feed_title = new Text("Maintenance feed");
        maintenance_feed_title.setId("title");
        Feed taskFeed = new Feed(task_list);
        VBox mainContent = new VBox(10);

        Text road_data_title = new Text("Road data feed");
        road_data_title.setId("title");

        Map<ArrayList<String>, ArrayList<String>> road_data_list = new HashMap<>();

        // get necessary data for road data feed

        if (!specificRCData.isEmpty()) {
            // parse data for feed
            for (var condition : specificRCData) {
                road_data_list.put(new ArrayList<String>(Arrays.asList("Section " + condition.getSection())),
                        new ArrayList<String>(Arrays.asList(
                                "Precipitation: " + condition.getPrecipitationCondition(),
                                "Overall road condition: " + condition.getOverallRoadCondition(),
                                "Road condition: " + condition.getRoadCondition())));
            }
        } else {
            road_data_list.put(new ArrayList<String>(Arrays.asList("No data for selected timerange")),
                    new ArrayList<String>(Arrays.asList("")));
        }

        Feed roadDataFeed = new Feed(road_data_list);

        // timerange bar top
        HBox feed_navigation_bar = new HBox();
        Button previous_time = new Button("<--");
        previous_time.setId("title");
        previous_time.setOnAction(e -> {
            if (timerange - 1 >= 0) {
                timerange -= 1;
                createContent();
                controller.refresh();
            }
        });
        Button next_time = new Button("-->");
        next_time.setId("title");
        next_time.setOnAction(e -> {
            if (timerange + 1 < 24) {
                timerange += 1;
                createContent();
                controller.refresh();
            }
        });
        Text timerange_text = new Text("Time range " + timerange + "h to " + (timerange + 1) + "h");
        timerange_text.setId("title");
        feed_navigation_bar.setSpacing(30);

        Region fill_top = new Region();
        fill_top.setPrefWidth(90);
        feed_navigation_bar.getChildren().addAll(fill_top, previous_time, timerange_text, next_time);

        mainContent.getChildren().addAll(feed_navigation_bar, graph, maintenance_feed_title, taskFeed.getElement(),
                road_data_title, roadDataFeed.getElement());
        row.getChildren().addAll(mainContent, sidepanel);
        vLayout.getChildren().addAll(backButton, row);

        // Possible nullPointerException throwing from .toExternalForm()
        this.getStylesheets().add(WeatherPageScene.class.getResource("/stylesheet.css").toExternalForm());

    }

    /**
     * lambda to go handle back button going back to menu
     */
    private void backToMenuClickHandle() {
        // Stuff happening after the "back to menu" button click
        controller.fromAnyPageToMenu();
    }
}
