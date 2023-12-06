package com.mygdx.game.Entity;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.CollisionManager;
import com.mygdx.game.GameHelpers.ICollidible;

public class Entity implements ICollidible {
  private Rectangle hurtbox;
  private ArrayList<Rectangle> hitBoxes;

  public Entity(Rectangle hurtbox) {
    this.hurtbox = hurtbox;
  }

  // Method to be overwritten
  public void display() {
  }

  public Rectangle getHurtbox() {
    return hurtbox;
  }

  public ArrayList<Rectangle> getHitboxes() {
    return hitBoxes;
  }

  public Vector2 getPosition() {
    return new Vector2(hurtbox.x, hurtbox.y);
  }

  public Vector2 getCenter() {
    return new Vector2(hurtbox.x + hurtbox.width / 2f, hurtbox.y + hurtbox.height / 2f);
  }

  public void setPosition(Vector2 position) {
    hurtbox.x = position.x;
    hurtbox.y = position.y;
  }

  public void changePosition(Vector2 velocity) {
    hurtbox.x += velocity.x;
    hurtbox.y += velocity.y;
  }

  public boolean rectangleCollide(Rectangle rectangle) {
    return hurtbox.contains(rectangle);
  }

  public boolean lineCollide(Vector2 start, Vector2 end) {
    return CollisionManager.rectangleLine(hurtbox, start, end);
  }

  public boolean pointCollide(Vector2 point) {
    return hurtbox.contains(point);
  }

  public Vector2[] getVertices() {
    return new Vector2[] {
        new Vector2(hurtbox.x, hurtbox.y),
        new Vector2(hurtbox.x + hurtbox.width, hurtbox.y),
        new Vector2(hurtbox.x + hurtbox.width, hurtbox.y + hurtbox.height),
        new Vector2(hurtbox.x, hurtbox.y + hurtbox.height),
    };
  }

}
