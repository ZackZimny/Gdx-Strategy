package com.mygdx.game.Entity;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.GameHelpers.Collidible;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ItemLoad;
import com.mygdx.game.GameHelpers.ItemType;

/**
 * Building class child that creates RobotUnits
 **/
public class Factory extends Building {
  private int unitGenerationSeconds = 15;

  /**
   * @param grid  grid that contains all building information and logic for
   *              converting into isometric units
   * @param tileX x position in isomteric coordinates
   * @param tileY y position in isometridc coordinates
   **/
  public Factory(Grid grid, int tileX, int tileY) {
    super(grid, tileX, tileY, "Factory");
    HashMap<ItemType, Integer> resourcesRequiredToBuild = new HashMap<>();
    resourcesRequiredToBuild.put(ItemType.Steel, 50);
    resourcesRequiredToBuild.put(ItemType.Energy, 50);
    setResourcesRequiredToBuild(resourcesRequiredToBuild);
  }

  /**
   * @param assetManager assetManager pre-loaded with textures and music; used to
   *                     initialize new Units created
   * @param collidibles  list of all Collidibles on screen
   * @return a PlayerUnit if enough time has elapsed to create one, null
   *         otherwise
   **/
  public PlayerUnit createUnits(AssetManager assetManager, List<Collidible> collidibles) {
    RobotUnit robot = null;
    if (getUnitTimer() > unitGenerationSeconds) {
      robot = new RobotUnit(generateRandomPosition(collidibles));
      robot.generateAnimations(assetManager);
      setUnitTimer(0);
    }
    return robot;
  }

  /**
   * @return String value dispalyed on this Building's ActionButton
   **/
  public String getDescription() {
    return "Creates Robot Units. \n" + super.getDescription();
  }

  /**
   * @return an ItemLoad containing the type and amount of resources created;
   *         returns null because factories do not create Energy or Steel
   **/
  public ItemLoad createResources() {
    return null;
  }
}
