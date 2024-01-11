package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.AssetManagerHandler;
import com.mygdx.game.GameHelpers.GameState;

public class Game extends ApplicationAdapter {
  SpriteBatch batch;
  ShapeRenderer sr;
  Viewport viewport;
  GameState gameState;
  AssetManagerHandler assetManagerHandler;
  boolean postLoading = true;
  OrthographicCamera camera;

  @Override
  public void create() {
    batch = new SpriteBatch();
    sr = new ShapeRenderer();
    camera = new OrthographicCamera();
    viewport = new ScreenViewport(camera);
    gameState = new GameState(viewport);
    assetManagerHandler = new AssetManagerHandler();
    assetManagerHandler.load();
  }

  @Override
  public void render() {
    ScreenUtils.clear(0.2f, 0.5f, 0.3f, 1);
    AssetManager assetManager = assetManagerHandler.getAssetManager();
    if (assetManager.update()) {
      if (postLoading) {
        gameState.generateAnimations(assetManager);
        postLoading = false;
      } else {
        gameState.update(viewport, assetManager);
        gameState.render(batch, sr);
      }
    }
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
