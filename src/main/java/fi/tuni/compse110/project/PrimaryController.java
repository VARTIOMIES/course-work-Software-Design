package fi.tuni.compse110.project;

import java.io.IOException;

import fi.tuni.compse110.project.UIView.TrafficPage;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        TrafficPage.setRoot("secondary");
    }
}
