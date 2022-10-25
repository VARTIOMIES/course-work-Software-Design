package fi.tuni.compse110.project;

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
import fi.tuni.compse110.project.components.FeedElement;


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

<<<<<<< HEAD
        
        scene = new Scene(feed.getElement(), 640, 480);
=======
            JSONArray raodConditions = location.getJSONArray("roadConditions");

            // loop one location
            for (Object location_datapoint : raodConditions) {

                FeedElement feed_element = new FeedElement();

                JSONObject point = (JSONObject) location_datapoint;
                feed_element.setTitle("this is title");
                feed_element.addAllInfo(point.getString("type"), point.getString("forecastName"), "temperature: ",
                        point.getString("temperature"));
                feed.getChildren().add(feed_element.getObject());
            }

        }
        ScrollPane scrollable = new ScrollPane();
        scrollable.setId("scroll");
        scrollable.setContent(feed);


        scene = new Scene(scrollable, 640, 480);
>>>>>>> 6f18172736280ea44c278b55b2c9d842e348ff3c

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