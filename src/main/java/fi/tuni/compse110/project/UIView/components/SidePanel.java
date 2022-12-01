package fi.tuni.compse110.project.UIView.components;/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import fi.tuni.compse110.project.UIView.UIController;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;


abstract class CheckBoxes extends VBox{
    ArrayList<UIController.Plottable> getSelected(){
        ArrayList<UIController.Plottable> temp = new ArrayList<>();
        temp.add(UIController.Plottable.EMPTY);
        return temp;
    }
}
/**
 * The side panel VBOX used in all the scenes. Every scene uses this same class,
 * but can change the included fields according to the needs by giving different parameters
 * to the constructor ( feature to be added )
 */

public class SidePanel extends VBox {

    private CoordinateInput coordinateInput;
    private final Text params_label;
    private CheckBoxes checkbox_stack;

    private final Button search_button;
    private Text coordinate;
    private UIController controller;
    private static double LON_MIN = 21.3;
    private static double LON_MAX = 31.0;
    private static double LAT_MIN = 59.0;
    private static double LAT_MAX = 70.0;
    public SidePanel(double v, UIController controller, UIController.CurrentSceneEnum parentScene){
        super(v);
        this.controller = controller;

        this.setId("sidepanel");
        this.setPrefWidth(300);
        // text title with text "enter coordinates" followed by textfields for max and min lat and lon coordinates

        params_label = new Text("Choose parameters:");
        params_label.setId("title");

        // big centered search button
        search_button = new Button("Search");
        search_button.setOnAction(event->searchButtonClickHandle());
        search_button.setId("search-button");

        switch (parentScene){
            case TRAFFIC_SCENE:
                coordinateInput = new CoordinateInput(10);
                checkbox_stack = new RoadConditionCheckBoxes();

                break;
            case WEATHER_SCENE:
                coordinateInput = new CoordinateInput(10);
                checkbox_stack = new WeatherCheckBoxes();
                break;
        }

        this.getChildren().addAll(coordinateInput, params_label, checkbox_stack, search_button);
    }

    private static class CoordinateInput extends VBox{
        private final Text coordinate_input_title;
        private final VBox coordinate_input_fields;
        private final Text coordinate_input_max_lat;
        private final Text coordinate_input_min_lat;
        private final Text coordinate_input_max_lon;
        private final Text coordinate_input_min_lon;
        private final Slider coordinate_input_max_lat_field;
        private final Slider coordinate_input_min_lat_field;
        private final Slider coordinate_input_max_lon_field;
        private final Slider coordinate_input_min_lon_field;

        public CoordinateInput(double v){

            super(v);
            this.setId("coordinate-input");
            coordinate_input_title = new Text("Enter coordinates");
            coordinate_input_title.setId("title");
            coordinate_input_fields = new VBox(10);
            coordinate_input_fields.setId("coordinate-input-fields");
            
            coordinate_input_min_lat = new Text("Min lat");
            coordinate_input_min_lat.setId("title");
            coordinate_input_max_lon = new Text("Max lon");
            coordinate_input_max_lon.setId("title");
            coordinate_input_min_lon = new Text("Min lon");
            coordinate_input_min_lon.setId("title");


            // MAX LAT FIELD
            coordinate_input_max_lat = new Text("Max lat");
            coordinate_input_max_lat.setId("title");
            coordinate_input_max_lat_field = new Slider();
            coordinate_input_max_lat_field.setId("coordinate-input-field");
            coordinate_input_max_lat_field.setMin(LAT_MIN);
            coordinate_input_max_lat_field.setMax(LAT_MAX);
            coordinate_input_max_lat_field.valueProperty().addListener((observable, oldValue, newValue) -> {
                double rounded = Math.round(newValue.doubleValue() * 100.0) / 100.0;
                coordinate_input_max_lat.setText("Max lat: " + rounded);
            });

            // MIN LAT FIELD
            coordinate_input_min_lat_field = new Slider();
            coordinate_input_min_lat_field.setMin(LAT_MIN);
            coordinate_input_min_lat_field.setMax(LAT_MAX);
            coordinate_input_min_lat_field.setId("coordinate-input-field");
            coordinate_input_min_lat_field.valueProperty().addListener((observable, oldValue, newValue) -> {
                double rounded = Math.round(newValue.doubleValue() * 100.0) / 100.0;
                coordinate_input_min_lat.setText("Min lat: " + rounded);
            });

            // MAX LON FIELD
            coordinate_input_max_lon_field = new Slider();
            coordinate_input_max_lon_field.setMin(LON_MIN);
            coordinate_input_max_lon_field.setMax(LON_MAX);
            coordinate_input_max_lon_field.setId("coordinate-input-field");
            coordinate_input_max_lon_field.valueProperty().addListener((observable, oldValue, newValue) -> {
                double rounded = Math.round(newValue.doubleValue() * 100.0) / 100.0;
                coordinate_input_max_lon.setText("Max lon: " + rounded);
            });

            // MIN LON FIELD
            coordinate_input_min_lon_field = new Slider();
            coordinate_input_min_lon_field.setMin(LON_MIN);
            coordinate_input_min_lon_field.setMax(LON_MAX);
            coordinate_input_min_lon_field.setId("coordinate-input-field");
            coordinate_input_min_lon_field.valueProperty().addListener((observable, oldValue, newValue) -> {
                double rounded = Math.round(newValue.doubleValue() * 100.0) / 100.0;
                coordinate_input_min_lon.setText("Min lon: " + rounded);
            });

            coordinate_input_fields.getChildren().addAll(
                    coordinate_input_max_lat,
                    coordinate_input_max_lat_field,
                    coordinate_input_min_lat,
                    coordinate_input_min_lat_field,
                    coordinate_input_max_lon,
                    coordinate_input_max_lon_field,
                    coordinate_input_min_lon,
                    coordinate_input_min_lon_field
            );
            this.getChildren().addAll(
                    coordinate_input_title,
                    coordinate_input_fields
            );
        }

        public Double getMaxLat(){
            return coordinate_input_max_lat_field.getValue();
        }
        public Double getMinLat(){
            return coordinate_input_min_lat_field.getValue();
        }
        public Double getMaxLon(){
            return coordinate_input_max_lon_field.getValue();
        }
        public Double getMinLon(){
            return coordinate_input_min_lon_field.getValue();
        }
    }

    protected static class RoadConditionCheckBoxes extends CheckBoxes{
        private final CheckBox roadConditionCheckBox;
        private final CheckBox roadTemperatureCheckBox;
        private final CheckBox windSpeedCheckBox;
        private final CheckBox additional_info_checkbox;

        public RoadConditionCheckBoxes(){
            super();
            this.setId("checkbox-stack");

            roadConditionCheckBox = new CheckBox("Road Condition");
            roadTemperatureCheckBox = new CheckBox("Road temperature");
            windSpeedCheckBox = new CheckBox("Wind speed");
            additional_info_checkbox = new CheckBox("Additional information");

            this.getChildren().addAll(
                    roadConditionCheckBox,
                    roadTemperatureCheckBox,
                    windSpeedCheckBox,
                    additional_info_checkbox
            );

        }

        @Override
        public ArrayList<UIController.Plottable> getSelected(){
            ArrayList<UIController.Plottable> selected = new ArrayList<>();
            if (roadConditionCheckBox.isSelected()){
                selected.add(UIController.Plottable.OVERALL_ROAD_CONDITION);
            }
            if (roadTemperatureCheckBox.isSelected()){
                selected.add(UIController.Plottable.ROAD_TEMPERATURE);
            }
            if (windSpeedCheckBox.isSelected()){
                selected.add(UIController.Plottable.WIND_SPEED);
            }
            return selected;
        }
    }
    protected static class WeatherCheckBoxes extends CheckBoxes{
        private final CheckBox precipitationCheckBox;
        private final CheckBox winter_slipperiness_checkbox;
        private final CheckBox overall_road_condition_checkbox;
        private final CheckBox additional_info_checkbox;

        public WeatherCheckBoxes(){
            this.setId("checkbox-stack");

            precipitationCheckBox = new CheckBox("Precipitation");
            winter_slipperiness_checkbox = new CheckBox("Winter slipperiness");
            overall_road_condition_checkbox = new CheckBox("Overall road condition");
            additional_info_checkbox = new CheckBox("Additional information");

            this.getChildren().addAll(
                    precipitationCheckBox,
                    winter_slipperiness_checkbox,
                    overall_road_condition_checkbox,
                    additional_info_checkbox
            );

        }

        @Override
        public ArrayList<UIController.Plottable> getSelected(){
            ArrayList<UIController.Plottable> selected = new ArrayList<>();
            if (precipitationCheckBox.isSelected()){
                selected.add(UIController.Plottable.PRECIPITATION);
            }
            if (winter_slipperiness_checkbox.isSelected()){
                selected.add(UIController.Plottable.SLIPPERINESS);
            }
            if (overall_road_condition_checkbox.isSelected()){
                selected.add(UIController.Plottable.OVERALL_ROAD_CONDITION);
            }
            return selected;
        }
    }

    private void searchButtonClickHandle(){
        ArrayList<UIController.Plottable> selected = checkbox_stack.getSelected();

        ArrayList<Double> coords = new ArrayList<>();
        coords.add(coordinateInput.getMaxLat());
        coords.add(coordinateInput.getMinLat());
        coords.add(coordinateInput.getMaxLon());
        coords.add(coordinateInput.getMinLon());

        controller.searchButtonPressed(coords,selected);

    }
}
