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

  public LinkedHashMap<String, Integer> getRecordsMap() {
    return recordsMap;
  }

  public void setRecordsMap(LinkedHashMap<String, Integer> recordsMap) {
    this.recordsMap = recordsMap;
  }

  public static String[] getRecordsStrings() {
    return recordsStrings;
  }
}
