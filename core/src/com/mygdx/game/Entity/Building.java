package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollisionManager;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.ICollidible;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Polygon;

public class Building implements ICollidible {
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

  public boolean lineCollide(Vector2 start, Vector2 end) {
    Vector2[] vertices = getVertices();
    for (int i = 0; i < 3; i++) {
      if (CollisionManager.lineLine(vertices[i], vertices[i + 1], start, end)) {
        return true;
      }
    }
    return false;
  }

  public boolean rectangleCollide(Rectangle rectangle) {
    Vector2[] vertices = getVertices();
    for (int i = 0; i < 3; i++) {
      if (CollisionManager.rectangleLine(rectangle, vertices[i], vertices[i + 1])) {
        return true;
      }
    }
    return false;
  }

  public boolean pointCollide(Vector2 point) {
    Vector2[] vertices = getVertices();
    float[] floatVertices = new float[8];
    for (int i = 0; i < vertices.length; i++) {
      floatVertices[i * 2] = vertices[i].x;
      floatVertices[i * 2 + 1] = vertices[i].y;
    }
    Polygon shape = new Polygon(floatVertices);
    return shape.contains(point);
  }
}
