package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.ICollidible;
import java.util.ArrayList;

public class EnemyUnit extends Unit {
  public EnemyUnit(Rectangle rectangle) {
    super(rectangle);
  }

  @Override
  public Color getColor() {
    return Color.PURPLE;
  }

  @Override
  protected void updateCurrentDestination(ArrayList<ICollidible> collidibles, Vector2 mousePos, String mode) {
    setCurrentDestination(getNearestCollidibleType(collidibles, CollidibleType.PlayerUnit).getVertices()[0]);
  }
}
