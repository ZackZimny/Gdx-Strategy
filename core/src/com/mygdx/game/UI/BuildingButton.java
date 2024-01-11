package com.mygdx.game.UI;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Entity.Building;

public class BuildingButton extends Button {
  public BuildingButton(Building building, ButtonAction action, Rectangle rectangle, FontHandler fontHandler) {
    super(action, building.getDescription(), rectangle, fontHandler);
  }
}
