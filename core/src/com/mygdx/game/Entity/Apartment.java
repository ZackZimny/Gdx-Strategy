package com.mygdx.game.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ItemLoad;
import java.util.List;
import com.mygdx.game.GameHelpers.Collidible;

/**
 * Building that creates human units
 **/
public class Apartment extends Building {
  private int secondsUntilUnitCreated = 10;

  /**
   * @param grid  grid object that contains the position of the other buildings on
   *              the map
   * @param tileX x position of the building in isometric coordinates
   * @param tileY y position of the building in isometric coordinates
   **/
  public Apartment(Grid grid, int tileX, int tileY) {
    super(grid, tileX, tileY, "Apartment");
  }

  /**
   * creates new human units once the unit timer exceeds the amount of time needed
   * to create a unit
   * 
   * @param assetManager assetManager with all of the game's textures preloaded;
   *                     used to initialize new human units
   * @param collidible   list of all of the collidibles on the map; used to
   *                     prevent units from spawning upon one another
   * @returns a player Unit if it is time to create one or null if the new unit is
   *          not ready to spawn
   **/
  public PlayerUnit createUnits(AssetManager assetManager, List<Collidible> collidibles) {
    HumanUnit humanUnit = null;
    if (getUnitTimer() > secondsUntilUnitCreated) {
      humanUnit = new HumanUnit(generateRandomPosition(collidibles));
      humanUnit.generateAnimations(assetManager);
      setUnitTimer(0);
    }
    return humanUnit;
  }

  /**
   * creates steel and energy if applicable; since the apartment doesn't, the
   * function returns null
   **/
  public ItemLoad createResources() {
    return null;
  }

  /**
   * @returns description used on the BuildButton for this Entity
   **/
  public String getDescription() {
    return "Creates Human units. \n" + super.getDescription();
  }
}
