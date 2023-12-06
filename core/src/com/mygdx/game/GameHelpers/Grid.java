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
import com.mygdx.game.Pathfinding.DestinationNode;
import com.mygdx.game.Pathfinding.NodeConnection;
import com.mygdx.game.Pathfinding.NodeGraph;
import com.badlogic.gdx.math.Vector2;

public class Grid {
  private float tileWidth;
  private float tileHeight;
  private float tileWidthHalf;
  private float tileHeightHalf;
  private static int SIZE = 20;
  private static int HALF_SIZE = SIZE / 2;
  private ArrayList<Building> buildings = new ArrayList<>();
  private NodeGraph nodeGraph = new NodeGraph();
  private DestinationNode[][] nodes = new DestinationNode[SIZE][SIZE];

  public Grid(int width, int height) {
    tileWidth = width;
    tileHeight = height;
    tileWidthHalf = tileWidth / 2f;
    tileHeightHalf = tileHeight / 2f;
    for (int i = -HALF_SIZE; i < HALF_SIZE; i++) {
      for (int j = -HALF_SIZE; j < HALF_SIZE; j++) {
        DestinationNode node = new DestinationNode(getIsoCenter(i, j));
        nodeGraph.addNode(node);
        nodes[i + HALF_SIZE][j + HALF_SIZE] = node;
      }
    }
    connectNodes();
  }

  private void connectNodes() {
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        if (i + 1 < SIZE) {
          nodeGraph.connectNodes(nodes[i][j], nodes[i + 1][j]);
          nodeGraph.connectNodes(nodes[i + 1][j], nodes[i][j]);
        }
        if (j + 1 < SIZE) {
          nodeGraph.connectNodes(nodes[i][j], nodes[i][j + 1]);
          nodeGraph.connectNodes(nodes[i][j + 1], nodes[i][j]);
        }
        if (i + 1 < SIZE && j + 1 < SIZE) {
          nodeGraph.connectNodes(nodes[i][j], nodes[i + 1][j + 1]);
          nodeGraph.connectNodes(nodes[i + 1][j + 1], nodes[i][j]);
        }
      }
    }
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
    for (Building building : buildings) {
      building.render(sr);
    }
    nodeGraph.render(sr);
  }

  private boolean tileContainsLine(int row, int column, Vector2 start, Vector2 end) {
    Vector2[] vertices = getVertices(row, column);
    for (int i = 0; i < vertices.length; i++) {
      for (int j = i; j < vertices.length; j++) {
        if ((start.equals(vertices[i]) && end.equals(vertices[j]))
            || (start.equals(vertices[j]) && end.equals(vertices[i]))) {
          return true;
        }
      }
    }
    // go through each of the vertices, plus the next
    // vertex in the list
    int next = 0;
    for (int current = 0; current < vertices.length; current++) {

      // get next vertex in list
      // if we've hit the end, wrap around to 0
      next = current + 1;
      if (next == vertices.length)
        next = 0;

      // get the PVectors at our current position
      // extract X/Y coordinates from each
      float x3 = vertices[current].x;
      float y3 = vertices[current].y;
      float x4 = vertices[next].x;
      float y4 = vertices[next].y;

      // do a Line/Line comparison
      // if true, return 'true' immediately and
      // stop testing (faster)
      boolean hit = CollisionManager.lineLine(start.x, start.y, end.x, end.y, x3, y3, x4, y4);
      if (hit) {
        // System.out.println(start + " " + end + " " + new Vector2(x3, y3) + " " + new
        // Vector2(x4, y4));
        return true;
      }
    }
    // never got a hit
    return false;
  }

  public boolean buildingContainsLine(Vector2 start, Vector2 end) {
    for (Building building : buildings) {
      if (tileContainsLine(building.getTileX(), building.getTileY(), start, end)) {
        return true;
      }
    }
    return false;
  }

  public void addBuilding(Building building) {
    buildings.add(building);
    DestinationNode node = nodes[building.getTileX() + HALF_SIZE][building.getTileY() + HALF_SIZE];
    nodeGraph.removeNode(node);
  }

  public ArrayList<Building> getBuildings() {
    return buildings;
  }

  public NodeGraph getNodeGraph() {
    return nodeGraph;
  }

  public DestinationNode getNode(int row, int column) {
    return nodes[row][column];
  }

}
