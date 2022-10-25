package fi.tuni.compse110.project;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        UITest.setRoot("secondary");
    }
}
