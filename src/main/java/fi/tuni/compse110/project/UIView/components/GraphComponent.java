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
    private TreeMap<String,ArrayList<RoadCondition>> section_conditions;
    private ArrayList<String> sections;
    private Pane chart_area;
    private double width;
    private double height;
    private HBox graph_section_selection_buttons;
    private ArrayList<UIController.Plottable> wantedData;
    private int section_index;
    private boolean empty;
    private Button next_section;
    private Button previous_section;
    private Text current_section;

    public GraphComponent(double width,
                   double height){
        super();
        this.width = width;
        this.height = height;
        section_conditions = new TreeMap<>();
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
        graph_section_selection_buttons.getChildren().addAll(
                previous_section,
                current_section,
                next_section
        );

        this.getChildren().addAll(chart_area,graph_section_selection_buttons);

    }
    private void drawNewChart(){
        ChartViewer chartviewer = GraphProvider.getRoadConditionChart(
                width,
                height,
                section_conditions.get(sections.get(section_index)),
                wantedData
        );
        chart_area.getChildren().setAll(chartviewer);
    }

    public void give_data(TreeMap<String,ArrayList<RoadCondition>> data,
                          ArrayList<UIController.Plottable> plottables){
        wantedData = plottables;
        section_conditions = data;

        section_index = 0;

        sections = new ArrayList<>(section_conditions.keySet());

        drawNewChart();

        current_section.setText(sections.get(section_index));
        next_section.setDisable(false);
        previous_section.setDisable(true);

    }


}
