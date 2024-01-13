package com.mygdx.game.Entity;

import com.badlogic.gdx.math.Vector2;

public class RobotUnit extends PlayerUnit {
  public RobotUnit(Vector2 position) {
    super(position, "Robot");
    setHealth(300);
    setAttackPower(6);
  }
}
