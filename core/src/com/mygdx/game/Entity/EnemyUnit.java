package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.ICollidible;
import java.util.ArrayList;

public class EnemyUnit extends Unit {
  ICollidible collidibleDestination = null;

  public EnemyUnit(Rectangle rectangle) {
    super(rectangle, CollidibleType.EnemyUnit);
    setArriveThroughTree(new CollidibleType[] { CollidibleType.PlayerUnit });
  }

  @Override
  public Color getColor() {
    return Color.PURPLE;
  }

  @Override
  protected void updateCurrentDestination(ArrayList<ICollidible> collidibles, Vector2 mousePos, String mode) {
    collidibleDestination = getNearestCollidibleType(collidibles, CollidibleType.PlayerUnit);
    setCurrentDestination(collidibleDestination.getVertices()[0]);
  }

  @Override
  protected ICollidible getCollidibleDestination(ArrayList<ICollidible> collidibles) {
    if (collidibleDestination != null) {
      return collidibleDestination;
    }
    return super.getCollidibleDestination(collidibles);
  }
}
