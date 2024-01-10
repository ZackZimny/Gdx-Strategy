package com.mygdx.game.Entity;

import java.util.HashMap;

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
import com.mygdx.game.GameHelpers.ItemHandler;
import com.mygdx.game.GameHelpers.ItemType;

public class Building extends Entity {
  private Grid grid;
  private int tileX;
  private int tileY;
  private Texture texture;
  private String name;
  private HashMap<ItemType, Integer> resourcesRequiredToBuild;
  private HashMap<ItemType, Integer> resourcesCreatedPerMinute;
  private HashMap<Class<? extends PlayerUnit>, Integer> unitsCreatedPerMinute;
  private float resourceTimer = 0f;
  private float unitTimer = 0f;
  private boolean unitIsCreatable = false;

  public Building(Grid grid, int tileX, int tileY, String name) {
    super(new Collidible(grid.getVertices(tileX, tileY), CollidibleType.Building), 1000, 0);
    this.grid = grid;
    this.tileX = tileX;
    this.tileY = tileY;
    this.name = name;
    setHealth(1000);
    setAttackPower(0);
    HashMap<ItemType, Integer> resourcesRequiredToBuild = new HashMap<>();
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
    ItemHandler itemHandler = gameState.getItemHandler();
    unitIsCreatable = resourcesPerUnit.keySet().stream()
        .allMatch((r) -> itemHandler.getItemCount(r) > resourcesPerUnit.get(r));
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

  public HashMap<ItemType, Integer> getResourcesRequiredToBuild() {
    return resourcesRequiredToBuild;
  }

  public HashMap<ItemType, Integer> getResourcesCreatedPerMinute() {
    return resourcesCreatedPerMinute;
  }

  public HashMap<ItemType, Integer> getResourcesPerUnit() {
    return resourcesPerUnit;
  }

  public float getResourceTimer() {
    return resourceTimer;
  }

  public float getUnitTimer() {
    return unitTimer;
  }

  public boolean unitIsCreatable() {
    return unitIsCreatable;
  }

  protected void setResourcesRequiredToBuild(HashMap<ItemType, Integer> resourcesRequiredToBuild) {
    this.resourcesRequiredToBuild = resourcesRequiredToBuild;
  }

  protected void setResourcesCreatedPerMinute(HashMap<ItemType, Integer> resourcesCreatedPerMinute) {
    this.resourcesCreatedPerMinute = resourcesCreatedPerMinute;
  }

  protected void setResourcesPerUnit(HashMap<ItemType, Integer> resourcesPerUnit) {
    this.resourcesPerUnit = resourcesPerUnit;
  }

  public HashMap<Class<? extends PlayerUnit>, Integer> getUnitsCreatedPerMinute() {
    return unitsCreatedPerMinute;
  }

  protected void setUnitsCreatedPerMinute(HashMap<Class<? extends PlayerUnit>, Integer> unitsCreatedPerMinute) {
    this.unitsCreatedPerMinute = unitsCreatedPerMinute;
  }
}
