package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

/**
 * contains various helper methods that deal with collisions
 **/
public class CollisionManager {

  /**
   * @param start1 starting position of the first line
   * @param end1   ending position of the first line
   * @param start2 starting position of the second line
   * @param end2   ending position of the second line
   * @return true if the lines overlap, false otherwise
   **/
  public static boolean lineLine(Vector2 start1, Vector2 end1, Vector2 start2, Vector2 end2) {
    return lineLine(start1.x, start1.y, end1.x, end1.y, start2.x, start2.y, end2.x, end2.y);
  }

  /**
   * @param x1 x position of the start of the first line
   * @param y1 y position of the start of the first line
   * @param x2 x position of the end of the first line
   * @param y2 y position of the end of the first line
   * @param x3 x position of the start of the second line
   * @param y3 y position of the start of the second line
   * @param x4 x position of the end of the second line
   * @param y4 y position of the end of the second line
   * @return true if the lines overlap, false otherwise
   **/
  public static boolean lineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

    // calculate the direction of the lines
    float uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
    float uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

    // if uA and uB are between 0-1, lines are colliding
    return uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1;
  }

  /**
   * @param rectangle rectangle to check collisions with
   * @param start     start position of the line
   * @param end       end position of the line
   * @return true if the rectangle and line overlap, false otherwise
   **/
  public static boolean rectangleLine(Rectangle rectangle, Vector2 start, Vector2 end) {
    Vector2 pos = new Vector2(rectangle.getX(), rectangle.getY());
    Vector2[] vertices = new Vector2[] {
        pos,
        new Vector2(pos.x + rectangle.width, pos.y),
        new Vector2(pos.x + rectangle.width, pos.y + rectangle.height),
        new Vector2(pos.x, pos.y + rectangle.height)
    };
    for (int i = 0; i < 3; i++) {
      if (lineLine(vertices[i], vertices[i + 1], start, end)) {
        return true;
      }
    }
    if (rectangle.contains(start))
      return true;
    if (rectangle.contains(end))
      return true;
    return false;
  }

  /**
   * @param collidible1 first collidible to check collisions with
   * @param collidible2 other collidible to check collisions with
   * @return true if the collidibles overlap, false otherwise
   **/
  public static boolean overlappingColldibles(Collidible collidible1, Collidible collidible2) {
    Vector2[] vertices1 = collidible1.getVertices();
    Vector2[] vertices2 = collidible2.getVertices();
    for (int i = 0; i < vertices1.length - 1; i++) {
      for (int j = 0; j < vertices2.length - 1; j++) {
        if (lineLine(vertices1[i], vertices1[i + 1], vertices2[j], vertices2[j + 1])) {
          return true;
        }
      }
    }
    return false;
  }
}
