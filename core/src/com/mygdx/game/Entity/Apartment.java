package com.mygdx.game.Entity;

import java.util.HashMap;

import com.mygdx.game.GameHelpers.Grid;

public class Apartment extends Building {
  public Apartment(Grid grid, int tileX, int tileY) {
    super(grid, tileX, tileY, "Apartment");
    HashMap<Class<? extends PlayerUnit>, Integer> unitsCreatedPerMinute = new HashMap<>();
    unitsCreatedPerMinute.put(HumanUnit.class, 1);
    setUnitsCreatedPerMinute(unitsCreatedPerMinute);
  }
}
