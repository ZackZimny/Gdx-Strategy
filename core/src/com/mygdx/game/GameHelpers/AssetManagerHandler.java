package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class AssetManagerHandler {
  private AssetManager assetManager;

  public AssetManagerHandler() {
    assetManager = new AssetManager();
  }

  /**
   * Loads texture into the AssetManager and logs it
   * 
   * @param path location in the asset folder where this texture is located
   **/
  private void loadTexture(String path) {
    try {
      assetManager.load(path, Texture.class);
      EventLogHandler.log("Texture " + path + " loaded successfully.");
    } catch (GdxRuntimeException e) {
      CrashLogHandler.logSevere("There was an error loading " + path, e.getMessage());
    }
  }

  /**
   * Loads sound into the AssetManager and logs it
   * 
   * @param path location in the asset folder where this sound is located
   **/
  private void loadSound(String path) {
    try {
      assetManager.load(path, Sound.class);
      EventLogHandler.log("Texture " + path + " loaded successfully.");
    } catch (GdxRuntimeException e) {
      CrashLogHandler.logSevere("There was an error loading " + path, e.getMessage());
    }
  }

  /**
   * Loads music into the AssetManager and logs it
   * 
   * @param path location in the asset folder where this music is located
   **/
  private void loadMusic(String path) {
    try {
      assetManager.load(path, Music.class);
      EventLogHandler.log("Texture " + path + " loaded successfully.");
    } catch (GdxRuntimeException e) {
      CrashLogHandler.logSevere("There was an error loading " + path, e.getMessage());
    }
  }

  /**
   * loads in all textures, sounds, and music at once into the AssetManager
   **/
  public void load() {
    String[] units = new String[] { "Human", "Enemy", "Robot" };
    String[] directions = new String[] { "Up", "Right", "Left", "Down" };
    for (String unit : units) {
      for (String direction : directions) {
        loadTexture(unit + "Walking" + direction + ".png");
      }
    }
    String[] textures = new String[] { "Apartment.png", "Generator.png", "Mine.png", "Factory.png",
        "BuildingTutorial.png", "BuildingTutorial2.png", "BuildingTutorial3.png", "CommandingTroops.png",
        "CommandingTroops2.png", "Goal.png", "MainMenu.png", "Options.png" };
    for (String texture : textures) {
      loadTexture(texture);
    }
    loadMusic("Calm.mp3");
    loadMusic("Fight.mp3");
    loadSound("Hit.wav");
    loadSound("Explosion.wav");
  }

  /**
   * @return a fully loaded assetManager if this is called after the load method
   **/
  public AssetManager getAssetManager() {
    return assetManager;
  }
}
