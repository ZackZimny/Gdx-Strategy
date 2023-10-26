package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Grid;

public class Building {
  private Grid grid;
  private int tileX;
  private int tileY;
  private Vector2 gridPosition;
  private Vector2 position;

  public Building(Grid grid, int tileX, int tileY) {
    this.grid = grid;
    this.tileX = tileX;
    this.tileY = tileY;
    gridPosition = new Vector2(tileX, tileY);
    position = grid.getIsoCoordinates(gridPosition);
  }

  public void render(ShapeRenderer sr) {
    sr.begin();
    sr.setColor(Color.ORANGE);
    grid.renderTile(sr, tileX, tileY);
    sr.end();
  }

  public Vector2[] getVertices() {
    return grid.getVertices(tileX, tileY);
  }

  public int getTileX() {
    return tileX;
  }

  public int getTileY() {
    return tileY;
  }

}
