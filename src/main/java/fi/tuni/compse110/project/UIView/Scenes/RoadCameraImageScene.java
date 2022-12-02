package fi.tuni.compse110.project.UIView.Scenes;

import fi.tuni.compse110.project.API.APICall;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

public class RoadCameraImageScene extends Scene {

  /* ROAD AVERAGE SPEED AND PASS INFORMATION */

  // Average speeds and passes for a set time period
  final static String tmsData = "https://tie.digitraffic.fi/api/v1/data/tms-data/";

  // All possible stations
  final static String allStations = "https://tie.digitraffic.fi/api/v3/metadata/tms-stations";
  TreeMap<Integer, ArrayList<Integer>> tmsStations = new TreeMap<>();

  /* ROAD WEATHER */

  // Weather data for camera id. Add "/{id}"
  // Id can be acquired from allCameraStations
  final static String weatherData = "https://tie.digitraffic.fi/api/v1/data/weather-data/";

  /* ROAD CAMERA */

  //URL for getting all stations and their locations. Includes id for weather info
  final static String allCameraStations = "https://tie.digitraffic.fi/api/v3/metadata/camera-stations";

  // Data from road cameras. Add "/{id}" if specific camera
  final static String cameraData = "https://tie.digitraffic.fi/api/weathercam/v1/stations/";

  private LinkedList<Pair<String, LinkedList<Pair<String, Image>>>> allImages = new LinkedList<>();
  //private ArrayList<String> keys = new ArrayList<>();
  HashMap<String, Integer> stations = new HashMap<>();
  HashMap<String, Integer> roadNumbers = new HashMap<>();

  private Integer roadNumberIndex;
  private Integer forecastTimeIndex;

  private TextField directionText;
  private TextField sectionText;
  private ImageView imageView;
  private VBox addInfoBox;
  private ScrollPane roadInfoContainer;
  private Button addInfoButton;


  public RoadCameraImageScene(ScrollPane root) throws IOException {
    super(root);

    HBox base = new HBox();
    base.setAlignment(Pos.CENTER);
    base.setSpacing(5);

    VBox params = new VBox();
    params.setAlignment(Pos.CENTER);

    HBox cityBox = new HBox();
    cityBox.setAlignment(Pos.CENTER_LEFT);
    cityBox.setSpacing(5);
    Text t = new Text("Select city:");
    ComboBox<String> cities = listAllCameras();
    cityBox.getChildren().addAll(t, cities);

    HBox upperSelection = new HBox();
    upperSelection.setAlignment(Pos.CENTER);
    upperSelection.setSpacing(5);
    Button previousSectionButton = new Button("<-");
    previousSectionButton.setOnAction(e -> {
      previousSection();
    });
    sectionText = new TextField("Section");
    sectionText.setMaxWidth(250);
    sectionText.setMinWidth(250);
    sectionText.setDisable(true);
    sectionText.opacityProperty().setValue(1);
    sectionText.setAlignment(Pos.CENTER);
    Button nextSectionButton = new Button("->");
    nextSectionButton.setOnAction(e -> {
      nextSection();
    });
    upperSelection.getChildren().addAll(previousSectionButton, sectionText, nextSectionButton);

    HBox bottomSelection = new HBox();
    bottomSelection.setSpacing(5);
    bottomSelection.setAlignment(Pos.CENTER);
    Button previousDirectionButton = new Button("<-");
    previousDirectionButton.setOnAction(e -> previousDirection());
    directionText = new TextField("Direction");
    directionText.setMaxWidth(250);
    directionText.setMinWidth(250);
    directionText.setDisable(true);
    directionText.opacityProperty().setValue(1);
    directionText.setAlignment(Pos.CENTER);
    Button nextDirectionButton = new Button("->");
    nextDirectionButton.setOnAction(e -> nextDirection());
    bottomSelection.getChildren().addAll(previousDirectionButton, directionText, nextDirectionButton);

    imageView = new ImageView();
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(500);
    imageView.setFitWidth(800);
    VBox v = new VBox();
    v.setMaxSize(800, 500);
    v.setMinSize(800, 500);
    v.getChildren().add(imageView);
    v.setAlignment(Pos.CENTER);
    params.getChildren().addAll(cityBox, bottomSelection, v, upperSelection);


    VBox addInfo = new VBox();

    addInfoButton = new Button("Search for additional information");
    addInfoButton.setDisable(true);
    addInfoButton.setOnAction(e -> additionalInfo());

    addInfoBox = new VBox();
    roadInfoContainer = new ScrollPane();
    addInfo.getChildren().addAll(addInfoBox, roadInfoContainer, addInfoButton);

    base.getChildren().addAll(params, addInfo);

    root.setContent(base);

  }

  public ComboBox<String> listAllCameras() throws IOException {
    ComboBox<String> combo = new ComboBox<>();
    listAllTmsStations();
    JSONObject jo = new JSONObject(APICall.getRequest(allCameraStations, true));
    JSONArray features = new JSONArray(jo.getJSONArray("features"));
    TreeMap<String, ArrayList<Pair<String, String>>> cities = new TreeMap<>();
    for (int i = 0; i < features.length(); i++) {
      JSONObject feature = (JSONObject) features.get(i);
      String roadStationId = feature.getString("id");
      JSONObject properties = feature.getJSONObject("properties");
      String id = properties.getString("id");
      String municipality = properties.getString("municipality");
      if (properties.getString("collectionStatus").equals("GATHERING")) {
        JSONObject names = properties.getJSONObject("names");
        String name = "";
        if (names.has("en")) {
          name = names.getString("en");
        } else if (names.has("fi")) {
          name = names.getString("fi");
        } else {
          name = properties.getString("name");
        }
        if (cities.containsKey(municipality)) {
          cities.get(municipality).add(new Pair<>(name, id));
        } else {
          ArrayList<Pair<String, String>> a = new ArrayList<>();
          a.add(new Pair<>(name, id));
          cities.put(municipality, a);
        }
      }
    }

    Stage stage = new Stage();
    stage.setTitle("Camera images");

    cities.forEach((k, v) -> combo.getItems().add(k));
//
//    VBox vBoxNonDelete = new VBox();
//    VBox vBox = new VBox();
//    ScrollPane sp = new ScrollPane();
//    sp.setContent(vBoxNonDelete);
//    Scene scene = new Scene(sp, 1400, 800);

    combo.setOnAction(e -> {
      addInfoButton.setDisable(false);
      roadNumberIndex = 0;
      forecastTimeIndex = 0;
      //keys.clear();
      allImages.clear();
      stations.clear();
      addInfoBox.getChildren().clear();
      roadInfoContainer.setContent(null);
      e.consume();
//      vBox.getChildren().clear();
//      ComboBox<String> combo2 = new ComboBox<>();
//      cities.get(combo.getSelectionModel().getSelectedItem()).forEach((k) -> {
////        combo2.getItems().add(k.getKey());
//        keys.add(k.getKey());
//
//      });
      cities.get(combo.getSelectionModel().getSelectedItem()).forEach(v -> {
        //if(v.getKey().equals(combo2.getSelectionModel().getSelectedItem())) {
        String url = cameraData + v.getValue();
        try {
          //System.out.println(url);
          JSONObject j = new JSONObject(APICall.getRequest(url, true));
          JSONObject s = j.getJSONObject("properties");
          String name = s.getString("name");
          //directionText.setText(name);
          JSONObject address = s.getJSONObject("roadAddress");
          int number = address.getInt("roadNumber");
          JSONArray presets = s.getJSONArray("presets");
          int nearestStation = 0;
          if (!s.get("nearestWeatherStationId").toString().equals("null") && s.has("nearestWeatherStationId")) {
            nearestStation = s.getInt("nearestWeatherStationId");
          }
          LinkedList<Pair<String, Image>> imagesPerSection = new LinkedList<>();
          for (int i = 0; i < presets.length(); i++) {
//            VBox imageBox = new VBox();
            JSONObject image = (JSONObject) presets.get(i);
//            URL imageUrl = new URL(image.getString("imageUrl"));
            String presentationName = image.get("presentationName").toString();
            if(presentationName.equals("null")) {
              JSONObject names = s.getJSONObject("names");
              presentationName = names.getString("fi");
            }
//
//            System.out.println(imageUrl);
            Image image1 = new Image(image.getString("imageUrl"));
//            vBox.setMaxWidth(image1.getWidth());
//            Text text = new Text(combo.getSelectionModel().getSelectedItem() + " - " + presentationName);
//            text.setFont(new Font(24));
//            imageBox.getChildren().add(text);
//            imageBox.getChildren().add(new ImageView(image1));
//            vBox.getChildren().add(imageBox);

            imagesPerSection.add(new Pair<>(presentationName, image1));
            //if(i == 0) {
              //sectionText.setText(presentationName);
              //imageView.setImage(image1);
            //}
          }
          if (nearestStation > 0) {
            stations.put(name, nearestStation);
          }
          roadNumbers.put(name, number);
          allImages.add(new Pair<>(name, imagesPerSection));
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        //}
      });
    });
    return combo;
  }

  private void listAllTmsStations() throws IOException {
    JSONObject j = new JSONObject(APICall.getRequest(allStations, true));
    JSONArray f = j.getJSONArray("features");
    for(int i = 0; i < f.length(); i++) {
      JSONObject feat = f.getJSONObject(i);
      JSONObject prop = feat.getJSONObject("properties");
      if(!prop.getString("collectionStatus").equals("REMOVED_TEMPORARILY")){
        int id = prop.getInt("roadStationId");
        JSONObject address = prop.getJSONObject("roadAddress");
        int number = address.getInt("roadNumber");
        if(tmsStations.containsKey(number)){
          tmsStations.get(number).add(id);
        } else {
          ArrayList<Integer> a = new ArrayList<>();
          a.add(id);
          tmsStations.put(number, a);
        }
      }
    }
  }

  public void nextSection() {
    if(forecastTimeIndex < allImages.get(roadNumberIndex).getValue().size() - 1) {
      forecastTimeIndex++;
    } else {
      forecastTimeIndex = 0;
    }
    LinkedList<Pair<String, Image>> cells = allImages.get(roadNumberIndex).getValue();
    sectionText.setText(cells.get(forecastTimeIndex).getKey());
    imageView.setImage(null);
    imageView.setImage(cells.get(forecastTimeIndex).getValue());
    imageView.setFitWidth(cells.get(forecastTimeIndex).getValue().getWidth());

  }

  public void previousSection() {
    if(forecastTimeIndex > 0) {
      forecastTimeIndex--;
    } else {
      forecastTimeIndex = allImages.get(roadNumberIndex).getValue().size() - 1;
    }
    LinkedList<Pair<String, Image>> cells = allImages.get(roadNumberIndex).getValue();
    sectionText.setText(cells.get(forecastTimeIndex).getKey());
    imageView.setImage(null);
    imageView.setImage(cells.get(forecastTimeIndex).getValue());
    imageView.setFitWidth(cells.get(forecastTimeIndex).getValue().getWidth());
  }

  public void nextDirection() {
    if(roadNumberIndex < allImages.size() - 1) {
      roadNumberIndex++;
    } else {
      roadNumberIndex = 0;
    }
    forecastTimeIndex = 0;
    directionText.setText(allImages.get(roadNumberIndex).getKey());
    LinkedList<Pair<String, Image>> cells = allImages.get(roadNumberIndex).getValue();
    sectionText.setText(cells.get(forecastTimeIndex).getKey());
    imageView.setImage(null);
    imageView.setImage(cells.get(forecastTimeIndex).getValue());
  }

  public void previousDirection() {
    if(roadNumberIndex > 0) {
      roadNumberIndex--;
    } else {
      roadNumberIndex = allImages.size() - 1;
    }
    forecastTimeIndex = 0;
    directionText.setText(allImages.get(roadNumberIndex).getKey());
    LinkedList<Pair<String, Image>> cells = allImages.get(roadNumberIndex).getValue();
    sectionText.setText(cells.get(forecastTimeIndex).getKey());
    imageView.setImage(null);
    imageView.setImage(cells.get(forecastTimeIndex).getValue());
  }

  public void additionalInfo() {
    addInfoBox.getChildren().clear();
    String url = weatherData + stations.get(allImages.get(roadNumberIndex).getKey());
    JSONObject j = null;
    try {
      j = new JSONObject(APICall.getRequest(url, true));
    } catch (IOException e) {
      e.printStackTrace();
    }
    assert j != null;
    JSONObject a = j.getJSONArray("weatherStations").getJSONObject(0);
    addInfoBox.getChildren().add(new Text("ADDITIONAL INFORMATION FROM NEAREST WEATHER STATION"));
    for(int i = 0; i < a.getJSONArray("sensorValues").length(); i++) {
      JSONObject info = a.getJSONArray("sensorValues").getJSONObject(i);
      if(info.getInt("id") == 1) {
        addInfoBox.getChildren().add(new Text("Air temperature: " + info.get("sensorValue") + info.get("sensorUnit")));
      }
      if(info.getInt("id") == 3) {
        addInfoBox.getChildren().add(new Text("Road temperature: " + info.get("sensorValue") + info.get("sensorUnit")));
      }
      if(info.getInt("id") == 16) {
        addInfoBox.getChildren().add(new Text("Average wind speed: " + info.get("sensorValue") + info.get("sensorUnit")));
      }
      if(info.getInt("id") == 23) {
        addInfoBox.getChildren().add(new Text("Precipitation intensity: " + info.get("sensorValue") + info.get("sensorUnit")));
      }
      if(info.getInt("id") == 26) {
        addInfoBox.getChildren().add(new Text("Visibility: " + info.get("sensorValue") + info.get("sensorUnit")));
      }
      if(info.getInt("id") == 94) {
        addInfoBox.getChildren().add(new Text("Depth of snow: " + info.get("sensorValue") + info.get("sensorUnit")));
      }
      if(info.getInt("id") == 176) {
        addInfoBox.getChildren().add(new Text("Friction: " + info.get("sensorValue") + info.get("sensorUnit")));
      }
      if(info.getInt("id") == 215) {
        addInfoBox.getChildren().add(new Text("Rain in last 24h: " + info.get("sensorValue") + info.get("sensorUnit")));
      }
    }
    if(tmsStations.containsKey(roadNumbers.get(allImages.get(roadNumberIndex).getKey()))) {
      roadInfoContainer.setContent(null);
      ArrayList<Integer> numbs = tmsStations.get(roadNumbers.get(allImages.get(roadNumberIndex).getKey()));
      VBox info = new VBox();
      for(int n : numbs) {
        JSONObject jo = null;
        try {
          jo = new JSONObject(APICall.getRequest(tmsData + n, true));
        } catch (IOException e) {
          e.printStackTrace();
        }
        assert jo != null;
        JSONObject stations = (JSONObject) jo.getJSONArray("tmsStations").get(0);
        JSONArray values = stations.getJSONArray("sensorValues");
        info.getChildren().add(new Text("STATION ID: " + stations.getInt("id")));
        for(int i = 0; i < values.length(); i++) {
          JSONObject value = values.getJSONObject(i);
          if(value.getInt("id") == 5054){
            info.getChildren().add(new Text("Passed cars in direction 1: " + value.getInt("sensorValue") + value.getString("sensorUnit")));
          }
          if(value.getInt("id") == 5056){
            info.getChildren().add(new Text("Average speed in direction 1: " + value.getInt("sensorValue") + value.getString("sensorUnit")));
          }
          if(value.getInt("id") == 5055){
            info.getChildren().add(new Text("Passed cars in direction 2: " + value.getInt("sensorValue") + value.getString("sensorUnit")));
          }
          if(value.getInt("id") == 5057){
            info.getChildren().add(new Text("Average speed in direction 2: " + value.getInt("sensorValue") + value.getString("sensorUnit")));
          }
        }
        info.getChildren().add(new Text(""));
      }
      roadInfoContainer.setContent(info);
    }
  }
}
