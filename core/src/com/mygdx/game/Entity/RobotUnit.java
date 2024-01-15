package com.mygdx.game.Entity;

import com.badlogic.gdx.math.Vector2;

/**
 * PlayerUnit that is more powerful than a HumanUnit, but requires more
 * resources to build
 **/
public class RobotUnit extends PlayerUnit {
  /**
   * @param position Vector2 that contains the position that this RobotUnit should
   *                 spawn into (bottom left corner)
   **/
  public RobotUnit(Vector2 position) {
    super(position, "Robot");
    setHealth(300);
    setAttackPower(5);
  }
}
