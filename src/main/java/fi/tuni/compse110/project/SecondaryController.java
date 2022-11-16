package fi.tuni.compse110.project;

import java.io.IOException;

import fi.tuni.compse110.project.UIView.TrafficPage;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        TrafficPage.setRoot("primary");
    }
}