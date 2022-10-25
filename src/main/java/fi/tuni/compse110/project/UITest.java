package fi.tuni.compse110.project;

import fi.tuni.compse110.project.API.RoadData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.jfree.chart.fx.ChartViewer;
import org.json.JSONArray;
import org.json.JSONObject;
import fi.tuni.compse110.project.components.Feed;



/**
 * JavaFX App
 */
public class UITest extends Application {

    private static Scene scene;
    @Override
    public void start(Stage stage) throws IOException {
        ArrayList<Double> coords = new ArrayList<>(Arrays.asList(25.72088, 62.24147, 25.8, 62.3));
        
        File file = new File("./data/road_conditions.json");
        Scanner myReader = new Scanner(file);
        StringBuilder str = new StringBuilder();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            str.append(data);
        }
        myReader.close();
        JSONObject obj = new JSONObject(str.toString());
        Feed feed = new Feed(obj);


        
        scene = new Scene(feed.getElement(), 640, 480);


        /*
        *
        * EXAMPLE ON HOW TO GET AND USE THE CHART
        *
        *
         */
        /*ChartViewer viewer = Graph.getChart();
        scene = new Scene(viewer);*/


        // Possible nullPointerException throwing from .toExternalForm()
        scene.getStylesheets().add(UITest.class.getResource("/stylesheet.css").toExternalForm());

        stage.setScene(scene);
        stage.show();

    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UITest.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

   /* public static void main(String[] args) {
        launch();
    }*/

}