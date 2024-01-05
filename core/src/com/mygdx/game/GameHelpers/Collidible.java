package com.mygdx.game.GameHelpers;

import java.util.Arrays;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Collidible {
  private Vector2[] vertices;
  private CollidibleType collidibleType;
  private Polygon polygon;

  public Collidible(Vector2[] vertices, CollidibleType collidibleType) {
    this.vertices = vertices;
    this.collidibleType = collidibleType;
    updatePolygon();
  }

  public void updatePolygon() {
    float[] floatVertices = new float[vertices.length * 2];
    for (int i = 0; i < vertices.length; i++) {
      floatVertices[i * 2] = vertices[i].x;
      floatVertices[i * 2 + 1] = vertices[i].y;
    }
    polygon = new Polygon(floatVertices);
  }

  public boolean lineCollide(Vector2 start, Vector2 end) {
    for (int i = 0; i < vertices.length - 1; i++) {
      if (CollisionManager.lineLine(vertices[i], vertices[i + 1], start, end)) {
        return true;
      }
    }
    return false;
  }

  public Vector2 getCenter() {
    Vector2 center = new Vector2(0, 0);
    for (Vector2 verticy : vertices) {
      center.add(verticy);
    }
    return center.scl(1f / vertices.length);
  }

  public void changePosition(Vector2 v) {
    for (Vector2 verticy : vertices) {
      verticy.add(v);
    }
    updatePolygon();
  }

  public boolean pointCollide(Vector2 point) {
    return polygon.contains(point);
  }

  public Vector2[] getVertices() {
    return vertices;
  }

  public CollidibleType getCollidibleType() {
    return collidibleType;
  }
}
