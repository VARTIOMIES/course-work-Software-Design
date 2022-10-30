package fi.tuni.compse110.project.API;

/*
 * Creator Miikka Venäläinen
 *
 * This class is used to save maintenance task related data from digitraffic.
 * Objects made from this class have all the needed data which can be
 * changed or requested.
 * Based on the user preferences needed information is used from this class.
 */

import java.util.ArrayList;

public class MaintenanceTask {

  private String sendingTime;
  private String createdTime;
  private ArrayList<String> tasks;
  private String startTime;
  private String endTime;
  private String domain;
  private String source;

  public MaintenanceTask() {

  }

  
  /** 
   * Get datetime string from start to end of task
   * @return String
   */
  public String getPrettyTimeRange() {
    // surprisingly, after trying multiple different methods, this was the most simple/robust
    String startingTime = startTime.substring(11,16);
    String endingTime = endTime.substring(11,16);
    String[] dateParts = endTime.substring(0,10).split("-");

    return startingTime + " - " + endingTime + " " + dateParts[2] + "." + dateParts[1] + "." + dateParts[0];
  }

  public String getSendingTime() {
    return sendingTime;
  }

  public void setSendingTime(String sendingTime) {
    this.sendingTime = sendingTime;
  }

  public String getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(String createdTime) {
    this.createdTime = createdTime;
  }

  public ArrayList<String> getTasks() {
    return tasks;
  }

  public void setTasks(ArrayList<String> tasks) {
    this.tasks = tasks;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

}
