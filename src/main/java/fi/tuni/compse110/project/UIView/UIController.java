package fi.tuni.compse110.project.UIView;/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import fi.tuni.compse110.project.UIView.Scenes.CombinedScene;
import fi.tuni.compse110.project.UIView.Scenes.MenuScene;
import fi.tuni.compse110.project.UIView.Scenes.RoadCameraImageScene;
import fi.tuni.compse110.project.UIView.Scenes.TrafficPageRoadScene;
import fi.tuni.compse110.project.UIView.Scenes.TrafficPageScene;
import fi.tuni.compse110.project.UIView.Scenes.WeatherPageScene;
import java.io.IOException;
//import fi.tuni.compse110.project.UIView.Scenes.WeatherPageScene;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
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
    private ScrollPane combinedSceneRoot;
    private ScrollPane extraStuffSceneRoot;
    private Pane menuPane;
    private Group root;
    private MenuScene menuScene;
    private TrafficPageScene trafficScene;
    private TrafficPageRoadScene trafficSceneRoad;
    //private WeatherPageScene weatherScene;
    private CombinedScene combinedScene;
    private RoadCameraImageScene extraStuffScene;

    private final double width = 1124;
    private final double height = 720;

    public enum CurrentSceneEnum{
        MENU_SCENE,
        TRAFFIC_SCENE,
        TRAFFIC_SCENE_ROAD,
        WEATHER_SCENE,
        COMBINED_SCENE,
        EXTRA_STUFF_SCENE
    }
    public CurrentSceneEnum currentSceneEnum;

    public UIController(Stage stage) throws IOException {
        this.stage = stage;
        stage.setResizable(false);
        currentSceneEnum = CurrentSceneEnum.MENU_SCENE;
        trafficSceneRoot = new ScrollPane();
        trafficSceneRoot.setFitToWidth(true);
        trafficRoadSceneRoot = new ScrollPane();
        trafficSceneRoot.setFitToWidth(true);
        weatherSceneRoot = new ScrollPane();
        weatherSceneRoot.setFitToWidth(true);
        combinedSceneRoot = new ScrollPane();
        extraStuffSceneRoot = new ScrollPane();
        combinedSceneRoot.setFitToWidth(true);
        menuPane = new Pane();
        root = new Group();
        menuScene = new MenuScene(menuPane,this);
        trafficScene = new TrafficPageScene(trafficSceneRoot,width,height,this);
        trafficSceneRoad = new TrafficPageRoadScene(trafficRoadSceneRoot, width, height, this);
        //weatherScene = new WeatherPageScene(weatherSceneRoot,width,height,this);
        combinedScene = new CombinedScene(combinedSceneRoot, width, height, this);
        extraStuffSceneRoot.setMaxSize(1024, 720);
        extraStuffSceneRoot.setMinSize(1024, 720);
        extraStuffScene = new RoadCameraImageScene(extraStuffSceneRoot);

        this.stage.setScene(menuScene);
        this.stage.show();
    }

    public void fromMenuToCombinedPage(){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);
        currentScene = combinedScene;
        currentSceneEnum = CurrentSceneEnum.COMBINED_SCENE;
        refresh();
    }

    public void fromMenuToTrafficPage(){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);
        currentScene = trafficScene;
        currentSceneEnum = CurrentSceneEnum.TRAFFIC_SCENE;
        refresh();
    }

    public void fromMenuToTrafficPageRoad(){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);
        currentScene = trafficSceneRoad;
        currentSceneEnum = CurrentSceneEnum.TRAFFIC_SCENE_ROAD;
        refresh();

    }
    public void fromMenuToWeatherPage(){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);
        //currentScene = weatherScene;
        currentSceneEnum = CurrentSceneEnum.WEATHER_SCENE;
        refresh();
    }

    public void fromMenuToExtraStuffPage(){
        currentScene = extraStuffScene;
        currentSceneEnum = CurrentSceneEnum.EXTRA_STUFF_SCENE;
        refresh();
    }
    public void fromAnyPageToMenu(){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - 400) / 2);
        stage.setY((screenBounds.getHeight() - 300) / 2);
        currentScene = menuScene;
        currentSceneEnum = CurrentSceneEnum.MENU_SCENE;
        refresh();
    }

    /**
     * From stack overflow
     * @author Kohler Fryer
     */
    public static <newType, oldType> ArrayList<newType> castArrayList(ArrayList<oldType> list){
        ArrayList<newType> newlyCastedArrayList = new ArrayList<newType>();
        for(oldType listObject : list){
            newlyCastedArrayList.add((newType)listObject);
        }
        return newlyCastedArrayList;
    }


    public void searchButtonPressed(ArrayList<Object> data,
                                    ArrayList<Plottable> selected){
        switch (currentSceneEnum){
            case MENU_SCENE:
                break;
            case TRAFFIC_SCENE:
                ArrayList<Double> coords = castArrayList(data);
                trafficScene.handleSearchButtonClick(coords,selected);
                break;
            case WEATHER_SCENE:
                //ArrayList<Double> coords = castArrayList(data);
                //weatherScene.makeNewChartViewer(coords,selected);
                break;
            case TRAFFIC_SCENE_ROAD:
                ArrayList<Integer> road = castArrayList(data);
                trafficSceneRoad.handleSearchButtonClick(road,selected);
                break;
            default:
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
        WIND_SPEED,
        EMPTY
    }

}
