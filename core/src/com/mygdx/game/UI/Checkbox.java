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
  private UIButton button;

  public Checkbox(Vector2 position, String text) {
    this.position = position;
    this.text = text;
  }

  public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 mousePos) {
    float sideLength = 50;
    float padding = 10;
    float height = fontHandler.getTextHeight(text);
    String buttonText = isOn() ? "On" : "Off";
    Color color = isOn ? Color.GREEN : Color.RED;
    UIButton uiButton = new UIButton(new Rectangle(position.x, position.y, sideLength, sideLength), buttonText, color,
        ScreenState.OPTIONS);
    if (uiButton.isClicked(mousePos)) {
      isOn = !isOn();
    }
    uiButton.render(sr, sb);
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

  public void setPosition(Vector2 position) {
    this.position = position;
  }
}
