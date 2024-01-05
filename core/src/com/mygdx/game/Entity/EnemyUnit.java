package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.Collidible;
import com.mygdx.game.GameHelpers.RectangleCollidible;
import java.util.ArrayList;
import java.util.List;

public class EnemyUnit extends Unit {
  Collidible collidibleDestination = null;

  public EnemyUnit(RectangleCollidible hurtBox) {
    super(hurtBox, CollidibleType.EnemyUnit, 15f);
    setArriveThroughTree(new CollidibleType[] { CollidibleType.PlayerUnit });
  }

  @Override
  public Color getColor() {
    return Color.PURPLE;
  }

  @Override
  protected void updateCurrentDestination(List<Collidible> collidibles, Vector2 mousePos, String mode) {
    collidibleDestination = getNearestCollidibleType(collidibles, CollidibleType.PlayerUnit);
    setCurrentDestination(collidibleDestination.getCenter());
  }

  @Override
  protected Collidible getCollidibleDestination(List<Collidible> collidibles) {
    if (collidibleDestination != null) {
      return collidibleDestination;
    }
    return super.getCollidibleDestination(collidibles);
  }

  protected void updateHealth(List<Unit> units) {
    updateHealthOnCollisionWithUnitType(units, CollidibleType.PlayerUnit);
  }
}
