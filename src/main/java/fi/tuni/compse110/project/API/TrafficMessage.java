package fi.tuni.compse110.project.API;

import java.util.ArrayList;

public class TrafficMessage {

  private String situationType;
  private String releaseTime;
  private String title;
  private String description;
  private String municipality;
  private ArrayList<String> features;
  private String startTime;
  private String comment;

  public TrafficMessage(){

  }

  public String getSituationType() {
    return situationType;
  }

  public void setSituationType(String situationType) {
    this.situationType = situationType;
  }

  public String getReleaseTime() {
    return releaseTime;
  }

  public void setReleaseTime(String releaseTime) {
    this.releaseTime = releaseTime;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMunicipality() {
    return municipality;
  }

  public void setMunicipality(String municipality) {
    this.municipality = municipality;
  }

  public ArrayList<String> getFeatures() {
    return features;
  }

  public void setFeatures(ArrayList<String> features) {
    this.features = features;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
