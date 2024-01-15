package com.mygdx.game.GameHelpers;

import java.sql.*;

public class DatabaseManager {
  /**
   * Establishes connection to SQLite database
   * 
   * @return Connection variable
   */
  public static Connection connect() {
    Connection conn = null;
    try {
      // create a connection to the database
      String url = "jdbc:sqlite:verne.db";
      conn = DriverManager.getConnection(url);
    } catch (SQLException e) {
      CrashLogHandler.logSevere("There has been a problem connecting to the database: ", e.getMessage());
    }
    return conn;
  }

  /**
   * Loads in Celestilyne.db file
   */
  public static void createDatabase() {
    try {
      Connection conn = connect();
      if (conn != null) {
        DatabaseMetaData meta = conn.getMetaData();
        EventLogHandler.log("The database has been loaded successfully. ");
        EventLogHandler.log("The driver is " + meta.getDriverName());
        conn.close();
      }
    } catch (SQLException e) {
      CrashLogHandler.logSevere("There has been an issue creating the database: ", e.getMessage());
    }
  }

  /**
   * determines if the table given in the parameters is given
   * 
   * @param table table to check
   * @return if the table in parameter has no data
   */
  private static boolean tableIsEmpty(String table) {
    String sql = String.format("SELECT * FROM %s;", table);
    try {
      Connection conn = connect();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      conn.close();
      // rs.next() returns if there is a result from the ResultSet
      return !rs.next();
    } catch (SQLException e) {
      CrashLogHandler.logSevere("There has been an issue determining if a table is empty. ",
          e.getMessage());
      // The application will be closed before this could ever return
      return false;
    }
  }

  public static void createTables() {
    try {
      String sql = "CREATE TABLE IF NOT EXISTS runtime_configurations(musicVolume INTEGER NOT NULL," +
          " sfxVolume INTEGER NOT NULL, fullscreen INTEGER NOT NULL);";
      runSql(sql);
      EventLogHandler.log("runtime_configurations table has been properly loaded.");
      if (tableIsEmpty("runtime_configurations")) {
        sql = "INSERT INTO runtime_configurations(musicVolume, sfxVolume, fullscreen) " +
            "VALUES (5, 5, 0)";
        runSql(sql);
        EventLogHandler.log("Default runtime_configurations have been added.");
      }
      sql = "CREATE TABLE IF NOT EXISTS records(record TEXT NOT NULL, count INTEGER NOT NULL)";
      runSql(sql);
      if (tableIsEmpty("records")) {
        sql = "INSERT INTO records(record, count) VALUES ";
        for (String record : Records.getRecordsStrings()) {
          sql += String.format("('%s', 0),", record);
        }
        // removes the final comma from the sql
        sql = sql.substring(0, sql.length() - 1);
        System.out.println(sql);
        runSql(sql);
      }
    } catch (SQLException e) {
      CrashLogHandler.logSevere("There has been an issue creating tables in the database. ", e.getMessage());
    }
  }

  public static int getTopScore() {
    try {
      String sql = "SELECT * FROM records WHERE record LIKE 'Top Score'";
      Connection connection = connect();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      return resultSet.getInt(1);
    } catch (SQLException e) {
      CrashLogHandler.logSevere("There has been an issue getting the player's high score.", e.getMessage());
    }
    return -1;
  }

  public static void updateRecords(Records records) {
    try {
      Records previousRecords = DatabaseManager.getRecords();
      records.getRecordsMap().put("Games Played", previousRecords.getRecordsMap().get("Games Played") + 1);
      for (String record : Records.getRecordsStrings()) {
        if (!record.equals("Top Score")) {
          int previousCount = previousRecords.getRecordsMap().get(record);
          int currentCount = records.getRecordsMap().get(record);
          records.getRecordsMap().put(record, previousCount + currentCount);
        }
      }
      boolean recordBeaten = records.getRecordsMap().get("Top Score") > previousRecords.getRecordsMap()
          .get("Top Score");
      for (String record : Records.getRecordsStrings()) {
        if (record.equals("Top Score")) {
          if (recordBeaten) {
            String sql = String.format("UPDATE records SET count = %d WHERE record = '%s'",
                records.getRecordsMap().get(record), record);
            runSql(sql);
          }
          continue;
        }
        String sql = String.format("UPDATE records SET count = %d WHERE record = '%s'",
            records.getRecordsMap().get(record), record);
        runSql(sql);
      }
    } catch (SQLException e) {
      CrashLogHandler.logSevere("There has been an issue updating records.", e.getMessage());
    }
  }

  public static String getRecordString() {
    String result = "";
    try {
      String sql = "SELECT * FROM records";
      Connection connection = connect();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      while (resultSet.next()) {
        result += resultSet.getString(1) + ": " + resultSet.getInt(2) + "\n";
      }
      connection.close();
    } catch (SQLException e) {
      CrashLogHandler.logSevere("There has been an issue gathering records.", e.getMessage());
    }
    return result;
  }

  public static Records getRecords() {
    Records result = new Records();
    try {
      String sql = "SELECT * FROM records";
      Connection connection = connect();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      while (resultSet.next()) {
        result.addRecord(resultSet.getString(1), resultSet.getInt(2));
      }
    } catch (SQLException e) {
      CrashLogHandler.logSevere("There has been an issue gathering a records object", e.getMessage());
    }
    return result;
  }

  /**
   * Updates table runtime_configurations with the runtimeConfigurations passed in
   * through the parameter
   * 
   * @param runtimeConfigurations current state of runtimeConfigurations to
   *                              publish to the database
   */
  public static void updateRuntimeConfigurations() {
    String sql = "UPDATE runtime_configurations SET musicVolume = ?, sfxVolume = ?, fullscreen = ?;";
    try {
      Connection connection = connect();
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, RuntimeConfigurations.getMusicVolume());
      preparedStatement.setInt(2, RuntimeConfigurations.getSfxVolume());
      preparedStatement.setInt(3, RuntimeConfigurations.isFullScreen() ? 1 : 0);
      preparedStatement.executeUpdate();
      connection.close();
      EventLogHandler.log("New runtime configurations have been added to the database. ");
    } catch (SQLException e) {
      CrashLogHandler.logSevere("There has been an issue adding runtime configurations into the database:",
          e.getMessage());
    }
  }

  /**
   * Updates runtime configurations from the database
   */
  public static void getRuntimeConfigurations() {
    String sql = "SELECT musicVolume, sfxVolume, fullscreen FROM runtime_configurations;";
    try {
      if (!tableIsEmpty("runtime_configurations")) {
        Connection connection = connect();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        RuntimeConfigurations.setMusicVolume(resultSet.getInt(1));
        RuntimeConfigurations.setSfxVolume(resultSet.getInt(2));
        RuntimeConfigurations.setFullScreen(resultSet.getInt(3) == 1);
        connection.close();
      } else {
        RuntimeConfigurations.setDefaults();
      }
      EventLogHandler.log("Runtime configurations have been successfully loaded from the database.");
    } catch (SQLException e) {
      CrashLogHandler.logSevere("There has been an issue getting runtime configurations from the database: ",
          e.getMessage());
    }
  }

  /**
   * Runs any sql string given on database
   * 
   * @param sql sql string to run
   * @throws SQLException if there is an error in the query
   */
  private static void runSql(String sql) throws SQLException {
    Connection conn = connect();
    Statement stmt = conn.createStatement();
    stmt.execute(sql);
    conn.close();
  }
}
