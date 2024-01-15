package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Collidible;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.GameLoop;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * handles collision logic and displaying texture to the screen
 **/
public abstract class Entity {
  private Collidible collidible;
  private CollidibleType collidibleType;
  private int health;
  private int attackPower;

  /**
   * @param collidible  enables hit detection with other collidibles
   * @param health      amount of hit points that this Entity can take
   * @param attackPower amount of hit points this Entity can do every frame
   **/
  public Entity(Collidible collidible, int health, int attackPower) {
    this.collidible = collidible;
    this.collidibleType = collidible.getCollidibleType();
    this.health = health;
    this.attackPower = attackPower;
  }

  /**
   * display this entity to the screen
   * 
   * @param sr ShapeRenderer that displays any geometry needed to the screen
   * @param sb SpriteBatch that displays any textures needed to the screen
   **/
  public abstract void render(ShapeRenderer sr, SpriteBatch sb);

  /**
   * handles physics and spawning logic. (Must be called before the render method)
   * 
   * @param gameState gameLoop information including deltaTime and position data
   *                  of other entities
   **/
  public abstract void updateState(GameLoop gameState);

  /**
   * loads in animations from the assetManager
   * 
   * @param assetManager assetManager containing all music and textures
   *                     pre-loaded; used to initialize new Entities
   **/
  public abstract void generateAnimations(AssetManager assetManager);

  /**
   * @return a value that determines when a Entity should be rendered; closely
   *         related to the Entity's position
   **/
  public abstract float getRenderOrderValue();

  /**
   * @return a Vector2 containing the center of this Entity's collsision shape
   **/
  public Vector2 getCenter() {
    return collidible.getCenter();
  }

  /**
   * changes position of this Entity's vertices by the specified amount
   * 
   * @param v Vector2 contianing the amount to move the vertices x and y position
   **/
  public void changePosition(Vector2 v) {
    collidible.changePosition(v);
  }

  /**
   * @param start Vector2 containing the start of the line
   * @param end   Vector2 contianing the end of the line (start and end are
   *              interchangable)
   * @return true if the line and this Entity's collision shapes overlap, false
   *         if they do not
   **/
  public boolean lineCollide(Vector2 start, Vector2 end) {
    return collidible.lineCollide(start, end);
  }

  /**
   * @param point point to check collision with this Entity's collision shape
   * @return true if the point is within this Entity's collision shape, false
   *         otherwise
   **/
  public boolean pointCollide(Vector2 point) {
    return collidible.pointCollide(point);
  }

  /**
   * @return true if the amount of hit points taken is greater than the health
   *         that this Entity has, false otherwise
   **/
  public boolean willDespawn() {
    return health <= 0;
  }

  /**
   * @return CollidibleType that links this Entity with being a Player, Building,
   *         or Enemy in an enum value
   **/
  public CollidibleType getCollidibleType() {
    return collidibleType;
  }

  /**
   * @return Vector2[] containing the position values of all of this Entity's
   *         collision shape values
   **/
  public Vector2[] getVertices() {
    return collidible.getVertices();
  }

  /**
   * @return this Entity's collision shape
   **/
  public Collidible getCollidible() {
    return collidible;
  }

  /**
   * @param collidible replaces this collidible with a new instance of the object
   **/
  public void setCollidible(Collidible collidible) {
    this.collidible = collidible;
  }

  /**
   * @param CollidibleType edits whether this Entity should be linked to the
   *                       Player, Enemy, or Building
   **/
  public void setCollidibleType(CollidibleType collidibleType) {
    this.collidibleType = collidibleType;
  }

  /**
   * @return amount of hit points that this Entity can take without despawning
   **/
  public int getHealth() {
    return health;
  }

  /**
   * @param health replaces this Entity's health value
   **/
  protected void setHealth(int health) {
    this.health = health;
  }

  /**
   * @return attackPower that this Entity can inflict per frame while overlapping
   *         with an enemy faction's unit
   **/
  public int getAttackPower() {
    return attackPower;
  }

  /**
   * @param attackPower attackPower that will replace this Entity's current value
   **/
  protected void setAttackPower(int attackPower) {
    this.attackPower = attackPower;
  }
}
