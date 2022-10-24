package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JavaFX App
 */
public class UITest extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        File file = new File("./data/road_conditions.json");
        Scanner myReader = new Scanner(file);
        StringBuilder str = new StringBuilder();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            str.append(data);
        }
        myReader.close();

        JSONObject obj = new JSONObject(str.toString());
        JSONArray weather_data = obj.getJSONArray("weatherData");
        VBox feed = new VBox(10);
        feed.setId("box");
        for (Object datapoint_obj : weather_data) {

            JSONObject location = (JSONObject) datapoint_obj;

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