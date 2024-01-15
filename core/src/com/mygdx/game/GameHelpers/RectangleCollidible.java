package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.math.Vector2;

/**
 * Collidible that can also find the bottom left hand corner of its position and
 * its width and height
 **/
public class RectangleCollidible extends Collidible {
  private float x, y, w, h;

  /**
   * @param x              x position of the bottom left corner of the rectangle
   * @param y              y position of the bottom left corner of the rectangle
   * @param w              width of the rectangle
   * @param collidibleType CollidibleType that corresponds to the Entity that this
   *                       Collidible is attached to
   **/
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

  /**
   * @param v changes position of the rectangle by adding the Vector2 passed in to
   *          each of its vertices
   **/
  @Override
  public void changePosition(Vector2 v) {
    x += v.x;
    y += v.y;
    super.changePosition(v);
  }

  /**
   * @return x position of the bottom left corner of the rectangle
   **/
  public float getX() {
    return x;
  }

  /**
   * @return y position of the bottom left corner of the rectangle
   **/
  public float getY() {
    return y;
  }

  /**
   * @return width of the rectangle
   **/
  public float getWidth() {
    return w;
  }

  /**
   * @return height of the rectangle
   **/
  public float getHeight() {
    return h;
  }
}
