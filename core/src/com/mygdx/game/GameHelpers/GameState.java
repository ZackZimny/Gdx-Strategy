package com.mygdx.game.GameHelpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Entity.Building;
import com.mygdx.game.Entity.EnemyUnit;
import com.mygdx.game.Entity.Entity;
import com.mygdx.game.Entity.HumanUnit;
import com.mygdx.game.Entity.RobotUnit;
import com.mygdx.game.UI.ButtonAction;
import com.mygdx.game.UI.ButtonGroup;

public class GameState {
  private Grid grid;
  private Selector selector;
  private List<Entity> entities = new ArrayList<>();
  private ButtonGroup buttonGroup;
  private ItemHandler itemHandler;
  private float deltaTime;
  private ButtonAction buttonAction;
  private List<Collidible> collidibles = new ArrayList<>();
  private Viewport viewport;
  private ArrayList<Building> buildings = new ArrayList<>();

  public GameState(Viewport viewport) {
    grid = new Grid(64, 32);
    selector = new Selector();
    buttonGroup = new ButtonGroup();
    itemHandler = new ItemHandler();
    for (int i = 0; i < 5; i++) {
      entities.add(new HumanUnit(new RectangleCollidible(i * 50, i * 50, 30, 30, CollidibleType.PlayerUnit)));
    }
    entities.add(new EnemyUnit(new RectangleCollidible(700, 700, 30, 30, CollidibleType.EnemyUnit)));
    entities.add(new RobotUnit(new RectangleCollidible(-100, -100, 30, 30, CollidibleType.PlayerUnit)));
    this.viewport = viewport;
  }

  public void generateAnimations(AssetManager assetManager) {
    for (Entity entity : entities) {
      entity.generateAnimations(assetManager);
    }
  }

  public Vector2 getMousePos() {
    return viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
  }

  private boolean neighboringBuilding(Vector2 mousePos, List<Building> buildings) {
    Vector2 pos = grid.getGridCoordinates(mousePos);
    return buildings.stream().anyMatch((b) -> pos.x >= b.getTileX() - 1 && pos.x <= b.getTileX() + 1
        && pos.y >= b.getTileY() - 1 && pos.y <= b.getTileY() + 1);
  }

  private void handleBuilding(Vector2 mousePos, AssetManager assetManager, List<Building> buildings) {
    if (buttonGroup.isInBuildMode() && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
        && !neighboringBuilding(mousePos, buildings)) {
      String name = buttonAction.toString();
      Vector2 pos = grid.getGridCoordinates(mousePos);
      Building newBuilding = new Building(grid, (int) pos.x, (int) pos.y, name);
      newBuilding.generateAnimations(assetManager);
      entities.add(newBuilding);
      buildings.add(newBuilding);
      HashMap<ItemType, Integer> itemMap = newBuilding.getResourcesCreatedPerMinute();
      int refreshTimeInSeconds = 5;
      for (ItemType item : itemMap.keySet()) {
        itemHandler.addItemCounter(item,
            new ItemCounter(refreshTimeInSeconds, itemMap.get(item) / (60 / refreshTimeInSeconds)));
      }
    }
  }

  private void handleDeath() {
    entities = entities.stream().filter((e) -> !e.isDead()).collect(Collectors.toList());
  }

  public <T extends Entity> List<T> getEntitiesByType(Class<T> type) {
    return entities.stream()
        .filter((e) -> type.isInstance(e))
        .map((e) -> type.cast(e))
        .collect(Collectors.toList());
  }

  public void update(Viewport viewport, AssetManager assetManager) {
    handleDeath();
    itemHandler.update(deltaTime);
    handleBuilding(getMousePos(), assetManager, buildings);
    deltaTime = Gdx.graphics.getDeltaTime();
    collidibles = entities.stream().map((e) -> e.getCollidible()).collect(Collectors.toList());
    Vector2 mousePos = getMousePos();
    buttonAction = buttonGroup.getCurrentAction(mousePos);
    for (Entity entity : entities) {
      entity.updateState(this);
    }
    itemHandler.update(deltaTime);
    if (buttonGroup.getCurrentAction(mousePos).equals(ButtonAction.Build)) {
      handleBuilding(mousePos, assetManager, buildings);
    }
  }

  public void render(SpriteBatch sb, ShapeRenderer sr) {
    sb.setProjectionMatrix(viewport.getCamera().combined);
    sr.setProjectionMatrix(viewport.getCamera().combined);
    Vector2 mousePos = getMousePos();
    // sorts entities high to low to ensure that no overlapping occurs
    entities.stream().sorted((e1, e2) -> (int) (e1.getCenter().y - e2.getCenter().y));
    grid.renderTakenTiles(sr, buildings);
    for (Entity entity : entities) {
      entity.render(sr, sb);
    }
    grid.renderCurrentTile(mousePos, sr);
    selector.render(sr, mousePos);
    itemHandler.render(sb);
    buttonGroup.render(sb, sr, mousePos);
  }

  public void handleResize() {
    buttonGroup.calculateButtons();
  }

  public Grid getGrid() {
    return grid;
  }

  public Selector getSelector() {
    return selector;
  }

  public List<Entity> getEntities() {
    return entities;
  }

  public ButtonGroup getButtonGroup() {
    return buttonGroup;
  }

  public ItemHandler getItemHandler() {
    return itemHandler;
  }

  public float getDeltaTime() {
    return deltaTime;
  }

  public ButtonAction getButtonAction() {
    return buttonAction;
  }

  public List<Collidible> getCollidibles() {
    return collidibles;
  }

}
