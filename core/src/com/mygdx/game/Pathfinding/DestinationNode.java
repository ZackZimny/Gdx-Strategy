package com.mygdx.game.Pathfinding;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class DestinationNode {
  float x, y;
  int index;
  private Object entity;

  public DestinationNode(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public DestinationNode(Vector2 position) {
    x = position.x;
    y = position.y;
  }

  public DestinationNode(float x, float y, int index) {
    this(x, y);
    this.index = index;
  }

  public DestinationNode(Vector2 position, int index) {
    this(position);
    this.index = index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public void render(ShapeRenderer sr, boolean onPath) {

    // draws filled in section
    sr.begin(ShapeRenderer.ShapeType.Filled);
    sr.setColor(onPath ? Color.GREEN : Color.RED);
    sr.circle(x, y, 10);
    sr.end();

    // draws outlline
    sr.begin(ShapeRenderer.ShapeType.Line);
    sr.setColor(0, 0, 0, 1);
    sr.circle(x, y, 10);
    sr.end();
  }

  public Vector2 getPosition() {
    return new Vector2(x, y);
  }

  public Object getEntity() {
    return entity;
  }

  public void setEntity(Object entity) {
    this.entity = entity;
  }
}
