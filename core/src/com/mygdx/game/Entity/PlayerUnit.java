package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.GameLoop;
import com.mygdx.game.GameHelpers.RuntimeConfigurations;
import com.mygdx.game.GameHelpers.Selector;
import com.mygdx.game.UI.ButtonAction;

import java.util.List;

public class PlayerUnit extends Unit {
  private boolean stationary;
  private boolean isSelected = false;
  private boolean isClicked = false;
  private Entity entityDestination = null;
  private float hitSoundTimer = 0f;

  public PlayerUnit(Vector2 position, String name) {
    super(position, CollidibleType.PlayerUnit, name);
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
  public void updateState(GameLoop gameState) {
    handleSelection(gameState.getSelector(), gameState.getMousePos());
    super.updateState(gameState);
  }

  @Override
  public Color getColor() {
    return isSelected ? Color.RED : Color.BLUE;
  }

  @Override
  protected void updateCurrentDestination(List<Entity> entities, Vector2 mousePos, ButtonAction mode) {
    if (mode.equals(ButtonAction.Move) && isSelected() && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
      setCurrentDestination(mousePos);
      entityDestination = super.getEntityDestination(entities);
    } else if (mode.equals(ButtonAction.Fight) && isSelected()) {
      Entity nearestEnemy = getNearestEntityType(entities, CollidibleType.EnemyUnit, true);
      if (nearestEnemy != null) {
        setCurrentDestination(nearestEnemy.getCenter());
        entityDestination = nearestEnemy;
      }
    }
  }

  @Override
  protected Entity getEntityDestination(List<Entity> entities) {
    if (entityDestination != null) {
      return entityDestination;
    }
    return super.getEntityDestination(entities);
  }

  protected void updateHealth(List<Unit> units, float deltaTime) {
    int prevHealth = getHealth();
    updateHealthOnCollisionWithUnitType(units, CollidibleType.EnemyUnit);
    if (getHealth() != prevHealth && hitSoundTimer >= 1) {
      getHitSound().play(RuntimeConfigurations.getSfxVolumePercent());
      hitSoundTimer = 0;
      return;
    }
    hitSoundTimer += deltaTime;
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
