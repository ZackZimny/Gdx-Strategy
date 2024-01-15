package com.mygdx.game.Entity;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Collidible;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.CollisionManager;
import com.mygdx.game.GameHelpers.GameLoop;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ItemLoad;
import com.mygdx.game.GameHelpers.ItemType;

/**
 * Creates a building on the map with collision detection and a visual render
 **/
public abstract class Building extends Entity {
  // contains information about other buildings and methods to convert between
  // actual and isometric coordinates
  private final Grid grid;
  private final int tileX;
  private final int tileY;
  // used to draw the building
  private Texture texture;
  private final String name;
  // accumalates each frame the amount of time that has elapsed to determine when
  // resources should be gathered
  private float resourceTimer = 0f;
  // accumalates each frame the amount of time that has elapsed to determine when
  // units should spawn
  private float unitTimer = 0f;
  // maps the items and amount of the items that must be consumed in order to
  // create this building
  private HashMap<ItemType, Integer> resourcesRequiredToBuild;

  /**
   * @param grid  grid that other buildings are placed on
   * @param tileX x position in isometric coordinates
   * @param tileY y position in isometric coordinates
   * @param name  name of the building that coordinates with the texture png in
   *              the assets folder
   **/
  public Building(final Grid grid, final int tileX, final int tileY, final String name) {
    super(new Collidible(grid.getVertices(tileX, tileY), CollidibleType.Building), 1000, 0);
    this.grid = grid;
    this.tileX = tileX;
    this.tileY = tileY;
    this.name = name;
    setHealth(1000);
    setAttackPower(0);
    resourcesRequiredToBuild = new HashMap<>();
    resourcesRequiredToBuild.put(ItemType.Steel, 50);
  }

  /**
   * creates units of the specified subclass type when overwritten
   * 
   * @param assetManager fully loaded assetManager with textures to spawn new
   *                     units
   * @param collidibles  full list of collidibles used to validate new spawn point
   * @returns null if it is not time to create a unit or a player unit not
   *          overlapping with other collidibles when it's time
   **/
  public abstract PlayerUnit createUnits(AssetManager assetManager, List<Collidible> collidibles);

  /**
   * creates resources when time is ready
   * 
   * @returns ItemLoad that contains the amount and type of a resource made
   **/
  public abstract ItemLoad createResources();

  /**
   * creates a description for this building to be displayed on its ActionButton
   * 
   * @return text to display on ActionButton
   **/
  public String getDescription() {
    String itemString = "Resources: \n";
    for (final ItemType item : resourcesRequiredToBuild.keySet()) {
      itemString += item.toString() + ": " + resourcesRequiredToBuild.get(item) + "\n";
    }
    return itemString;
  }

  /**
   * displays the building to the screen
   * 
   * @param sr ShapeRenderer that draws necessary shapes
   * @param sb SpriteBatch that draws the proper texture for the building
   **/
  public void render(final ShapeRenderer sr, final SpriteBatch sb) {
    sb.begin();
    sb.draw(texture, getCenter().x - grid.getTileWidthHalf(), getCenter().y - grid.getTileHeightHalf());
    sb.end();
  }

  /**
   * loads in textures from the assetManager
   * 
   * @param assetManager assetManager to load textures from
   **/
  public void generateAnimations(final AssetManager assetManager) {
    texture = assetManager.get(name + ".png", Texture.class);
  }

  /**
   * creates a list of all edges of the isometric tile that this building lays
   * upon
   * 
   * @returns Vector2[] containing all edges
   **/
  public Vector2[] getVertices() {
    return grid.getVertices(tileX, tileY);
  }

  /**
   * @returns x position in isometric coordinates
   **/
  public int getTileX() {
    return tileX;
  }

  /**
   * @return y position in isometric coordinates
   **/
  public int getTileY() {
    return tileY;
  }

  /**
   * @param start starting position of the line to check for collision
   * @param end   ending position of the line to check for collision (start and
   *              end are reversible)
   * @returns true if the line overlaps with the isometric tile that this building
   *          is on and false if it doesn't
   **/
  public boolean lineCollide(final Vector2 start, final Vector2 end) {
    return getCollidible().lineCollide(start, end);
  }

  /**
   * updates various timers used to time building actions, like creating resources
   * or spawning units
   * 
   * @param gameState used to gether deltaTime; holds render and physics
   *                  information
   **/
  public void updateState(final GameLoop gameState) {
    resourceTimer += gameState.getDeltaTime();
    unitTimer += gameState.getDeltaTime();
  }

  /**
   * determines if the isometric tile that the building is on overlaps with the
   * parameter rectangle
   * 
   * @param rectangle rectangle to do the collision test against
   * @returns true if the tile overlaps with the paramter rectangle, false if the
   *          tile does not overlap
   **/
  public boolean rectangleCollide(final Rectangle rectangle) {
    // loops through each line that makes up the isometric tile and checks if it
    // overlaps with the rectangle
    final Vector2[] vertices = getVertices();
    for (int i = 0; i < 3; i++) {
      if (CollisionManager.rectangleLine(rectangle, vertices[i], vertices[i + 1])) {
        return true;
      }
    }
    return false;
  }

  /**
   * determines if the isometric tile that the building is on overlaps with a
   * point (Vector2)
   * 
   * @param point point to check collision against
   * @returns true if the point overlaps with this isometric tile, false if the
   *          point does not
   **/
  public boolean pointCollide(final Vector2 point) {
    // changes Vector2[] to float[] to use Libgdx's polygon.contains(Vector2 point)
    // method
    final Vector2[] vertices = getVertices();
    final float[] floatVertices = new float[8];
    for (int i = 0; i < vertices.length; i++) {
      floatVertices[i * 2] = vertices[i].x;
      floatVertices[i * 2 + 1] = vertices[i].y;
    }
    final Polygon shape = new Polygon(floatVertices);
    return shape.contains(point);
  }

  /**
   * @returns a ranking value related to the y position of the building. This
   *          value is used to render entities in order. Buildings tends to have a
   *          higher value so that units and other moving entities can move behind
   *          them.
   **/
  public float getRenderOrderValue() {
    return (float) (getCenter().y + grid.getTileHeight() * 2);
  }

  /**
   * @returns CollidibleType.Building to signify that this Entity is a Building;
   *          especially important for children
   **/
  public CollidibleType getCollidibleType() {
    return CollidibleType.Building;
  }

  /**
   * @returns grid containing the information for other buildings and methods for
   *          transferring between real and isometric coordinates
   **/
  public Grid getGrid() {
    return grid;
  }

  /**
   * @returns texture used to draw this building
   **/
  public Texture getTexture() {
    return texture;
  }

  /**
   * @returns name that is the same as the png file in the asset folder (does not
   *          include .png)
   **/
  public String getName() {
    return name;
  }

  /**
   * @returns how many seconds have elapsed since a resouce was last created or
   *          the game round started
   **/
  public float getResourceTimer() {
    return resourceTimer;
  }

  /**
   * @returns how many seconds have elapsed since a unit was spawned or the game
   *          round started
   **/
  public float getUnitTimer() {
    return unitTimer;
  }

  /**
   * @param resourcesRequiredToBuild hashmap that contains the ItemType and amount
   *                                 (Integer) of resources that must be consumed
   *                                 to create this building that replaces the
   *                                 current value
   **/
  public void setResourcesRequiredToBuild(final HashMap<ItemType, Integer> resourcesRequiredToBuild) {
    this.resourcesRequiredToBuild = resourcesRequiredToBuild;
  }

  /**
   * @returns hashmap that contains the ItemType and amount (Integer) of resources
   *          that must be consumed to create this building
   **/
  public HashMap<ItemType, Integer> getResourcesRequiredToBuild() {
    return resourcesRequiredToBuild;
  }

  /**
   * Finds a position on the map close to the building that does not have a unit
   * on it
   * 
   * @param collidibles complete list of collidibles to prevent spawning on
   *                    another collidible
   * @returns Vector2 position where no other units overlap
   **/
  protected Vector2 generateRandomPosition(final List<Collidible> collidibles) {
    final float degrees = (float) Math.random() * 360;
    float radius = 64;
    final Vector2 position = getCenter().cpy().add(new Vector2(radius, 0).rotateDeg(degrees));
    boolean invalidSpawn = !isValidSpawn(collidibles, position);
    while (invalidSpawn) {
      radius += 10;
      invalidSpawn = !isValidSpawn(collidibles, position);
    }
    return position;
  }

  /**
   * @param resourceTimer time to replace current amount of time elapsed in
   *                      resource timer in seconds
   **/
  protected void setResourceTimer(final float resourceTimer) {
    this.resourceTimer = resourceTimer;
  }

  /**
   * @param unitTimer time to replace current amount of time elapsed in
   *                  unit timer in seconds
   **/
  protected void setUnitTimer(final float unitTimer) {
    this.unitTimer = unitTimer;
  }

  /**
   * determines if a spawn point overlaps with any other collidibles
   * 
   * @param collidibles complete list of collidibles used to verify position
   * @param position    Vector2 to check if position is safe
   * @returns true if the spawn point is empty, false if it isn't
   **/
  private boolean isValidSpawn(final List<Collidible> collidibles, final Vector2 position) {
    return collidibles.stream()
        .anyMatch((c) -> c.pointCollide(position) || Grid.onScreen(position));
  }
}
