package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button {
  private Rectangle rectangle;
  private ButtonAction action;
  private FontHandler fontHandler;

  public Button(ButtonAction action, Rectangle rectangle, FontHandler fontHandler) {
    this.action = action;
    this.rectangle = rectangle;
    this.fontHandler = fontHandler;
  }

  public boolean isHovered(Vector2 mousePos) {
    return rectangle.contains(mousePos);
  }

  public boolean isClicked(Vector2 mousePos) {
    return isHovered(mousePos) && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
  }

  public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 mousePos) {
    Color color = Color.RED;
    if (isClicked(mousePos)) {
      color = Color.PURPLE;
    } else if (isHovered(mousePos)) {
      color = Color.BLUE;
    }
    sr.setColor(color);
    sr.begin(ShapeType.Filled);
    sr.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    sr.end();
    String text = action.toString();
    float width = fontHandler.getTextWidth(text);
    float height = fontHandler.getTextHeight(text);
    sb.begin();
    fontHandler.getFont().draw(sb, text, rectangle.getX() + rectangle.getWidth() / 2f - width / 2f,
        rectangle.getY() + rectangle.getHeight() / 2f + height / 2f);
    sb.end();
  }

  public Rectangle getRectangle() {
    return rectangle;
  }

  public ButtonAction getAction() {
    return action;
  }

  public FontHandler getFontHandler() {
    return fontHandler;
  }

}
