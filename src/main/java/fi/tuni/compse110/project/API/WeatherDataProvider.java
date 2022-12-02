package fi.tuni.compse110.project.API;
/*
 * Creator Miikka Venäläinen
 *
 * This file contains all the logic related to FMI data
 */

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class WeatherDataProvider {

  // For URL creation
  final static String place = "&place=";
  final static String starttime = "&starttime=";
  final static String endtime = "&endtime=";
  final static String timestep = "&timestep=";
  final static String parameters = "&parameters="; //temperature, windspeedms, winddirection, pressure, humidity, windgust, totalcloudcover
  final static String crs = "&crs=";
  final static String bbox = "&bbox=";
  final static String fmisid = "&fmisid=";
  final static String maxlocation = "&maxlocation=";
  final static String geoid = "&geoid=";
  final static String wmo = "&wmo=";

  //Base URL
  final static String FMIBaseURL = "https://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&storedquery_id=";

  //Weather observations for cities as time value pairs
  final static String cityObservation = "fmi::observations::weather::cities::timevaluepair";

  //Daily Weather Observations
  final static String dailyObservation = "fmi::observations::weather::daily::timevaluepair";

  //Hourly Weather Observations
  final static String hourlyObservation = "fmi::observations::weather::hourly::timevaluepair";

  //Instantaneous Weather Observations
  final static String instObservation = "fmi::observations::weather::timevaluepair";

  //Instantaneous Road Weather Observations
  final static String roadObservation = "livi::observations::road::default::timevaluepair";

  //Hourly Air Quality Observations
  final static String airQuality = "urban::observations::airquality::hourly::timevaluepair";

  //Radioactivity in outdoor air
  final static String radioActivity = "stuk::observations::air::radionuclide-activity-concentration::latest::simple";

  /* FMI FORECAST */

  //Forecast for cities
  final static String forecast = "ecmwf::forecast::surface::point::timevaluepair";


  /**
   * Created by Miikka Venäläinen
   *
   * Method to create URL with user given parameters which are then passed onto
   * another method (createParameterTimeValuePair()) which can then be used to
   * calculate average parameters or min-max parameters.
   *
   * @param places List of cities
   * @param coordinates List of coordinates
   * @param params List of wanted parameters
   * @param startingTime Starting date
   * @param endingTime Ending date. Ending date must be after starting day
   * @return List of Pairs of Time and Value for every parameter and for every city.
   *         See the structure down below in createParameterTimeValuePair() function
   * @throws IOException In case of an error
   */
  public static ArrayList<WeatherData> weatherURLCreator(ArrayList<String> places, ArrayList<Double> coordinates,
      ArrayList<String> params, String startingTime, String endingTime) throws IOException {
    String url = FMIBaseURL + hourlyObservation;
    if(places.size() > 0) {
      for(String p : places) {
        url += place + p;
      }
    }
    // lon, lon, lat, lat
    else if(coordinates.size() == 4) {
      url += bbox + coordinates.get(0) + "," + coordinates.get(1) + "," + coordinates.get(2) + "," + coordinates.get(3);
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
    JSONObject jo = XML.toJSONObject(APICall.getRequest(url, false));
    return createParameterTimeValuePair(jo, params.size() > 1 || places.size() > 1);
  }

  /**
   * Created by Miikka Venäläinen
   *
   * Method which goes through the data and gets necessary values
   * This function first collects date + value pairs. Those are saved in a list.
   * Then this list is paired with the parameter name and this parameter name is the type of previously mentioned values.
   * After that all different parameters and their values are paired with city its gathered from.
   * And lastly these cities are saved in list.
   *
   * @param jo JSONObject from API call
   * @param multipleParams Boolean which tells if JSONObject contains data from multiple cities and/or parameters
   * @return Returns a list of WeatherData objects which contains dates/times and respected values for every city
   *
   */
  public static ArrayList<WeatherData> createParameterTimeValuePair(JSONObject jo, Boolean multipleParams) {
    List<JSONObject> jsonObjects = new ArrayList<>();
    JSONObject info = jo.getJSONObject("wfs:FeatureCollection");

    if(multipleParams) {
      for(int i = 0; i < info.getJSONArray("wfs:member").length(); i++) {
        jsonObjects.add((JSONObject) info.getJSONArray("wfs:member").get(i));
      }
    } else {
      jsonObjects.add(info.getJSONObject("wfs:member"));
    }

    String city;
    ArrayList<WeatherData> listOfValues = new ArrayList<>();

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

      JSONObject results = observation.getJSONObject("om:result");
      JSONObject timeSeries = results.getJSONObject("wml2:MeasurementTimeseries");
      String parameter = sampling.get("gml:id").toString().split("-")[sampling.get("gml:id").toString().split("-").length - 1];
      JSONArray points = timeSeries.getJSONArray("wml2:point");

      for(int i = 0; i < points.length(); i++) {
        JSONObject element = (JSONObject) points.get(i);
        JSONObject innerElement = (JSONObject) element.get("wml2:MeasurementTVP");
        String time = (String) innerElement.get("wml2:time");
        String value = innerElement.get("wml2:value").toString();
        Pair<String, Double> pair = new Pair<>(time, Double.valueOf(value)); // Pair of date + value
        boolean notAdded = true;
        for (WeatherData w : listOfValues) {
          if(w.getCity().equals(city) && w.getParameter().equals(parameter)) {
            w.addValue(pair);
            notAdded = false;
          }
        }
        if(notAdded) {
          WeatherData w = new WeatherData(parameter, city, pair);
          listOfValues.add(w);
        }
      }
    }
    for (WeatherData w : listOfValues) {
      System.out.println("Weather values");
      w.calculateAverageValues();
      w.calculateMinMaxValues();
    }
    return listOfValues;
  }

  /**
   * Created by Miikka Venäläinen
   *
   * A method for acquiring weather forecast for a given location and with given parameters
   *
   * @param city Name of the city
   * @param coordinates Coordinates of the location
   * @param params List of wanted parameters
   * @param endDate Last day of the forecast
   * @throws IOException In case of an exception
   * @return Returns a list of WeatherData objects
   */
  public static ArrayList<WeatherData> weatherForecast(String city, ArrayList<Double> coordinates, ArrayList<String> params, String endDate)
      throws IOException {
    String url = FMIBaseURL + forecast;

    if(!city.isEmpty()) {
      url += place + city;
    }
    // lon, lon, lat, lat
    else if(coordinates.size() == 4) {
      url += bbox + coordinates.get(0) + "," + coordinates.get(1) + "," + coordinates.get(2) + "," + coordinates.get(3);
    }
    if(params.size() > 0) {
      url += parameters;
      for(String p : params) {
        url += p + ",";
      }
      url = url.substring(0, url.length() - 1);
    }
    DateTimeFormatter day = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter hours = DateTimeFormatter.ofPattern("HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    url += starttime + day.format(now) + "T" + hours.format(now) + "Z";
    if(endDate.length() > 0) {
      url += endtime + endDate;
    }
    System.out.println(url);
    JSONObject jo = XML.toJSONObject(APICall.getRequest(url, false));
    return createParameterTimeValuePair(jo, params.size() > 1 || coordinates.size() == 4);
  }
}
