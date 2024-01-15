package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Filler screen while the game closes (likely not to be seen)
 **/
public class ExitScreen extends Screen {
  public ExitScreen() {
    super("Exit", ScreenState.EXIT);
  }

  /**
   * closes the app
   **/
  @Override
  public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 mousePos, float deltaTime) {
    Gdx.app.exit();
    System.exit(0);
  }
}
