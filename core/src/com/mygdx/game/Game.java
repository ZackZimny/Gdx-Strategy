package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.Selector;
import com.mygdx.game.Entity.Unit;

public class Game extends ApplicationAdapter {
  SpriteBatch batch;
  ShapeRenderer sr;
  Viewport viewport;
  Grid grid;
  Selector selector;
  Unit unit;
  ArrayList<Unit> units = new ArrayList<>();

  @Override
  public void create() {
    batch = new SpriteBatch();
    sr = new ShapeRenderer();
    viewport = new ScreenViewport();
    grid = new Grid(64, 32);
    selector = new Selector();
    unit = new Unit(new Rectangle(150, 100, 25, 25));
    for (int i = 0; i < 10; i++) {
      float x = (float) Math.random() * Gdx.graphics.getWidth();
      float y = (float) Math.random() * Gdx.graphics.getHeight();
      units.add(new Unit(new Rectangle(x, y, 25, 25)));
    }
  }

  private Vector2 getMousePos(Viewport viewport) {
    Vector3 mousePos = viewport
        .unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    return new Vector2(mousePos.x, mousePos.y);
  }

  @Override
  public void render() {
    ScreenUtils.clear(1, 1, 1, 1);
    float deltaTime = Gdx.graphics.getDeltaTime();
    sr.setAutoShapeType(true);
    sr.setProjectionMatrix(viewport.getCamera().combined);
    sr.setColor(0, 0, 0, 1);
    Vector2 mousePos = getMousePos(viewport);
    unit.handlePhysics(selector, getMousePos(viewport), deltaTime);
    for (int i = 0; i < units.size(); i++) {
      units.get(i).handlePhysics(selector, mousePos, deltaTime);
    }
    grid.displayGrid(sr);
    grid.displayCurrentTile(mousePos, sr);
    selector.display(sr, mousePos);
    unit.display(sr);
    for (int i = 0; i < units.size(); i++) {
      units.get(i).display(sr);
    }
  }

  @Override
  public void resize(int screenWidth, int screenHeight) {
    viewport.update(screenWidth, screenHeight);
    System.out.println("After resize: " + new Vector2(screenWidth, screenHeight));
  }

  @Override
  public void dispose() {
    batch.dispose();
  }
}
