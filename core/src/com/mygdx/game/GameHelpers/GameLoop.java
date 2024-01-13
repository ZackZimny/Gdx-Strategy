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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Entity.Apartment;
import com.mygdx.game.Entity.Building;
import com.mygdx.game.Entity.EnemyUnit;
import com.mygdx.game.Entity.Entity;
import com.mygdx.game.Entity.Factory;
import com.mygdx.game.Entity.Generator;
import com.mygdx.game.Entity.HumanUnit;
import com.mygdx.game.Entity.Mine;
import com.mygdx.game.Entity.PlayerUnit;
import com.mygdx.game.Entity.RobotUnit;
import com.mygdx.game.UI.ButtonAction;
import com.mygdx.game.UI.ButtonGroup;
import com.mygdx.game.UI.EnemiesBeatDisplay;
import com.mygdx.game.UI.Screen;
import com.mygdx.game.UI.ScreenState;

public class GameLoop extends Screen {
  private Grid grid;
  private Selector selector;
  private List<Entity> entities;
  private ButtonGroup buttonGroup;
  private ItemRenderer itemRenderer;
  private OrderedMap<ItemType, Integer> itemMap;
  private float deltaTime;
  private ButtonAction buttonAction;
  private List<Collidible> collidibles;
  private Viewport viewport;
  private ArrayList<Building> buildings;
  private List<PlayerUnit> playerUnits;
  private float enemySpawnRate;
  private float enemySpawnTimer;
  private float enemySpawnDivisor;
  private int enemiesBeat;
  private EnemiesBeatDisplay enemiesBeatDisplay;
  private ScreenState screenState;

  public GameLoop(Viewport viewport) {
    super("Verne", ScreenState.GAME_LOOP);
    this.viewport = viewport;
    entities = new ArrayList<>();
    itemMap = new OrderedMap<>();
    collidibles = new ArrayList<>();
    buildings = new ArrayList<>();
    playerUnits = new ArrayList<>();
    enemySpawnRate = 12f;
    enemySpawnTimer = 0f;
    enemySpawnDivisor = 1.025f;
    enemiesBeat = 0;
    screenState = ScreenState.GAME_LOOP;
    grid = new Grid(64, 32);
    selector = new Selector();
    buttonGroup = new ButtonGroup();
    itemRenderer = new ItemRenderer();
    enemiesBeatDisplay = new EnemiesBeatDisplay();
    for (int i = 0; i < 5; i++) {
      entities.add(new HumanUnit(new Vector2(i * 60, i * 60)));
    }
    entities.add(new EnemyUnit(new Vector2(700, 700)));
    // entities.add(new RobotUnit(new Vector2(-100, -100)));
    Mine mine = new Mine(grid, -5, 5);
    Generator generator = new Generator(grid, 5, 5);
    entities.add(mine);
    buildings.add(mine);
    entities.add(generator);
    buildings.add(generator);
    for (ItemType item : ItemType.values()) {
      itemMap.put(item, 50);
    }
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
      Vector2 pos = grid.getGridCoordinates(mousePos);
      Building building = null;
      switch (buttonAction) {
        case Mine:
          building = new Mine(grid, (int) pos.x, (int) pos.y);
          break;
        case Apartment:
          building = new Apartment(grid, (int) pos.x, (int) pos.y);
          break;
        case Generator:
          building = new Generator(grid, (int) pos.x, (int) pos.y);
          break;
        case Factory:
          building = new Factory(grid, (int) pos.x, (int) pos.y);
          break;
        default:
          System.out.println("FIX ERROR");
      }
      if (building != null && buildingIsBuildible(building)) {
        building.generateAnimations(assetManager);
        entities.add(building);
        buildings.add(building);
        consumeItems(building);
      }
    }
  }

  private void consumeItems(Building building) {
    HashMap<ItemType, Integer> buildingResources = building.getResourcesRequiredToBuild();
    for (ItemType item : buildingResources.keySet()) {
      itemMap.put(item, itemMap.get(item) - buildingResources.get(item));
    }
  }

  private boolean buildingIsBuildible(Building building) {
    HashMap<ItemType, Integer> buildingResources = building.getResourcesRequiredToBuild();
    for (ItemType item : buildingResources.keySet()) {
      if (itemMap.get(item) < buildingResources.get(item)) {
        return false;
      }
    }
    return true;
  }

  private void handleDeath() {
    ArrayList<Entity> entitiesCopy = new ArrayList<>();
    for (Entity entity : entities) {
      if (!entity.isDead()) {
        entitiesCopy.add(entity);
      } else if (entity instanceof EnemyUnit) {
        enemiesBeat++;
      }
    }
    entities = entitiesCopy;
  }

  private void handleSpawn(AssetManager assetManager) {
    for (Building building : buildings) {
      PlayerUnit playerUnit = building.createUnits(assetManager, collidibles);
      if (playerUnit != null) {
        entities.add(playerUnit);
      }
    }
  }

  private void handleResources() {
    for (Building building : buildings) {
      ItemLoad itemLoad = building.createResources();
      if (itemLoad != null) {
        ItemType type = itemLoad.getItem();
        itemMap.put(type, itemMap.get(type) + itemLoad.getAmount());
      }
    }
  }

  private void handleEnemySpawn(AssetManager assetManager) {
    enemySpawnTimer += deltaTime;
    if (enemySpawnTimer >= enemySpawnRate) {
      enemySpawnRate /= enemySpawnDivisor;
      enemySpawnTimer = 0;
      float radius = 1500;
      float degrees = (float) Math.random() * 360;
      Vector2 position = new Vector2(radius, 0).rotateDeg(degrees);
      EnemyUnit enemyUnit = new EnemyUnit(position);
      enemyUnit.generateAnimations(assetManager);
      entities.add(enemyUnit);
    }
  }

  private void handleGameOver() {
    List<PlayerUnit> units = getEntitiesByType(PlayerUnit.class);
    screenState = units.size() == 0 ? ScreenState.GAME_OVER : ScreenState.GAME_LOOP;
  }

  public <T extends Entity> List<T> getEntitiesByType(Class<T> type) {
    return entities.stream()
        .filter((e) -> type.isInstance(e))
        .map((e) -> type.cast(e))
        .collect(Collectors.toList());
  }

  public void update(Viewport viewport, AssetManager assetManager) {
    enemiesBeatDisplay.setEnemiesBeat(enemiesBeat);
    playerUnits = getEntitiesByType(PlayerUnit.class);
    handleGameOver();
    handleDeath();
    handleEnemySpawn(assetManager);
    handleSpawn(assetManager);
    handleResources();
    handleBuilding(getMousePos(), assetManager, buildings);
    deltaTime = Gdx.graphics.getDeltaTime();
    collidibles = entities.stream().map((e) -> e.getCollidible()).collect(Collectors.toList());
    Vector2 mousePos = getMousePos();
    buttonAction = buttonGroup.getCurrentAction(mousePos);
    for (Entity entity : entities) {
      entity.updateState(this);
    }
    if (buttonGroup.getCurrentAction(mousePos).equals(ButtonAction.Build)) {
      handleBuilding(mousePos, assetManager, buildings);
    }
    itemRenderer.setItemMap(itemMap);
  }

  @Override
  public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 mousePos, float deltaTime) {
    sb.setProjectionMatrix(viewport.getCamera().combined);
    sr.setProjectionMatrix(viewport.getCamera().combined);
    // sorts entities high to low to ensure that no overlapping occurs
    entities = entities.stream()
        .sorted((e1, e2) -> (int) Math.round(e1.getRenderOrderValue() - e2.getRenderOrderValue()))
        .collect(Collectors.toList());
    grid.renderTakenTiles(sr, buildings);
    for (Entity entity : entities) {
      entity.render(sr, sb);
    }
    grid.renderCurrentTile(mousePos, sr);
    selector.render(sr, mousePos);
    itemRenderer.render(sb);
    buttonGroup.render(sb, sr, mousePos);
    enemiesBeatDisplay.render(sb);
  }

  public int getEnemyCountOnScreen() {
    return (int) getEntitiesByType(EnemyUnit.class)
        .stream()
        .filter((e) -> Grid.onScreen(e.getCenter()) && !e.isDead())
        .count();
  }

  public ScreenState getScreenState() {
    return screenState;
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

  public float getDeltaTime() {
    return deltaTime;
  }

  public ButtonAction getButtonAction() {
    return buttonAction;
  }

  public List<Collidible> getCollidibles() {
    return collidibles;
  }

  public List<PlayerUnit> getPlayerUnits() {
    return playerUnits;
  }

}
