package fi.tuni.compse110.project;

import fi.tuni.compse110.project.UIView.UIController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */public class Main extends Application {

     public Main(){
         super();
     }

    public static void main(String[] args) throws IOException {
        //APItest apitestSingleton = APItest.getInstance();
        //apitestSingleton.testing();
        launch();

        
    }

    @Override
    public void start(Stage stage) throws Exception {
        UIController uiController = new UIController(stage);

    }
}
