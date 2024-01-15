package com.mygdx.game.GameHelpers;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.Building;
import com.badlogic.gdx.math.Vector2;

/**
 * contains a list full of all Buildings on the map
 **/
public class Grid {
  private float tileWidth;
  private float tileHeight;
  private float tileWidthHalf;
  private float tileHeightHalf;

  /**
   * @param width  number of isometric tiles wide
   * @param height number of isometric tiles high
   **/
  public Grid(int width, int height) {
    tileWidth = width;
    tileHeight = height;
    tileWidthHalf = tileWidth / 2f;
    tileHeightHalf = tileHeight / 2f;
  }

  /**
   * @param position Vector2 to check if is onScreen
   * @return true if the point is on screen, false otherwise
   **/
  public static boolean onScreen(Vector2 position) {
    float halfHeight = Gdx.graphics.getHeight() / 2f;
    float halfWidth = Gdx.graphics.getWidth() / 2f;
    return position.x > -halfWidth && position.x < halfWidth && position.y > -halfHeight
        && position.y < halfHeight;
  }

  /**
   * displays the tile at position (row, column) to the screen
   * 
   * @param sr     ShapeRenderer that draws the tile lines
   * @param row    row number of the isometric tile (isometric coordinates)
   * @param column column number of the isometric tile (isometric coordinates)
   **/
  private void drawTile(ShapeRenderer sr, int row, int column) {

    Vector2[] positions = {
        new Vector2(0, 0),
        new Vector2(0, 1),
        new Vector2(1, 1),
        new Vector2(1, 0),
        new Vector2(0, 0)
    };
    for (int line = 0; line < positions.length - 1; line++) {
      Vector2 start = getIsoCoordinates(positions[line].cpy().add(row, column));
      Vector2 end = getIsoCoordinates(positions[line + 1].cpy().add(row, column));
      sr.line(start, end);
    }
  }

  /**
   * @param position Vector3 to convert to isometric coordinates
   * @return Vector2 containing isometric coordinates
   **/
  public Vector2 getGridCoordinates(Vector3 position) {
    // map.x = (screen.x / TILE_WIDTH_HALF + screen.y / TILE_HEIGHT_HALF) /2;
    // map.y = (screen.y / TILE_HEIGHT_HALF -(screen.x / TILE_WIDTH_HALF)) /2;
    return new Vector2((float) Math.floor((position.x / tileWidthHalf + position.y / tileHeightHalf) / 2),
        (float) Math.floor((position.y / tileHeightHalf - (position.x / tileWidthHalf)) / 2));
  }

  /**
   * @param position Vector2 to convert to isometric coordinates
   * @return Vector2 containing isometric coordinates
   **/
  public Vector2 getGridCoordinates(Vector2 position) {
    // map.x = (screen.x / TILE_WIDTH_HALF + screen.y / TILE_HEIGHT_HALF) /2;
    // map.y = (screen.y / TILE_HEIGHT_HALF -(screen.x / TILE_WIDTH_HALF)) /2;
    return new Vector2((float) Math.floor((position.x / tileWidthHalf + position.y / tileHeightHalf) / 2),
        (float) Math.floor((position.y / tileHeightHalf - (position.x / tileWidthHalf)) / 2));
  }

  /**
   * @param position isometric position to convert back to actual coordinates
   * @return real coordinates
   **/
  public Vector2 getIsoCoordinates(Vector2 position) {
    // screen.x = (map.x - map.y) * TILE_WIDTH_HALF;
    // screen.y = (map.x + map.y) * TILE_HEIGHT_HALF;
    return new Vector2((position.x - position.y) * tileWidthHalf,
        (position.x + position.y) * tileHeightHalf);
  }

  /**
   * Displays the tile the mouse is hovering over
   * 
   * @param mousePos position of the ouse in world coordinates
   * @param sr       ShapeRenderer used to draw the tile lines
   **/
  public void renderCurrentTile(Vector2 mousePos, ShapeRenderer sr) {
    Vector2 tile = getGridCoordinates(mousePos);
    sr.begin(ShapeType.Filled);
    sr.setColor(Color.SKY);
    drawTile(sr, (int) tile.x, (int) tile.y);
    sr.end();
  }

  /**
   * @param row    row number to get isometric vertices from
   * @param column column number to get isometric vertices from
   * @return Vector2[] vertices of isometric tile in actual coordinates
   **/
  public Vector2[] getVertices(int row, int column) {
    Vector2[] positions = {
        new Vector2(0, 0),
        new Vector2(0, 1),
        new Vector2(1, 1),
        new Vector2(1, 0)
    };
    Vector2[] verticies = new Vector2[4];
    for (int i = 0; i < positions.length; i++) {
      verticies[i] = getIsoCoordinates(positions[i].cpy().add(row, column));
    }
    return verticies;
  }

  /**
   * @param row    row number of the isometric coordinate
   * @param column column number of the isometric coordinate
   * @return Vector2 containing the center of the isometric tile asked for in the
   *         paramters in actual coordinates
   **/
  public Vector2 getIsoCenter(int row, int column) {
    Vector2 leftEdge = getIsoCoordinates(new Vector2(row, column));
    leftEdge.y += tileHeightHalf;
    return leftEdge;
  }

  /**
   * renders the tile asked for in the paramters of the method
   * 
   * @param sr     ShapeRenderer used to draw lines
   * @param row    row number of the isometric tile
   * @param column column number of the isometric tile
   **/
  public void renderTile(ShapeRenderer sr, int row, int column) {

    Vector2[] positions = {
        new Vector2(0, 0),
        new Vector2(0, 1),
        new Vector2(1, 1),
        new Vector2(1, 0),
        new Vector2(0, 0)
    };
    for (int line = 0; line < positions.length - 1; line++) {
      Vector2 start = getIsoCoordinates(positions[line].cpy().add(row, column));
      Vector2 end = getIsoCoordinates(positions[line + 1].cpy().add(row, column));
      sr.begin(ShapeType.Line);
      sr.setColor(Color.BLUE);
      sr.line(start, end);
      sr.end();
    }
  }

  /**
   * renders the tiles underneath and directly beside all buildings
   * 
   * @param sr        ShapeRenderer used to draw lines
   * @param buildings list of buildings to draw isometric tiles under
   **/
  public void renderTakenTiles(ShapeRenderer sr, List<Building> buildings) {
    for (Building building : buildings) {
      for (int x = -1; x <= 1; x++) {
        for (int y = -1; y <= 1; y++) {
          renderTile(sr, building.getTileX() + x, building.getTileY() + y);
        }
      }
    }
  }

  /**
   * @return width of an isometric tile
   **/
  public float getTileWidth() {
    return tileWidth;
  }

  /**
   * @return height of an isometric tile
   **/
  public float getTileHeight() {
    return tileHeight;
  }

  /**
   * @return width of an isometric tile
   **/
  public float getTileWidthHalf() {
    return tileWidthHalf;
  }

  /**
   * @return half the height of an isometric tile
   **/
  public float getTileHeightHalf() {
    return tileHeightHalf;
  }
}
