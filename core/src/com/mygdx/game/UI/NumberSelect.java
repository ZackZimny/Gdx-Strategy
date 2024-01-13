package com.mygdx.game.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class NumberSelect {
  private String text;
  private Vector2 position;
  private FontHandler fontHandler = new FontHandler(20, Color.BLACK);
  private UIButton decreaseButton;
  private UIButton increaseButton;
  private float sideLength = 30;
  private float padding = 5;
  private int number = 5;

  public NumberSelect(String text, Vector2 position) {
    this.text = text;
    this.position = position;
    decreaseButton = new UIButton(new Rectangle(position.x, position.y, sideLength, sideLength), "-", Color.RED,
        ScreenState.OPTIONS);
    increaseButton = new UIButton(
        new Rectangle(position.x + padding + sideLength, position.y, sideLength, sideLength), "+", Color.BLUE,
        ScreenState.OPTIONS);
  }

  public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 mousePos) {
    decreaseButton.render(sr, sb);
    increaseButton.render(sr, sb);
    if (increaseButton.isClicked(mousePos) && number < 10) {
      number++;
    } else if (decreaseButton.isClicked(mousePos) && number > 0) {
      number--;
    }
    float textHeight = fontHandler.getFont().getLineHeight();
    sb.begin();
    fontHandler.getFont().draw(sb, text + ": " + number, position.x + sideLength * 2 + padding * 2,
        position.y + textHeight);
    sb.end();
  }

  public boolean isButtonClicked(Vector2 mousePos) {
    return decreaseButton.isClicked(mousePos) || increaseButton.isClicked(mousePos);
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public int getNumber() {
    return number;
  }
}