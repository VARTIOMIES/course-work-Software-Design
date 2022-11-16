package fi.tuni.compse110.project.UIView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout.Alignment;

import javafx.scene.Parent;
import org.jfree.chart.fx.ChartViewer;

import fi.tuni.compse110.project.API.MaintenanceTask;
import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.API.RoadDataProvider;
import fi.tuni.compse110.project.Graph.GraphProvider;
import fi.tuni.compse110.project.components.Feed;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WeatherPageScene extends Scene{

    VBox vLayout;
    Pane searchBar;
    Feed taskFeed;
    Region filler;
    HBox row;
    Pane graph;
    UIController controller;

    public WeatherPageScene(ScrollPane root,double v,double v1,UIController controller) {
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

    public void stuff(){

        ArrayList<Double> coords = new ArrayList<>(Arrays.asList(25.72088, 62.24147, 25.8, 62.3));

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
         * Here are some easily modifiable parameters to get different kinds of data,
         * These are useful especially for the begin-phase of the program
         */
        int roadNumber = 5;
        int sectionArrayListIndex = 3;
        GraphProvider.Plottable wantedData = GraphProvider.Plottable.ROAD_TEMPERATURE;
        String titleForChart = "Road:" + roadNumber + "  Section:" + sectionArrayListIndex;

        Map<ArrayList<String>, ArrayList<String>> task_list = new HashMap<>();

        VBox feed_window = new VBox(20);

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

        // example data to feed
        task_list.put(new ArrayList<String>() {
            {
                add("Example title");
            }
        }, new ArrayList<String>() {
            {
                add("Add weatherdata here when its providable");
            }
        });
        task_list.put(new ArrayList<String>() {
            {
                add("Second example");
            }
        }, new ArrayList<String>() {
            {
                add("Add weatherdata here when its providable");
            }
        });
        Feed taskFeed = new Feed(task_list);

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

        // space between feed and sidepanel
        Region filler = new Region();
        filler.setPrefWidth(95);

        VBox sidepanel = new VBox();
        sidepanel.setId("sidepanel");
        sidepanel.setSpacing(20);
        sidepanel.setPrefWidth(300);

        Text params_label = new Text("Choose parameters:");
        params_label.setId("title");

        VBox checkbox_stack = new VBox();
        checkbox_stack.setId("checkbox-stack");
        CheckBox precipitationCheckBox = new CheckBox("Precipitation");
        CheckBox winter_slipperiness_checkbox = new CheckBox("Winter slipperiness");
        CheckBox overall_road_condition_checkbox = new CheckBox("Overall road condition");
        CheckBox additional_info_checkbox = new CheckBox("Additional information");

        checkbox_stack.getChildren().addAll(precipitationCheckBox, winter_slipperiness_checkbox, overall_road_condition_checkbox, additional_info_checkbox);
        
        Text visualize_label = new Text("Visualize:");
        visualize_label.setId("title");

        Pane small_graph = new Pane();
        small_graph.setId("graph");
        small_graph.setPrefSize(200, 200);
        
        sidepanel.getChildren().addAll(params_label, checkbox_stack, visualize_label, small_graph);


        row.getChildren().addAll(feed_window, filler, sidepanel);

        vLayout.getChildren().addAll(searchBar, row);

        // Possible nullPointerException throwing from .toExternalForm()
        this.getStylesheets().add(WeatherPageScene.class.getResource("/stylesheet.css").toExternalForm());

    }
}
