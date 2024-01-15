package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Button used during game loop
 **/
public class GameButton {
  private Rectangle rectangle;
  private ButtonAction action;
  private FontHandler fontHandler;
  private String description = null;

  /**
   * @param ButtonAction action corresponding to this GameButton
   * @param rectangle    button bounding box
   * @param fontHandler  renders fonts
   **/
  public GameButton(ButtonAction action, Rectangle rectangle, FontHandler fontHandler) {
    this.action = action;
    this.rectangle = rectangle;
    this.fontHandler = fontHandler;
  }

  /**
   * @param ButtonAction action corresponding to this GameButton
   * @param description  description about this button
   * @param rectangle    button bounding box
   * @param fontHandler  renders fonts
   **/
  public GameButton(ButtonAction action, String description, Rectangle rectangle, FontHandler fontHandler) {
    this(action, rectangle, fontHandler);
    this.description = description;
  }

  /**
   * @param mousePos mouse position in the game world
   * @return true if the mouse is in the button, false otherwise
   **/
  public boolean isHovered(Vector2 mousePos) {
    return rectangle.contains(mousePos);
  }

  /**
   * @param mousePos mouse position in the game world
   * @return true if the mouse is in the button and clicked, false otherwise
   **/
  public boolean isClicked(Vector2 mousePos) {
    return isHovered(mousePos) && Gdx.input.isButtonPressed(Input.Buttons.LEFT);
  }

  /**
   * @param sb       SpriteBatch that displays this button to the screen
   * @param sr       ShapeRenderer that displays rectangle background
   * @param mousePos mouse position in the game world
   **/
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
    if (description == null) {
      sb.begin();
      fontHandler.getFont().draw(sb, text, rectangle.getX() + rectangle.getWidth() / 2f - width / 2f,
          rectangle.getY() + rectangle.getHeight() / 2f + height / 2f);
      sb.end();
    } else {
      sb.begin();
      fontHandler.getFont().draw(sb, text, rectangle.getX() + rectangle.getWidth() / 2f - width / 2f,
          rectangle.getY() + rectangle.getHeight() - height);
      sb.end();
      sb.begin();
      fontHandler.getSmallFont().draw(sb, description, rectangle.getX(),
          rectangle.getY() + rectangle.getHeight() - height * 2 - 5);
      sb.end();
    }
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

  protected void setDescription(String description) {
    this.description = description;
  }

}
