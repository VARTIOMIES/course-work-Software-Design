package fi.tuni.compse110.project;

import fi.tuni.compse110.project.API.APItest;
import fi.tuni.compse110.project.API.RoadDataProvider;
import fi.tuni.compse110.project.UIView.TrafficPage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */public class Main{

    public static void main(String[] args) throws IOException {
        //TODO: launch menu instead of traffipage
        APItest apitestSingleton = APItest.getInstance();
        //apitestSingleton.testing();

        Application.launch(TrafficPage.class, args);
        
        
        
    }
}
