package com.mygdx.game.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.CollisionManager;
import com.mygdx.game.GameHelpers.GameLoop;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.RectangleCollidible;
import com.mygdx.game.UI.ButtonAction;
import com.mygdx.game.GameHelpers.Collidible;

import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Entity that can find its way to destinations and battle other Units
 **/
public abstract class Unit extends Entity {

  private enum Direction {
    Up,
    Down,
    Left,
    Right
  }

  private Vector2 currentDestination = null;
  // outlines which CollidibleTypes this unit can cluster up with when going to a
  // destination
  private CollidibleType[] arriveThroughTree;
  private RectangleCollidible rectangleCollidible;
  private Vector2 velocity = new Vector2(0, 0);
  private HashMap<Direction, Animation<TextureRegion>> animationMap = new HashMap<>();
  private String name;
  // continuous time counter used to time animationa
  private float stateTime;
  private boolean stationary = true;
  private Sound hitSound;

  /**
   * @param position       Vector2 of the bottom left corner of where this Unit
   *                       should spawn
   * @param collidibleType enum containing whether this is a player or enemy unit
   * @param name           String that coordinates with the png used to create
   *                       this Unit
   **/
  public Unit(Vector2 position, CollidibleType collidibleType, String name) {
    super(new RectangleCollidible(position.x, position.y, 32, 32, collidibleType), 0, 0);
    currentDestination = null;
    this.rectangleCollidible = new RectangleCollidible(position.x, position.y, 32, 32, collidibleType);
    this.name = name;
    arriveThroughTree = new CollidibleType[] { collidibleType };
  }

  /**
   * loads in animations from the assetManager
   * 
   * @param assetManager assetManager pre-loaded with sounds and animations
   **/
  public void generateAnimations(AssetManager assetManager) {
    for (Direction direction : Direction.values()) {
      Texture texture = assetManager.get(name + "Walking" + direction.toString() + ".png", Texture.class);
      TextureRegion[][] regions = TextureRegion.split(texture, 16, 16);
      Animation<TextureRegion> animation = new Animation<>(1 / 4f, regions[0]);
      animationMap.put(direction, animation);
    }
    hitSound = assetManager.get("Hit.wav", Sound.class);
  }

  /**
   * contains logic that edits the amount of health that this unit has
   * 
   * @param units     list of all Units on the map
   * @param deltaTime time between render frames in seconds
   **/
  protected abstract void updateHealth(List<Unit> units, float deltaTime);

  /**
   * determines which direction this Unit should move
   * 
   * @param collidibles           list of all collidibles on the map
   * @param collidibleDestination the collidible overlapping with this Entity's
   *                              destination; ignored when calculating heading
   * @param depth                 the length of the line used to determine which
   *                              direction to go; how far ahead the unit can
   *                              "see"
   * @return a normalized Vector2 containing which direction this unit can go
   **/
  public Vector2 getHeading(List<Collidible> collidibles, Collidible collidibleDestination, float depth) {
    // look directly at your destination
    Vector2 detection = currentDestination != null ? currentDestination.cpy().sub(getCenter()).nor().scl(depth)
        : new Vector2(0, 0);
    float rotationCheck = 10f;
    boolean clearPath = false;
    int iterationCount = 0;
    // if your line of sight overlaps with any collidibles rotate it until your line
    // of sight is clear
    while (currentDestination != null && !clearPath && iterationCount < (int) Math.ceil(360 / rotationCheck)) {
      for (Collidible collidible : collidibles) {
        if (collidibleDestination != null && collidibleDestination.equals(collidible)
            || collidible.equals(getCollidible())
            || CollisionManager.overlappingColldibles(getCollidible(), collidible)) {
          continue;
        }
        Vector2 detectionEnd = getCenter().cpy().add(detection);
        if (collidible.lineCollide(getCenter(), detectionEnd) || collidible.pointCollide(detectionEnd)) {
          detection.rotateDeg(rotationCheck);
        }
      }
      iterationCount++;
    }
    return detection.nor();
  }

  /**
   * @param entities list of all entities on the map
   * @return Collidible that overlaps with this Unit's currentDestination
   **/
  protected Entity getEntityDestination(List<Entity> entities) {
    for (Entity entity : entities) {
      Collidible collidible = entity.getCollidible();
      if (!collidible.equals(getCollidible()) && collidible.pointCollide(currentDestination)) {
        return entity;
      }
    }
    return null;
  }

  /**
   * stops moving this unit if it is overlapping with any unit that may be
   * touching this Unit's collidibleDestination, even indirectly through multiple
   * other units
   * 
   * @param collidibles           list of all collidibles on the map
   * @param collidibleDestination Collidible that overlaps with this Unit's
   *                              currentDestination
   * @return true if this unit overlaps with a unit that may be touching this
   *         Unit's collidibleDestination, false otherwise
   **/
  private boolean arrivedAtColldibleTree(List<Collidible> collidibles, Collidible collidibleDestination) {
    if (collidibleDestination == null) {
      return false;
    }
    if (CollisionManager.overlappingColldibles(getCollidible(), collidibleDestination)) {
      return true;
    }
    List<Collidible> arrivableCollidibles = new ArrayList<>();
    arrivableCollidibles.add(collidibleDestination);
    for (int i = 0; i < collidibles.size(); i++) {
      // compare every collidible
      for (int j = 0; j < arrivableCollidibles.size(); j++) {
        Collidible collidible = collidibles.get(i);
        Collidible arrivableCollidible = arrivableCollidibles.get(j);
        // ignore if the collidible has already been checked, is this unit's collidible,
        // or is currently being iterated through
        if (collidible.equals(arrivableCollidible) || collidible.equals(getCollidible())
            || arrivableCollidibles.contains(collidible)) {
          continue;
        }
        // if this collidible is touching a unit that may be touching my
        // arrivableCollidible, even indirectly, restart the search
        if (CollisionManager.overlappingColldibles(collidible, arrivableCollidible)) {
          arrivableCollidibles.add(collidible);
          i = 0;
          j = 0;
          // if I am touching a unit that is touching my collidibleDestination, I am free
          // to stop moving
          if (CollisionManager.overlappingColldibles(getCollidible(), collidible)) {
            return true;
          }
        }
      }
    }
    // if I am not touching any units that may be touching my collidibleDestination,
    // I must keep moving
    return false;
  }

  /**
   * @param entities       list of all Entities on the map
   * @param collidibleType is the Entity your searching for a player or enemy
   * @param onScreen       if true, this method will only search for visible
   *                       Entities
   * @return Entity object with matching collidibleType
   **/
  protected Entity getNearestEntityType(List<Entity> entities, CollidibleType collidibleType, boolean onScreen) {
    float recordDistance = Float.MAX_VALUE;
    Entity closestEntity = null;
    for (Entity entity : entities) {
      if (entity.getCollidible().equals(getCollidible()) || !entity.getCollidibleType().equals(collidibleType)) {
        continue;
      }
      float distance = entity.getCenter().dst(getCenter());
      if (distance < recordDistance && (onScreen == false || onScreen == true && Grid.onScreen(entity.getCenter()))) {
        recordDistance = distance;
        closestEntity = entity;
      }
    }
    return closestEntity;
  }

  /**
   * Updates internally where this Unit wants to go
   * 
   * @param entities list of all entities on the map
   * @param mousePos Vector2 containing where the map is on the world
   * @param mode     what action is the player currently using
   **/
  protected abstract void updateCurrentDestination(List<Entity> entities, Vector2 mousePos, ButtonAction mode);

  /**
   * decrements this unit's health value if it is overlapping a Unit of the given
   * faction
   * 
   * @pram units lists of all units on the map
   * @param type CollidibleType to take damage from
   **/
  protected void updateHealthOnCollisionWithUnitType(List<Unit> units, CollidibleType type) {
    units.stream()
        .filter((u) -> u.getCollidibleType().equals(type)
            && !u.equals(this)
            && CollisionManager.overlappingColldibles(getCollidible(), u.getCollidible()))
        .forEach((u) -> setHealth(getHealth() - u.getAttackPower()));
  }

  /**
   * handles physics and spawning logic (must be run before render)
   * 
   * @param gameState contains where every Entity is and important physics values
   **/
  @Override
  public void updateState(GameLoop gameState) {
    updateHealth(gameState.getEntitiesByType(Unit.class), gameState.getDeltaTime());
    updateCurrentDestination(gameState.getEntities(), gameState.getMousePos(), gameState.getButtonAction());
    if (currentDestination == null) {
      return;
    }

    Entity entityDestination = getEntityDestination(gameState.getEntities());
    float detectionDepth = 70f;
    Collidible collidibleDestination = entityDestination == null ? null : entityDestination.getCollidible();
    Vector2 heading = getHeading(gameState.getCollidibles(), collidibleDestination, detectionDepth);
    float speed = 50f;
    velocity = heading.cpy().scl(gameState.getDeltaTime() * speed);
    boolean arrivedAtDestination = currentDestination != null && currentDestination.epsilonEquals(getCenter(), 3);
    boolean arrivatedAtCollidible = false;
    // if this unit has a destination to get to and is allowed to cluster up,
    // cluster up; otherwise avoid other units to prevent overlap
    if (entityDestination != null) {
      if (!Arrays.asList(arriveThroughTree).contains(entityDestination.getCollidibleType())) {
        arrivatedAtCollidible = CollisionManager.overlappingColldibles(getCollidible(),
            collidibleDestination);
      } else {
        arrivatedAtCollidible = arrivedAtColldibleTree(gameState.getCollidibles(), collidibleDestination);
      }
    }
    stationary = arrivedAtDestination || arrivatedAtCollidible;
    if (!stationary) {
      changePosition(velocity);
      rectangleCollidible.changePosition(velocity);
    }
    stateTime += gameState.getDeltaTime();
  }

  /**
   * displays this unit to the screen
   * 
   * @param sr ShapeRender that displays any geometry needed to display
   * @param sb SpriteBatch that displays the Unit's texture to the screen
   **/
  public void render(ShapeRenderer sr, SpriteBatch sb) {
    Animation<TextureRegion> animation;
    if (Math.abs(velocity.x) >= Math.abs(velocity.y)) {
      animation = velocity.x <= 0 ? animationMap.get(Direction.Left) : animationMap.get(Direction.Right);
    } else {
      animation = velocity.y <= 0 ? animationMap.get(Direction.Down) : animationMap.get(Direction.Up);
    }
    TextureRegion frame;
    if (stationary) {
      frame = animationMap.get(Direction.Down).getKeyFrames()[0];
      stateTime = 0;
    } else {
      frame = animation.getKeyFrame(stateTime, true);
    }
    Vector2 center = rectangleCollidible.getCenter();
    sb.begin();
    sb.draw(frame, center.x - frame.getRegionWidth() / 2, center.y - frame.getRegionHeight() / 2);
    sb.end();
  }

  /**
   * @return y value corresponding to what order this Unit should be rendered in
   *         (in this case its center y value)
   **/
  public float getRenderOrderValue() {
    return getCenter().y;
  }

  /**
   * @param currentDestination overwrites this Unit's currentDestinaion
   **/
  protected void setCurrentDestination(Vector2 currentDestination) {
    this.currentDestination = currentDestination;
  }

  /**
   * @return where this unit is walking towards
   **/
  protected Vector2 getCurrentDestination() {
    return currentDestination;
  }

  /**
   * @param arriveThroughTree sets what CollidibleType this Unit is not allowed to
   *                          cluster up with when moving around the map
   **/
  protected void setArriveThroughTree(CollidibleType[] arriveThroughTree) {
    this.arriveThroughTree = arriveThroughTree;
  }

  /**
   * @return loaded sound that plays when this Unit gets hit
   **/
  protected Sound getHitSound() {
    return hitSound;
  }
}
