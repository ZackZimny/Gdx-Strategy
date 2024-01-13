package com.mygdx.game.UI;

import java.util.HashMap;

import javax.xml.crypto.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader.Inputs;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameHelpers.DatabaseManager;
import com.mygdx.game.GameHelpers.GameLoop;
import com.mygdx.game.GameHelpers.RuntimeConfigurations;

public class ScreenManager {
  private GameLoop gameLoop;
  private ScreenState screenState = ScreenState.MAIN_MENU;
  private AssetManager assetManager;
  private Viewport viewport;
  private HashMap<ScreenState, Screen> screenHashMap = new HashMap<>();
  private GameOverScreen gameOverScreen = new GameOverScreen();
  private Screen currentScreen;
  private MainMenuScreen mainMenuScreen = new MainMenuScreen();
  private ExitScreen exitScreen = new ExitScreen();
  private OptionsScreen optionsScreen;
  private Music calmMusic;
  private Music fightMusic;

  public ScreenManager(Viewport viewport, AssetManager assetManager) {
    this.assetManager = assetManager;
    this.viewport = viewport;
    gameLoop = new GameLoop(viewport);
    gameLoop.generateAnimations(assetManager);
    screenHashMap.put(ScreenState.GAME_OVER, gameOverScreen);
    screenHashMap.put(ScreenState.GAME_LOOP, gameLoop);
    screenHashMap.put(ScreenState.MAIN_MENU, mainMenuScreen);
    screenHashMap.put(ScreenState.EXIT, exitScreen);
    currentScreen = mainMenuScreen;
    calmMusic = assetManager.get("Calm.mp3", Music.class);
    calmMusic.setLooping(true);
    fightMusic = assetManager.get("Fight.mp3", Music.class);
    fightMusic.setLooping(true);
    DatabaseManager.createDatabase();
    DatabaseManager.createTables();
    DatabaseManager.getRuntimeConfigurations();
    optionsScreen = new OptionsScreen();
    optionsScreen.loadSounds(assetManager);
    screenHashMap.put(ScreenState.OPTIONS, optionsScreen);
    calmMusic.setVolume(RuntimeConfigurations.getMusicVolumePercent());
    fightMusic.setVolume(RuntimeConfigurations.getMusicVolumePercent());
  }

  /**
   * Displays the background square in screens
   * 
   * @param shapeRenderer displays background square
   * @param color         color of the square
   */
  public static void renderBackground(ShapeRenderer shapeRenderer, Color color) {
    shapeRenderer.setAutoShapeType(true);
    shapeRenderer.begin();
    shapeRenderer.setColor(color);
    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.rect(-Gdx.graphics.getWidth() / 2f, -Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth(),
        Gdx.graphics.getHeight());
    shapeRenderer.end();
  }

  public void render(ShapeRenderer sr, SpriteBatch sb, Vector2 mousePos, float deltaTime) {
    if (screenState == ScreenState.GAME_LOOP) {
      gameLoop.update(viewport, assetManager);
      gameLoop.render(sb, sr, mousePos, deltaTime);
      if (gameLoop.getEnemyCountOnScreen() >= 3) {
        fightMusic.play();
        calmMusic.stop();
      } else {
        calmMusic.play();
        fightMusic.stop();
      }
      screenState = gameLoop.getScreenState();
      if (screenState == ScreenState.GAME_OVER) {
        gameLoop = new GameLoop(viewport);
        gameLoop.generateAnimations(assetManager);
        currentScreen = gameOverScreen;
      }
    } else {
      if (screenState == ScreenState.OPTIONS) {
        calmMusic.setVolume(RuntimeConfigurations.getMusicVolumePercent());
        fightMusic.setVolume(RuntimeConfigurations.getMusicVolume());
      }
      fightMusic.stop();
      calmMusic.play();
      screenState = currentScreen.getScreenState(mousePos);
      currentScreen = screenHashMap.get(screenState);
      currentScreen.render(sb, sr, mousePos, deltaTime);
    }
  }

  public void handleResize() {
    gameLoop.handleResize();
  }

  public Vector2 getMousePos() {
    return gameLoop.getMousePos();
  }
}
