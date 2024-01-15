package com.mygdx.game.UI;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
  private RecordsScreen recordsScreen = new RecordsScreen();
  private TutorialScreen tutorialScreen = new TutorialScreen();
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
    screenHashMap.put(ScreenState.RECORDS, recordsScreen);
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
    tutorialScreen.loadImages(assetManager);
    screenHashMap.put(ScreenState.OPTIONS, optionsScreen);
    screenHashMap.put(ScreenState.TUTORIAL, tutorialScreen);
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
      if (gameLoop.getPlayerUnits().size() <= 2 && !fightMusic.isPlaying()) {
        fightMusic.play();
        calmMusic.stop();
      } else if (gameLoop.getPlayerUnits().size() > 2 && !calmMusic.isPlaying()) {
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
      } else if (screenState == ScreenState.MAIN_MENU
          && currentScreen.getStringButtonHashMap().get("Records").isClicked(mousePos)) {
        recordsScreen.setRecordString(DatabaseManager.getRecordString());
      }
      if (!calmMusic.isPlaying()) {
        fightMusic.stop();
        calmMusic.play();
      }
      screenState = currentScreen.getScreenState(mousePos);
      currentScreen = screenHashMap.get(screenState);
      currentScreen.render(sb, sr, mousePos, deltaTime);
    }
  }

  public void handleResize() {
    gameLoop.handleResize();
    for (Screen screen : screenHashMap.values()) {
      screen.handleResize();
    }
  }

  public Vector2 getMousePos() {
    return gameLoop.getMousePos();
  }
}
