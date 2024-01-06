package com.mygdx.game;

import java.io.IOException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.GameState;

public class Game extends ApplicationAdapter {
  SpriteBatch batch;
  ShapeRenderer sr;
  Viewport viewport;
  GameState gameState;

  @Override
  public void create() {
    batch = new SpriteBatch();
    sr = new ShapeRenderer();
    viewport = new ScreenViewport();
    try {
      gameState = new GameState(viewport);
    } catch (IOException e) {
      System.out.println("Error reading file: " + e);
    }
  }

  @Override
  public void render() {
    ScreenUtils.clear(1, 1, 1, 1);
    gameState.update(viewport);
    gameState.render(batch, sr);
  }

  @Override
  public void resize(int screenWidth, int screenHeight) {
    viewport.update(screenWidth, screenHeight);
    gameState.handleResize();
    System.out.println("After resize: " + new Vector2(screenWidth, screenHeight));
  }

  @Override
  public void dispose() {
    batch.dispose();
  }
}
