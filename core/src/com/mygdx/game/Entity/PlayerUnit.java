package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.ICollidible;
import com.mygdx.game.GameHelpers.Selector;
import java.util.ArrayList;

public class PlayerUnit extends Unit {
  private boolean stationary;
  private boolean isSelected = false;
  private boolean isClicked = false;
  private ICollidible collidibleDestination = null;
  private float attackPower = 2f;

  public PlayerUnit(Rectangle hurtbox) {
    super(hurtbox, CollidibleType.PlayerUnit);
    setCurrentDestination(null);
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
  }

  @Override
  public Color getColor() {
    return isSelected ? Color.RED : Color.BLUE;
  }

  @Override
  protected void updateCurrentDestination(ArrayList<ICollidible> collidibles, Vector2 mousePos, String mode) {
    if (mode.equals("Move") && isSelected() && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
      setCurrentDestination(mousePos);
      collidibleDestination = super.getCollidibleDestination(collidibles);
    } else if (mode.equals("Fight") && isSelected()) {
      ICollidible nearestEnemy = getNearestCollidibleType(collidibles, CollidibleType.EnemyUnit);
      System.out.println(nearestEnemy);
      setCurrentDestination(nearestEnemy.getVertices()[0]);
      collidibleDestination = nearestEnemy;
    }
  }

  @Override
  protected ICollidible getCollidibleDestination(ArrayList<ICollidible> collidibles) {
    if (collidibleDestination != null) {
      return collidibleDestination;
    }
    return super.getCollidibleDestination(collidibles);
  }

  public boolean isBounded(Selector selector) {
    if (selector.getBound() == null) {
      return false;
    }
    Vector2[] points = { new Vector2(getHurtbox().getX(), getHurtbox().getY()),
        new Vector2(getHurtbox().getX() + getHurtbox().getWidth(), getHurtbox().getY()),
        new Vector2(getHurtbox().getX() + getHurtbox().getWidth(), getHurtbox().getY() + getHurtbox().getHeight()),
        new Vector2(getHurtbox().getX(), getHurtbox().getY() + getHurtbox().getHeight()) };
    boolean isBounded = false;
    for (int i = 0; i < points.length; i++) {
      if (selector.getBound().contains(points[i])) {
        isBounded = true;
      }
    }
    return isBounded;
  }

  public boolean isSelected() {
    return isSelected;
  }

  public boolean isStationary() {
    return stationary;
  }
}
