package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.DatabaseManager;
import com.mygdx.game.GameHelpers.RuntimeConfigurations;

import java.util.LinkedHashMap;

/**
 * Screen where different runtime configurations can be altered
 */
public class OptionsScreen extends Screen {
  private Checkbox fullScreenCheckBox;
  private NumberSelect soundEffectsSelect;
  private NumberSelect musicSelect;
  private Sound explosionSound;
  private float leftPadding = 30;
  private float topPadding = 200;

  /**
   * Initializes options screen
   * 
   * @param runtimeConfigurations configuration state to display
   */
  public OptionsScreen() {
    super("Options", ScreenState.OPTIONS);
    LinkedHashMap<String, ScreenState> stateScreenMap = new LinkedHashMap<>();
    stateScreenMap.put("Apply", ScreenState.OPTIONS);
    stateScreenMap.put("Return to Main Menu", ScreenState.MAIN_MENU);
    createButtonColumn(stateScreenMap);
    float startY = Gdx.graphics.getHeight() / 2f - topPadding;
    float startX = -Gdx.graphics.getWidth() / 2f + leftPadding;
    fullScreenCheckBox = new Checkbox(new Vector2(startX, startY), "Fullscreen");
    soundEffectsSelect = new NumberSelect("Sound Effects Volume", new Vector2(startX, startY - 60));
    musicSelect = new NumberSelect("Music Volume", new Vector2(startX, startY - 120));
    syncWithRuntimeConfigurations();
    determineFullscreen();
  }

  @Override
  public void handleResize() {
    float startY = Gdx.graphics.getHeight() / 2f - topPadding;
    float startX = -Gdx.graphics.getWidth() / 2f + leftPadding;
    fullScreenCheckBox.setPosition(new Vector2(startX, startY));
    soundEffectsSelect.setPosition(new Vector2(startX, startY - 60));
    musicSelect.setPosition(new Vector2(startX, startY - 120));
    super.handleResize();
  }

  /**
   * Toggles fullscreen depending on runtime configurations
   */
  public void determineFullscreen() {
    if (RuntimeConfigurations.isFullScreen()) {
      Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    } else {
      DisplayMode displayMode = Gdx.graphics.getDisplayMode();
      Gdx.graphics.setWindowedMode(displayMode.width, displayMode.height);
    }
  }

  /**
   * updates RuntimeConfigurations in the UI
   **/
  private void syncWithRuntimeConfigurations() {
    fullScreenCheckBox.setIsOn(RuntimeConfigurations.isFullScreen());
    soundEffectsSelect.setNumber(RuntimeConfigurations.getSfxVolume());
    musicSelect.setNumber(RuntimeConfigurations.getMusicVolume());
  }

  /**
   * updates runtimeConfigurations from the UI
   **/
  private void syncRuntimeConfigurations() {
    RuntimeConfigurations.setSfxVolume(soundEffectsSelect.getNumber());
    RuntimeConfigurations.setMusicVolume(musicSelect.getNumber());
    RuntimeConfigurations.setFullScreen(fullScreenCheckBox.isOn());
  }

  @Override
  public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 mousePos, float deltaTime) {
    syncWithRuntimeConfigurations();
    fullScreenCheckBox.render(sb, sr, mousePos);
    soundEffectsSelect.render(sb, sr, mousePos);
    musicSelect.render(sb, sr, mousePos);
    if (soundEffectsSelect.isButtonClicked(mousePos)) {
      explosionSound.play(RuntimeConfigurations.getSfxVolumePercent());
    }
    if (getStringButtonHashMap().get("Apply").isClicked(mousePos)) {
      determineFullscreen();
      DatabaseManager.updateRuntimeConfigurations();
    }
    syncRuntimeConfigurations();
    super.render(sb, sr, mousePos, deltaTime);
  }

  public void loadSounds(AssetManager assetManager) {
    explosionSound = assetManager.get("Explosion.wav", Sound.class);
  }
}
