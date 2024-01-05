package com.mygdx.game.GameHelpers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Entity.Building;
import com.mygdx.game.Entity.EnemyUnit;
import com.mygdx.game.Entity.Entity;
import com.mygdx.game.Entity.PlayerUnit;
import com.mygdx.game.UI.ButtonGroup;

public class GameState {
  private Grid grid;
  private Selector selector;
  private List<Entity> entities = new ArrayList<>();
  private ButtonGroup buttonGroup;
  private ItemHandler itemHandler;
  private float deltaTime;
  private String actionMode;
  private List<Collidible> collidibles = new ArrayList<>();
  private Viewport viewport;

  public GameState(Viewport viewport) {
    grid = new Grid(64, 32);
    selector = new Selector();
    buttonGroup = new ButtonGroup(new String[] { "Move", "Build", "Fight" }, "Move");
    itemHandler = new ItemHandler();
    for (int i = 0; i < 5; i++) {
      entities.add(new PlayerUnit(new RectangleCollidible(i * 50, i * 50, 30, 30, CollidibleType.PlayerUnit)));
    }
    entities.add(new EnemyUnit(new RectangleCollidible(700, 700, 30, 30, CollidibleType.EnemyUnit)));
    this.viewport = viewport;
  }

  public Vector2 getMousePos() {
    return viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
  }

  private void handleBuilding(Vector2 mousePos) {
    if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
      Vector2 gridCoordinates = grid.getGridCoordinates(mousePos);
      int resourceCost = 20;
      if (itemHandler.getItemCount("Steel") > resourceCost) {
        Building newBuilding = new Building(grid, (int) gridCoordinates.x, (int) gridCoordinates.y, 100);
        itemHandler.consumeItem("Steel", resourceCost);
        entities.add(newBuilding);
      }
    }
  }

  public <T extends Entity> List<T> getEntitiesByType(Class<T> type) {
    return entities.stream()
        .filter((e) -> type.isInstance(e))
        .map((e) -> type.cast(e))
        .collect(Collectors.toList());
  }

  public void update(Viewport viewport) {
    deltaTime = Gdx.graphics.getDeltaTime();
    collidibles = entities.stream().map((e) -> e.getCollidible()).collect(Collectors.toList());
    Vector2 mousePos = getMousePos();
    actionMode = buttonGroup.getCurrentAction(mousePos);
    for (Entity entity : entities) {
      entity.updateState(this);
    }
    itemHandler.update(deltaTime);
    if (buttonGroup.getCurrentAction(mousePos).equals("Build")) {
      handleBuilding(mousePos);
    }
  }

  public void render(SpriteBatch sb, ShapeRenderer sr) {
    sb.setProjectionMatrix(viewport.getCamera().combined);
    sr.setProjectionMatrix(viewport.getCamera().combined);
    Vector2 mousePos = getMousePos();
    for (Entity entity : entities) {
      entity.render(sr);
    }
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

  public String getActionMode() {
    return actionMode;
  }

  public List<Collidible> getCollidibles() {
    return collidibles;
  }

}
