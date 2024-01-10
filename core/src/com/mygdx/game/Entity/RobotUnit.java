package com.mygdx.game.Entity;

import com.mygdx.game.GameHelpers.RectangleCollidible;

public class RobotUnit extends PlayerUnit {
  public RobotUnit(RectangleCollidible rectangleCollidible) {
    super(rectangleCollidible, "Robot");
    setHealth(300);
    setAttackPower(8);
  }
}
