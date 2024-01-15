package com.mygdx.game.GameHelpers;

/**
 * holds current value of settings that serialize to the database to persist
 * between game sessions
 **/
public class RuntimeConfigurations {
  private static int sfxVolume;
  private static int musicVolume;
  private static boolean fullScreen;

  /**
   * @return sound effects volume on a scale from 1-10
   **/
  public static int getSfxVolume() {
    return sfxVolume;
  }

  /**
   * @param sfxVolume overwrites sound effects volume on a scale from 1-10
   **/
  public static void setSfxVolume(int sfxVolume) {
    RuntimeConfigurations.sfxVolume = sfxVolume;
  }

  /**
   * @return percent volume to play sound effects files at
   **/
  public static float getSfxVolumePercent() {
    // sound effects toned down by 80%
    return RuntimeConfigurations.sfxVolume / 10f * 0.8f;
  }

  /**
   * @return music volume on a scale from 1-10
   **/
  public static int getMusicVolume() {
    return musicVolume;
  }

  /**
   * @return music volume as a percentage
   **/
  public static float getMusicVolumePercent() {
    // music volume toned down by 80%
    return RuntimeConfigurations.musicVolume / 10f * 0.8f;
  }

  /**
   * @param sfxVolume overwrites sound effects volume on a scale from 1-10
   **/
  public static void setMusicVolume(int musicVolume) {
    RuntimeConfigurations.musicVolume = musicVolume;
  }

  /**
   * @return true if option is set to fullScreen, false otherwise
   **/
  public static boolean isFullScreen() {
    return fullScreen;
  }

  /**
   * @param fullScreen overwrites fullScreen value
   **/
  public static void setFullScreen(boolean fullScreen) {
    RuntimeConfigurations.fullScreen = fullScreen;
  }

  /**
   * sets up default values that the settings are set to
   **/
  public static void setDefaults() {
    sfxVolume = 5;
    musicVolume = 5;
    fullScreen = false;
  }
}
