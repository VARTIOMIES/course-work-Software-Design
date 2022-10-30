package fi.tuni.compse110.project.API;
/*
 * Creator Miikka Venäläinen
 *
 * This file contains all the logic related to FMI data
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
  final static String parameters = "&parameters="; //temperature, windspeedms
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
  public static List<Pair<String, List<Pair<String, List<Pair<String, Double>>>>>> weatherURLCreator(ArrayList<String> places, ArrayList<Double> coordinates,
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
   * @return Returns a list of dates/times and respected values for every city
   *
   * {city : parameters {
   *                      parameter1 : {
   *                                    { time1 : value1 },
   *                                    { time2 : value2 },
   *                                    { time3 : value3 },
   *                                    ...
   *                                   },
   *                      parameter2 : {
   *                                    { time1 : value1 },
   *                                    { time2 : value2 },
   *                                    { time3 : value3 },
   *                                    ...
   *                                   },
   *                                   ...
   *                    }
   * },{
   *   ...
   * }
   */
  public static List<Pair<String, List<Pair<String, List<Pair<String, Double>>>>>> createParameterTimeValuePair(JSONObject jo, Boolean multipleParams) {
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
   *
   * Method for calculations of daily average values like temperature
   *
   * @param list List of Pair which contains time and value information
   * @param city Name of the city
   * @param parameter Name of the parameter
   * @return Returns a list of day + average value pairs.
   */
  public static List<Pair<String, Double>> averageTemperature(List<Pair<String, Double>> list, String city, String parameter) {
    Double allValues = 0.0;
    int divider = 0;
    String previousDay = "";
    List<Pair<String, Double>> dailyAverages = new ArrayList<>();

    for (Pair<String, Double> pair : list) {
      String day = Utility.dateSplitter(pair.getKey(), true);
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
   *
   * Method for calculating daily minimum and maximum temperatures
   *
   * @param list List of Pair which contains time and value information
   * @param city Name of the city
   * @param parameter Name of the parameter
   * @return Returns a HashMap which contains min and max values for each day
   */
  public static HashMap<String, Pair<Double, Double>> minMaxTemperature(
      List<Pair<String, Double>> list, String city, String parameter) {
    double highPoint = -100.0;
    double lowPoint = 100.0;
    String previousDay = "";
    HashMap<String, Pair<Double, Double>> temps = new HashMap<>();

    for (Pair<String, Double> pair : list) {
      String day = Utility.dateSplitter(pair.getKey(), true);
      if(previousDay.equals("")) {
        previousDay = day;
      }
      if(!previousDay.equals(day)) {
        Pair<Double, Double> t = new Pair<>(lowPoint, highPoint);
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
}
