package fi.tuni.compse110.project.API;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONObject;

public class LogicHandler {

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


  public String getMaintenanceComponent(ArrayList<Double> coords, ArrayList<String> taskIds, String from, String to)
      throws IOException {
    String url = maintenanceRoutes;
    if(coords.size() == 4) {
      if(coords.get(0) < coords.get(2) && 19 < coords.get(0) && coords.get(2) < 32){
        if(coords.get(1) < coords.get(3) && 59 < coords.get(1) && coords.get(3) < 72) {
          url += "?xMin=" + coords.get(0) + "&yMin=" + coords.get(1) + "&xMax=" + coords.get(2) + "&yMax=" + coords.get(3);
        } else {
          return "Latitude values are not valid. Values must be between 59 and 72 and minLat < maxLat";
        }
      } else {
        return "Longitude values are not valid. Values must be between 19 and 32 and minLong < maxLong";
      }
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
      return maintenance.getString("message");
    }
    return maintenance.toString();
  }
}
