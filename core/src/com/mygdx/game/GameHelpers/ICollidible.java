package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollidibleType;

public interface ICollidible {
  public boolean lineCollide(Vector2 start, Vector2 end);

  public boolean rectangleCollide(Rectangle rectangle);

  public boolean pointCollide(Vector2 point);

  public Vector2[] getVertices();

  public CollidibleType getCollidibleType();
}
