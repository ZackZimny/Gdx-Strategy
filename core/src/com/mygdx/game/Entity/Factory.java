package com.mygdx.game.Entity;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.GameHelpers.Collidible;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ItemLoad;
import com.mygdx.game.GameHelpers.ItemType;

public class Factory extends Building {
  private int unitGenerationSeconds = 15;

  public Factory(Grid grid, int tileX, int tileY) {
    super(grid, tileX, tileY, "Factory");
    HashMap<ItemType, Integer> resourcesRequiredToBuild = new HashMap<>();
    resourcesRequiredToBuild.put(ItemType.Steel, 50);
    resourcesRequiredToBuild.put(ItemType.Energy, 50);
    setResourcesRequiredToBuild(resourcesRequiredToBuild);
  }

  public PlayerUnit createUnits(AssetManager assetManager, List<Collidible> collidibles) {
    RobotUnit robot = null;
    if (getUnitTimer() > unitGenerationSeconds) {
      robot = new RobotUnit(generateRandomPosition(collidibles));
      robot.generateAnimations(assetManager);
      setUnitTimer(0);
    }
    return robot;
  }

  public String getDescription() {
    return "Creates Robot Units. \n" + super.getDescription();
  }

  public ItemLoad createResources() {
    return null;
  }
}
