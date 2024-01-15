package com.mygdx.game.Entity;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.GameHelpers.Collidible;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ItemLoad;
import com.mygdx.game.GameHelpers.ItemType;

/**
 * Child of the Building class that creates Energy
 **/
public class Generator extends Building {
  private int productionTime = 3;

  /**
   * @param grid  grid that contains all building information and logic for
   *              converting into isometric units
   * @param tileX x position in isomteric coordinates
   * @param tileY y position in isometridc coordinates
   **/
  public Generator(Grid grid, int tileX, int tileY) {
    super(grid, tileX, tileY, "Generator");
  }

  /**
   * @return HumanUnit if enough time has elapsed for a unit to be created; null
   *         for Generators because they do not produce units
   **/
  public HumanUnit createUnits(AssetManager assetManager, List<Collidible> collidibles) {
    return null;
  }

  /**
   * @return ItemLoad containing the type and amount of resources created by this
   *         Generator or null if enough time has not elapsed
   **/
  public ItemLoad createResources() {
    ItemLoad itemLoad = null;
    if (getResourceTimer() > productionTime) {
      itemLoad = new ItemLoad(ItemType.Energy, 1);
      setResourceTimer(0);
    }
    return itemLoad;
  }

  /**
   * @return String value dispalyed on this Building's ActionButton
   **/
  public String getDescription() {
    return "Generates energy.\n" + super.getDescription();
  }
}
