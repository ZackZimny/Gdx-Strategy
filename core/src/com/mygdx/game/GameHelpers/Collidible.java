package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * handles collision logic between various different shapes
 **/
public class Collidible {
  private Vector2[] vertices;
  private CollidibleType collidibleType;
  private Polygon polygon;

  /**
   * @param vertices       Vector2[] that contains the positions of each of the
   *                       shapes vertices
   * @param collidibleType is this Collidible attatched to a player, enemy, or
   *                       building
   **/
  public Collidible(Vector2[] vertices, CollidibleType collidibleType) {
    this.vertices = vertices;
    this.collidibleType = collidibleType;
    updatePolygon();
  }

  /**
   * updates the values within the polygon whenever this Collidible changes
   * position; polygon must be updated to make proper use of the
   * polygon.contains() method
   **/
  public void updatePolygon() {
    float[] floatVertices = new float[vertices.length * 2];
    for (int i = 0; i < vertices.length; i++) {
      floatVertices[i * 2] = vertices[i].x;
      floatVertices[i * 2 + 1] = vertices[i].y;
    }
    polygon = new Polygon(floatVertices);
  }

  /**
   * @param start Vector2 containing the beginning of the line to check collisions
   *              from
   * @param end   Vector2 containing the end of the line to check collisions from
   *              (interchangable with the start)
   * @return true if the line overlaps with the collisible, false otherwise
   **/
  public boolean lineCollide(Vector2 start, Vector2 end) {
    for (int i = 0; i < vertices.length - 1; i++) {
      if (CollisionManager.lineLine(vertices[i], vertices[i + 1], start, end)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return Vector2 containing the center position of this collidible shape
   **/
  public Vector2 getCenter() {
    Vector2 center = new Vector2(0, 0);
    for (Vector2 verticy : vertices) {
      center.add(verticy);
    }
    return center.scl(1f / vertices.length);
  }

  /**
   * @param v Vector2 to add to each of the verticies of the Collidible
   **/
  public void changePosition(Vector2 v) {
    for (Vector2 verticy : vertices) {
      verticy.add(v);
    }
    updatePolygon();
  }

  /**
   * @param point Vector2 to check if this collidible contains
   * @return true if this collidible contains the point, false otherwise
   **/
  public boolean pointCollide(Vector2 point) {
    return polygon.contains(point);
  }

  /**
   * @return Vector2[] containing the positions of all of this shape's verticies
   **/
  public Vector2[] getVertices() {
    return vertices;
  }

  /**
   * @return what Entity is this attatched to a playerUnit, enemyUnit, or building
   **/
  public CollidibleType getCollidibleType() {
    return collidibleType;
  }
}
