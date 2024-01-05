package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Collidible;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.GameState;

public abstract class Entity {
  private Collidible collidible;
  private CollidibleType collidibleType;

  public Entity(Collidible collidible, CollidibleType collidibleType) {
    this.collidible = collidible;
    this.collidibleType = collidibleType;
  }

  public abstract void render(ShapeRenderer sr);

  public abstract void updateState(GameState gameState);

  public Vector2 getCenter() {
    return collidible.getCenter();
  }

  public void changePosition(Vector2 v) {
    collidible.changePosition(v);
  }

  public boolean lineCollide(Vector2 start, Vector2 end) {
    return collidible.lineCollide(start, end);
  }

  public boolean pointCollide(Vector2 point) {
    return collidible.pointCollide(point);
  }

  public CollidibleType getCollidibleType() {
    return collidibleType;
  }

  public Vector2[] getVertices() {
    return collidible.getVertices();
  }

  public Collidible getCollidible() {
    return collidible;
  }
}
