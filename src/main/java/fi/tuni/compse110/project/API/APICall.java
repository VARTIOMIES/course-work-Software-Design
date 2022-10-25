package fi.tuni.compse110.project.API;
/*
 * Creator Miikka Venäläinen
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class APICall {

  /**
   * Created by Miikka Venäläinen
   * A method for GET requests
   *
   * @param u URL address for a request
   * @return Returns API request return value as a String
   * @throws IOException Throws an exception if something is wrong with the request
   */
  public static String getRequest(String u, Boolean isDigiTraffic) throws IOException {
    URL url = new URL(u);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setReadTimeout(20000);
    connection.setConnectTimeout(20000);
    BufferedReader br;
    if (isDigiTraffic) {
      connection.setRequestMethod("GET");
      //connection.setRequestProperty("Accept", "application/json");
      //connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Accept-Encoding", "gzip");
      if(connection.getResponseCode() != 200) {
        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
      } else {
        br = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
      }
    } else {
      if(connection.getResponseCode() != 200) {
        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
      } else {
        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      }
    }
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = br.readLine()) != null) {
      sb.append(line).append("\n");
    }
    br.close();
    return sb.toString();
  }
}
