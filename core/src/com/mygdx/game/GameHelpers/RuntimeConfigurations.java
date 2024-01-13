package com.mygdx.game.GameHelpers;

public class RuntimeConfigurations {
  private static int sfxVolume;
  private static int musicVolume;
  private static boolean fullScreen;

  public static int getSfxVolume() {
    return sfxVolume;
  }

  public static void setSfxVolume(int sfxVolume) {
    RuntimeConfigurations.sfxVolume = sfxVolume;
  }

  public static float getSfxVolumePercent() {
    return RuntimeConfigurations.sfxVolume / 10f;
  }

  public static int getMusicVolume() {
    return musicVolume;
  }

  public static float getMusicVolumePercent() {
    return RuntimeConfigurations.musicVolume / 10f;
  }

  public static void setMusicVolume(int musicVolume) {
    RuntimeConfigurations.musicVolume = musicVolume;
  }

  public static boolean isFullScreen() {
    return fullScreen;
  }

  public static void setFullScreen(boolean fullScreen) {
    RuntimeConfigurations.fullScreen = fullScreen;
  }

  public static void setDefaults() {
    sfxVolume = 5;
    musicVolume = 5;
    fullScreen = false;
  }

  public static String prettyPrint() {
    return String.format("RuntimeConfigurations: \n sfxVolume: %d \n musicVolume: %d \n fullScreen: %b", sfxVolume,
        musicVolume, fullScreen);
  }

}
