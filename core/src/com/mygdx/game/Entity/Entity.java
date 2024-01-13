package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Collidible;
import com.mygdx.game.GameHelpers.CollidibleType;
import com.mygdx.game.GameHelpers.GameLoop;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity {
  private Collidible collidible;
  private CollidibleType collidibleType;
  private int health;
  private int attackPower;

  public Entity(Collidible collidible, int health, int attackPower) {
    this.collidible = collidible;
    this.collidibleType = collidible.getCollidibleType();
    this.health = health;
    this.attackPower = attackPower;
  }

  public abstract void render(ShapeRenderer sr, SpriteBatch sb);

  public abstract void updateState(GameLoop gameState);

  public abstract void generateAnimations(AssetManager assetManager);

  public abstract float getRenderOrderValue();

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

  public boolean isDead() {
    return health <= 0;
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

  public void setCollidible(Collidible collidible) {
    this.collidible = collidible;
  }

  public void setCollidibleType(CollidibleType collidibleType) {
    this.collidibleType = collidibleType;
  }

  public int getHealth() {
    return health;
  }

  protected void setHealth(int health) {
    this.health = health;
  }

  public int getAttackPower() {
    return attackPower;
  }

  protected void setAttackPower(int attackPower) {
    this.attackPower = attackPower;
  }
}
