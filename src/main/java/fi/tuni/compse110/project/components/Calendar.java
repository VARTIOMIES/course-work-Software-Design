/*
 * Author Miikka Venäläinen
 *
 */

package fi.tuni.compse110.project.components;

import fi.tuni.compse110.project.API.WeatherDataProvider;
import java.io.IOException;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class Calendar {

  private int year;
  private String month;
  private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
  private int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

  public VBox createCalendar() {
    VBox calendar = new VBox();
    calendar.setAlignment(Pos.CENTER);
    calendar.setSpacing(5);

    HBox y = new HBox();
    y.setAlignment(Pos.CENTER);
    y.setSpacing(5);
    Button prev = new Button("<-");
    Button next = new Button("->");
    Text yearText = new Text("2022");

    prev.setOnAction(e -> {
      year = Integer.parseInt(yearText.getText()) - 1;
      yearText.setText(String.valueOf(Integer.parseInt(yearText.getText()) - 1));
    });
    next.setOnAction(e -> {
      year = Integer.parseInt(yearText.getText()) + 1;
      yearText.setText(String.valueOf(Integer.parseInt(yearText.getText()) + 1));
    });
    y.getChildren().addAll(prev, yearText, next);

    year = 2022;

    GridPane g = new GridPane();
    g.setHgap(2);
    g.setVgap(2);
    ToggleGroup group = new ToggleGroup();
    int index = 0;
    ArrayList<Node> buttons = new ArrayList<>();
    for (int i = 0; i < 12; i++){
      ToggleButton t = new ToggleButton(months[i]);
      t.setStyle("-fx-background-radius: 25");
      t.setMinWidth(75);
      t.setToggleGroup(group);
      t.setOnAction(e -> month = t.getText());
      buttons.add(t);
      if((i + 1) % 3 == 0) {
        int col = 0;
        for(Node b : buttons) {
          g.add(b, col, index);
          col++;
        }
        index++;
        buttons.clear();
      }
    }
    Button pick = new Button("Select date");
    pick.setOnAction(e -> {
      try {
        getDate();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    });
    HBox h = new HBox();
    h.setAlignment(Pos.CENTER_RIGHT);
    h.getChildren().add(pick);

    calendar.getChildren().addAll(y, g, h);

    return calendar;
  }

  public Pair<String, String> getDate() throws IOException {

    String startDate = "";
    String endDate = "";

    for(int i = 0; i < months.length; i++) {
      if(months[i].equals(month)) {
        int m = i + 1;
        if(i < 10) {
          startDate = year + "-0" + m + "-01T00:00:00Z";
          if(month.equals("February") && year % 4 == 0) {
            endDate = year + "-0" + m + "-29T23:59:59Z";
          } else {
            endDate = year + "-0" + m + "-" + days[i] + "T23:59:59Z";
          }
        } else {
          startDate = year + "-" + m + "-01T00:00:00Z";
          endDate = year + "-" + m + "-" + days[i] + "T23:59:59Z";
        }
      }
    }
    ArrayList<String> places = new ArrayList<>();
    places.add("Kajaani");

    ArrayList<String> params = new ArrayList<>();
    params.add("temperature");
    params.add("windspeedms");


    //WeatherDataProvider.weatherURLCreator(places, new ArrayList<>(), params, startDate, endDate);
    System.out.println(startDate + "  -  " + endDate);
    return new Pair<>(startDate, endDate);
  }

}
