package fi.tuni.compse110.project.components;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class Feed {
    private ScrollPane root;
    
    public Feed(JSONObject data) {
        root = new ScrollPane();
        
        JSONArray weather_data = data.getJSONArray("weatherData");
        VBox feed = new VBox(10);
        feed.setId("box");
        for (Object datapoint_obj : weather_data) {

            JSONObject location = (JSONObject) datapoint_obj;

            JSONArray roadConditions = location.getJSONArray("roadConditions");

            // loop one location
            for (Object location_datapoint : roadConditions) {

                FeedElement feed_element = new FeedElement();

                JSONObject point = (JSONObject) location_datapoint;
                feed_element.setTitle("this is title");
                feed_element.addAllInfo(point.getString("type"), point.getString("forecastName"), "temperature: ",
                        point.getString("temperature"));
                feed.getChildren().add(feed_element.getObject());
            }

        }
        
        root.setId("scroll");
        root.setContent(feed);
    }

    public ScrollPane getElement(){
        return root;
    }


}
