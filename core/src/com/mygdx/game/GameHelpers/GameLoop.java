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

/**
 * Runs the main processes to play the game after navigating the menus
 **/
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
  private int enemiesBeaten;
  private EnemiesBeatDisplay enemiesBeatDisplay;
  private ScreenState screenState;
  private HashMap<SpawnType, Integer> spawnCounts;

  /**
   * Loads in all necessary Entities and objects to begin the game
   * 
   * @param viewport viewport that the game runs on; used to calculate mouse
   *                 position
   **/
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
    enemySpawnDivisor = 1.05f;
    enemiesBeaten = 0;
    screenState = ScreenState.GAME_LOOP;
    grid = new Grid(64, 32);
    selector = new Selector();
    buttonGroup = new ButtonGroup();
    itemRenderer = new ItemRenderer();
    enemiesBeatDisplay = new EnemiesBeatDisplay();
    int startingHumanCount = 5;
    for (int i = 0; i < startingHumanCount; i++) {
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
    spawnCounts = new HashMap<>();
    for (SpawnType type : SpawnType.values()) {
      spawnCounts.put(type, 0);
    }
    spawnCounts.put(SpawnType.MINE, 1);
    spawnCounts.put(SpawnType.GENERATOR, 1);
    spawnCounts.put(SpawnType.HUMAN, startingHumanCount);
  }

  /**
   * Generates all animations from textures loaded in from the assetManager
   * 
   * @param assetManager assetManager that must have pre-loaded game textures
   **/
  public void generateAnimations(AssetManager assetManager) {
    for (Entity entity : entities) {
      entity.generateAnimations(assetManager);
    }
  }

  /**
   * Gets the actual coordinates on the screen where the mouse is
   **/
  public Vector2 getMousePos() {
    return viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
  }

  /**
   * returns true if the position the mouse is in is in a tile on top of or
   * directly adjacent to a building
   * 
   * @param mousePos  position of the mouse on the screen
   * @param buildings list of all of the buildings on the map
   **/
  private boolean neighboringBuilding(Vector2 mousePos, List<Building> buildings) {
    Vector2 pos = grid.getGridCoordinates(mousePos);
    return buildings.stream().anyMatch((b) -> pos.x >= b.getTileX() - 1 && pos.x <= b.getTileX() + 1
        && pos.y >= b.getTileY() - 1 && pos.y <= b.getTileY() + 1);
  }

  /**
   * handles all logic related with creating a building and consuming the
   * resources to do {
   * 
   * @param mousePos     position of the mouse on the screen
   * @param assetManager assetManager that contains all pre-loaded textures; used
   *                     to initialize new buildings
   * @param buildings    list of all buildings on the map; used to determine if a
   *                     position is valid to place a building
   **/
  private void handleBuilding(Vector2 mousePos, AssetManager assetManager, List<Building> buildings) {
    SpawnType spawnType = null;
    if (buttonGroup.isInBuildMode() && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)
        && !neighboringBuilding(mousePos, buildings)) {
      Vector2 pos = grid.getGridCoordinates(mousePos);
      Building building = null;
      switch (buttonAction) {
        case Mine:
          building = new Mine(grid, (int) pos.x, (int) pos.y);
          spawnType = SpawnType.MINE;
          break;
        case Apartment:
          building = new Apartment(grid, (int) pos.x, (int) pos.y);
          spawnType = SpawnType.APARTMENT;
          break;
        case Generator:
          building = new Generator(grid, (int) pos.x, (int) pos.y);
          spawnType = SpawnType.GENERATOR;
          break;
        case Factory:
          building = new Factory(grid, (int) pos.x, (int) pos.y);
          spawnType = SpawnType.FACTORY;
          break;
        default:
          System.out.println("FIX ERROR");
      }
      if (building != null && buildingIsBuildible(building)) {
        building.generateAnimations(assetManager);
        entities.add(building);
        buildings.add(building);
        consumeItems(building);
        spawnCounts.put(spawnType, spawnCounts.get(spawnType) + 1);
      }
    }
  }

  /**
   * decrements item values if a building is properly made
   * 
   * @param building new building being created
   **/
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

  /**
   * removes entities from the entity list if they despawn and increments the
   * enemiesBeaten value if an enemy despawns
   **/
  private void handleDespawn() {
    ArrayList<Entity> entitiesCopy = new ArrayList<>();
    for (Entity entity : entities) {
      if (!entity.isDead()) {
        entitiesCopy.add(entity);
      } else if (entity instanceof EnemyUnit) {
        enemiesBeaten++;
      }
    }
    entities = entitiesCopy;
  }

  /**
   * handles the creation of new PlayerUnits that spawn from buildings
   * 
   * @param assetManager assetManager pre-loaded with all of the game's textures
   **/
  private void handleSpawn(AssetManager assetManager) {
    for (Building building : buildings) {
      PlayerUnit playerUnit = building.createUnits(assetManager, collidibles);
      if (playerUnit != null) {
        entities.add(playerUnit);
        if (playerUnit instanceof RobotUnit) {
          spawnCounts.put(SpawnType.ROBOT, spawnCounts.get(SpawnType.ROBOT) + 1);
        } else if (playerUnit instanceof HumanUnit) {
          spawnCounts.put(SpawnType.HUMAN, spawnCounts.get(SpawnType.HUMAN) + 1);
        }
      }
    }
  }

  /**
   * increments resources values when they are ready to be gathered
   **/
  private void handleResources() {
    for (Building building : buildings) {
      ItemLoad itemLoad = building.createResources();
      if (itemLoad != null) {
        ItemType type = itemLoad.getItem();
        itemMap.put(type, itemMap.get(type) + itemLoad.getAmount());
      }
    }
  }

  /**
   * creates enemies when the enemy spawn timer exceeds the enemy spawn rate
   **/
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

  /**
   * gathers all data from the last play session after all units have died
   **/
  private Records createRecords() {
    Records records = new Records();
    records.addRecord("Top Score", enemiesBeaten);
    records.addRecord("Enemies Beaten", enemiesBeaten);
    for (SpawnType type : spawnCounts.keySet()) {
      switch (type) {
        case HUMAN:
          records.addRecord("Humans Spawned", spawnCounts.get(type));
          break;
        case ROBOT:
          records.addRecord("Robots Spawned", spawnCounts.get(type));
          break;
        case APARTMENT:
          records.addRecord("Apartments Built", spawnCounts.get(type));
          break;
        case MINE:
          records.addRecord("Steel Mines Built", spawnCounts.get(type));
          break;
        case GENERATOR:
          records.addRecord("Generators Built", spawnCounts.get(type));
          break;
        case FACTORY:
          records.addRecord("Factories Built", spawnCounts.get(type));
          break;
      }
    }
    return records;
  }

  /**
   * reloads the GameLoop and switches to the game over screen when all player
   * units have despawned
   **/
  private void handleGameOver() {
    List<PlayerUnit> units = getEntitiesByType(PlayerUnit.class);
    if (units.size() == 0) {
      DatabaseManager.updateRecords(createRecords());
      screenState = ScreenState.GAME_OVER;
    } else {
      screenState = ScreenState.GAME_LOOP;
    }
  }

  /**
   * filters the entity list to only members of a certain class and casts them to
   * that class
   * 
   * @param class filters and casts the entities by this class
   * @returns filtered and casted list of entities
   **/
  public <T extends Entity> List<T> getEntitiesByType(Class<T> type) {
    return entities.stream()
        .filter((e) -> type.isInstance(e))
        .map((e) -> type.cast(e))
        .collect(Collectors.toList());
  }

  /**
   * updates physics and entity spawn data before rendering
   * 
   * @param viewport     viewport that the game is running on
   * @param assetManager assetManager that is pre-loaded with all of the game's
   *                     textures
   **/
  public void update(Viewport viewport, AssetManager assetManager) {
    enemiesBeatDisplay.setEnemiesBeat(enemiesBeaten);
    playerUnits = getEntitiesByType(PlayerUnit.class);
    handleGameOver();
    handleDespawn();
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

  /**
   * displays all visible objects to the screen along with HUD information
   * 
   * @param sb        SpriteBatch that displays Textures to the screen
   * @param sr        shapeRenderer that displays shapes to the screen
   * @param mousePos  Vector2 that holds the position of the mouse on the screen
   * @param deltaTime time between concurrent renders of the game
   **/
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

  /**
   * @returns screenState that is either GAME_LOOP if the game is still running or
   *          GAME_OVER if all player units have despawned
   **/
  public ScreenState getScreenState() {
    return screenState;
  }

  /**
   * resizes buttons upon resize of the window
   **/
  public void handleResize() {
    buttonGroup.calculateButtons();
  }

  /**
   * @returns grid object that contains the positions of all of the buildings on
   *          the grid and isometric conversion functions
   **/
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
