package com.mygdx.game.Entity;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.RuntimeConfigurations;
import com.mygdx.game.UI.ButtonAction;

/**
 * Evil robot that attempts to despawn player units
 **/
public class EnemyUnit extends Unit {
  Entity entityDestination = null;
  private Sound explosionSound;

  /**
   * creates an enmy unit in the position given in the paramter
   * 
   * @param position position where the unit will spawn
   **/
  public EnemyUnit(final Vector2 position) {
    super(position, CollidibleType.EnemyUnit, "Enemy");
    setArriveThroughTree(new CollidibleType[] {});
    setAttackPower(7);
    setHealth(300);
  }

  /**
   * loads in animations from texture and sounds from assetManager
   * 
   * @param assetManager assetManager loaded with all sounds and textures for the
   *                     game
   **/
  @Override
  public void generateAnimations(final AssetManager assetManager) {
    explosionSound = assetManager.get("Explosion.wav", Sound.class);
    super.generateAnimations(assetManager);
  }

  /**
   * finds the current position that this EnemyUnit is travelling to
   * 
   * @param entities list of all entities in the game world
   * @param mousePos Vector2 point where the mouse pointer is on screen
   * @param mode     ButtonAction that describes which action is currently being
   *                 used by the player
   **/
  @Override
  protected void updateCurrentDestination(final List<Entity> entities, final Vector2 mousePos,
      final ButtonAction mode) {
    entityDestination = getNearestEntityType(entities, CollidibleType.PlayerUnit, false);
    if (entityDestination != null) {
      setCurrentDestination(entityDestination.getCenter());
    }
  }

  /**
   * finds the entity overlapping with this EnemyUnit's currentDestination
   * 
   * @param entities full list containing all entities in the game world
   * @return entity that this EnemyUnit is currently chasing
   **/
  @Override
  protected Entity getEntityDestination(final List<Entity> entities) {
    if (entityDestination != null) {
      return entityDestination;
    }
    return super.getEntityDestination(entities);
  }

  /**
   * decrements this EnemyUnit's health by the amount of attack power that it is
   * overlapping with
   * 
   * @param units     completed list of all units in the game world
   * @param deltaTime time in seconds since the previous render
   **/
  @Override
  protected void updateHealth(final List<Unit> units, final float deltaTime) {
    updateHealthOnCollisionWithUnitType(units, CollidibleType.PlayerUnit);
    if (getHealth() <= 0) {
      explosionSound.play(RuntimeConfigurations.getSfxVolumePercent());
    }
  }
}
