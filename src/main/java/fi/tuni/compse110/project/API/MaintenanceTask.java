package fi.tuni.compse110.project.API;

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
