package fi.tuni.compse110.project.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

/**
 * JavaFX App
 */
public class APItest{


  private static APItest single_instance = null;

  private APItest(){

  }

  // Digitraffic URL
  static final String baseURL = "https://tie.digitraffic.fi";

  /* ROAD CAMERA */

  // Data from road cameras. Add "/{id}" if spesific camera
  static final String cameraData = "https://tie.digitraffic.fi/api/v1/data/camera-data";

  // Search for camera image history "={preset or camera id}". Returns link to
  // images
  static final String cameraHistory = "https://tie.digitraffic.fi/api/v3/data/camera-history/history?id=";

  /* ROAD WEATHER */

  // Weather data. Add "/{id}"
  static final String weatherData = "https://tie.digitraffic.fi/api/v1/data/weather-data";

  // Weather data history. Add "/{stationId}"
  static final String weatherHistory = "https://tie.digitraffic.fi/api/beta/weather-history-data";

  /* ROAD CONDITION */

  // Forecasts for roads
  // For coordinates use
  // "/{minLongitude}/{minLatitude}/{maxLongitude}/{maxLatitude}"
  // For spesific road use "/{roadNumber}"
  static final String roadForecast = "https://tie.digitraffic.fi/api/v3/data/road-conditions";

  /* Maintenance API calls */
  static final String maintenanceTasks = baseURL + "/api/maintenance/v1/tracking/tasks";
  static final String maintenanceRoutes = baseURL + "/api/maintenance/v1/tracking/routes";

  /* Traffic message API calls */
  static final String message = baseURL + "/api/traffic-message/v1/messages";


  /* FMI WEATHER DATA */

  /* Search id  + simple/timevaluepair + & + place/starttime/endtime/timestep/parameters/crs/bbox/fmisid/maxlocation/geoid/wmo */
  static final String place = "&place=";
  static final String starttime = "&starttime=";
  static final String endtime = "&endtime=";
  static final String timestep = "&timestep=";
  static final String parameters = "&parameters="; //temperature, windspeedms
  static final String crs = "&crs=";
  static final String bbox = "&bbox=";
  static final String fmisid = "&fmisid=";
  static final String maxlocation = "&maxlocation=";
  static final String geoid = "&geoid=";
  static final String wmo = "&wmo=";

  //Base URL
  static final String FMIBaseURL = "https://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&storedquery_id=";

  //Weather observations for cities as time value pairs
  static final String cityObservation = "fmi::observations::weather::cities::timevaluepair";

  //Daily Weather Observations
  static final String dailyObservation = "fmi::observations::weather::daily::timevaluepair";

  //Hourly Weather Observations
  static final String hourlyObservation = "fmi::observations::weather::hourly::timevaluepair";

  //Instantaneous Weather Observations
  static final String instObservation = "fmi::observations::weather::timevaluepair";

  //Instantaneous Road Weather Observations
  static final String roadObservation = "livi::observations::road::default::timevaluepair";

  //Hourly Air Quality Observations
  static final String airQuality = "urban::observations::airquality::hourly::timevaluepair";

  //Radioactivity in outdoor air
  static final String radioActivity = "stuk::observations::air::radionuclide-activity-concentration::latest::simple";

  /* FMI FORECAST */

  //Forecast for cities
  static final String forecast = "ecmwf::forecast::surface::point::timevaluepair";

  public static APItest getInstance(){
    if (single_instance == null)
      single_instance = new APItest();

    return single_instance;
  }


  // This was start(Stage stage) before (not needed)
  public void testing() throws IOException {

    ArrayList<String> places = new ArrayList<>();
    places.add("Tampere");
    places.add("Rovaniemi");

    ArrayList<String> params = new ArrayList<>();
    params.add("temperature");
    params.add("windspeedms");
    params.add("winddirection");
    params.add("pressure");
    params.add("humidity");
    params.add("windgust");
    params.add("totalcloudcover");

    String stime = dateFormatter(2022, 10, 6, 0, 0); //starttime
    String etime = dateFormatter(2022, 10, 13, 0, 0); //endtime

    getAllCities();
    getAllTasks();
    weatherInformation(places, params, stime , etime);

    //System.exit(0);
  }

  /**
   * Created by Miikka Venäläinen
   * A method for GET requests
   *
   * @param u URL address for a request
   * @return Returns API request return value as a String
   * @throws IOException
   */
  public String getRequest(String u, Boolean isDigiTraffic) throws IOException {
    URL url = new URL(u);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setReadTimeout(20000);
    connection.setConnectTimeout(20000);
    BufferedReader br;
    if(isDigiTraffic) {
      connection.setRequestMethod("GET");
      //connection.setRequestProperty("Accept", "application/json");
      //connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Accept-Encoding", "gzip");
      br = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
    } else {
      br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }
    StringBuffer sb = new StringBuffer();
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
  public String dateSplitter(String date, String argument) {
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
  public String dateFormatter(int year, int month, int day, int hours, int minutes) {
    String date;
    DateTimeFormatter dateForm = DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.US).withResolverStyle(ResolverStyle.STRICT);
    date = dateForm.format(LocalDate.of(year, month, day)) + "T" + LocalTime.of(hours, minutes) + ":00Z";
    return date;
  }


/*
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
  }*/


  /**
   * Created by Miikka Venäläinen
   * Method for a query with user given parameters
   *
   * @param places List of cities
   * @param params List of wanted parameters
   * @return List of Pairs of Time and Value
   * @throws IOException
   */
  public void weatherInformation(ArrayList<String> places, ArrayList<String> params, String startingTime, String endingTime) throws IOException {
    String url = FMIBaseURL + dailyObservation;
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
    JSONObject jo = XML.toJSONObject(getRequest(url, false));
    filterTemperatureTimeValuePair(jo, params.size() > 1 || places.size() > 1);
  }

  public void roadInformation(ArrayList<String> places, ArrayList<String> params, String startingTime, String endingTime) throws IOException {
    String url = FMIBaseURL + dailyObservation;
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
    JSONObject jo = new JSONObject(getRequest(url, false));
    filterTemperatureTimeValuePair(jo, params.size() > 1 || places.size() > 1);
  }

  public List<String> getAllTasks() throws IOException {
    JSONArray ja = new JSONArray(getRequest(maintenanceTasks, true));
    List<String> tasks = new ArrayList<>();
    for(int i = 0; i < ja.length(); i++) {
      JSONObject jo = (JSONObject) ja.get(i);
      tasks.add(jo.get("nameEn").toString());
    }
    System.out.println(tasks);
    double minLong = 25.72088;
    double minLat = 62.24147;
    double maxLong = 25.8;
    double maxLat = 62.3;

    String url = roadForecast + "/" + minLong + "/" + minLat + "/" + maxLong + "/" + maxLat;
    System.out.println(url);
    JSONObject data = new JSONObject(getRequest(url, true));
    getRoadConditions(data);
    return tasks;
  }

  /**
   * Created by Miikka Venäläinen
   * Function to get all the necessary road forecast data from JSON data
   *
   * @param data Data contains all JSON data of the query
   */
  public void getRoadConditions(JSONObject data) {
    JSONArray weatherData = data.getJSONArray("weatherData");
    for(int i = 0; i < weatherData.length(); i++) {
      JSONObject cond = (JSONObject) weatherData.get(i);
      String roadNumber = cond.get("id").toString().split("_")[0];
      System.out.println("--" + roadNumber + "--");
      JSONArray roadConditions = cond.getJSONArray("roadConditions");
      for(int j = 0; j < roadConditions.length(); j++) {
        JSONObject condition= (JSONObject) roadConditions.get(j);
        String forecastTime = condition.getString("forecastName");
        String roadTemperature = condition.getString("roadTemperature");
        String temperature = condition.getString("temperature");
        String overallRoadCondition = condition.getString("overallRoadCondition");
        boolean dayLight = condition.getBoolean("daylight");
        System.out.println("In " + forecastTime + " road temperature is " + roadTemperature + " and temperature is " + temperature);
        if(dayLight) {
          System.out.println("Sun is shining");
        } else {
          System.out.println("Sun is not shining");
        }
        System.out.print("Overall road condition is " + overallRoadCondition);
        if(condition.has("forecastConditionReason")) {
          JSONObject jsonObject = condition.getJSONObject("forecastConditionReason");
          if(jsonObject.has("roadCondition")) {
            String roadSurface = jsonObject.getString("roadCondition");
            System.out.println(" and surface of the road is " + roadSurface.toLowerCase());
          } else {
            System.out.println();
          }
        } else {
          System.out.println();
        }
        System.out.println();
      }
    }
  }

  /**
   * Created by Miikka Venäläinen
   * Function gets all the biggest cities where data can be queried
   *
   * @return Returns a sorted list of cities
   * @throws IOException
   */
  public List<String> getAllCities() throws IOException {
    String url = "https://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&storedquery_id=ecmwf::forecast::surface::cities::timevaluepair&parameters=temperature";
    JSONObject jo = XML.toJSONObject(getRequest(url, false));
    List<JSONObject> jsonObjects = new ArrayList<>();
    List<String> cities = new ArrayList<>();
    JSONObject info = jo.getJSONObject("wfs:FeatureCollection");

    for(int i = 0; i < info.getJSONArray("wfs:member").length(); i++) {
      jsonObjects.add((JSONObject) info.getJSONArray("wfs:member").get(i));
    }
    for (JSONObject j : jsonObjects) {
      JSONObject observation = j.getJSONObject("omso:PointTimeSeriesObservation");
      JSONObject interest = observation.getJSONObject("om:featureOfInterest");
      JSONObject sampling = interest.getJSONObject("sams:SF_SpatialSamplingFeature");
      JSONObject sampledFeature = sampling.getJSONObject("sam:sampledFeature");
      JSONObject locCollection = sampledFeature.getJSONObject("target:LocationCollection");
      JSONObject member = locCollection.getJSONObject("target:member");
      JSONArray location = (JSONArray) member.getJSONObject("target:Location").get("gml:name");
      JSONObject locContent = (JSONObject) location.get(0);
      cities.add(locContent.get("content").toString());
    }
    cities.sort(Comparator.naturalOrder());
    System.out.println(cities);
    return cities;
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
  public List<Pair<String, List<Pair<String, List<Pair<String, Double>>>>>> filterTemperatureTimeValuePair(JSONObject jo, Boolean multipleParams) {
    List<JSONObject> jsonObjects = new ArrayList<>();
    System.out.println(jo.length());
    JSONObject info = jo.getJSONObject("wfs:FeatureCollection");

    if(multipleParams) {
      for(int i = 0; i < info.getJSONArray("wfs:member").length(); i++) {
        jsonObjects.add((JSONObject) info.getJSONArray("wfs:member").get(i));
      }
    } else {
      jsonObjects.add(info.getJSONObject("wfs:member"));
    }

    String city;
    String currentCity = "";
    List<Pair<String, List<Pair<String, List<Pair<String, Double>>>>>> allData = new ArrayList<>();
    List<Pair<String, List<Pair<String, Double>>>> parameterDateValueListList = new ArrayList<>(); // List for parameter + (date + value) list pairs
    for (JSONObject j : jsonObjects) {

      JSONObject observation = j.getJSONObject("omso:PointTimeSeriesObservation");
      JSONObject interest = observation.getJSONObject("om:featureOfInterest");
      JSONObject sampling = interest.getJSONObject("sams:SF_SpatialSamplingFeature");
      JSONObject sampledFeature = sampling.getJSONObject("sam:sampledFeature");
      JSONObject locCollection = sampledFeature.getJSONObject("target:LocationCollection");
      JSONObject member = locCollection.getJSONObject("target:member");
      JSONArray location = (JSONArray) member.getJSONObject("target:Location").get("gml:name");
      JSONObject locContent = (JSONObject) location.get(0);
      city = locContent.get("content").toString();
      if(currentCity.equals("")) {
        currentCity = city;
      }

      JSONObject results = observation.getJSONObject("om:result");
      JSONObject timeSeries = results.getJSONObject("wml2:MeasurementTimeseries");
      String parameter = sampling.get("gml:id").toString().split("-")[sampling.get("gml:id").toString().split("-").length - 1];
      JSONArray points = timeSeries.getJSONArray("wml2:point");

      List<Pair<String, Double>> list = new ArrayList<>(); // List for date + value pairs
      for(int i = 0; i < points.length(); i++) {
        JSONObject element = (JSONObject) points.get(i);
        JSONObject innerElement = (JSONObject) element.get("wml2:MeasurementTVP");
        String time = (String) innerElement.get("wml2:time");
        String value = innerElement.get("wml2:value").toString();
        Pair<String, Double> pair = new Pair<>(time, Double.valueOf(value)); // Pair of date + value
        list.add(pair); // Add date + value to list
      }

      Pair<String, List<Pair<String, Double>>> parameterDateValuePair = new Pair<>(parameter, list); // Pair for parameter + (date + value) list
      if(!city.equals(currentCity)) {
        Pair<String, List<Pair<String, List<Pair<String, Double>>>>> pair = new Pair<>(currentCity, parameterDateValueListList);
        allData.add(pair);
        parameterDateValueListList = new ArrayList<>();
      }
      currentCity = city;
      parameterDateValueListList.add(parameterDateValuePair);
    }
    Pair<String, List<Pair<String, List<Pair<String, Double>>>>> pair = new Pair<>(currentCity, parameterDateValueListList);
    allData.add(pair);
    for (Pair<String, List<Pair<String, List<Pair<String, Double>>>>> p : allData) {
      String cityCopy = p.getKey();
      for (Pair<String, List<Pair<String, Double>>> q : p.getValue()) {
        String parameterCopy = q.getKey();
        averageTemperature(q.getValue(), cityCopy, parameterCopy);
        minMaxTemperature(q.getValue(), cityCopy, parameterCopy);
        System.out.println();
      }
    }
    return allData;
  }

  /**
   * Created by Miikka Venäläinen
   * Method for calculations of daily average values like temperature
   *
   * @param list List of Pair which contains time and value information
   * @param city Name of the city
   */
  public List<Pair<String, Double>> averageTemperature(List<Pair<String, Double>> list, String city, String parameter) {
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
      System.out.println("Day: " + r.getKey() + "   Average " + parameter + " of " + city + " : " + Math.round(r.getValue() * 10) / 10.0);
    }
    return dailyAverages;
  }

  /**
   * Created by Miikka Venäläinen
   * Method for calculating daily minimum and maximum temperatures
   *
   * @param list List of Pair which contains time and value information
   * @param city Name of the city
   */
  public HashMap<String, HashMap<String, Double>> minMaxTemperature(List<Pair<String, Double>> list, String city, String parameter) {
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
        System.out.println("Day: " + day + " in " + city + " highest " + parameter + ": " + highPoint + " and lowest " + parameter + ": " + lowPoint);
        highPoint = -100.0;
        lowPoint = 100.0;
        previousDay = day;
      }
      if(pair.getValue() > highPoint || highPoint == -100.0) {
        highPoint = pair.getValue();
      }
      if(pair.getValue() < lowPoint || lowPoint == 100.0) {
        lowPoint = pair.getValue();
      }
    }
    return temps;
  }

  /*public static void main(String[] args) {
    launch();
  }*/

}