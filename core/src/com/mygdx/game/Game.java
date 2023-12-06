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
import com.mygdx.game.GameHelpers.ICollidible;
import com.mygdx.game.GameHelpers.Selector;
import com.mygdx.game.Entity.Unit;
import com.mygdx.game.Entity.Building;

public class Game extends ApplicationAdapter {
  SpriteBatch batch;
  ShapeRenderer sr;
  Viewport viewport;
  Grid grid;
  Selector selector;
  Building building;
  ArrayList<Building> buildings = new ArrayList<>();
  ArrayList<ICollidible> collidibles = new ArrayList<>();
  ArrayList<Unit> units = new ArrayList<>();

  @Override
  public void create() {
    batch = new SpriteBatch();
    sr = new ShapeRenderer();
    viewport = new ScreenViewport();
    grid = new Grid(64, 32);
    selector = new Selector();
    building = new Building(grid, 7, 5);
    buildings.add(building);
    grid.addBuilding(building);
    for (int i = 0; i < 20; i++) {
      Building newBuilding = new Building(grid, (int) Math.floor(Math.random() * 20) - 10,
          (int) Math.floor(Math.random() * 20) - 10);
      grid.addBuilding(newBuilding);
      collidibles.add(newBuilding);
    }
    for (int i = 0; i < 5; i++) {
      Unit newUnit = new Unit(new Rectangle(i * 20, i * 20, 12, 12));
      units.add(newUnit);
      collidibles.add(newUnit);
    }
    collidibles.add(building);
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
    for (Unit unit : units) {
      unit.handleSelection(selector, mousePos, deltaTime);
      unit.handleMovement(collidibles, sr, grid, mousePos, deltaTime);
    }
    grid.renderCurrentTile(mousePos, sr);
    selector.render(sr, mousePos);
    building.render(sr);
    for (Building building : grid.getBuildings()) {
      building.render(sr);
    }
    for (Unit unit : units) {
      unit.render(sr);
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
