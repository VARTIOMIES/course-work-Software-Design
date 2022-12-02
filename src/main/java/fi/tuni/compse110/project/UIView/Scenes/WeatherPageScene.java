package fi.tuni.compse110.project.UIView.Scenes;

import java.util.*;

import fi.tuni.compse110.project.API.WeatherData;
import fi.tuni.compse110.project.UIView.components.GraphComponent;
import fi.tuni.compse110.project.UIView.components.SidePanel;
import javafx.scene.layout.Region;
import org.jfree.chart.fx.ChartViewer;

import fi.tuni.compse110.project.UIView.UIController;
import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.API.RoadDataProvider;
import fi.tuni.compse110.project.Graph.GraphProvider;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class WeatherPageScene extends Scene{

    // The main container
    private VBox rootVerticalContainer;
    // Is inside the VBox
    private HBox horizontalrootElementContainer;


    private SidePanel sidepanel;
    private VBox mainContent;

    private Region filler;

    private UIController controller;
    private GraphComponent graph;

    private ArrayList<UIController.Plottable> wantedData;
    private TreeMap<Integer, TreeMap<String,ArrayList<WeatherData>>> data;


    public WeatherPageScene(ScrollPane root,
                            double v,
                            double v1,
                            UIController controller) {
        super(root,v,v1);
        root.setFitToWidth(true);
        root.setPannable(false);
        this.controller = controller;

        mainContent = new VBox(20);

        rootVerticalContainer = new VBox(20);
        rootVerticalContainer.setMinWidth(1024);
        rootVerticalContainer.setAlignment(Pos.CENTER);
        rootVerticalContainer.setId("background");

        horizontalrootElementContainer = new HBox();
        horizontalrootElementContainer.setId("row");


        Button backButton = new Button("<- back to menu");
        backButton.setPrefSize(120,40);
        backButton.setOnAction(event->backToMenuClickHandle());
        rootVerticalContainer.getChildren().add(backButton);

        graph = new GraphComponent(
                634,
                500,
                UIController.CurrentSceneEnum.WEATHER_SCENE);
        graph.setId("graph");

        wantedData = new ArrayList<>();

        mainContent.getChildren().addAll(graph);

        filler = new Region();
        filler.setPrefWidth(50);
        sidepanel = new SidePanel(20,
                this.controller,
                UIController.CurrentSceneEnum.WEATHER_SCENE
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
        //graph.give_data(this.data,this.wantedData);
    }
}

