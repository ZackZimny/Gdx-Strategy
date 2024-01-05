package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import java.math.*;

public class Selector {
  private Vector2 prevVector;
  private boolean selecting = false;
  private Rectangle bound;

  public void render(ShapeRenderer sr, Vector2 mousePos) {
    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
      prevVector = mousePos;
      selecting = true;
    }

    if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
      selecting = false;
      prevVector = null;
      bound = null;
    }

    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
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

  public Rectangle getBound() {
    return bound;
  }
}
