package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.math.Vector2;

public class RectangleCollidible extends Collidible {
  private float x, y, w, h;

  public RectangleCollidible(float x, float y, float w, float h, CollidibleType collidibleType) {
    super(new Vector2[] {
        new Vector2(x, y),
        new Vector2(x + w, y),
        new Vector2(x + w, y + h),
        new Vector2(x, y + h),
    }, collidibleType);
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  @Override
  public void changePosition(Vector2 v) {
    x += v.x;
    y += v.y;
    super.changePosition(v);
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public float getWidth() {
    return w;
  }

  public float getHeight() {
    return h;
  }
}
