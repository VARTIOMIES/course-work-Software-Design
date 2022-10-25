package fi.tuni.compse110.project.components;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FeedElement {
    private Text title_obj;
    private VBox feed_obj;
    private VBox info_box;

    public FeedElement() {
        feed_obj = new VBox();
        feed_obj.setId("feed-element");
        info_box = new VBox();
        title_obj = new Text();
        title_obj.setId("title");
    }

    public void addInfo(String info){
        info_box.getChildren().add(new Text(info));
    }

    public void addAllInfo(String... list){
        for(String text : list){
            Text t = new Text(text);
            info_box.getChildren().add(t);
        }
    }

    public void setTitle(String title){
        title_obj.setText(title);
    }

    public VBox getObject(){
        feed_obj.getChildren().add(title_obj);
        feed_obj.getChildren().add(info_box);

        return feed_obj;
    }
}
