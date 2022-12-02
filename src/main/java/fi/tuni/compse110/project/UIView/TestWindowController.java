package fi.tuni.compse110.project.UIView;

import fi.tuni.compse110.project.API.RoadCondition;
import fi.tuni.compse110.project.API.RoadDataProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class TestWindowController {

  public Button previousRoadButton;
  public TextField roadNumberText;
  public Button nextRoadButton;
  public Button previousForecastButton;
  public TextField forecastText;
  public Button nextForecastButton;
  public CheckBox precipitationChoice;
  public CheckBox slipperyChoice;
  public CheckBox overallChoice;
  public CheckBox conditionChoice;
  public LinkedList<Pair<Integer, LinkedList<Pair<String, ScrollPane>>>> allCells = new LinkedList<>();
  public ScrollPane cellContainer;
  public AnchorPane mainWindow;
  public Button searchButton;

  private Integer roadNumberIndex;
  private Integer forecastTimeIndex;
  private HashMap<String, Image> symbols = new HashMap<>();

  private void getData() throws IOException {
    double minLong = 23;
    double minLat = 60;
    double maxLong = 24.5;
    double maxLat = 61.4;
    ArrayList<Double> coords = new ArrayList<>();
    coords.add(minLong);
    coords.add(minLat);
    coords.add(maxLong);
    coords.add(maxLat);

    TreeMap<Integer, LinkedHashMap<String, ArrayList<RoadCondition>>> data = RoadDataProvider.getRoadConditions(0, coords);

    data.forEach((key, value) -> {
      LinkedList<Pair<String, ScrollPane>> scrollPanes = new LinkedList<>();
      value.forEach( (k, v) -> {
        ArrayList<HBox> cells = new ArrayList<>();
        for(RoadCondition r : v) {
          HBox cell = null;
          try {
            cell = createCell(r);
          } catch (IOException e) {
            e.printStackTrace();
          }
          cells.add(cell);
        }
        scrollPanes.addLast(new Pair<>(k, createScrollPane(cells)));
      });
      allCells.addLast(new Pair<>(key, scrollPanes));
    });

  }

  private ScrollPane createScrollPane(ArrayList<HBox> a){
    ScrollPane scrollPane = new ScrollPane();
    VBox vBox = new VBox();
    vBox.setSpacing(5);
    scrollPane.setMinSize(565, 427);
    vBox.getChildren().addAll(a);
    scrollPane.setContent(vBox);
    return scrollPane;
  }

  private HBox createCell(RoadCondition r) throws IOException {

    HBox cell = new HBox();
    VBox cellV = new VBox();
    cellV.setMinSize(450, 50);
    cell.setBackground(new Background(new BackgroundFill(Color.rgb(70, 220, 200), CornerRadii.EMPTY, Insets.EMPTY)));

    Text section = new Text("Road id: " + r.getId());
    Text precipitation = new Text("Road precipitation: " + r.getPrecipitationCondition());
    Text overall = new Text("Road overall condition: " + r.getOverallRoadCondition());
    Text condition = new Text("Road condition: " + r.getRoadCondition());

    cellV.getChildren().addAll(section, precipitation, overall, condition);

    cell.getChildren().add(cellV);
    if(symbols.containsKey(r.getWeatherSymbol())) {
      VBox v = new VBox(new ImageView(symbols.get(r.getWeatherSymbol())));
      cell.getChildren().add(v);
    } else {
      System.out.println(r.getWeatherSymbol());
      Image v = RoadDataProvider.getSymbol(r.getWeatherSymbol());
      symbols.put(r.getWeatherSymbol(), RoadDataProvider.getSymbol(r.getWeatherSymbol()));
      cell.getChildren().add(new ImageView(v));
    }

    return cell;
  }

  public void start() throws IOException {
    getData();
    System.out.println(allCells.get(0).getKey());
    roadNumberText.setText("Road " + allCells.get(0).getKey());
    roadNumberIndex = 0;
    LinkedList<Pair<String, ScrollPane>> cells = allCells.get(0).getValue();
    forecastText.setText(cells.get(0).getKey());
    forecastTimeIndex = 0;

    cellContainer.setContent(null);
    cellContainer.setContent(cells.get(0).getValue());
  }

  public void nextSection() {
    if(forecastTimeIndex < 4) {
      forecastTimeIndex++;
    } else {
      forecastTimeIndex = 0;
    }
    LinkedList<Pair<String, ScrollPane>> cells = allCells.get(roadNumberIndex).getValue();
    forecastText.setText(cells.get(forecastTimeIndex).getKey());
    cellContainer.setContent(null);
    cellContainer.setContent(cells.get(forecastTimeIndex).getValue());
  }

  public void previousSection() {
    if(forecastTimeIndex > 0) {
      forecastTimeIndex--;
    } else {
      forecastTimeIndex = 4;
    }
    LinkedList<Pair<String, ScrollPane>> cells = allCells.get(roadNumberIndex).getValue();
    forecastText.setText(cells.get(forecastTimeIndex).getKey());
    cellContainer.setContent(null);
    cellContainer.setContent(cells.get(forecastTimeIndex).getValue());
  }

  public void nextRoad() {
    if(roadNumberIndex < allCells.size() - 1) {
      roadNumberIndex++;
    } else {
      roadNumberIndex = 0;
    }
    roadNumberText.setText("Road " + allCells.get(roadNumberIndex).getKey());
    LinkedList<Pair<String, ScrollPane>> cells = allCells.get(roadNumberIndex).getValue();
    forecastText.setText(cells.get(forecastTimeIndex).getKey());
    cellContainer.setContent(null);
    cellContainer.setContent(cells.get(forecastTimeIndex).getValue());
  }

  public void previousRoad() {
    if(roadNumberIndex > 0) {
      roadNumberIndex--;
    } else {
      roadNumberIndex = allCells.size() - 1;
    }
    roadNumberText.setText("Road " + allCells.get(roadNumberIndex).getKey());
    LinkedList<Pair<String, ScrollPane>> cells = allCells.get(roadNumberIndex).getValue();
    forecastText.setText(cells.get(forecastTimeIndex).getKey());
    cellContainer.setContent(null);
    cellContainer.setContent(cells.get(forecastTimeIndex).getValue());
  }

}
