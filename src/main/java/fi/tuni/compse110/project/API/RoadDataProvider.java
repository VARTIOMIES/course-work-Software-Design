package fi.tuni.compse110.project.API;
/*
 * Creator Miikka Venäläinen
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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


  public static HashMap<String, Integer> getMaintenanceData(ArrayList<Double> coords, ArrayList<String> taskIds, String from, String to) throws IOException {

    String url = maintenanceRoutes;
    if(coords.size() == 4) {
      url += "?xMin=" + coords.get(0) + "&yMin=" + coords.get(1) + "&xMax" + coords.get(2) + "&yMax=" + coords.get(3);
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
    System.out.println("GET MAINTENANCE DATA");
    System.out.println(url);
    JSONObject maintenance = new JSONObject(APICall.getRequest(url, true));

    if(maintenance.has("message")) {
      System.out.println(maintenance.getString("message"));
      System.exit(0);
    }

    HashMap<String, Integer> amounts = new HashMap<>();
    HashMap<String, ArrayList<String>> allTasks = new HashMap<>();

    JSONArray list = maintenance.getJSONArray("features");
    for(int i = 0; i < list.length(); i++) {
      JSONObject feature = list.getJSONObject(i);
      JSONObject properties = feature.getJSONObject("properties");
      String startTime = properties.getString("startTime");
      String endTime = properties.getString("endTime");
      String source  = properties.getString("source");
      System.out.println(startTime + " - " + endTime);
      System.out.println(source);
      for (int j = 0; j < properties.getJSONArray("tasks").length(); j++) {
        String task = tasks.get(properties.getJSONArray("tasks").get(j).toString());
        System.out.print(task);
        System.out.print(", ");
        if(!amounts.containsKey(task)) {
          amounts.put(task, 1);
        } else {
          amounts.replace(task, amounts.get(task) + 1);
        }

      }
      System.out.println();
      System.out.println();
    }
    amounts.forEach((key, value) -> System.out.println(key + " : " + value));
    return amounts;
  }



  /**
   * Created by Miikka Venäläinen
   * Function to get all the necessary road forecast data from JSON data
   *
   * @return
   */
  public static void getRoadConditions(int roadNum, ArrayList<Double> coords) throws IOException {
    String url = roadForecast;
    HashMap<Integer, ArrayList<HashMap<Integer, RoadCondition>>> allValues = new HashMap<>();
    if(roadNum > 0) {
      url += "/" + roadNum;
    } else if(coords.size() == 4) {
      url += "/" + coords.get(0) + "/" + coords.get(1) + "/" + coords.get(2) + "/" + coords.get(3);
    }
    System.out.println("GET ROAD CONDITIONS");
    System.out.println(url);
    JSONObject data = new JSONObject(APICall.getRequest(url, true));
    JSONArray weatherData = data.getJSONArray("weatherData");
    for(int i = 0; i < weatherData.length(); i++) {
      JSONObject cond = (JSONObject) weatherData.get(i);
      String roadNumber = cond.get("id").toString().split("_")[0];
      System.out.println("--" + roadNumber + "--");
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
        System.out.println("In " + forecastTime + " road temperature is " + roadTemperature + " and temperature is " + temperature);
        if(dayLight) {
          System.out.println("Sun is shining");
        } else {
          System.out.println("It's dark in here");
        }
        System.out.print("Overall road condition is " + overallRoadCondition);
        if(condition.has("forecastConditionReason")) {
          JSONObject jsonObject = condition.getJSONObject("forecastConditionReason");
          if(jsonObject.has("precipitationCondition")) {
            String roadSurface = jsonObject.getString("precipitationCondition");
            roadCondition.setPrecipitationCondition(roadSurface.toLowerCase());
            System.out.print(" and surface of the road is " + roadCondition.getPrecipitationCondition());
          }
          if(jsonObject.has("roadCondition")) {
            String roadSurface = jsonObject.getString("roadCondition");
            roadCondition.setRoadCondition(roadSurface.toLowerCase());
            System.out.print(" and surface of the road is " + roadSurface.toLowerCase());
          }
          if(jsonObject.has("windCondition")) {
            String roadSurface = jsonObject.getString("windCondition");
            roadCondition.setWindCondition(roadSurface.toLowerCase());
            System.out.print(" and wind condition is " + roadSurface.toLowerCase());
          }
          if(jsonObject.has("freezingRainCondition")) {
            boolean roadSurface = jsonObject.getBoolean("freezingRainCondition");
            roadCondition.setFreezingRainCondition(roadSurface);
            if(roadSurface) {
              System.out.println("There is freezing rain");
            }
          }
          if(jsonObject.has("winterSlipperiness")) {
            boolean roadSurface = jsonObject.getBoolean("winterSlipperiness");
            roadCondition.setWinterSlipperiness(roadSurface);
            if(roadSurface) {
              System.out.println("Road is slippery");
            }
          }
          if(jsonObject.has("visibilityCondition")) {
            String roadSurface = jsonObject.getString("visibilityCondition");
            roadCondition.setVisibilityCondition(roadSurface.toLowerCase());
            System.out.println(" and visibility is " + roadSurface.toLowerCase());
          }
          if(jsonObject.has("frictionCondition")) {
            String roadSurface = jsonObject.getString("frictionCondition");
            roadCondition.setFrictionCondition(roadSurface.toLowerCase());
            System.out.println(" and surface of the road is " + roadSurface.toLowerCase());
          }
        } else {
          System.out.println();
        }
        System.out.println();
        System.out.println();
        HashMap<Integer, RoadCondition> h = new HashMap<>();
        h.put(Integer.parseInt(cond.get("id").toString().split("_")[1]), roadCondition);
        if(allValues.containsKey(Integer.parseInt(cond.get("id").toString().split("_")[0]))) {
          allValues.get(Integer.parseInt(cond.get("id").toString().split("_")[0])).add(h);
        } else {
          ArrayList<HashMap<Integer, RoadCondition>> a = new ArrayList<>();
          a.add(h);
          allValues.put(Integer.parseInt(cond.get("id").toString().split("_")[0]), a);
        }
      }
    }
    allValues.forEach((key, value) -> {
      System.out.println(key);
      for(HashMap<Integer, RoadCondition> values : value){
        values.forEach((k, v) -> {
          //System.out.println(v.getWindSpeed());
          //System.out.println(v.getWindDirection());
        });
      }
      System.out.println();
    });
    //WindDirectionGraph.graphDrawer(allValues);
  }


  public static void getAllTasks() throws IOException {
    JSONArray ja = new JSONArray(APICall.getRequest(maintenanceTasks, true));
    for(int i = 0; i < ja.length(); i++) {
      JSONObject jo = (JSONObject) ja.get(i);
      tasks.put(jo.get("id").toString(), jo.get("nameEn").toString());
    }
    System.out.println(tasks);
  }


  public static void getTrafficMessages(int hours, String situationType) throws IOException {

    String url = message + iHours + hours + "&includeAreaGeometry=false" + sitType + situationType;
    System.out.println("GET TRAFFIC MESSAGES");
    System.out.println(url);
    JSONObject messages = new JSONObject(APICall.getRequest(url, true));

    JSONArray features = messages.getJSONArray("features");
    for(int i = 0; i < features.length(); i++) {
      JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
      JSONArray announcements = properties.getJSONArray("announcements");
      for (int j = 0; j < announcements.length(); j++) {
        JSONObject announcement = announcements.getJSONObject(j);
        String title = announcement.get("title").toString();
        System.out.println(title);
        JSONObject location = announcement.getJSONObject("location");
        String description = location.getString("description");
        System.out.println(description);
        if(announcement.has("comment")) System.out.println(announcement.get("comment").toString());
        JSONObject locationDetail = announcement.getJSONObject("locationDetails");
        if(locationDetail.has("roadAddressLocation")) {
          JSONObject addressLocation = locationDetail.getJSONObject("roadAddressLocation");
          JSONObject primaryPoint = addressLocation.getJSONObject("primaryPoint");
          System.out.println(primaryPoint.getString("municipality"));
        }
        JSONObject time = announcement.getJSONObject("timeAndDuration");
        String startTime = time.getString("startTime");
        System.out.println("Time stamp: " + startTime);
        JSONArray feats = announcement.getJSONArray("features");
        for (int k = 0; k < feats.length(); k++) {
          JSONObject name = feats.getJSONObject(k);
          System.out.println(name.get("name"));
        }
      }
      System.out.println();
      System.out.println();
    }

  }


}

