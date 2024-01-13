package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Button that interacts with the mouse and can be clicked or hovered
 */
public class UIButton {
  // used for text centering
  private final GlyphLayout layout = new GlyphLayout();
  private final String text;
  private final Color bgColor;
  // used to create text
  private final FontHandler fontHandler = new FontHandler();
  private final ScreenState screenState;
  private final Rectangle rectangle;

  /**
   * Creates a Button at the specified position
   * 
   * @param x       x position of the bottom left corner of the button
   * @param y       y position of the bottom left corner of the button
   * @param width   width of the button's box
   * @param height  height of the button's box
   * @param text    text to be displayed in the center of the button
   * @param bgColor color of the box behind the button
   */
  public UIButton(Rectangle rectangle, String text, Color bgColor, ScreenState screenState) {
    this.rectangle = rectangle;
    this.text = text;
    this.bgColor = bgColor;
    layout.setText(fontHandler.getFont(), text);
    this.screenState = screenState;
  }

  /**
   * Displays the button to the screen
   * 
   * @param shapeRenderer shapeRenderer to draw the button's box
   * @param spriteBatch   spriteBatch to draw the text of the button
   */
  public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.setColor(bgColor);
    shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    shapeRenderer.end();
    spriteBatch.begin();
    fontHandler.getFont().draw(spriteBatch, text, rectangle.getX() + rectangle.getWidth() / 2 - layout.width / 2,
        rectangle.getY() + rectangle.getHeight() / 2 + layout.height / 2);
    spriteBatch.end();
  }

  public boolean isHovered(Vector2 mousePos) {
    return rectangle.contains(mousePos);
  }

  public boolean isClicked(Vector2 mousePos) {
    return isHovered(mousePos) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
  }

  /**
   * gets what screen this button should transition to when clicked
   * 
   * @return ScreenState of screen change
   */
  public ScreenState getScreenState() {
    return screenState;
  }
}
