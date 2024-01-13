package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Handles the resizing and recoloring of the "FFFFORWA.TTF" font
 */
public class FontHandler {
  private final BitmapFont font;
  private final BitmapFont smallFont;
  private final FreeTypeFontGenerator fontGenerator;
  private final FreeTypeFontGenerator.FreeTypeFontParameter parameter;
  private final GlyphLayout layout = new GlyphLayout();

  /**
   * Loads the font from the "FFFFORWA.TTF" FILE
   */
  public FontHandler() {
    fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("FFFFORWA.TTF"));
    parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = 15;
    parameter.color = Color.BLACK;
    font = fontGenerator.generateFont(parameter);
    parameter.size = 10;
    smallFont = fontGenerator.generateFont(parameter);
  }

  public FontHandler(int size, Color color) {
    fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("FFFFORWA.TTF"));
    parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = size;
    parameter.color = color;
    font = fontGenerator.generateFont(parameter);
    parameter.size = size * 2 / 3;
    smallFont = fontGenerator.generateFont(parameter);
  }

  /**
   * gets the x to center the text from the screen width
   * 
   * @param text        text to display on the screen
   * @param screenWidth width of the screen that the text is being centered on
   * @return x to center the text on the screen
   */
  public float centerX(String text, float screenWidth) {
    layout.setText(font, text);
    return -layout.width / 2;
  }

  public float getTextHeight(String text) {
    layout.setText(font, text);
    return layout.height;
  }

  public float getTextWidth(String text) {
    layout.setText(font, text);
    return layout.width;
  }

  public BitmapFont getFont() {
    return font;
  }

  public BitmapFont getSmallFont() {
    return smallFont;
  }
}
