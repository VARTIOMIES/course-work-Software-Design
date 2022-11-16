package fi.tuni.compse110.project.UIView;/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import fi.tuni.compse110.project.UIView.Scenes.MenuScene;
import fi.tuni.compse110.project.UIView.Scenes.TrafficPageScene;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This is the class that controls the one Stage of the application.
 *
 * With this classes methods you can switch between the different scenes
 */
public class UIController{

    private Stage stage;
    private Scene currentScene;
    private ScrollPane trafficSceneRoot;
    private Pane menuPane;
    private Group root;
    private MenuScene menuScene;
    private TrafficPageScene trafficScene;


    public UIController(Stage stage){
        this.stage = stage;
        stage.setResizable(false);

        trafficSceneRoot = new ScrollPane();
        menuPane = new Pane();
        menuPane.setPrefSize(1024,720);
        root = new Group();
        menuScene = new MenuScene(menuPane,this);
        trafficScene = new TrafficPageScene(trafficSceneRoot,1024,720,this);

        stage.setScene(menuScene);
        stage.show();
    }

    public void fromMenuToTrafficPage(){
        currentScene = trafficScene;
        refresh();
    }
    public void fromTrafficPageToMenu(){
        currentScene = menuScene;
        refresh();
    }

    /**
     * Used to put the changes visible (a little useless atm but maybe will
     * become usefull later on as more functionalities are added).
     */
    public void refresh(){
        stage.setScene(currentScene);
        stage.show();
    }

}
