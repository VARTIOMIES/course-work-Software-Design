package fi.tuni.compse110.project.UIView.Scenes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.fx.ChartViewer;

import fi.tuni.compse110.project.UIView.UIController;
import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.API.RoadDataProvider;
import fi.tuni.compse110.project.Graph.GraphProvider;
import fi.tuni.compse110.project.components.Feed;
import javafx.geometry.Insets;
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

public class WeatherPageScene extends Scene{

    VBox vLayout;
    Pane searchBar;
    HBox row;
    Pane graph;
    UIController controller;

    public WeatherPageScene(ScrollPane root,double v,double v1,UIController controller) {
        super(root,v,v1);
        this.controller = controller;
        vLayout = new VBox(20);
        searchBar = new Pane();
        row = new HBox(20);
        graph = new Pane();
        createContent();
        root.setContent(vLayout);
    }

    public void createContent(){

        ArrayList<Double> coords = new ArrayList<>(Arrays.asList(25.72088, 62.24147, 25.8, 62.3));

        // vertical layout
        vLayout.setMinWidth(1024);
        vLayout.setAlignment(Pos.CENTER);
        vLayout.setId("background");

        Button backButton = new Button("<- back to menu");
        backButton.setPrefSize(120,40);
        backButton.setOnAction(event->backToMenuClickHandle());



        row.setId("row");

        /*
         * Here are some easily modifiable parameters to get different kinds of data,
         * These are useful especially for the begin-phase of the program
         */
        int roadNumber = 5;
        int sectionArrayListIndex = 3;
        GraphProvider.Plottable wantedData = GraphProvider.Plottable.ROAD_TEMPERATURE;
        String titleForChart = "Road:" + roadNumber + "  Section:" + sectionArrayListIndex;



        // graph
        graph.setId("graph");
        graph.setMaxHeight(500);

        /*
            Here are some easily modifiable parameters to get different kinds of data,
            These are useful especially for the begin-phase of the program
         */


        try {
            // TODO: weather data to graph
            
            List<RoadCondition> specificRCData = RoadDataProvider.getSpecificSectionRoadCondition(roadNumber,sectionArrayListIndex,coords);
            ChartViewer dataChartViewer = GraphProvider.getRoadConditionChart(664,500,specificRCData, wantedData,titleForChart);
            graph.getChildren().add(dataChartViewer);
        }
        catch (Exception e){ // If there occurs any errors while creating the chart
            // from API data, creates a hardcoded chart to act as a placeholder
            System.out.println("test data");
            ChartViewer testChartViewer = GraphProvider.getTestChart(500,400);
            graph.getChildren().add(testChartViewer);
        }

        VBox sidepanel = new VBox(20);
        sidepanel.setId("sidepanel");
        sidepanel.setPrefWidth(300);

        // text title with text "enter coordinates" followed by textfields for max and min lat and lon coordinates
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
        coordinate_input_fields.getChildren().addAll(coordinate_input_max_lat, coordinate_input_max_lat_field, coordinate_input_min_lat, coordinate_input_min_lat_field, coordinate_input_max_lon, coordinate_input_max_lon_field, coordinate_input_min_lon, coordinate_input_min_lon_field);
        coordinate_input.getChildren().addAll(coordinate_input_title, coordinate_input_fields);
        

        Text params_label = new Text("Choose parameters:");
        params_label.setId("title");

        VBox checkbox_stack = new VBox();
        checkbox_stack.setId("checkbox-stack");

        CheckBox precipitationCheckBox = new CheckBox("Precipitation");
        CheckBox winter_slipperiness_checkbox = new CheckBox("Winter slipperiness");
        CheckBox overall_road_condition_checkbox = new CheckBox("Overall road condition");
        CheckBox additional_info_checkbox = new CheckBox("Additional information");

        checkbox_stack.getChildren().addAll(precipitationCheckBox, winter_slipperiness_checkbox, overall_road_condition_checkbox, additional_info_checkbox);


        // big centered search button
        Button search_button = new Button("Search");
        search_button.setId("search-button");
        sidepanel.getChildren().addAll(coordinate_input, params_label, checkbox_stack, search_button);


        

        row.getChildren().addAll(graph, sidepanel);

        vLayout.getChildren().addAll(backButton, row);

        // Possible nullPointerException throwing from .toExternalForm()
        this.getStylesheets().add(WeatherPageScene.class.getResource("/stylesheet.css").toExternalForm());

    }


    /**
     * lambda to go handle back button going back to menu
     */
    private void backToMenuClickHandle(){
        // Stuff happening after the "back to menu" button click
        controller.fromAnyPageToMenu();
    }
}
