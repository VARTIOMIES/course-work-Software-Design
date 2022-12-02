package fi.tuni.compse110.project.API;
/*
 * Creator Miikka Venäläinen
 */

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

public class Utility {

  /**
   * Created by Miikka Venäläinen
   * Function splits given date into day or time based on the argument
   *
   * @param date Formatted date t.ex. 2022-10-06T00:50:00Z
   * @param getDay Tells if day is wanted or time
   * @return Returns "2022-10-06" if argument is "day", "00:50:00" if argument is "time"
   */
  public static String dateSplitter(String date, boolean getDay) {
    if(getDay) {
      String[] day = date.split("T")[0].split("-");
      return day[2] + "." + day[1] + "." + day[0];
    }
    return date.split("T")[1].substring(0, date.split("T")[1].length() - 1);
  }

  /**
   * Created by Miikka Venäläinen
   * Creates a valid date from given inputs
   *
   * @param year User's selection for the year
   * @param month User's selection for the month
   * @param day User's selection for the day
   * @param hours User's selection for the hours
   * @param minutes User's selection for the minutes
   * @return Returns correctly formatted date t.ex 2022-10-06T00:50:00Z
   */
  public static String dateFormatter(int year, int month, int day, int hours, int minutes) {
    String date;
    DateTimeFormatter dateForm = DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.US).withResolverStyle(
        ResolverStyle.STRICT);
    date = dateForm.format(LocalDate.of(year, month, day)) + "T" + LocalTime.of(hours, minutes) + ":00Z";
    return date;
  }

    /**
     *
     * @param dateStr
     * @return
     */
  public static int getHourIntFromDateSplitterString(String dateStr){
      String time = dateSplitter(dateStr,false);
      String[] timeArray = time.split(":");
      return Integer.parseInt(timeArray[0]);
  }

}
