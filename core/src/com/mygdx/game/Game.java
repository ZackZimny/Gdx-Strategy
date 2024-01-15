package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameHelpers.AssetManagerHandler;
import com.mygdx.game.GameHelpers.EventLogHandler;
import com.mygdx.game.UI.ScreenManager;

public class Game extends ApplicationAdapter {
  SpriteBatch batch;
  ShapeRenderer sr;
  Viewport viewport;
  AssetManagerHandler assetManagerHandler;
  boolean postLoading = true;
  OrthographicCamera camera;
  ScreenManager screenManager;

  @Override
  public void create() {
    batch = new SpriteBatch();
    sr = new ShapeRenderer();
    camera = new OrthographicCamera();
    viewport = new ScreenViewport(camera);
    assetManagerHandler = new AssetManagerHandler();
    assetManagerHandler.load();
    EventLogHandler.start();
  }

  @Override
  public void render() {
    ScreenUtils.clear(0.2f, 0.5f, 0.3f, 1);
    AssetManager assetManager = assetManagerHandler.getAssetManager();
    float deltaTime = Gdx.graphics.getDeltaTime();
    if (assetManager.update()) {
      if (postLoading) {
        screenManager = new ScreenManager(viewport, assetManager);
        screenManager.handleResize();
        postLoading = false;
      } else {
        sr.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        screenManager.render(sr, batch, screenManager.getMousePos(), deltaTime);
      }
    }
  }

  @Override
  public void resize(int screenWidth, int screenHeight) {
    viewport.update(screenWidth, screenHeight);
    if (!postLoading) {
      screenManager.handleResize();
    }
    EventLogHandler.log(String.format("Screen resize: (%d, %d)", screenWidth, screenHeight));
  }

  @Override
  public void dispose() {
    batch.dispose();
    sr.dispose();
    assetManagerHandler.getAssetManager().dispose();
  }
}
