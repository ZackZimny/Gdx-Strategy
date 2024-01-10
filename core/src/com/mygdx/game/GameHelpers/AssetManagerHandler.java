package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class AssetManagerHandler {
  private AssetManager assetManager;

  public AssetManagerHandler() {
    assetManager = new AssetManager();
  }

  public void load() {
    String[] units = new String[] { "Human", "Enemy", "Robot" };
    String[] directions = new String[] { "Up", "Right", "Left", "Down" };
    for (String unit : units) {
      for (String direction : directions) {
        assetManager.load(unit + "Walking" + direction + ".png", Texture.class);
      }
    }
    assetManager.load("Apartment.png", Texture.class);
    assetManager.load("Generator.png", Texture.class);
    assetManager.load("Mine.png", Texture.class);
    assetManager.load("Factory.png", Texture.class);
  }

  public AssetManager getAssetManager() {
    return assetManager;
  }
}
