package fi.tuni.compse110.project.UIView;/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import fi.tuni.compse110.project.UIView.Scenes.MenuScene;
import fi.tuni.compse110.project.UIView.Scenes.TrafficPageRoadScene;
import fi.tuni.compse110.project.UIView.Scenes.TrafficPageScene;
import fi.tuni.compse110.project.UIView.Scenes.WeatherPageScene;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * This is the class that controls the one Stage of the application.
 *
 * With this classes methods you can switch between the different scenes
 */
public class UIController{

    private Stage stage;
    private Scene currentScene;
    private ScrollPane trafficSceneRoot;
    private ScrollPane trafficRoadSceneRoot;
    private ScrollPane weatherSceneRoot;
    private Pane menuPane;
    private Group root;
    private MenuScene menuScene;
    private TrafficPageScene trafficScene;
    private TrafficPageRoadScene trafficSceneRoad;
    private WeatherPageScene weatherScene;

    private enum CurrentSceneEnum{
        MENU_SCENE,
        TRAFFIC_SCENE,
        TRAFFIC_SCENE_ROAD,
        WEATHER_SCENE
    }
    private CurrentSceneEnum currentSceneEnum;

    public UIController(Stage stage){
        this.stage = stage;
        stage.setResizable(false);

        trafficSceneRoot = new ScrollPane();
        trafficRoadSceneRoot = new ScrollPane();
        weatherSceneRoot = new ScrollPane();
        menuPane = new Pane();
        menuPane.setPrefSize(1024,720);
        root = new Group();
        menuScene = new MenuScene(menuPane,this);
        trafficScene = new TrafficPageScene(trafficSceneRoot,1024,720,this);
        trafficSceneRoad = new TrafficPageRoadScene(trafficRoadSceneRoot, 1024, 720, this);
        weatherScene = new WeatherPageScene(weatherSceneRoot,1024,720,this);

        stage.setScene(menuScene);
        currentSceneEnum = CurrentSceneEnum.MENU_SCENE;
        stage.show();
    }

    public void fromMenuToTrafficPage(){
        currentScene = trafficScene;
        currentSceneEnum = CurrentSceneEnum.TRAFFIC_SCENE;
        refresh();
    }

    public void fromMenuToTrafficPageRoad(){
        currentScene = trafficSceneRoad;
        currentSceneEnum = CurrentSceneEnum.TRAFFIC_SCENE_ROAD;
        refresh();

    }
    public void fromMenuToWeatherPage(){
        currentScene = weatherScene;
        currentSceneEnum = CurrentSceneEnum.WEATHER_SCENE;
        refresh();
    }
    public void fromAnyPageToMenu(){
        currentScene = menuScene;
        currentSceneEnum = CurrentSceneEnum.MENU_SCENE;
        refresh();
    }
    public void searchButtonPressed(ArrayList<Double> coords,
                                    ArrayList<Plottable> selected){
        switch (currentSceneEnum){
            case MENU_SCENE:
                break;
            case TRAFFIC_SCENE:
                trafficScene.makeNewChartViewer(coords,selected);
                break;
            case WEATHER_SCENE:
                break;
            case TRAFFIC_SCENE_ROAD:
                break;
        }
    }


    /**
     * Used to put the changes visible (a little useless atm but maybe will
     * become usefull later on as more functionalities are added).
     */
    public void refresh(){
        stage.setScene(currentScene);
        stage.show();
    }

    /**
     * All the different plottable data types.
     */
    public enum Plottable {
        PRECIPITATION,
        SLIPPERINESS,
        OVERALL_ROAD_CONDITION,
        ROAD_TEMPERATURE,
        TEMPERATURE,
        WIND_SPEED
    }

}
