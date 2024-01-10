package com.mygdx.game.Entity;

import com.mygdx.game.GameHelpers.Grid;

public class Mine extends Building {
  public Mine(Grid grid, int tileX, int tileY) {
    super(grid, tileX, tileY, "Mine");
  }
}
