package com.mygdx.game.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ItemLoad;
import java.util.List;
import com.mygdx.game.GameHelpers.Collidible;

public class Apartment extends Building {
  private int secondsUntilUnitCreated = 15;

  public Apartment(Grid grid, int tileX, int tileY) {
    super(grid, tileX, tileY, "Apartment");
  }

  public PlayerUnit createUnits(AssetManager assetManager, List<Collidible> collidibles) {
    HumanUnit humanUnit = null;
    if (getUnitTimer() > secondsUntilUnitCreated) {
      humanUnit = new HumanUnit(generateRandomPosition(collidibles));
      humanUnit.generateAnimations(assetManager);
      setUnitTimer(0);
    }
    return humanUnit;
  }

  public ItemLoad createResources() {
    return null;
  }

  public String getDescription() {
    return "Creates Human units. \n" + super.getDescription();
  }
}
