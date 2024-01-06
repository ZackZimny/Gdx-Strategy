package com.mygdx.game.Entity;

import com.mygdx.game.GameHelpers.RectangleCollidible;

public class HumanUnit extends PlayerUnit {
  public HumanUnit(RectangleCollidible hurtbox) {
    super(hurtbox, "Human");
  }
}
