package com.mygdx.game.Entity;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Entity {
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

  public void setPosition(Vector2 position) {
    hurtbox.x = position.x;
    hurtbox.y = position.y;
  }

  public void changePosition(Vector2 velocity) {
    hurtbox.x += velocity.x;
    hurtbox.y += velocity.y;
  }
}
