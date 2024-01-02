package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.mygdx.game.GameHelpers.ItemHandler;
import com.mygdx.game.GameHelpers.Selector;
import com.mygdx.game.UI.ButtonGroup;
import com.mygdx.game.Entity.Unit;
import com.mygdx.game.Entity.Building;
import com.mygdx.game.Entity.EnemyUnit;
import com.mygdx.game.Entity.Entity;

public class Game extends ApplicationAdapter {
  SpriteBatch batch;
  ShapeRenderer sr;
  Viewport viewport;
  Viewport uiViewport;
  Grid grid;
  Selector selector;
  ArrayList<Building> buildings = new ArrayList<>();
  ArrayList<ICollidible> collidibles = new ArrayList<>();
  ArrayList<Unit> units = new ArrayList<>();
  ButtonGroup buttonGroup;
  SpriteBatch spriteBatch;
  ItemHandler itemHandler;
  EnemyUnit enemyUnit;

  @Override
  public void create() {
    batch = new SpriteBatch();
    sr = new ShapeRenderer();
    viewport = new ScreenViewport();
    uiViewport = new ScreenViewport();
    grid = new Grid(64, 32);
    selector = new Selector();
    for (int i = 0; i < 5; i++) {
      Unit newUnit = new Unit(new Rectangle(i * 20, i * 20, 22, 22));
      units.add(newUnit);
      collidibles.add(newUnit);
    }
    enemyUnit = new EnemyUnit(new Rectangle(500, 500, 28, 28));
    collidibles.add(enemyUnit);
    buttonGroup = new ButtonGroup(new String[] { "Move", "Fight", "Build" }, "Move");
    spriteBatch = new SpriteBatch();
    itemHandler = new ItemHandler();
  }

  private Vector2 getMousePos(Viewport viewport) {
    Vector3 mousePos = viewport
        .unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    return new Vector2(mousePos.x, mousePos.y);
  }

  private void handleBuilding(Vector2 mousePos) {
    if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
      Vector2 gridCoordinates = grid.getGridCoordinates(mousePos);
      int resourceCost = 20;
      if (itemHandler.getItemCount("Steel") > resourceCost) {
        Building newBuilding = new Building(grid, (int) gridCoordinates.x, (int) gridCoordinates.y, 100);
        buildings.add(newBuilding);
        collidibles.add(newBuilding);
        grid.addBuilding(newBuilding);
        itemHandler.consumeItem("Steel", resourceCost);
      }
    }
  }

  @Override
  public void render() {
    ScreenUtils.clear(1, 1, 1, 1);
    float deltaTime = Gdx.graphics.getDeltaTime();
    sr.setAutoShapeType(true);
    sr.setProjectionMatrix(viewport.getCamera().combined);
    sr.setColor(0, 0, 0, 1);
    Vector2 mousePos = getMousePos(viewport);
    String mode = buttonGroup.getCurrentAction(mousePos);
    for (Unit unit : units) {
      unit.handleSelection(selector, mousePos, deltaTime);
      unit.handleMovement(collidibles, sr, mousePos, deltaTime, mode);
    }
    enemyUnit.handleMovement(collidibles, sr, mousePos, deltaTime, mode);
    itemHandler.update(deltaTime);
    grid.renderCurrentTile(mousePos, sr);
    if (buttonGroup.getCurrentAction(mousePos).equals("Build")) {
      handleBuilding(mousePos);
    }
    selector.render(sr, mousePos);
    for (Building building : grid.getBuildings()) {
      building.render(sr);
    }
    for (Unit unit : units) {
      unit.render(sr);
    }
    enemyUnit.render(sr);
    sr.setProjectionMatrix(uiViewport.getCamera().combined);
    spriteBatch.setProjectionMatrix(uiViewport.getCamera().combined);
    itemHandler.render(spriteBatch);
    buttonGroup.render(spriteBatch, sr, mousePos);
  }

  @Override
  public void resize(int screenWidth, int screenHeight) {
    viewport.update(screenWidth, screenHeight);
    uiViewport.update(screenWidth, screenHeight);
    buttonGroup.calculateButtons();
    System.out.println("After resize: " + new Vector2(screenWidth, screenHeight));
  }

  @Override
  public void dispose() {
    batch.dispose();
  }
}
