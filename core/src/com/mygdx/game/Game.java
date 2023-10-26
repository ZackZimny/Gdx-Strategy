package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.GraphPath;
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
import com.mygdx.game.Pathfinding.DestinationNode;
import com.mygdx.game.Pathfinding.NodeConnection;
import com.mygdx.game.Pathfinding.NodeGraph;
import com.mygdx.game.Entity.Unit;
import com.mygdx.game.Entity.UnitGroup;
import com.mygdx.game.Entity.Building;

public class Game extends ApplicationAdapter {
  SpriteBatch batch;
  ShapeRenderer sr;
  Viewport viewport;
  Grid grid;
  Selector selector;
  Unit unit;
  UnitGroup unitGroup;
  Building building;
  ArrayList<Building> buildings = new ArrayList<>();

  @Override
  public void create() {
    batch = new SpriteBatch();
    sr = new ShapeRenderer();
    viewport = new ScreenViewport();
    grid = new Grid(64, 32);
    selector = new Selector();
    ArrayList<Unit> units = new ArrayList<>();
    for (int i = 0; i < 15; i++) {
      Vector2 position = grid.getIsoCenter((int) Math.floor(Math.random() * 20) - 10,
          (int) Math.floor(Math.random() * 20) - 10);
      units.add(new Unit(new Rectangle(position.x, position.y, 25, 25)));
    }
    unitGroup = new UnitGroup(units);
    building = new Building(grid, 7, 5);
    buildings.add(building);
    grid.addBuilding(building);
    for (int i = 0; i < 20; i++) {
      grid.addBuilding(
          new Building(grid, (int) Math.floor(Math.random() * 20) - 10, (int) Math.floor(Math.random() * 20) - 10));
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
    unitGroup.handlePhysics(grid, selector, mousePos, deltaTime);
    grid.render(sr);
    grid.renderCurrentTile(mousePos, sr);
    selector.render(sr, mousePos);
    unitGroup.render(sr);
    building.render(sr);
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
