package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Checkbox {
  boolean isOn = false;
  private Vector2 position;
  private String text;
  private FontHandler fontHandler = new FontHandler(20, Color.BLACK);

  public Checkbox(Vector2 position, String text) {
    this.position = position;
    this.text = text;
  }

  public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 mousePos) {
    float sideLength = 50;
    float padding = 10;
    float height = fontHandler.getTextHeight(text);
    Rectangle rectangle = new Rectangle(position.x, position.y, sideLength, sideLength);
    if (rectangle.contains(mousePos) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
      isOn = !isOn;
    }
    Color color = isOn ? Color.GREEN : Color.RED;
    sr.begin(ShapeType.Filled);
    sr.setColor(color);
    sr.rect(position.x, position.y, sideLength, sideLength);
    sr.end();
    sb.begin();
    fontHandler.getFont().draw(sb, text, position.x + sideLength + padding, position.y + height / 2f + sideLength / 2f);
    sb.end();
  }

  public boolean isOn() {
    return isOn;
  }

  public void setIsOn(boolean isOn) {
    this.isOn = isOn;
  }
}
