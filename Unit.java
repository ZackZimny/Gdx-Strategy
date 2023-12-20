package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollisionManager;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ICollidible;
import com.mygdx.game.GameHelpers.Selector;
import java.util.ArrayList;

public class Unit extends Entity {

  private Color color = Color.BLUE;
  private Vector2 currentDestination;
  private boolean stationary;
  private boolean isSelected = false;
  private boolean isClicked = false;

  public Unit(Rectangle hurtbox) {
    super(hurtbox);
    currentDestination = null;
  }

  public void handleSelection(Selector selector, Vector2 mousePos, float deltaTime) {
    boolean isBounded = isBounded(selector);

    if (isBounded) {
      isSelected = true;
    }

    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
      if (getHurtbox().contains(mousePos)) {
        isClicked = true;
        isSelected = true;
      } else {
        isClicked = false;
        isSelected = false;
      }
    }

    if (selector.getBound() != null && !isBounded && !isClicked) {
      isSelected = false;
    }

    color = isSelected ? Color.RED : Color.BLUE;
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

  private ICollidible getCollidibleDestination(ArrayList<ICollidible> collidibles) {
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
      ICollidible collidible = collidibles.get(i);
      for (int j = 0; j < arrivableCollidibles.size(); j++) {
        ICollidible arrivableCollidible = arrivableCollidibles.get(j);
        if (collidible.equals(arrivableCollidible)
            || collidible.equals(this) || arrivableCollidibles.contains(collidible)) {
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

  public void handleMovement(ArrayList<ICollidible> collidibles, ShapeRenderer sr, Grid grid, Vector2 mousePos,
      float deltaTime) {
    if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && isSelected) {
      currentDestination = mousePos;
      stationary = false;
    }
    if (stationary || currentDestination == null) {
      return;
    }

    ICollidible collidibleDestination = getCollidibleDestination(collidibles);
    float detectionDepth = 35f;
    Vector2 heading = getHeading(collidibles, collidibleDestination, detectionDepth);
    Vector2 velocity = heading.cpy().scl(deltaTime * 60f);
    Vector2 detectionEnd = getCenter().cpy().add(heading.cpy().scl(detectionDepth));
    boolean arrivedAtDestination = currentDestination != null && currentDestination.epsilonEquals(getCenter(), 3);
    boolean destinationIsBuilding = collidibleDestination != null
        && collidibleDestination.getClass().equals(Building.class);
    boolean arrivatedAtCollidble = false;
    if (collidibleDestination != null) {
      if (destinationIsBuilding) {
        arrivatedAtCollidble = CollisionManager.overlappingColldibles(this, collidibleDestination);
      } else {
        arrivatedAtCollidble = arrivedAtColldibleTree(collidibles, collidibleDestination);
      }
    }
    if (!(arrivedAtDestination || arrivatedAtCollidble)) {
      changePosition(velocity);
    }
    sr.begin();
    sr.line(getCenter(), detectionEnd);
    sr.end();
  }

  public boolean isBounded(Selector selector) {
    if (selector.getBound() == null) {
      return false;
    }
    Vector2[] points = {
        new Vector2(getHurtbox().getX(), getHurtbox().getY()),
        new Vector2(getHurtbox().getX() + getHurtbox().getWidth(), getHurtbox().getY()),
        new Vector2(getHurtbox().getX() + getHurtbox().getWidth(), getHurtbox().getY() + getHurtbox().getHeight()),
        new Vector2(getHurtbox().getX(), getHurtbox().getY() + getHurtbox().getHeight())
    };
    boolean isBounded = false;
    for (int i = 0; i < points.length; i++) {
      if (selector.getBound().contains(points[i])) {
        isBounded = true;
      }
    }
    return isBounded;
  }

  public void render(ShapeRenderer sr) {
    sr.begin(ShapeType.Line);
    sr.setColor(Color.BLACK);
    sr.rect(getHurtbox().getX(), getHurtbox().getY(), getHurtbox().getWidth(), getHurtbox().getHeight());
    sr.end();
    float padding = 10f;
    sr.setColor(color);
    sr.begin(ShapeType.Filled);
    sr.rect(getHurtbox().getX() + padding / 2f, getHurtbox().getY() + padding / 2f, getHurtbox().getWidth() - padding,
        getHurtbox().getHeight() - padding);
    sr.end();
  }

  public boolean isSelected() {
    return isSelected;
  }

  public boolean isStationary() {
    return stationary;
  }

}
