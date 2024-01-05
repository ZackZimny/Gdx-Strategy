package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.GameState;
import com.mygdx.game.GameHelpers.RectangleCollidible;
import com.mygdx.game.GameHelpers.Collidible;
import com.mygdx.game.GameHelpers.Selector;
import java.util.List;

public class PlayerUnit extends Unit {
  private boolean stationary;
  private boolean isSelected = false;
  private boolean isClicked = false;
  private Collidible collidibleDestination = null;

  public PlayerUnit(RectangleCollidible hurtbox) {
    super(hurtbox, CollidibleType.PlayerUnit, 20f);
    setCurrentDestination(null);
  }

  public void handleSelection(Selector selector, Vector2 mousePos) {
    boolean isBounded = isBounded(selector);

    if (isBounded) {
      isSelected = true;
    }

    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
      if (pointCollide(mousePos)) {
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
  public void updateState(GameState gameState) {
    handleSelection(gameState.getSelector(), gameState.getMousePos());
    super.updateState(gameState);
  }

  @Override
  public Color getColor() {
    return isSelected ? Color.RED : Color.BLUE;
  }

  @Override
  protected void updateCurrentDestination(List<Collidible> collidibles, Vector2 mousePos, String mode) {
    if (mode.equals("Move") && isSelected() && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
      setCurrentDestination(mousePos);
      collidibleDestination = super.getCollidibleDestination(collidibles);
    } else if (mode.equals("Fight") && isSelected()) {
      Collidible nearestEnemy = getNearestCollidibleType(collidibles, CollidibleType.EnemyUnit);
      setCurrentDestination(nearestEnemy.getCenter());
      collidibleDestination = nearestEnemy;
    }
  }

  @Override
  protected Collidible getCollidibleDestination(List<Collidible> collidibles) {
    if (collidibleDestination != null) {
      return collidibleDestination;
    }
    return super.getCollidibleDestination(collidibles);
  }

  protected void updateHealth(List<Unit> units) {
    updateHealthOnCollisionWithUnitType(units, CollidibleType.EnemyUnit);
  }

  public boolean isBounded(Selector selector) {
    if (selector.getBound() == null) {
      return false;
    }
    for (Vector2 verticy : getVertices()) {
      if (selector.getBound().contains(verticy)) {
        return true;
      }
    }
    return false;
  }

  public boolean isSelected() {
    return isSelected;
  }

  public boolean isStationary() {
    return stationary;
  }

  public boolean isClicked() {
    return isClicked;
  }

}
