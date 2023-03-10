package fi.tuni.compse110.project.UIView.components;
import java.util.ArrayList;
import java.util.Map;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class Feed {
    private ScrollPane root;
    
    public Feed(Map<ArrayList<String>, ArrayList<String>> data) {
        root = new ScrollPane();
        int index = 0;
        VBox feed = new VBox();
        feed.setId("box");
        for (Map.Entry<ArrayList<String>, ArrayList<String>>location : data.entrySet()) {

            FeedElement feed_element = new FeedElement();
            
            //temporarely only gets the 1st task
            feed_element.setTitle(location.getKey().get(0));
            
            feed_element.addAllInfo(location.getValue());
            if(index % 2 == 0){feed_element.setBackgroundColor("white");}
            else{
                feed_element.setBackgroundColor("rgb(211,211,211)");
            }
            feed.getChildren().add(feed_element.getObject());
            index++;
            

        }
        root.setPrefSize(550, 300);
        root.setId("scroll");
        root.setContent(feed);
    }

    public ScrollPane getElement(){
        return root;
    }


}
