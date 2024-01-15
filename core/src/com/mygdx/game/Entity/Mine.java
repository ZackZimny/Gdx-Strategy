package com.mygdx.game.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.GameHelpers.Collidible;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ItemLoad;
import com.mygdx.game.GameHelpers.ItemType;

import java.util.List;

/**
 * Child of the buliding class that creates Steel
 **/
public class Mine extends Building {
  private int productionTime = 4;

  /**
   * @param grid  grid that contains all building information and logic for
   *              converting into isometric units
   * @param tileX x position in isomteric coordinates
   * @param tileY y position in isometridc coordinates
   **/
  public Mine(Grid grid, int tileX, int tileY) {
    super(grid, tileX, tileY, "Mine");
  }

  /**
   * @return HumanUnit if enough time has elapsed for a unit to be created; null
   *         for Mines because they do not produce units
   **/
  public HumanUnit createUnits(AssetManager assetManager, List<Collidible> collidibles) {
    return null;
  }

  /**
   * @return ItemLoad containing the type and amount of resources created by this
   *         Mine or null if enough time has not elapsed
   **/
  public ItemLoad createResources() {
    ItemLoad itemLoad = null;
    if (getResourceTimer() > productionTime) {
      itemLoad = new ItemLoad(ItemType.Steel, 5);
      setResourceTimer(0);
    }
    return itemLoad;
  }

  /**
   * @return String value dispalyed on this Building's ActionButton
   **/
  public String getDescription() {
    return "Creates Steel. \n" + super.getDescription();
  }
}
