package com.mygdx.game.Entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.RectangleCollidible;

public class HumanUnit extends PlayerUnit {
  public HumanUnit(Vector2 position) {
    super(position, "Human");
    setHealth(200);
    setAttackPower(3);
  }
}
