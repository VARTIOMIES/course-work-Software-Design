package fi.tuni.compse110.project.API;
/*
 * Creator Miikka Venäläinen
 *
 * This file contains all the logic related to digitraffic data
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONObject;

public class RoadDataProvider {

  private static HashMap<String, String> tasks = new HashMap<>();

  // Digitraffic URL
  final static String baseURL = "https://tie.digitraffic.fi";

  // All possible locations
  String locations = "https://tie.digitraffic.fi/api/v3/metadata/locations";

  // All weather stations
  String weatherStations = "https://tie.digitraffic.fi/api/v3/metadata/weather-stations";

  /* ROAD CAMERA */

  //URL for getting all stations and their locations. Includes id for weather info
  String allCameraStations = "https://tie.digitraffic.fi/api/v3/metadata/camera-stations";

  // Data from road cameras. Add "/{id}" if specific camera
  final static String cameraData = "https://tie.digitraffic.fi/api/v1/data/camera-data";

  // Search for camera image history "={preset or camera id}". Returns links to images
  final static String cameraHistory = "https://tie.digitraffic.fi/api/v3/data/camera-history/history?id=";

  /* ROAD WEATHER */

  // Weather data for camera id. Add "/{id}"
  // Id can be acquired from allCameraStations
  final static String weatherData = "https://tie.digitraffic.fi/api/v1/data/weather-data";

  // Weather data history. Add "/{stationId}"
  final static String weatherHistory = "https://tie.digitraffic.fi/api/beta/weather-history-data";

  /* ROAD AVERAGE SPEED AND PASS INFORMATION */

  // Average speeds and passes for a set time period
  final static String tmsData = "https://tie.digitraffic.fi/api/v1/data/tms-data/";

  // All possible stations
  final static String allStations = "https://tie.digitraffic.fi/api/v3/metadata/tms-stations";

  /* ROAD CONDITION */

  // Forecasts for roads
  // For coordinates use
  // "/{minLongitude}/{minLatitude}/{maxLongitude}/{maxLatitude}"
  // For spesific road use "/{roadNumber}"
  final static String roadForecast = "https://tie.digitraffic.fi/api/v3/data/road-conditions";

  /* Maintenance API calls */
  final static String maintenanceTasks = baseURL + "/api/maintenance/v1/tracking/tasks";
  final static String maintenanceRoutes = baseURL + "/api/maintenance/v1/tracking/routes";

  /* Traffic message API calls */
  final static String message = baseURL + "/api/traffic-message/v1/messages";
  final static String iHours = "?inactiveHours=";
  final static String sitType = "&situationType=";

  /* Weather logos */

  final static HashMap<String, String> weatherSymbols = new HashMap<>();
  final static String symbolBaseURL = "https://www.vaisala.com/sites/default/files/2021-05/";


  /**
   * Created by Miikka Venäläinen
   *
   * A method for acquiring weather symbol image from vaisala
   *
   * @param symbol Is the symbol code of the weather
   * @return Returns url address for the image of the weather symbol
   */
  public static Image getSymbol(String symbol) {
    addSymbols();
    String url = symbolBaseURL + symbol + ".png";
    if(symbol.equals("d000") || symbol.equals("d100") || symbol.equals("d200") || symbol.equals("d300") || symbol.equals("d400")) {
      url = weatherSymbols.get(symbol);
    }
    return new Image(url);
  }

  /**
   * Created by Miikka Venäläinen
   *
   * Method for saving few weather symbols from vaisala API into a HashMap
   * because their url style doesn't match to others'
   */
  private static void addSymbols() {
    weatherSymbols.clear();
    weatherSymbols.put("d000", "https://www.vaisala.com/sites/default/files/images/d000_0.png");
    weatherSymbols.put("d100", "https://www.vaisala.com/sites/default/files/2021-04/d100_0.png");
    weatherSymbols.put("d200", "https://www.vaisala.com/sites/default/files/2021-04/d200.png");
    weatherSymbols.put("d300", "https://www.vaisala.com/sites/default/files/images/d300.png");
    weatherSymbols.put("d400", "https://www.vaisala.com/sites/default/files/images/d400.png");
  }

  /**
   * Created by Miikka Venäläinen
   *
   * Method searches for maintenance data from given coordinates.
   * It is also possible to give a list of task types and/or a starting time
   * and/or ending time.
   * Time between From and To -parameter values must be less or equal to 24 hours.
   *
   * @param coords A list of coordinates
   * @param taskIds A list of tasks user wants to see
   * @param from From a given time. Time format 2022-10-11T00:00:00Z
   * @param to To a given time. Time format 2022-10-11T00:00:00Z
   * @return Returns a list of maintenance tasks. List contains MaintenanceTask
   * objects and those objects include all the data
   * @throws IOException In case of an exception
   */
  public static List<MaintenanceTask> getMaintenanceData(ArrayList<Double> coords, ArrayList<String> taskIds, String from, String to) throws IOException {

    getAllTasks();
    String url = maintenanceRoutes;
    if(coords.size() == 4) {
      url += "?xMin=" + coords.get(0) + "&yMin=" + coords.get(1) + "&xMax=" + coords.get(2) + "&yMax=" + coords.get(3);
    }
    if(!from.equals("")) {
      url += "&endFrom=" + from;
    }
    if(!to.equals("")) {
      url += "&endBefore=" + to;
    }
    if(taskIds.size() > 0) {
      for(String task : taskIds) {
        url += "&taskId=" + task;
      }
    }
    JSONObject maintenance = new JSONObject(APICall.getRequest(url, true));

    if(maintenance.has("message")) {
      System.out.println(maintenance.getString("message"));
      //System.exit(0);
    }

    HashMap<String, Integer> amounts = new HashMap<>();
    List<MaintenanceTask> allTasks = new ArrayList<>();

    JSONArray list = maintenance.getJSONArray("features");
    for(int i = 0; i < list.length(); i++) {
      JSONObject feature = list.getJSONObject(i);
      JSONObject properties = feature.getJSONObject("properties");
      MaintenanceTask maintenanceTask = new MaintenanceTask();
      String startTime = properties.getString("startTime");
      maintenanceTask.setStartTime(startTime);
      String endTime = properties.getString("endTime");
      maintenanceTask.setEndTime(endTime);
      String source  = properties.getString("source");
      maintenanceTask.setSource(source);
      ArrayList<String> maintTasks = new ArrayList<>();
      for (int j = 0; j < properties.getJSONArray("tasks").length(); j++) {
        String task = tasks.get(properties.getJSONArray("tasks").get(j).toString());
        maintTasks.add(task);
        if(!amounts.containsKey(task)) {
          amounts.put(task, 1);
        } else {
          amounts.replace(task, amounts.get(task) + 1);
        }
      }
      maintenanceTask.setTasks(maintTasks);
      allTasks.add(maintenanceTask);
    }
    return allTasks;
  }

  static class AccordingTime implements Comparator<RoadCondition> {
    public int compare(RoadCondition r1, RoadCondition r2)
    {
      return r1.getForecastTime().compareTo(r2.getForecastTime());
    }
  }



  /**
   * Created by Miikka Venäläinen
   *
   * Function to get all the necessary road forecast data from JSON data.
   *
   * @param roadNum Integer value of the road number where user wants road condition data from
   * @param coords List of coordinates where user wants road condition data from
   * @return Returns a TreeMap which contains road numbers and for each road number
   * there is a list of its road sections and forecasts for each of the section in a HashMap
   *
   * {roadNumber : sections {{
   *                          section 1 : forecast 0h
   *                          section 1 : forecast 2h
   *                          ...
   *                        },{
   *                          section 2 : forecast 0h
   *                          section 2 : forecast 2h
   *                          ...
   *                        },{
   *                          ....
   *                        }
   *                       }
   *  },{
   *    ...
   *  }
   *
   */
  public static TreeMap<Integer, LinkedHashMap<String, ArrayList<RoadCondition>>> getRoadConditions(int roadNum, ArrayList<Double> coords) throws IOException {
    String url = roadForecast;
    TreeMap<Integer, LinkedHashMap<String, ArrayList<RoadCondition>>> values = new TreeMap<>();

    if(roadNum > 0) {
      url += "/" + roadNum;
    } else if(coords.size() == 4) {
      url += "/" + coords.get(0) + "/" + coords.get(1) + "/" + coords.get(2) + "/" + coords.get(3);
    }
    JSONObject data = new JSONObject(APICall.getRequest(url, true));
    JSONArray weatherData = data.getJSONArray("weatherData");
    for(int i = 0; i < weatherData.length(); i++) {
      JSONObject cond = (JSONObject) weatherData.get(i);
      String roadNumber = cond.get("id").toString().split("_")[0];
      JSONArray roadConditions = cond.getJSONArray("roadConditions");
      for(int j = 0; j < roadConditions.length(); j++) {
        RoadCondition roadCondition = new RoadCondition(cond.get("id").toString(), Integer.parseInt(roadNumber));
        JSONObject condition= (JSONObject) roadConditions.get(j);
        String forecastTime = condition.getString("forecastName");
        roadCondition.setForecastTime(forecastTime);
        String roadTemperature = condition.getString("roadTemperature");
        roadCondition.setRoadTemperature(roadTemperature);
        String temperature = condition.getString("temperature");
        roadCondition.setTemperature(temperature);
        int windSpeed = condition.getInt("windSpeed");
        roadCondition.setWindSpeed(windSpeed);
        int windDirection = condition.getInt("windDirection");
        roadCondition.setWindDirection(windDirection);
        String overallRoadCondition = condition.getString("overallRoadCondition");
        roadCondition.setOverallRoadCondition(overallRoadCondition);
        boolean dayLight = condition.getBoolean("daylight");
        roadCondition.setDayLight(dayLight);
        String weatherSymbol = condition.getString("weatherSymbol");
        roadCondition.setWeatherSymbol(weatherSymbol);
        if(condition.has("forecastConditionReason")) {
          JSONObject jsonObject = condition.getJSONObject("forecastConditionReason");
          if(jsonObject.has("precipitationCondition")) {
            String roadSurface = jsonObject.getString("precipitationCondition");
            roadCondition.setPrecipitationCondition(roadSurface.toLowerCase());
          }
          if(jsonObject.has("roadCondition")) {
            String roadSurface = jsonObject.getString("roadCondition");
            roadCondition.setRoadCondition(roadSurface.toLowerCase());
          }
          if(jsonObject.has("windCondition")) {
            String roadSurface = jsonObject.getString("windCondition");
            roadCondition.setWindCondition(roadSurface.toLowerCase());
          }
          if(jsonObject.has("freezingRainCondition")) {
            boolean roadSurface = jsonObject.getBoolean("freezingRainCondition");
            roadCondition.setFreezingRainCondition(roadSurface);
          }
          if(jsonObject.has("winterSlipperiness")) {
            boolean roadSurface = jsonObject.getBoolean("winterSlipperiness");
            roadCondition.setWinterSlipperiness(roadSurface);
          }
          if(jsonObject.has("visibilityCondition")) {
            String roadSurface = jsonObject.getString("visibilityCondition");
            roadCondition.setVisibilityCondition(roadSurface.toLowerCase());
          }
          if(jsonObject.has("frictionCondition")) {
            String roadSurface = jsonObject.getString("frictionCondition");
            roadCondition.setFrictionCondition(roadSurface.toLowerCase());
          }
          if(jsonObject.has("time")){
            String time = jsonObject.getString("time");
            roadCondition.setTime(time.toLowerCase());
          }
        }
        if(values.containsKey(Integer.parseInt(cond.get("id").toString().split("_")[0]))) {
          if(values.get(Integer.parseInt(cond.get("id").toString().split("_")[0])).containsKey(forecastTime)){
            values.get(Integer.parseInt(cond.get("id").toString().split("_")[0])).get(forecastTime).add(roadCondition);
          } else {
            ArrayList<RoadCondition> r = new ArrayList<>();
            r.add(roadCondition);
            values.get(Integer.parseInt(cond.get("id").toString().split("_")[0])).put(forecastTime, r);
          }
        } else {
          LinkedHashMap<String, ArrayList<RoadCondition>> a = new LinkedHashMap<>();
          ArrayList<RoadCondition> r = new ArrayList<>();
          r.add(roadCondition);
          a.put(forecastTime, r);
          values.put(Integer.parseInt(cond.get("id").toString().split("_")[0]), a);
        }
      }
    }
    values.forEach((key, value) -> {
      //System.out.println("KEY IS " + key);
      value.forEach( (k, v) -> {
        //System.out.println("FORECAST TIME " + k);
        for(RoadCondition r : v) {
          //System.out.println(r.getForecastTime() + " " + r.getSection());
        }
      });
      //System.out.println();
    });
    values.forEach((k,v) -> v.forEach((key, value) -> {
      value.sort(Comparator.comparing(RoadCondition::getSection));
    }));
    return values;
  }

  private static TreeMap<Integer, LinkedHashMap<String, ArrayList<RoadCondition>>> sortRoadConditions(TreeMap<Integer, LinkedHashMap<String, ArrayList<RoadCondition>>> allValues) {
    allValues.forEach((k,v) -> v.forEach((key, value) -> {
      value.sort(Comparator.comparing(RoadCondition::getSection));
    }));
    return allValues;
  }

  /**
   * @author Onni Merilä
   * Function used to parse the wanted data from the bigger data structure
   *
   * @param roadNumber the number of the road (used to get the data of the whole road from the above function
   * @param sectionArrayIndex ,index of the section in the sorted arraylist of all sections
   * @param coords, coordinates used for the data fetching from getRoadConditions() function
   * @return Returns a list of one specific sections roadcondition data.
   *
   */
  public static List<RoadCondition> getSpecificSectionRoadCondition(int roadNumber,
                                                                    int sectionArrayIndex,
                                                                    ArrayList<Double> coords) throws IOException {

    TreeMap<Integer, LinkedHashMap<String, ArrayList<RoadCondition>>> roadConditionData = RoadDataProvider.getRoadConditions(roadNumber,coords);
    LinkedHashMap<String, ArrayList<RoadCondition>> forecastList = roadConditionData.get(roadNumber);

    List<RoadCondition> specificSectionForecast = new ArrayList<>();
    try {
      for (ArrayList<RoadCondition> allSections:forecastList.values()) {
        specificSectionForecast.add(allSections.get(sectionArrayIndex));
      }
    } catch (java.lang.NullPointerException e) {
      System.out.println("Index out of bounds");
    }
    

    return specificSectionForecast;
  }


  /**
   * Created by Miikka Venäläinen
   *
   * Method gets all possible maintenance types and their meanings
   * and saves them for further usage
   *
   * @throws IOException In case of an exception
   */
  public static void getAllTasks() throws IOException {
    JSONArray ja = new JSONArray(APICall.getRequest(maintenanceTasks, true));
    for(int i = 0; i < ja.length(); i++) {
      JSONObject jo = (JSONObject) ja.get(i);
      tasks.put(jo.get("id").toString(), jo.get("nameEn").toString());
    }
  }

  /**
   * Created by Miikka Venäläinen
   *
   * Function to get all traffic related messages of certain type and from
   * certain time
   *
   * @param hours How many hours ago
   * @param situationType What kind of situation data user wants to see
   * @return Returns a list of traffic messages. Type is TrafficMessage which contains
   * all necessary data
   * @throws IOException If error occurs
   */
  public static List<TrafficMessage> getTrafficMessages(int hours, String situationType) throws IOException {

    String url = message + iHours + hours + "&includeAreaGeometry=false" + sitType + situationType;
    JSONObject messages = new JSONObject(APICall.getRequest(url, true));

    List<TrafficMessage> allMessages = new ArrayList<>();

    JSONArray features = messages.getJSONArray("features");
    for(int i = 0; i < features.length(); i++) {
      JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
      JSONArray announcements = properties.getJSONArray("announcements");
      for (int j = 0; j < announcements.length(); j++) {
        TrafficMessage trafficMessage = new TrafficMessage();
        JSONObject announcement = announcements.getJSONObject(j);
        String title = announcement.get("title").toString();
        trafficMessage.setTitle(title);
        JSONObject location = announcement.getJSONObject("location");
        String description = location.getString("description");
        trafficMessage.setDescription(description);
        if(announcement.has("comment")) {
          trafficMessage.setComment(announcement.get("comment").toString());
        }
        JSONObject locationDetail = announcement.getJSONObject("locationDetails");
        if(locationDetail.has("roadAddressLocation")) {
          JSONObject addressLocation = locationDetail.getJSONObject("roadAddressLocation");
          JSONObject primaryPoint = addressLocation.getJSONObject("primaryPoint");
          trafficMessage.setMunicipality(primaryPoint.getString("municipality"));
        }
        JSONObject time = announcement.getJSONObject("timeAndDuration");
        String startTime = time.getString("startTime");
        trafficMessage.setStartTime(startTime);
        JSONArray feats = announcement.getJSONArray("features");
        ArrayList<String> f = new ArrayList<>();
        for (int k = 0; k < feats.length(); k++) {
          JSONObject name = feats.getJSONObject(k);
          f.add(name.getString("name"));
        }
        trafficMessage.setFeatures(f);
        allMessages.add(trafficMessage);
      }
    }
    return allMessages;
  }
}

