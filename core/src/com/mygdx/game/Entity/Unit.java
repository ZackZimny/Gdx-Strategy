package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.CollisionManager;
import com.mygdx.game.GameHelpers.GameState;
import com.mygdx.game.GameHelpers.ItemType;
import com.mygdx.game.GameHelpers.RectangleCollidible;
import com.mygdx.game.GameHelpers.Collidible;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class Unit extends Entity {

  private Vector2 currentDestination = null;
  private CollidibleType[] arriveThroughTree;
  private RectangleCollidible rectangleCollidible;
  private float padding = 20f;
  private Vector2 detectionEnd;
  private HashMap<ItemType, Integer> resourcesUsedToRun = new HashMap<>();

  public Unit(RectangleCollidible rectangleCollidible, CollidibleType collidibleType, String name) {
    super(rectangleCollidible, 0, 0);
    currentDestination = null;
    this.rectangleCollidible = rectangleCollidible;
    arriveThroughTree = new CollidibleType[] { collidibleType };
    JsonReader jsonReader = new JsonReader();
    JsonValue root = jsonReader.parse(Gdx.files.internal("stats.json"));
    JsonValue stats = root.get("Units").get(name);
    setHealth(stats.getInt("health"));
    setAttackPower(stats.getInt("attackPower"));
    JsonValue resources = stats.get("resourcesUsedToRun");
    if (resources == null) {
      return;
    }
    for (ItemType type : ItemType.values()) {
      if (resources.hasChild(type.toString())) {
        resourcesUsedToRun.put(type, resources.getInt(type.toString()));
      }
    }
  }

  public abstract Color getColor();

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

  protected Entity getNearestEntityType(List<Entity> entities, CollidibleType collidibleType) {
    float recordDistance = Float.MAX_VALUE;
    Entity closestEntity = null;
    for (Entity entity : entities) {
      if (entity.getCollidible().equals(getCollidible()) || !entity.getCollidibleType().equals(collidibleType)) {
        continue;
      }
      float distance = entity.getVertices()[0].dst(getCenter());
      if (distance < recordDistance) {
        recordDistance = distance;
        closestEntity = entity;
      }
    }
    return closestEntity;
  }

  protected abstract void updateCurrentDestination(List<Entity> entities, Vector2 mousePos, String mode);

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
    updateCurrentDestination(gameState.getEntities(), gameState.getMousePos(), gameState.getActionMode());
    if (currentDestination == null) {
      return;
    }

    Entity entityDestination = getEntityDestination(gameState.getEntities());
    float detectionDepth = 70f;
    Collidible collidibleDestination = entityDestination == null ? null : entityDestination.getCollidible();
    Vector2 heading = getHeading(gameState.getCollidibles(), collidibleDestination, detectionDepth);
    Vector2 velocity = heading.cpy().scl(gameState.getDeltaTime() * 35f);
    detectionEnd = getCenter().cpy().add(heading.cpy().scl(detectionDepth));
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
    if (!(arrivedAtDestination || arrivatedAtCollidible)) {
      changePosition(velocity);
      rectangleCollidible.changePosition(velocity);
    }
  }

  public void render(ShapeRenderer sr) {
    sr.begin(ShapeType.Line);
    sr.setColor(Color.BLACK);
    sr.rect(rectangleCollidible.getX(), rectangleCollidible.getY(), rectangleCollidible.getWidth(),
        rectangleCollidible.getHeight());
    sr.end();
    sr.setColor(getColor());
    sr.begin(ShapeType.Filled);
    sr.rect(rectangleCollidible.getX() + padding / 2f, rectangleCollidible.getY() + padding / 2f,
        rectangleCollidible.getWidth() - padding,
        rectangleCollidible.getHeight() - padding);
    sr.end();
    if (detectionEnd != null) {
      sr.begin(ShapeType.Line);
      sr.line(getCenter(), detectionEnd);
      sr.end();
    }
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
