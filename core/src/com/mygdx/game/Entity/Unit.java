package com.mygdx.game.Entity;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Selector;

public class Unit extends Entity {

  private Color color;
  private Vector2 destination;
  private boolean stationary;
  private float speed = 60;
  private boolean isSelected = false;
  private boolean isClicked = false;

  public Unit(Rectangle hurtbox) {
    super(hurtbox);
    destination = getPosition().cpy();
  }

  public void handlePhysics(Selector selector, Vector2 mousePos, float deltaTime) {
    boolean isBounded = isBounded(selector);

    if (isBounded) {
      isSelected = true;
    }

    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && getHurtbox().contains(mousePos)) {
      isClicked = true;
      isSelected = true;
    }

    if (selector.getBound() != null && !isBounded && !isClicked) {
      isSelected = false;
    }

    if (isSelected && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
      destination = mousePos.cpy();
      stationary = false;
    }
    color = isSelected ? Color.RED : Color.BLUE;

    if (!stationary) {
      Vector2 velocity = destination.cpy().sub(getPosition().cpy()).nor().scl(speed).scl(deltaTime);
      changePosition(velocity);
      isClicked = false;
    }

    if (getPosition().epsilonEquals(destination, 2)) {
      stationary = true;
      setPosition(destination.cpy());
    }
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

  public void display(ShapeRenderer sr) {
    sr.begin(ShapeType.Filled);
    sr.setColor(color);
    sr.rect(getHurtbox().getX(), getHurtbox().getY(), getHurtbox().getWidth(), getHurtbox().getHeight());
    sr.end();
  }
}
