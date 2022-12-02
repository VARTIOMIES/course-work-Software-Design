package fi.tuni.compse110.project.UIView;

import fi.tuni.compse110.project.API.WeatherDataProvider;
import fi.tuni.compse110.project.UIView.components.Calendar;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TestWindowLogic extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws IOException {

    ArrayList<String> params = new ArrayList<>();
    params.add("temperature");
    params.add("humidity");
    WeatherDataProvider.weatherForecast("Tampere", new ArrayList<>(), params, "2022-12-21T00:00:00Z");
    VBox startWindow = new VBox();
    startWindow.setAlignment(Pos.CENTER);
    HBox selection = new HBox();
    Button weather = new Button("Show weather related data");
    Button road = new Button("Show road related data");
    Button combination = new Button("Show combination of data");
    selection.getChildren().addAll(weather, road, combination);
    startWindow.getChildren().add(selection);

    GridPane loc = new GridPane();
    RadioButton coords = new RadioButton("Give coordinates:");
    RadioButton roadNumb = new RadioButton("Give road number:");
    TextField numberField = new TextField();
    numberField.setDisable(true);
    numberField.setOnKeyTyped(e -> {
      if(!numberField.getText().matches("\\d+")) {
        numberField.deletePreviousChar();
      }
    });

    GridPane g = createCoordinateSearch();

    ToggleGroup buttons = new ToggleGroup();
    coords.setToggleGroup(buttons);
    roadNumb.setToggleGroup(buttons);

    coords.setOnAction(e -> {
      numberField.setDisable(true);
      g.setDisable(false);
    });
    roadNumb.setOnAction(e -> {
      numberField.setDisable(false);
      g.setDisable(true);
    });

    loc.add(coords, 0, 0);
    loc.add(g, 0, 1);
    loc.add(roadNumb, 1, 0);
    loc.add(numberField, 1, 1);

    GridPane calendars = new GridPane();
    Text dayStart = new Text("Pick starting date:");
    Text dayEnd = new Text("Pick ending date:");
    DatePicker cal1 = new DatePicker();
    DatePicker cal2 = new DatePicker();

    Calendar calendar = new Calendar();

    calendars.add(dayStart, 0, 0);
    calendars.add(calendar.createCalendar(), 0, 1);
    calendars.add(dayEnd, 1, 0);
    calendars.add(cal2, 1,1);

    startWindow.getChildren().addAll(loc, calendars);
    VBox data = new VBox();
    data.setMinSize(150, 150);
    startWindow.getChildren().add(data);
    weather.setOnAction(e -> {
      data.getChildren().clear();
      data.getChildren().add(weatherWindow());
    });
    road.setOnAction(e -> {
      data.getChildren().clear();
      data.getChildren().add(roadWindow());
    });

    Scene startScene = new Scene(startWindow);
    stage.setScene(startScene);
    stage.show();

  }


  public GridPane createCoordinateSearch() {
    GridPane g = new GridPane();
    Text minLat = new Text("Min Lat");
    Text maxLat = new Text("Max Lat");
    Text minLong = new Text("Min Long");
    Text maxLong = new Text("Max Long");

    TextField fieldLongMin = textFieldValidator("21", "33");
    TextField fieldLatMin = textFieldValidator("58", "72");
    TextField fieldLongMax = textFieldValidator("21", "33");
    TextField fieldLatMax = textFieldValidator("58", "72");

    g.add(minLat, 0, 0);
    g.add(fieldLatMin, 1, 0);
    g.add(maxLat, 0, 1);
    g.add(fieldLatMax, 1, 1);

    g.add(minLong, 2, 0);
    g.add(fieldLongMin, 3, 0);
    g.add(maxLong, 2, 1);
    g.add(fieldLongMax, 3, 1);

    g.setPadding(new Insets(5));
    g.setDisable(true);
    return g;
  }

  public TextField textFieldValidator(String min, String max) {
    TextField t = new TextField();

    t.setOnKeyTyped(e -> {
      if(t.getText().matches("\\d+") && t.getText().length() == 1) {
        if(Integer.parseInt(t.getText()) < Integer.parseInt(String.valueOf(min.charAt(0))) || Integer.parseInt(t.getText()) > Integer.parseInt(String.valueOf(max.charAt(0)))) {
          t.clear();
        }
      } else if (t.getText().matches("\\d+") && t.getText().length() == 2) {
        if(Integer.parseInt(t.getText()) < Integer.parseInt(min) || Integer.parseInt(t.getText()) > Integer.parseInt(max)) {
          t.deletePreviousChar();
        }
      } else if(t.getText().matches("\\d{3}")) {
        t.setText(t.getText().substring(0, 2) + "." + t.getText().substring(2, 3));
      } else {
        if(!t.getText().matches("\\d{2}\\.\\d+") && t.getText().length() > 0) {
          t.deletePreviousChar();
        }
      }
    });
    return t;
  }

  public GridPane weatherWindow() {

    GridPane w = new GridPane();
    Text info = new Text("Weather related data view");
    Button avg = new Button("Get average temperatures");
    Button minMax = new Button("Get min-max temperatures");
    VBox vis = new VBox();

    VBox selections = new VBox();
    CheckBox temp = new CheckBox("Temperature");
    CheckBox obWind = new CheckBox("Observed wind");
    CheckBox obCloud = new CheckBox("Observed cloudiness");
    CheckBox prWind = new CheckBox("Predicted wind");
    CheckBox prTemp = new CheckBox("Predicted temperature");

    selections.getChildren().addAll(temp, obWind, obCloud, prWind, prTemp);

    w.add(info, 0, 0, 3, 1);
    w.add(avg, 0,3);
    w.add(minMax, 2, 3);
    w.add(vis, 0,1, 2, 2);
    w.add(selections, 3, 1, 1, 2);

    return w;
  }

  public GridPane roadWindow() {

    GridPane w = new GridPane();
    Text info = new Text("Road related data view");
    Button maint = new Button("Get road maintenance data");
    Button cond = new Button("Get road condition forecast");
    cond.setOnAction(e -> {
      Stage stage = new Stage();
      Parent root = null;
      try {
        root = FXMLLoader.load(new File("C:/Users/venal/IdeaProjects/ProjectSoftwareDesign/src/main/java/fi/tuni/compse110/project/UIView/TestWindow.fxml").toURI().toURL());
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      assert root != null;
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    });
    Button mess = new Button("Get traffic messages");
    VBox vis = new VBox();

    w.add(info, 0, 0, 3, 1);
    w.add(maint, 0,3);
    w.add(cond, 1, 3);
    w.add(mess, 2, 3);
    w.add(vis, 0,1, 2, 2);

    return w;
  }
}
