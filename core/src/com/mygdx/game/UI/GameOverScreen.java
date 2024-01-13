package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedHashMap;

/**
 * Screen that displays once the units have lost all of their health
 */
public class GameOverScreen extends Screen {
  private int score;

  /**
   * initializes the gameOver screen
   */
  public GameOverScreen() {
    super("Game Over", ScreenState.GAME_OVER);
    LinkedHashMap<String, ScreenState> screenStateMap = new LinkedHashMap<>();
    screenStateMap.put("Play Again", ScreenState.GAME_LOOP);
    screenStateMap.put("Return to Main Menu", ScreenState.MAIN_MENU);
    createButtonColumn(screenStateMap);
  }

  /**
   * displays the GameOverScreen
   * 
   * @param shapeRenderer displays button rectangles
   * @param spriteBatch   renders text
   */
  @Override
  public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 mousePos, float deltaTime) {
    ScreenManager.renderBackground(sr, Color.RED);
    String scoreString = String.format("Your Score: %d", score);
    sb.begin();
    getTitleFontHandler().getFont().draw(sb, scoreString,
        getCenterTextX(scoreString), Gdx.graphics.getHeight() - 150);
    sb.end();
    super.render(sb, sr, mousePos, deltaTime);
  }

  public void setScore(int score) {
    this.score = score;
  }
}
