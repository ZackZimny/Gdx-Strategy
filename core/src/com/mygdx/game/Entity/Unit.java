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
import com.mygdx.game.GameHelpers.ICollidible;
import com.mygdx.game.GameHelpers.ItemType;

import java.util.ArrayList;
import java.util.Arrays;

public class Unit extends Entity {

  private Vector2 currentDestination;
  private boolean stationary;
  private boolean isSelected = false;
  private CollidibleType[] arriveThroughTree;

  public Unit(Rectangle hurtbox, CollidibleType collidibleType) {
    super(hurtbox, collidibleType);
    currentDestination = null;
    arriveThroughTree = new CollidibleType[] { collidibleType };
  }

  public Color getColor() {
    return Color.ORANGE;
  }

  public Vector2 getHeading(ArrayList<ICollidible> collidibles, ICollidible collidibleDestination, float depth) {
    Vector2 detection = currentDestination != null ? currentDestination.cpy().sub(getCenter()).nor().scl(depth)
        : new Vector2(0, 0);
    float rotationCheck = 10f;
    boolean clearPath = false;
    int iterationCount = 0;
    while (currentDestination != null && !clearPath && iterationCount < (int) Math.ceil(360 / rotationCheck)) {
      for (ICollidible collidible : collidibles) {
        if (collidibleDestination != null && collidibleDestination.equals(collidible) || collidible.equals(this)) {
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

  protected ICollidible getCollidibleDestination(ArrayList<ICollidible> collidibles) {
    for (ICollidible collidible : collidibles) {
      if (!collidible.equals(this) && collidible.pointCollide(currentDestination)) {
        return collidible;
      }
    }
    return null;
  }

  private boolean arrivedAtColldibleTree(ArrayList<ICollidible> collidibles, ICollidible collidibleDestination) {
    if (collidibleDestination == null) {
      return false;
    }
    if (CollisionManager.overlappingColldibles(this, collidibleDestination)) {
      return true;
    }
    ArrayList<ICollidible> arrivableCollidibles = new ArrayList<>();
    arrivableCollidibles.add(collidibleDestination);
    for (int i = 0; i < collidibles.size(); i++) {
      for (int j = 0; j < arrivableCollidibles.size(); j++) {
        ICollidible collidible = collidibles.get(i);
        ICollidible arrivableCollidible = arrivableCollidibles.get(j);
        if (collidible.equals(arrivableCollidible) || collidible.equals(this)
            || arrivableCollidibles.contains(collidible)) {
          continue;
        }
        if (CollisionManager.overlappingColldibles(collidible, arrivableCollidible)) {
          arrivableCollidibles.add(collidible);
          i = 0;
          j = 0;
          if (CollisionManager.overlappingColldibles(this, collidible)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  protected ICollidible getNearestCollidibleType(ArrayList<ICollidible> collidibles, CollidibleType collidibleType) {
    float recordDistance = Float.MAX_VALUE;
    ICollidible closestCollidible = null;
    for (ICollidible collidible : collidibles) {
      if (collidible.equals(this) || !collidible.getCollidibleType().equals(collidibleType)) {
        continue;
      }
      float distance = collidible.getVertices()[0].dst(getPosition());
      if (distance < recordDistance) {
        recordDistance = distance;
        closestCollidible = collidible;
      }
    }
    return closestCollidible;
  }

  protected void updateCurrentDestination(ArrayList<ICollidible> collidibles, Vector2 mousePos, String mode) {
    if (!(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && isSelected)) {
      return;
    }
    stationary = false;
    currentDestination = mousePos;
  }

  public void handleMovement(ArrayList<ICollidible> collidibles, ShapeRenderer sr, Vector2 mousePos, float deltaTime,
      String mode) {
    updateCurrentDestination(collidibles, mousePos, mode);
    if (stationary || currentDestination == null) {
      return;
    }

    ICollidible collidibleDestination = getCollidibleDestination(collidibles);
    float detectionDepth = 35f;
    Vector2 heading = getHeading(collidibles, collidibleDestination, detectionDepth);
    Vector2 velocity = heading.cpy().scl(deltaTime * 60f);
    Vector2 detectionEnd = getCenter().cpy().add(heading.cpy().scl(detectionDepth));
    boolean arrivedAtDestination = currentDestination != null && currentDestination.epsilonEquals(getCenter(), 3);
    boolean arrivatedAtCollidible = false;
    if (collidibleDestination != null) {
      if (!Arrays.asList(arriveThroughTree).contains(collidibleDestination.getCollidibleType())) {
        arrivatedAtCollidible = CollisionManager.overlappingColldibles(this, collidibleDestination);
      } else {
        arrivatedAtCollidible = arrivedAtColldibleTree(collidibles, collidibleDestination);
      }
    }
    if (!(arrivedAtDestination || arrivatedAtCollidible)) {
      changePosition(velocity);
    }
    sr.begin();
    sr.line(getCenter(), detectionEnd);
    sr.end();
  }

  public void render(ShapeRenderer sr) {
    sr.begin(ShapeType.Line);
    sr.setColor(Color.BLACK);
    sr.rect(getHurtbox().getX(), getHurtbox().getY(), getHurtbox().getWidth(), getHurtbox().getHeight());
    sr.end();
    float padding = 10f;
    sr.setColor(getColor());
    sr.begin(ShapeType.Filled);
    sr.rect(getHurtbox().getX() + padding / 2f, getHurtbox().getY() + padding / 2f, getHurtbox().getWidth() - padding,
        getHurtbox().getHeight() - padding);
    sr.end();
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

}
