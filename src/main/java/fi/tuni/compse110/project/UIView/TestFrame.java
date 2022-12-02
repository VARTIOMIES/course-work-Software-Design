package fi.tuni.compse110.project.UIView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestFrame extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(new File("C:/Users/venal/IdeaProjects/ProjectSoftwareDesign/src/main/java/fi/tuni/compse110/project/UIView/TestWindow.fxml").toURI().toURL());
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
