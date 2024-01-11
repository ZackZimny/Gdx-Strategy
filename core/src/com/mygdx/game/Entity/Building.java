package com.mygdx.game.Entity;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
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
import com.mygdx.game.GameHelpers.GameState;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ItemLoad;
import com.mygdx.game.GameHelpers.ItemType;

public abstract class Building extends Entity {
  private Grid grid;
  private int tileX;
  private int tileY;
  private Texture texture;
  private String name;
  private float resourceTimer = 0f;
  private float unitTimer = 0f;
  private boolean unitIsCreatable = false;
  private HashMap<ItemType, Integer> resourcesRequiredToBuild;

  public Building(Grid grid, int tileX, int tileY, String name) {
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

  protected Vector2 generateRandomPosition(List<Collidible> collidibles) {
    float degrees = (float) Math.random() * 360;
    float radius = 64;
    Vector2 position = getCenter().cpy().add(new Vector2(radius, 0).rotateDeg(degrees));
    boolean invalidSpawn = !isValidSpawn(collidibles, position);
    while (invalidSpawn) {
      radius += 10;
      invalidSpawn = !isValidSpawn(collidibles, position);
    }
    return position;
  }

  private boolean isValidSpawn(List<Collidible> collidibles, Vector2 position) {
    return collidibles.stream()
        .anyMatch((c) -> c.pointCollide(position) || Grid.onScreen(position));
  }

  public abstract PlayerUnit createUnits(AssetManager assetManager, List<Collidible> collidibles);

  public abstract ItemLoad createResources();

  public String getDescription() {
    String itemString = "Resources: ";
    for (ItemType item : resourcesRequiredToBuild.keySet()) {
      itemString += item.toString() + ": " + resourcesRequiredToBuild.get(item) + "\n";
    }
    return itemString;
  }

  public void render(ShapeRenderer sr, SpriteBatch sb) {
    sb.begin();
    sb.draw(texture, getCenter().x - grid.getTileWidthHalf(), getCenter().y - grid.getTileHeightHalf());
    sb.end();
  }

  public void generateAnimations(AssetManager assetManager) {
    texture = assetManager.get(name + ".png", Texture.class);
  }

  public Vector2[] getVertices() {
    return grid.getVertices(tileX, tileY);
  }

  public int getTileX() {
    return tileX;
  }

  public int getTileY() {
    return tileY;
  }

  public boolean lineCollide(Vector2 start, Vector2 end) {
    return getCollidible().lineCollide(start, end);
  }

  public void updateState(GameState gameState) {
    resourceTimer += gameState.getDeltaTime();
    unitTimer += gameState.getDeltaTime();
  }

  public boolean rectangleCollide(Rectangle rectangle) {
    Vector2[] vertices = getVertices();
    for (int i = 0; i < 3; i++) {
      if (CollisionManager.rectangleLine(rectangle, vertices[i], vertices[i + 1])) {
        return true;
      }
    }
    return false;
  }

  public boolean pointCollide(Vector2 point) {
    Vector2[] vertices = getVertices();
    float[] floatVertices = new float[8];
    for (int i = 0; i < vertices.length; i++) {
      floatVertices[i * 2] = vertices[i].x;
      floatVertices[i * 2 + 1] = vertices[i].y;
    }
    Polygon shape = new Polygon(floatVertices);
    return shape.contains(point);
  }

  public float getRenderOrderValue() {
    return (float) (getCenter().y + grid.getTileHeight() * 2);
  }

  public CollidibleType getCollidibleType() {
    return CollidibleType.Building;
  }

  public Grid getGrid() {
    return grid;
  }

  public Texture getTexture() {
    return texture;
  }

  public String getName() {
    return name;
  }

  public float getResourceTimer() {
    return resourceTimer;
  }

  protected void setResourceTimer(float resourceTimer) {
    this.resourceTimer = resourceTimer;
  }

  public float getUnitTimer() {
    return unitTimer;
  }

  protected void setUnitTimer(float unitTimer) {
    this.unitTimer = unitTimer;
  }

  public boolean unitIsCreatable() {
    return unitIsCreatable;
  }

  public void setResourcesRequiredToBuild(HashMap<ItemType, Integer> resourcesRequiredToBuild) {
    this.resourcesRequiredToBuild = resourcesRequiredToBuild;
  }

  public HashMap<ItemType, Integer> getResourcesRequiredToBuild() {
    return resourcesRequiredToBuild;
  }
}
