package fi.tuni.compse110.project.API;
/*
 * Creator Miikka Venäläinen
 *
 * This class is used to save road condition related data from digitraffic.
 * Objects made from this class have all the needed data which can be
 * changed or requested.
 * Based on the user preferences needed information is used from this class.
 */

public class RoadCondition {

  private String time;
  private String id;
  private String section;
  private int roadNumber;
  private String forecastTime;
  private String roadTemperature;
  private String temperature;
  private int windSpeed;
  private int windDirection;
  private String overallRoadCondition;
  private boolean dayLight;
  private String precipitationCondition;
  private String roadCondition;
  private String windCondition;
  private boolean freezingRainCondition;
  private boolean winterSlipperiness;
  private String visibilityCondition;
  private String frictionCondition;
  private String weatherSymbol;

  public RoadCondition(String id, int roadNumber) {
    setId(id);
    setRoadNumber(roadNumber);
    setSection(id);
  }
  public String getTime(){return time;}

  public void setTime(String time){this.time = time;}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSection() { return section; }

  public void setSection(String section) { this.section = section.split("_")[1] + "_" + section.split("_")[2]; }

  public int getRoadNumber() {
    return roadNumber;
  }

  public void setRoadNumber(int roadNumber) {
    this.roadNumber = roadNumber;
  }

  public String getForecastTime() {
    return forecastTime;
  }

  public void setForecastTime(String forecastTime) {
    this.forecastTime = forecastTime;
  }

  public String getRoadTemperature() {
    return roadTemperature;
  }

  public void setRoadTemperature(String roadTemperature) {
    this.roadTemperature = roadTemperature;
  }

  public String getTemperature() {
    return temperature;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public int getWindSpeed() {
    return windSpeed;
  }

  public void setWindSpeed(int windSpeed) {
    this.windSpeed = windSpeed;
  }

  public int getWindDirection() {
    return windDirection;
  }

  public void setWindDirection(int windDirection) {
    this.windDirection = windDirection;
  }

  public String getOverallRoadCondition() {
    return overallRoadCondition;
  }

  public void setOverallRoadCondition(String overallRoadCondition) {
    this.overallRoadCondition = overallRoadCondition;
  }

  public boolean isDayLight() {
    return dayLight;
  }

  public void setDayLight(boolean dayLight) {
    this.dayLight = dayLight;
  }

  public String getPrecipitationCondition() {
    return precipitationCondition;
  }

  public void setPrecipitationCondition(String precipitationCondition) {
    String[] text = precipitationCondition.split("_");
    String copy = "";
    for(String t : text) {
      copy += t + " ";
    }
    this.precipitationCondition = copy;
  }

  public String getRoadCondition() {
    return roadCondition;
  }

  public void setRoadCondition(String roadCondition) {
    this.roadCondition = roadCondition;
  }

  public String getWindCondition() {
    return windCondition;
  }

  public void setWindCondition(String windCondition) {
    this.windCondition = windCondition;
  }

  public boolean isFreezingRainCondition() {
    return freezingRainCondition;
  }

  public void setFreezingRainCondition(boolean freezingRainCondition) {
    this.freezingRainCondition = freezingRainCondition;
  }

  public boolean isWinterSlipperiness() {
    return winterSlipperiness;
  }

  public void setWinterSlipperiness(boolean winterSlipperiness) {
    this.winterSlipperiness = winterSlipperiness;
  }

  public String getVisibilityCondition() {
    return visibilityCondition;
  }

  public void setVisibilityCondition(String visibilityCondition) {
    this.visibilityCondition = visibilityCondition;
  }

  public String getFrictionCondition() {
    return frictionCondition;
  }

  public void setFrictionCondition(String frictionCondition) {
    this.frictionCondition = frictionCondition;
  }

  public String getWeatherSymbol() {
    return weatherSymbol;
  }

  public void setWeatherSymbol(String weatherSymbol) {
    this.weatherSymbol = weatherSymbol;
  }
}
