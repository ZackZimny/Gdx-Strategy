package com.mygdx.game.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.GameHelpers.Collidible;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ItemLoad;
import com.mygdx.game.GameHelpers.ItemType;

import java.util.List;

public class Mine extends Building {
  private int productionTime = 4;

  public Mine(Grid grid, int tileX, int tileY) {
    super(grid, tileX, tileY, "Mine");
  }

  public HumanUnit createUnits(AssetManager assetManager, List<Collidible> collidibles) {
    return null;
  }

  public ItemLoad createResources() {
    ItemLoad itemLoad = null;
    if (getResourceTimer() > productionTime) {
      itemLoad = new ItemLoad(ItemType.Steel, 5);
      setResourceTimer(0);
    }
    return itemLoad;
  }

  public String getDescription() {
    return "Creates Steel. \n" + super.getDescription();
  }
}
