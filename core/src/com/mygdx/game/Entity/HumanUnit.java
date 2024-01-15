package com.mygdx.game.Entity;

import com.badlogic.gdx.math.Vector2;

/**
 * A PlayerUnit that's weaker, but cheaper in resources than the RobotUnit
 **/
public class HumanUnit extends PlayerUnit {
  /**
   * @param position Vector2 location where this HumanUnit should be spawned
   *                 (bottom left corner)
   **/
  public HumanUnit(Vector2 position) {
    super(position, "Human");
    setHealth(200);
    setAttackPower(3);
  }
}
