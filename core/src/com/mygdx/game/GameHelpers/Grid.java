package com.mygdx.game.GameHelpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Entity.Building;
import com.badlogic.gdx.math.Vector2;

public class Grid {
  private float tileWidth;
  private float tileHeight;
  private float tileWidthHalf;
  private float tileHeightHalf;
  private static int SIZE = 400;
  private static int HALF_SIZE = SIZE / 2;

  public Grid(int width, int height) {
    tileWidth = width;
    tileHeight = height;
    tileWidthHalf = tileWidth / 2f;
    tileHeightHalf = tileHeight / 2f;
  }

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

  public Vector2 getGridCoordinates(Vector3 mousePos) {
    // map.x = (screen.x / TILE_WIDTH_HALF + screen.y / TILE_HEIGHT_HALF) /2;
    // map.y = (screen.y / TILE_HEIGHT_HALF -(screen.x / TILE_WIDTH_HALF)) /2;
    return new Vector2((float) Math.floor((mousePos.x / tileWidthHalf + mousePos.y / tileHeightHalf) / 2),
        (float) Math.floor((mousePos.y / tileHeightHalf - (mousePos.x / tileWidthHalf)) / 2));
  }

  public Vector2 getGridCoordinates(Vector2 mousePos) {
    // map.x = (screen.x / TILE_WIDTH_HALF + screen.y / TILE_HEIGHT_HALF) /2;
    // map.y = (screen.y / TILE_HEIGHT_HALF -(screen.x / TILE_WIDTH_HALF)) /2;
    return new Vector2((float) Math.floor((mousePos.x / tileWidthHalf + mousePos.y / tileHeightHalf) / 2),
        (float) Math.floor((mousePos.y / tileHeightHalf - (mousePos.x / tileWidthHalf)) / 2));
  }

  public Vector2 getIsoCoordinates(Vector2 globalPosition) {
    // screen.x = (map.x - map.y) * TILE_WIDTH_HALF;
    // screen.y = (map.x + map.y) * TILE_HEIGHT_HALF;
    return new Vector2((globalPosition.x - globalPosition.y) * tileWidthHalf,
        (globalPosition.x + globalPosition.y) * tileHeightHalf);
  }

  public void renderCurrentTile(Vector2 mousePos, ShapeRenderer sr) {
    Vector2 tile = getGridCoordinates(mousePos);
    sr.begin(ShapeType.Filled);
    sr.setColor(Color.SKY);
    drawTile(sr, (int) tile.x, (int) tile.y);
    sr.end();
  }

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

  public Vector2 getIsoCenter(int row, int column) {
    Vector2 leftEdge = getIsoCoordinates(new Vector2(row, column));
    leftEdge.y += tileHeightHalf;
    return leftEdge;
  }

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
      sr.line(start, end);
    }
  }

  public void render(ShapeRenderer sr) {
    sr.begin(ShapeType.Line);
    sr.setColor(Color.BLACK);
    for (int i = -10; i < 10; i++) {
      for (int j = -10; j < 10; j++) {
        renderTile(sr, i, j);
      }
    }
    sr.end();
  }

}
