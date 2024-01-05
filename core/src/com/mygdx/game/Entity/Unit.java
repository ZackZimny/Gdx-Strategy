package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.CollisionManager;
import com.mygdx.game.GameHelpers.GameState;
import com.mygdx.game.GameHelpers.RectangleCollidible;
import com.mygdx.game.GameHelpers.Collidible;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Unit extends Entity {

  private Vector2 currentDestination = null;
  private CollidibleType[] arriveThroughTree;
  private int attackPower = 2;
  private int health = 100;
  private RectangleCollidible rectangleCollidible;
  private float padding;
  private Vector2 detectionEnd;

  public Unit(RectangleCollidible rectangleCollidible, CollidibleType collidibleType, float padding) {
    super(rectangleCollidible, collidibleType);
    currentDestination = null;
    this.rectangleCollidible = rectangleCollidible;
    arriveThroughTree = new CollidibleType[] { collidibleType };
    this.padding = padding;
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

  protected Collidible getCollidibleDestination(List<Collidible> collidibles) {
    for (Collidible collidible : collidibles) {
      if (!collidible.equals(getCollidible()) && collidible.pointCollide(currentDestination)) {
        return collidible;
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

  protected Collidible getNearestCollidibleType(List<Collidible> collidibles, CollidibleType collidibleType) {
    float recordDistance = Float.MAX_VALUE;
    Collidible closestCollidible = null;
    for (Collidible collidible : collidibles) {
      if (collidible.equals(getCollidible()) || !collidible.getCollidibleType().equals(collidibleType)) {
        continue;
      }
      float distance = collidible.getVertices()[0].dst(getCenter());
      if (distance < recordDistance) {
        recordDistance = distance;
        closestCollidible = collidible;
      }
    }
    return closestCollidible;
  }

  protected abstract void updateCurrentDestination(List<Collidible> entities, Vector2 mousePos, String mode);

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
    updateCurrentDestination(gameState.getCollidibles(), gameState.getMousePos(), gameState.getActionMode());
    if (currentDestination == null) {
      return;
    }

    Collidible collidibleDestination = getCollidibleDestination(gameState.getCollidibles());
    float detectionDepth = 70f;
    Vector2 heading = getHeading(gameState.getCollidibles(), collidibleDestination, detectionDepth);
    Vector2 velocity = heading.cpy().scl(gameState.getDeltaTime() * 35f);
    detectionEnd = getCenter().cpy().add(heading.cpy().scl(detectionDepth));
    boolean arrivedAtDestination = currentDestination != null && currentDestination.epsilonEquals(getCenter(), 3);
    boolean arrivatedAtCollidible = false;
    if (collidibleDestination != null) {
      if (!Arrays.asList(arriveThroughTree).contains(collidibleDestination.getCollidibleType())) {
        arrivatedAtCollidible = CollisionManager.overlappingColldibles(getCollidible(), collidibleDestination);
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

  protected void setCurrentDestination(Vector2 collidibleDestination) {
    this.currentDestination = collidibleDestination;
  }

  protected Vector2 getCurrentDestination() {
    return currentDestination;
  }

  protected void setArriveThroughTree(CollidibleType[] arriveThroughTree) {
    this.arriveThroughTree = arriveThroughTree;
  }

  public int getAttackPower() {
    return attackPower;
  }

  public int getHealth() {
    return health;
  }

  protected void setHealth(int health) {
    this.health = health;
  }

}
