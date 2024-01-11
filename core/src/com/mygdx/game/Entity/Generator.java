package com.mygdx.game.Entity;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.GameHelpers.Collidible;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ItemLoad;
import com.mygdx.game.GameHelpers.ItemType;

public class Generator extends Building {
  private int productionTime = 5;

  public Generator(Grid grid, int tileX, int tileY) {
    super(grid, tileX, tileY, "Generator");
  }

  public HumanUnit createUnits(AssetManager assetManager, List<Collidible> collidibles) {
    return null;
  }

  public ItemLoad createResources() {
    ItemLoad itemLoad = null;
    if (getResourceTimer() > productionTime) {
      itemLoad = new ItemLoad(ItemType.Energy, 1);
      setResourceTimer(0);
    }
    return itemLoad;
  }

  public String getDescription() {
    return "Generates energy.\n" + super.getDescription();
  }
}
