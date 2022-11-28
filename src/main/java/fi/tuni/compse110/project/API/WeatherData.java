/*
 * Author Miikka Venäläinen
 *
 */

package fi.tuni.compse110.project.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.util.Pair;

public class WeatherData {

  private String city;
  private String parameter;
  private List<Pair<String, Double>> values;

  public WeatherData(String parameter, String city, Pair<String, Double> pair){
    values = new ArrayList<>();
    values.add(pair);
    this.city = city;
    this.parameter = parameter;
  }

  public void addValue(Pair<String, Double> v) {
    values.add(v);
  }

  public String getCity() {
    return city;
  }

  public String getParameter() {
    return parameter;
  }

  /**
   * Created by Miikka Venäläinen
   *
   * Method for calculations of daily average values like temperature
   *
   * @return Returns a list of day + average value pairs.
   */
  public List<Pair<String, Double>> calculateAverageValues() {
    Double allValues = 0.0;
    int divider = 0;
    String previousDay = "";
    List<Pair<String, Double>> dailyAverages = new ArrayList<>();

    for (Pair<String, Double> pair : values) {
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
   * Method for calculating daily minimum and maximum parameter values
   *
   * @return Returns a HashMap which contains min and max values for each day
   */
  public  HashMap<String, Pair<Double, Double>> calculateMinMaxValues() {
    double highPoint = -100.0;
    double lowPoint = 100.0;
    String previousDay = "";
    HashMap<String, Pair<Double, Double>> temps = new HashMap<>();

    for (Pair<String, Double> pair : values) {
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
