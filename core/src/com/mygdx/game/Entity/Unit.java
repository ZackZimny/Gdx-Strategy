package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.CollisionManager;
import com.mygdx.game.GameHelpers.GameState;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ItemType;
import com.mygdx.game.GameHelpers.RectangleCollidible;
import com.mygdx.game.UI.ButtonAction;
import com.mygdx.game.GameHelpers.Collidible;

import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class Unit extends Entity {

  private enum Direction {
    Up,
    Down,
    Left,
    Right
  }

  private Vector2 currentDestination = null;
  private CollidibleType[] arriveThroughTree;
  private RectangleCollidible rectangleCollidible;
  private Vector2 velocity = new Vector2(0, 0);
  private HashMap<ItemType, Integer> resourcesUsedToRun = new HashMap<>();
  private HashMap<Direction, Animation<TextureRegion>> animationMap = new HashMap<>();
  private String name;
  private float stateTime;
  private boolean stationary = true;

  public Unit(Vector2 position, CollidibleType collidibleType, String name) {
    super(new RectangleCollidible(position.x, position.y, 32, 32, collidibleType), 0, 0);
    currentDestination = null;
    this.rectangleCollidible = new RectangleCollidible(position.x, position.y, 32, 32, collidibleType);
    this.name = name;
    arriveThroughTree = new CollidibleType[] { collidibleType };
  }

  public abstract Color getColor();

  public void generateAnimations(AssetManager assetManager) {
    for (Direction direction : Direction.values()) {
      Texture texture = assetManager.get(name + "Walking" + direction.toString() + ".png", Texture.class);
      TextureRegion[][] regions = TextureRegion.split(texture, 16, 16);
      Animation<TextureRegion> animation = new Animation<>(1 / 4f, regions[0]);
      animationMap.put(direction, animation);
    }
  }

  protected abstract void updateHealth(List<Unit> units);

  public Vector2 getHeading(List<Collidible> collidibles, Collidible collidibleDestination, float depth) {
    Vector2 detection = currentDestination != null ? currentDestination.cpy().sub(getCenter()).nor().scl(depth)
        : new Vector2(0, 0);
    float rotationCheck = 10f;
    boolean clearPath = false;
    int iterationCount = 0;
    while (currentDestination != null && !clearPath && iterationCount < (int) Math.ceil(360 / rotationCheck)) {
      for (Collidible collidible : collidibles) {
        if (collidibleDestination != null && collidibleDestination.equals(collidible)
            || collidible.equals(getCollidible())) {
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

  protected Entity getEntityDestination(List<Entity> entities) {
    for (Entity entity : entities) {
      Collidible collidible = entity.getCollidible();
      if (!collidible.equals(getCollidible()) && collidible.pointCollide(currentDestination)) {
        return entity;
      }
    }
    return null;
  }

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
      for (int j = 0; j < arrivableCollidibles.size(); j++) {
        Collidible collidible = collidibles.get(i);
        Collidible arrivableCollidible = arrivableCollidibles.get(j);
        if (collidible.equals(arrivableCollidible) || collidible.equals(getCollidible())
            || arrivableCollidibles.contains(collidible)) {
          continue;
        }
        if (CollisionManager.overlappingColldibles(collidible, arrivableCollidible)) {
          arrivableCollidibles.add(collidible);
          i = 0;
          j = 0;
          if (CollisionManager.overlappingColldibles(getCollidible(), collidible)) {
            return true;
          }
        }
      }
    }
    return false;
  }

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

  protected abstract void updateCurrentDestination(List<Entity> entities, Vector2 mousePos, ButtonAction mode);

  protected void updateHealthOnCollisionWithUnitType(List<Unit> units, CollidibleType type) {
    units.stream()
        .filter((u) -> u.getCollidibleType().equals(type)
            && !u.equals(this)
            && CollisionManager.overlappingColldibles(getCollidible(), u.getCollidible()))
        .forEach((u) -> setHealth(getHealth() - u.getAttackPower()));
  }

  @Override
  public void updateState(GameState gameState) {
    updateHealth(gameState.getEntitiesByType(Unit.class));
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

  public void render(ShapeRenderer sr, SpriteBatch sb) {
    Animation<TextureRegion> animation;
    if (Math.abs(velocity.x) >= Math.abs(velocity.y)) {
      animation = velocity.x <= 0 ? animationMap.get(Direction.Left) : animationMap.get(Direction.Right);
    } else {
      animation = velocity.y <= 0 ? animationMap.get(Direction.Down) : animationMap.get(Direction.Up);
    }
    TextureRegion frame;
    Color line = stationary ? Color.BLUE : Color.RED;
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
    sr.begin(ShapeType.Line);
    sr.setColor(line);
    sr.rect(rectangleCollidible.getX(), rectangleCollidible.getY(),
        rectangleCollidible.getWidth(),
        rectangleCollidible.getHeight());
    sr.end();
  }

  public float getRenderOrderValue() {
    return getCenter().y;
  }

  protected void setCurrentDestination(Vector2 currentDestination) {
    this.currentDestination = currentDestination;
  }

  protected Vector2 getCurrentDestination() {
    return currentDestination;
  }

  protected void setArriveThroughTree(CollidibleType[] arriveThroughTree) {
    this.arriveThroughTree = arriveThroughTree;
  }
}
