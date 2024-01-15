package com.mygdx.game.UI;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Entity.Building;

/**
 * GameButton with an auto-filled description based on the building passed in
 **/
public class BuildingButton extends GameButton {
  /**
   * @param building    building to gather description from
   * @param action      ButtonAction this button is associated with
   * @param rectangle   button display and collision rectangle
   * @param fontHandler handles text display
   **/
  public BuildingButton(Building building, ButtonAction action, Rectangle rectangle, FontHandler fontHandler) {
    super(action, building.getDescription(), rectangle, fontHandler);
  }
}
