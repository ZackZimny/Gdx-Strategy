package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

/**
 * handles the user's left click and drag motion to select units
 **/
public class Selector {
  private Vector2 prevVector;
  private Rectangle bound;

  /**
   * displays the selector to the screen and calculates its bounding box
   * 
   * @param sr       ShapeRenderer that displays the bounding box
   * @param mousePos position that the mouse is on the screen
   **/
  public void render(ShapeRenderer sr, Vector2 mousePos) {
    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
      prevVector = mousePos;
    }

    if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
      prevVector = null;
      bound = null;
    }

    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && prevVector != null) {
      float x = prevVector.x;
      float y = prevVector.y;
      float width = mousePos.x - prevVector.x;
      float height = mousePos.y - prevVector.y;
      if (width < 0) {
        x += width;
        width = -width;
      }
      if (height < 0) {
        y += height;
        height = -height;
      }
      bound = new Rectangle(x, y, width, height);
      sr.begin(ShapeType.Line);
      sr.setColor(Color.GOLD);
      sr.rect(bound.x, bound.y, bound.width, bound.height);
      sr.end();
    }
  }

  /**
   * @return rectangle that contains the bounds of the click and drag motion, null
   *         if the action is not being done
   **/
  public Rectangle getBound() {
    return bound;
  }
}
