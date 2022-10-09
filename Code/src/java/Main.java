import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import javafx.scene.chart.LineChart;

/**
 * JavaFX App
 */
public class Main extends Application {

  // Digitraffic URL
  final String baseURL = "https://tie.digitraffic.fi";

  /* ROAD CAMERA */

  // Data from road cameras. Add "/{id}" if spesific camera
  final String cameraData = "https://tie.digitraffic.fi/api/v1/data/camera-data";

  // Search for camera image history "={preset or camera id}". Returns link to
  // images
  final String cameraHistory = "https://tie.digitraffic.fi/api/v3/data/camera-history/history?id=";

  /* ROAD WEATHER */

  // Weather data. Add "/{id}"
  final String weatherData = "https://tie.digitraffic.fi/api/v1/data/weather-data";

  // Weather data history. Add "/{stationId}"
  final String weatherHistory = "https://tie.digitraffic.fi/api/beta/weather-history-data";

  /* ROAD CONDITION */

  // Forecasts for roads
  // For coordinates use
  // "/{minLongitude}/{minLatitude}/{maxLongitude}/{maxLatitude}"
  // For spesific road use "/{roadNumber}"
  final String roadForecast = "https://tie.digitraffic.fi/api/v3/data/road-conditions";

  /* Maintenance API calls */
  final String maintenanceTasks = baseURL + "/api/maintenance/v1/tracking/tasks";
  final String maintenanceRoutes = baseURL + "/api/maintenance/v1/tracking/routes";

  /* Traffic message API calls */
  final String message = baseURL + "/api/traffic-message/v1/messages";


  /* FMI WEATHER DATA */

  /* Search id  + simple/timevaluepair + & + place/starttime/endtime/timestep/parameters/crs/bbox/fmisid/maxlocation/geoid/wmo */
  final String place = "&place=";
  final String starttime = "&starttime=";
  final String endtime = "&endtime=";
  final String timestep = "&timestep=";
  final String parameters = "&parameters="; //temperature, windspeedms
  final String crs = "&crs=";
  final String bbox = "&bbox=";
  final String fmisid = "&fmisid=";
  final String maxlocation = "&maxlocation=";
  final String geoid = "&geoid=";
  final String wmo = "&wmo=";

  //Weather observations for cities as time value pairs
  final String cityObservation = "fmi::observations::weather::cities::timevaluepair";

  //Daily Weather Observations
  final String dailyObservation = "fmi::observations::weather::daily::timevaluepair";

  //Hourly Weather Observations
  final String hourlyObservation = "fmi::observations::weather::hourly::timevaluepair";

  //Instantaneous Weather Observations
  final String instObservation = "fmi::observations::weather::timevaluepair";

  //Instantaneous Road Weather Observations
  final String roadObservation = "livi::observations::road::default::timevaluepair";

  //Hourly Air Quality Observations
  final String airQuality = "urban::observations::airquality::hourly::timevaluepair";

  //Radioactivity in outdoor air
  final String radioActivity = "stuk::observations::air::radionuclide-activity-concentration::latest::simple";

  final String FMIBaseURL = "https://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&storedquery_id=";

  /**
   * Created by Miikka Venäläinen
   * A method for GET requests
   *
   * @param u URL address for a request
   * @return Returns API request return value as a String
   * @throws IOException
   */
  public static String getRequest(String u) throws IOException {
    URL url = new URL(u);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("accept", "application/json");
    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = br.readLine()) != null) {
      sb.append(line).append("\n");
    }
    br.close();
    return sb.toString();
  }

  /**
   * Created by Miikka Venäläinen
   * Function splits given date into day or time based on the argument
   *
   * @param date Formatted date t.ex. 2022-10-06T00:50:00Z
   * @param argument Is either "day" or "time"
   * @return Returns "2022-10-06" if argument is "day", "00:50:00" if argument is "time"
   */
  public static String dateSplitter(String date, String argument) {
    if(argument.equals("day")) {
      return date.split("T")[0];
    }
    return date.split("T")[1].substring(0, date.split("T")[1].length() - 1);
  }

  /**
   * Created by Miikka Venäläinen
   * Creates a valid date from given inputs
   *
   * @param year User's selection for the year
   * @param month User's selection for the month
   * @param day User's selection for the day
   * @param hours User's selection for the hours
   * @param minutes User's selection for the minutes
   * @return Returns correctly formatted date t.ex 2022-10-06T00:50:00Z
   */
  public static String dateFormatter(int year, int month, int day, int hours, int minutes) {
    String date;
    DateTimeFormatter dateForm = DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.US).withResolverStyle(ResolverStyle.STRICT);
    date = dateForm.format(LocalDate.of(year, month, day)) + "T" + LocalTime.of(hours, minutes) + ":00Z";
    return date;
  }

  public static void createChart() {
    NumberAxis xaxis = new NumberAxis();
    CategoryAxis yaxis = new CategoryAxis();
    xaxis.setLabel("Time");
    yaxis.setLabel("Temperature");
    LineChart linechart = new LineChart(xaxis,yaxis);
    linechart.setTitle("Temperature line chart");
    XYChart.Series series = new XYChart.Series();
    series.setName("Values");
    //series.getData().add(new XYChart.Data(time, value));
    linechart.getData().add(series);
    Group group = new Group();
    group.getChildren().add(linechart);
    //stage.setScene(new Scene(group, 800, 500));
    //stage.show();
  }

  /**
   * Created by Miikka Venäläinen
   * Method for a query with user given parameters
   *
   * @param places List of cities
   * @param params List of wanted parameters
   * @return List of Pairs of Time and Value
   * @throws IOException
   */
  public List<Pair<String, Double>> weatherInformation(ArrayList<String> places, ArrayList<String> params, String startingTime, String endingTime) throws IOException {
    String url = FMIBaseURL + dailyObservation;
    //String url = FMIBaseURL + instObservation;
    if(places.size() > 0) {
      for(String p : places) {
        url += place + p;
      }
    }
    if(params.size() > 0) {
      url += parameters;
      for(String p : params) {
        url += p + ",";
      }
      url = url.substring(0, url.length() - 1);
    }

    if(startingTime.length() > 0) {
      url += starttime + startingTime;
    }
    if(endingTime.length() > 0) {
      url += endtime + endingTime;
    }
    System.out.println(url);
    JSONObject jo = XML.toJSONObject(getRequest(url));
    return filterTemperatureTimeValuePair(jo, params.size() > 1);
  }

  // For simple weather data
  public void filterTemperatureSimple(JSONObject jo) {
    JSONObject info = jo.getJSONObject("wfs:FeatureCollection");
    JSONArray member = info.getJSONArray("wfs:member");
    System.out.println(member.length());
    System.out.println(dateFormatter(2025, 8, 10, 12, 34));
    for(int i = 0; i < member.length(); i++) {
      JSONObject element = (JSONObject) member.get(i);
      JSONObject innerElement = (JSONObject) element.get("BsWfs:BsWfsElement");
      String name = (String) innerElement.get("BsWfs:ParameterName");
      if(name.equals("temperature")) {
        String t = (String) innerElement.get("BsWfs:Time");
        String value = innerElement.get("BsWfs:ParameterValue").toString();
        System.out.println(dateSplitter(t, "time") + " - " + value);
      }
    }
  }

  /**
   * Created by Miikka Venäläinen
   * Method which goes through the data and gets necessary values
   *
   * @param jo JSONObject from API call
   * @param multipleParams Boolean which tells whether JSONObject contains data from multiple cities and/or parameters
   * @return Returns a list of dates/times and respected values
   */
  public List<Pair<String, Double>> filterTemperatureTimeValuePair(JSONObject jo, Boolean multipleParams) {
    List<JSONObject> jsonObjects = new ArrayList<>();
    JSONObject info = jo.getJSONObject("wfs:FeatureCollection");

    if(multipleParams) {
      for(int i = 0; i < info.getJSONArray("wfs:member").length(); i++) {
        jsonObjects.add((JSONObject) info.getJSONArray("wfs:member").get(i));
      }
    } else {
      jsonObjects.add(info.getJSONObject("wfs:member"));
    }

    List<Pair<String, Double>> list = new ArrayList<>();

    String city = "";
    for (JSONObject j : jsonObjects) {
      JSONObject observation = j.getJSONObject("omso:PointTimeSeriesObservation");
      JSONObject interest = observation.getJSONObject("om:featureOfInterest");
      JSONObject sampling = interest.getJSONObject("sams:SF_SpatialSamplingFeature");
      JSONObject shape = sampling.getJSONObject("sams:shape");
      JSONObject point = shape.getJSONObject("gml:Point");
      city = point.get("gml:name").toString();

      JSONObject results = observation.getJSONObject("om:result");
      JSONObject timeSeries = results.getJSONObject("wml2:MeasurementTimeseries");
      JSONArray points = timeSeries.getJSONArray("wml2:point");

      for(int i = 0; i < points.length(); i++) {
        JSONObject element = (JSONObject) points.get(i);
        JSONObject innerElement = (JSONObject) element.get("wml2:MeasurementTVP");
        String time = (String) innerElement.get("wml2:time");
        String value = innerElement.get("wml2:value").toString();
        //System.out.println(dateSplitter(time, "time") + " - " + value);
        list.add(new Pair<>(time, Double.valueOf(value)));
      }
    }
    averageTemperature(list, city);
    minMaxTemperature(list, city);
    return list;
  }

  /**
   *
   * Created by Miikka Venäläinen
   * Method for calculations of daily average values like temperature
   *
   * @param list List of Pair which contains time and value information
   * @param city Name of the city
   */
  public void averageTemperature(List<Pair<String, Double>> list, String city) {
    Double allValues = 0.0;
    int divider = 0;
    String previousDay = "";
    List<Pair<String, Double>> dailyAverages = new ArrayList<>();

    for (Pair<String, Double> pair : list) {
      String day = dateSplitter(pair.getKey(), "day");
      if(previousDay.equals("")) {
        previousDay = day;
      }
      if(!previousDay.equals(day)) {
        dailyAverages.add(new Pair<>(previousDay, allValues / divider));
        divider = 0;
        allValues = 0.0;
        previousDay = day;
      }
      Double value = pair.getValue();
      allValues += value;
      divider++;
    }
    for (Pair<String, Double> r : dailyAverages) {
      System.out.println("Day: " + r.getKey() + "   Average temperature of " + city + " : " + Math.round(r.getValue() * 10) / 10.0);
    }
  }

  /**
   * Created by Miikka Venäläinen
   * Method for calculating daily minimum and maximum temperatures
   *
   * @param list List of Pair which contains time and value information
   * @param city Name of the city
   */
  public void minMaxTemperature(List<Pair<String, Double>> list, String city) {
    double highPoint = -100.0;
    double lowPoint = 100.0;
    String previousDay = "";
    HashMap<String, HashMap<String, Double>> temps = new HashMap<>();

    for (Pair<String, Double> pair : list) {
      String day = dateSplitter(pair.getKey(), "day");
      if(previousDay.equals("")) {
        previousDay = day;
      }
      if(!previousDay.equals(day)) {
        HashMap<String, Double> t = new HashMap<>();
        t.put("highPoint", highPoint);
        t.put("lowPoint", lowPoint);
        temps.put(day, t);
        System.out.println(day + " in " + city + " highest temperature: " + highPoint + "c and lowest temperature: " + lowPoint + "c");
        highPoint = -100.0;
        lowPoint = 100.0;
        previousDay = day;
      }
      if(pair.getValue() > highPoint) {
        highPoint = pair.getValue();
      }
      if(pair.getValue() < lowPoint) {
        lowPoint = pair.getValue();
      }
    }
  }

  @Override
  public void start(Stage stage) throws IOException {
    stage.setTitle("Hello World!");
    Button btn = new Button();
    btn.setText("Say 'Hello World'");
    btn.setOnAction(event -> System.out.println("Hello World!"));

    StackPane root = new StackPane();
    root.getChildren().add(btn);

    ArrayList<String> places = new ArrayList<>();
    places.add("Joensuu");
    //places.add("Kuopio");

    ArrayList<String> params = new ArrayList<>();
    params.add("temperature");
    //params.add("windspeedms");

    String stime = dateFormatter(2021, 6, 1, 0, 0); //starttime
    String etime = dateFormatter(2021, 7, 24, 0, 0); //endtime

    List<Pair<String, Double>> list = weatherInformation(places, params, "" , "");
    System.exit(0);
  }

  public static void main(String[] args) {
    launch();
  }

}