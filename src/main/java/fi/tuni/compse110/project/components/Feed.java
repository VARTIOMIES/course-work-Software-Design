package fi.tuni.compse110.project.components;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class Feed {
    private ScrollPane root;
    
    public Feed(Map<ArrayList<String>, ArrayList<String>> data) {
        root = new ScrollPane();
        int index = 0;
        VBox feed = new VBox(10);
        feed.setId("box");
        for (Map.Entry<ArrayList<String>, ArrayList<String>>location : data.entrySet()) {

            FeedElement feed_element = new FeedElement();
            
            //temporarely only gets the 1st task
            feed_element.setTitle(location.getKey().get(0));

            feed_element.addAllInfo(location.getValue());
            if(index % 2 == 0){feed_element.setBackgroundColor("white");}
            else{
                feed_element.setBackgroundColor("grey");
            }
            feed.getChildren().add(feed_element.getObject());
            index++;
            

        }
        root.setMaxHeight(400);
        root.setId("scroll");
        root.setContent(feed);
    }

    public ScrollPane getElement(){
        return root;
    }


}
