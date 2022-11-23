package fi.tuni.compse110.project.UIView.components;/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import fi.tuni.compse110.project.UIView.UIController;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SidePanel extends VBox {

    private CoordinateInput coordinateInput;
    private final Text params_label;
    private CheckBoxes checkbox_stack;

    private final Button search_button;
    private Text coordinate;
    private UIController controller;

    public SidePanel(double v,UIController controller){
        super(v);
        this.controller = controller;
        this.setId("sidepanel");
        this.setPrefWidth(300);
        // text title with text "enter coordinates" followed by textfields for max and min lat and lon coordinates
        coordinateInput = new CoordinateInput(10);

        params_label = new Text("Choose parameters:");
        params_label.setId("title");

        checkbox_stack = new CheckBoxes();


        // big centered search button
        search_button = new Button("Search");
        search_button.setOnAction(event->searchButtonClickHandle());
        search_button.setId("search-button");
        this.getChildren().addAll(coordinateInput, params_label, checkbox_stack, search_button);
    }

    private static class CoordinateInput extends VBox{
        private final Text coordinate_input_title;
        private final VBox coordinate_input_fields;
        private final Text coordinate_input_max_lat;
        private final Text coordinate_input_min_lat;
        private final Text coordinate_input_max_lon;
        private final Text coordinate_input_min_lon;
        private final TextField coordinate_input_max_lat_field;
        private final TextField coordinate_input_min_lat_field;
        private final TextField coordinate_input_max_lon_field;
        private final TextField coordinate_input_min_lon_field;

        public CoordinateInput(double v){
            super(v);
            this.setId("coordinate-input");
            coordinate_input_title = new Text("Enter coordinates");
            coordinate_input_title.setId("title");
            coordinate_input_fields = new VBox(10);
            coordinate_input_fields.setId("coordinate-input-fields");
            coordinate_input_max_lat = new Text("Max lat");
            coordinate_input_max_lat.setId("title");
            coordinate_input_min_lat = new Text("Min lat");
            coordinate_input_min_lat.setId("title");
            coordinate_input_max_lon = new Text("Max lon");
            coordinate_input_max_lon.setId("title");
            coordinate_input_min_lon = new Text("Min lon");
            coordinate_input_min_lon.setId("title");
            coordinate_input_max_lat_field = new TextField();
            coordinate_input_max_lat_field.setId("coordinate-input-field");
            coordinate_input_min_lat_field = new TextField();
            coordinate_input_min_lat_field.setId("coordinate-input-field");
            coordinate_input_max_lon_field = new TextField();
            coordinate_input_max_lon_field.setId("coordinate-input-field");
            coordinate_input_min_lon_field = new TextField();
            coordinate_input_min_lon_field.setId("coordinate-input-field");

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
            return Double.parseDouble(coordinate_input_max_lat_field.getText());
        }
        public Double getMinLat(){
            return Double.parseDouble(coordinate_input_min_lat_field.getText());
        }
        public Double getMaxLon(){
            return Double.parseDouble(coordinate_input_max_lon_field.getText());
        }
        public Double getMinLon(){
            return Double.parseDouble(coordinate_input_min_lon_field.getText());
        }
    }

    protected static class CheckBoxes extends VBox{
        private final CheckBox precipitationCheckBox;
        private final CheckBox winter_slipperiness_checkbox;
        private final CheckBox overall_road_condition_checkbox;
        private final CheckBox additional_info_checkbox;

        public CheckBoxes(){
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
