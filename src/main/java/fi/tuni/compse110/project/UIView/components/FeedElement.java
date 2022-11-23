package fi.tuni.compse110.project.UIView.components;

import java.util.ArrayList;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FeedElement {
    private Text title_obj;
    private VBox feed_obj;
    private VBox info_box;
    private String bg_color;

    public FeedElement() {
        feed_obj = new VBox();
        feed_obj.setPrefWidth(550);
        feed_obj.setId("feed-element");
        info_box = new VBox();
        title_obj = new Text();
        title_obj.setId("title");
    }

    public void addInfo(String info){
        info_box.getChildren().add(new Text(info));
    }

    public void setBackgroundColor(String color){
        bg_color = color;
    }

    public void addAllInfo(ArrayList<String> info){
        for(String text : info){
            Text t = new Text(text);
            info_box.getChildren().add(t);
        }
    }

    public void setTitle(String title){
        title_obj.setText(title);
    }

    public VBox getObject(){
        feed_obj.setStyle(("-fx-background-color: " + bg_color));
        feed_obj.getChildren().add(title_obj);
        feed_obj.getChildren().add(info_box);

        return feed_obj;
    }
}
