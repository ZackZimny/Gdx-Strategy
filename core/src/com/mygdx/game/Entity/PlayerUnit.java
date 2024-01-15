package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.CollisionManager;
import com.mygdx.game.GameHelpers.GameLoop;
import com.mygdx.game.GameHelpers.RuntimeConfigurations;
import com.mygdx.game.GameHelpers.Selector;
import com.mygdx.game.UI.ButtonAction;

import java.util.List;

/**
 * Unit aligned with the Player's faction; can be commanded by the player to do
 * various actions
 **/
public class PlayerUnit extends Unit {
  private boolean stationary;
  private boolean isSelected = false;
  private boolean isClicked = false;
  private Entity entityDestination = null;
  private float hitSoundTimer = 0f;

  /**
   * @param position Vector2 where the bottom left corner of this Unit will be
   *                 spawned
   * @param name     String that corresponds with this Unit's Texture png file in
   *                 the asset folder
   **/
  public PlayerUnit(Vector2 position, String name) {
    super(position, CollidibleType.PlayerUnit, name);
    setCurrentDestination(null);
  }

  /**
   * handles all logic related to the left click to select feature
   * 
   * @param selector holds the bounding box of the current selection
   * @param mousePos Vector2 containing the current mouse position on the screen
   **/
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

  /**
   * makes units move apart if they spawn atop each other
   * 
   * @param units complete list of PlayerUnits to check against to prevent
   *              overlapping
   **/
  private void handleOverlapping(List<PlayerUnit> units) {
    if (getCurrentDestination() == null || stationary) {
      for (Unit unit : units) {
        if (CollisionManager.overlappingColldibles(unit.getCollidible(), getCollidible()) && !unit.equals(this)) {
          setCurrentDestination(getCenter().cpy().add(50, 50));
          unit.setCurrentDestination(unit.getCenter().cpy().add(-50, -50));
        }
      }
    }
  }

  /**
   * updates physics and spawning logic
   * 
   * @param gameState gameLoop variable containing the collision values of other
   *                  units and physics values
   **/
  @Override
  public void updateState(GameLoop gameState) {
    handleOverlapping(gameState.getPlayerUnits());
    handleSelection(gameState.getSelector(), gameState.getMousePos());
    super.updateState(gameState);
  }

  /**
   * determines which position this Units should move to
   * 
   * @param entities complete list of entities on the screen
   * @param mousePos Vector2 position containing where the mouse is on the game
   *                 world
   * @param mode     enum containing what mode the player currently has selected
   *                 on the ActionButtons
   **/
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

  /**
   * @param entities complete list of entities in the game world
   * @return Entity that overlaps with this Unit's setCurrentDestination
   **/
  @Override
  protected Entity getEntityDestination(List<Entity> entities) {
    if (entityDestination != null) {
      return entityDestination;
    }
    return super.getEntityDestination(entities);
  }

  /**
   * decreases this Unit's health when it overlaps with an enemy Unit, and plays a
   * sound when hit
   * 
   * @param units     list of Units in the game world
   * @param deltaTime time between graphics renders of the screen
   **/
  protected void updateHealth(List<Unit> units, float deltaTime) {
    int prevHealth = getHealth();
    updateHealthOnCollisionWithUnitType(units, CollidibleType.EnemyUnit);
    if (getHealth() != prevHealth && hitSoundTimer >= 0.5f) {
      getHitSound().play(RuntimeConfigurations.getSfxVolumePercent());
      hitSoundTimer = 0;
      return;
    }
    hitSoundTimer += deltaTime;
  }

  /**
   * @param selector contains the bounding box when the player left clicks and
   *                 drags the mouse
   * @return true if this Unit is within the selector's bounding box, false
   *         otherwise
   **/
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

  /**
   * @return true if this Unit was selected during the player's last click and
   *         drag gesture false otherwise
   **/
  public boolean isSelected() {
    return isSelected;
  }

  /**
   * @return true if this Unit is not moving, false otherwise
   **/
  public boolean isStationary() {
    return stationary;
  }

  /**
   * @return true if this Unit was clicked during the last time the player left
   *         clicked, false otherwise
   **/
  public boolean isClicked() {
    return isClicked;
  }

}
