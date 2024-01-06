package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.RectangleCollidible;
import java.util.List;

public class EnemyUnit extends Unit {
  Entity entityDestination = null;

  public EnemyUnit(RectangleCollidible hurtBox) {
    super(hurtBox, CollidibleType.EnemyUnit, "Enemy");
    setArriveThroughTree(new CollidibleType[] {});
  }

  @Override
  public Color getColor() {
    return Color.PURPLE;
  }

  @Override
  protected void updateCurrentDestination(List<Entity> entities, Vector2 mousePos, String mode) {
    entityDestination = getNearestEntityType(entities, CollidibleType.PlayerUnit);
    if (entityDestination != null) {
      setCurrentDestination(entityDestination.getCenter());
    }
  }

  @Override
  protected Entity getEntityDestination(List<Entity> entities) {
    if (entityDestination != null) {
      return entityDestination;
    }
    return super.getEntityDestination(entities);
  }

  protected void updateHealth(List<Unit> units) {
    updateHealthOnCollisionWithUnitType(units, CollidibleType.PlayerUnit);
  }
}
