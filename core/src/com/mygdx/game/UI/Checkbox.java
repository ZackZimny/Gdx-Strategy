package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * user interface element to toggle an options on or off
 **/
public class Checkbox {
  boolean isOn = false;
  private Vector2 position;
  private String text;
  private FontHandler fontHandler = new FontHandler(20, Color.BLACK);
  private UIButton button;

  /**
   * @param postion bottom left corner of the box to display
   * @param text    text placed next to the button
   **/
  public Checkbox(Vector2 position, String text) {
    this.position = position;
    this.text = text;
  }

  /**
   * displays the Checkbox to the screen
   * 
   * @param sb       SpriteBatch used to display text
   * @param sr       ShapeRenderer used to render button background
   * @param mousePos Vector2 position of the mouse on the screen
   **/
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

  /**
   * @return true if the button is toggled on, false otherwise
   **/
  public boolean isOn() {
    return isOn;
  }

  /**
   * @param isOn overwrites the current isOn value
   **/
  public void setIsOn(boolean isOn) {
    this.isOn = isOn;
  }

  /**
   * @param position overwrites the current position value
   **/
  public void setPosition(Vector2 position) {
    this.position = position;
  }
}
