package com.mygdx.game.Entity;

import java.util.HashMap;

import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ItemType;

public class Factory extends Building {
  public Factory(Grid grid, int tileX, int tileY) {
    super(grid, tileX, tileY, "Factory");
    HashMap<Class<? extends PlayerUnit>, Integer> unitsCreatedPerMinute = new HashMap<>();
    unitsCreatedPerMinute.put(RobotUnit.class, 1);
    setUnitsCreatedPerMinute(unitsCreatedPerMinute);
    HashMap<ItemType, Integer> resourcesPerUnit = new HashMap<>();
    resourcesPerUnit.put(ItemType.Steel, 30);
    setResourcesPerUnit(resourcesPerUnit);
  }
}
