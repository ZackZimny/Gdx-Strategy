package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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

  public void handleMovement(ArrayList<ICollidible> collidibles, ShapeRenderer sr, Grid grid, Vector2 mousePos,
      float deltaTime) {
    if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && isSelected) {
      currentDestination = mousePos;
      stationary = false;
    }
    if (stationary || currentDestination == null) {
      return;
    }

    Vector2 detection = currentDestination != null ? currentDestination.cpy().sub(getCenter()).nor().scl(35f)
        : new Vector2(0, 0);
    float rotationCheck = 10f;
    boolean clearPath = false;
    int iterationCount = 0;
    ICollidible collidibleDestination = null;
    while (currentDestination != null && !clearPath && iterationCount < (int) Math.ceil(360 / rotationCheck)) {
      for (ICollidible collidible : collidibles) {
        if (collidibleDestination != null && collidibleDestination.equals(collidible) || collidible.equals(this)) {
          continue;
        }
        Vector2 detectionEnd = getCenter().cpy().add(detection);
        if (collidible.lineCollide(getCenter(), detectionEnd) || collidible.pointCollide(detectionEnd)) {
          detection.rotateDeg(rotationCheck);
        }
        if (collidible.pointCollide(currentDestination)) {
          collidibleDestination = collidible;
        }
      }
      iterationCount++;
    }
    Vector2 velocity = detection.cpy().nor().scl(deltaTime * 60f);
    Vector2 ignorePathCheck = currentDestination.cpy().sub(getCenter()).nor().scl(deltaTime * 60 * 3);
    boolean arrivedAtDestination = currentDestination != null && currentDestination.epsilonEquals(getCenter(), 3);
    Vector2 endPoint = getCenter().cpy().add(ignorePathCheck);
    boolean arrivedAtColldible = collidibleDestination != null
        && (collidibleDestination.rectangleCollide(getHurtbox())
            || collidibleDestination.pointCollide(endPoint));
    if (!(arrivedAtDestination || arrivedAtColldible)) {
      changePosition(velocity);
    }
    sr.begin();
    sr.line(getCenter(), getCenter().cpy().add(detection));
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
    sr.begin(ShapeType.Filled);
    sr.setColor(color);
    sr.rect(getHurtbox().getX(), getHurtbox().getY(), getHurtbox().getWidth(), getHurtbox().getHeight());
    sr.end();
  }

  public boolean isSelected() {
    return isSelected;
  }

  public boolean isStationary() {
    return stationary;
  }

}
