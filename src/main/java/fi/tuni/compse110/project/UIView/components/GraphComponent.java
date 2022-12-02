package fi.tuni.compse110.project.UIView.components;/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.Graph.GraphProvider;
import fi.tuni.compse110.project.UIView.UIController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jfree.chart.fx.ChartViewer;

import java.util.ArrayList;
import java.util.TreeMap;

public class GraphComponent extends VBox{
    private TreeMap<Integer,
            TreeMap<String,
                    ArrayList<RoadCondition>>> conditions;
    private ArrayList<String> sections;
    private ArrayList<Integer> roads;
    private Pane chart_area;
    private double width;
    private double height;
    private HBox graph_section_selection_buttons;
    private ArrayList<UIController.Plottable> wantedData;

    private boolean empty;

    private int section_index;
    private Button next_section;
    private Button previous_section;
    private Text current_section;

    private int road_index;
    private Button next_road;
    private Button previous_road;
    private Text current_road;

    public GraphComponent(double width,
                   double height,
                          UIController.CurrentSceneEnum currentScene){
        super();
        this.width = width;
        this.height = height;
        conditions = new TreeMap<>();
        section_index = 0;
        empty = true;

        chart_area = new Pane();
        chart_area.setPrefSize(634,500);
        String info_text = "Nothing to show yet :(";

        Label emptytext = new Label(info_text);
        emptytext.setAlignment(Pos.CENTER);
        emptytext.minWidthProperty().bind(chart_area.widthProperty());
        emptytext.minHeightProperty().bind(chart_area.heightProperty());

        chart_area.getChildren().setAll(emptytext);

        graph_section_selection_buttons = new HBox();
        graph_section_selection_buttons.setAlignment(Pos.CENTER);
        initButtons();



        switch (currentScene){
            case TRAFFIC_SCENE_ROAD:
                graph_section_selection_buttons.getChildren().addAll(
                        previous_section,
                        current_section,
                        next_section
                );
                break;

            case TRAFFIC_SCENE:
                graph_section_selection_buttons.getChildren().addAll(
                        previous_road,
                        current_road,
                        next_road,
                        previous_section,
                        current_section,
                        next_section
                );
                break;
        }



        this.getChildren().addAll(chart_area,graph_section_selection_buttons);

    }
    private void initButtons(){
        previous_section = new Button("<--");
        previous_section.setId("title");
        previous_section.setOnAction(event -> {
            if (section_index - 1 >= 0){
                section_index--;
                current_section.setText(sections.get(section_index));
                drawNewChart();
                if (next_section.isDisabled()){
                    next_section.setDisable(false);
                }

            }else{
                previous_section.setDisable(true);
            }

        });

        next_section = new Button("-->");
        next_section.setId("title");
        next_section.setOnAction(event->{
            if (section_index + 1 < sections.size()){
                section_index++;
                current_section.setText(sections.get(section_index));
                drawNewChart();
                if (previous_section.isDisabled()){
                    previous_section.setDisable(false);
                }

            }else{
                next_section.setDisable(true);
            }
        });

        current_section = new Text("Section:");
        current_section.setId("title");

        previous_road = new Button("<--");
        previous_road.setId("title");
        previous_road.setOnAction(event -> {
            if (road_index - 1 >= 0){
                road_index--;
                current_road.setText(roads.get(road_index).toString());
                section_index = 0;

                change_section_array();
                drawNewChart();
                if (next_road.isDisabled()){
                    next_road.setDisable(false);
                }


            }else{
                previous_road.setDisable(true);
            }

        });

        next_road = new Button("-->");
        next_road.setId("title");
        next_road.setOnAction(event->{
            if (road_index + 1 < roads.size()){
                road_index++;
                current_road.setText(roads.get(road_index).toString());
                section_index = 0;

                change_section_array();
                drawNewChart();
                if (previous_road.isDisabled()){
                    previous_road.setDisable(false);
                }


            }else{
                next_road.setDisable(true);
            }
        });

        current_road = new Text("Road:");
        current_road.setId("title");

    }

    private void drawNewChart(){
        ChartViewer chartviewer = GraphProvider.getRoadConditionChart(
                width,
                height,
                conditions.get(roads.get(road_index)).get(sections.get(section_index)),
                wantedData
        );
        chart_area.getChildren().setAll(chartviewer);
    }
    private void change_section_array(){
        sections = new ArrayList<>(conditions.get(roads.get(road_index)).keySet());
    }

    public void give_data(TreeMap<Integer,TreeMap<String,ArrayList<RoadCondition>>> data,
                          ArrayList<UIController.Plottable> plottables){
        wantedData = plottables;
        conditions = data;

        section_index = 0;
        road_index = 0;

        roads = new ArrayList<>(data.keySet());

        sections = new ArrayList<>(data.get(roads.get(road_index)).keySet());

        drawNewChart();

        current_section.setText(sections.get(section_index));
        current_road.setText(roads.get(road_index).toString());
        next_section.setDisable(false);
        previous_section.setDisable(true);
        next_road.setDisable(false);
        previous_road.setDisable(true);

    }


}
