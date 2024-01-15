package com.mygdx.game.GameHelpers;

import java.util.InputMismatchException;
import java.util.LinkedHashMap;

public class Records {
  private LinkedHashMap<String, Integer> recordsMap = new LinkedHashMap<>();
  private static String[] recordsStrings = new String[] { "Top Score", "Enemies Beaten", "Humans Spawned",
      "Robots Spawned", "Apartments Built", "Steel Mines Built", "Generators Built", "Factories Built",
      "Games Played" };

  public Records() {
    for (String record : recordsStrings) {
      recordsMap.put(record, 0);
    }
  }

  /**
   * adds a record to the records map and check if it matches any records on file.
   * If not, an error is thrown to the CrashLogHandler
   * 
   * @param record String to match to current records
   * @param amount number to associate with record
   **/
  public void addRecord(String record, int amount) {
    try {
      for (String name : recordsStrings) {
        if (name.equals(record)) {
          recordsMap.put(name, amount);
          return;
        }
      }
      throw new InputMismatchException("Record does not match record title");
    } catch (InputMismatchException e) {
      CrashLogHandler.logSevere("There has been an issue creating a records object.", e.getMessage());
    }
  }

  /**
   * @return LinkedHashMap<String, Integer> pairs with a valid records as the
   *         Strings and their corresponding amounts
   **/
  public LinkedHashMap<String, Integer> getRecordsMap() {
    return recordsMap;
  }

  /**
   * @param LinkedHashMap<String, Integer> overwrites this object's pairs with a
   *                              valid records as the Strings and their
   *                              corresponding amounts
   **/
  public void setRecordsMap(LinkedHashMap<String, Integer> recordsMap) {
    this.recordsMap = recordsMap;
  }

  /**
   * @return all valid Strings for a record
   **/
  public static String[] getRecordsStrings() {
    return recordsStrings;
  }
}
