package fi.tuni.compse110.project.UIView.Scenes;

import fi.tuni.compse110.project.UIView.UIController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * A SCENE
 */
public class MenuScene extends Scene {

    private VBox buttonsContainer;
    private Button pointButton;
    private Button roadButton;
    private Button weatherButton;
    private Button combinedButton;
    private Button extraStuff;
    private UIController controller;
    private HBox row;

    public MenuScene(Pane root, UIController controller) {
        super(root);
        root.setPrefSize(400, 300);
        this.controller = controller;

        row = new HBox();
        buttonsContainer = new VBox(10);

        Region region = new Region();
        Region region2 = new Region();

        region.setPrefWidth(45);
        region2.setPrefHeight(30);

        // Create buttons
        pointButton = new Button("Select a Point");
        pointButton.setPrefSize(300, 50);
        pointButton.setOnAction(event -> buttonSelectPointClicked());
        pointButton.setId("menubutton");

        roadButton = new Button("Select a Road");
        roadButton.setPrefSize(300, 50);
        roadButton.setOnAction(event->buttonSelectRoadClicked());
        roadButton.setId("menubutton");

        weatherButton = new Button("Weather");
        weatherButton.setPrefSize(300, 50);
        weatherButton.setOnAction(event->buttonWeatherClicked());
        weatherButton.setId("menubutton");

        combinedButton = new Button("Combined");
        combinedButton.setPrefSize(300, 50);
        combinedButton.setOnAction(event->buttonCombinedClicked());
        combinedButton.setId("menubutton");

        extraStuff = new Button("Extra Stuff");
        extraStuff.setPrefSize(150,250);
        extraStuff.setOnAction(event->extraStuffClicked());

        buttonsContainer.getChildren().addAll(region2, pointButton,roadButton,weatherButton,combinedButton, extraStuff);
        row.getChildren().addAll(region, buttonsContainer);
        root.getChildren().addAll(row);

    }

    /**
     * Lamda function for button click handling
     */
    private void buttonSelectPointClicked(){
        // Stuff that happens after "Select a point" -button is clicked
        controller.fromMenuToTrafficPage();
    }
    
    /**
     * Lamda function for button click handling
     */
    private void buttonSelectRoadClicked(){
        // Stuff that happens after "Select a road" -button is clicked
        controller.fromMenuToTrafficPageRoad();

    }
    private void buttonWeatherClicked(){
        // Stuff that happens after "Weather" -button is clicked
        controller.fromMenuToWeatherPage();
    }

    private void buttonCombinedClicked(){
        controller.fromMenuToCombinedPage();
    }

    private void extraStuffClicked(){
        // Stuff that happens after "Weather" -button is clicked
        controller.fromMenuToExtraStuffPage();
    }

}