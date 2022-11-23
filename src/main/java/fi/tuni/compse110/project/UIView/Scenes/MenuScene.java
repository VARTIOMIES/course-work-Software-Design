package fi.tuni.compse110.project.UIView.Scenes;


import fi.tuni.compse110.project.UIView.UIController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

/**
 * A SCENE
 */
public class MenuScene extends Scene {

    private HBox buttonsContainer;
    private Button pointButton;
    private Button roadButton;
    private Button weatherButton;
    private Button combinedButton;
    private UIController controller;


    public MenuScene(Pane root, UIController controller) {
        super(root);
        this.controller = controller;

        buttonsContainer = new HBox();

        // Create buttons
        pointButton = new Button("Select a Point");
        pointButton.setPrefSize(150,250);
        pointButton.setOnAction(event -> buttonSelectPointClicked());

        roadButton = new Button("Select a Road");
        roadButton.setPrefSize(150,250);
        roadButton.setOnAction(event->buttonSelectRoadClicked());

        weatherButton = new Button("Weather");
        weatherButton.setPrefSize(150,250);
        weatherButton.setOnAction(event->buttonWeatherClicked());

        combinedButton = new Button("Combined");
        combinedButton.setPrefSize(150,250);
        combinedButton.setOnAction(event->buttonCombinedClicked());

        buttonsContainer.getChildren().addAll(pointButton,roadButton,weatherButton,combinedButton);
        root.getChildren().add(buttonsContainer);

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

}