package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

/**
 * holds the screen data, handles the fonts, and renders the background for
 * navigation
 */
public class Screen {
  private final FontHandler titleFontHandler;
  private final String title;
  private final ArrayList<UIButton> buttons = new ArrayList<>();
  private final HashMap<String, UIButton> stringButtonHashMap = new HashMap<>();
  private final ScreenState defaultScreen;
  private float timeOut = 0;

  /**
   * Initializes screen without buttons
   * 
   * @param title         title of the screen that will be displayed at the top
   * @param defaultScreen ScreenState that this screen is paired with
   */
  public Screen(String title, ScreenState defaultScreen) {
    titleFontHandler = new FontHandler(30, Color.BLACK);
    this.title = title;
    this.defaultScreen = defaultScreen;
  }

  /**
   * Initializes buttons in correct positions
   * 
   * @param stringStateHashMap linkedHashMap of button names and their associated
   *                           screen redirects
   */
  protected void createButtonColumn(LinkedHashMap<String, ScreenState> stringStateHashMap) {
    Color[] colors = { Color.BLUE, Color.LIGHT_GRAY };
    int i = 0;
    for (String text : stringStateHashMap.keySet()) {
      UIButton newButton = new UIButton(getButtonRectangle(i, stringStateHashMap.size()),
          text, colors[i % colors.length], stringStateHashMap.get(text));
      buttons.add(newButton);
      stringButtonHashMap.put(text, newButton);
      i++;
    }
  }

  private Rectangle getButtonRectangle(int i, int buttonCount) {
    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    float buttonX = 20;
    float buttonHeight = screenHeight * 0.1f;
    float totalHeight = buttonCount * (10f + buttonHeight);
    return new Rectangle(buttonX - screenWidth / 2f, totalHeight - ((10f + buttonHeight) * i) - screenHeight / 2f,
        screenWidth - 40,
        buttonHeight);
  }

  public void handleResize() {
    int i = 0;
    for (UIButton button : buttons) {
      button.setRectangle(getButtonRectangle(i, stringButtonHashMap.size()));
      i++;
    }
  }

  /**
   * Displays the title at the top of the screen
   * 
   * @param spriteBatch displays text
   */
  public void renderTitle(SpriteBatch spriteBatch) {
    spriteBatch.begin();
    float screenWidth = Gdx.graphics.getWidth();
    float textY = Gdx.graphics.getHeight() / 2f - titleFontHandler.getFont().getLineHeight() + 10;
    float textX = titleFontHandler.centerX(title, screenWidth);
    titleFontHandler.getFont().draw(spriteBatch, title, textX, textY);
    spriteBatch.end();
  }

  /**
   * renders the buttons in their correct position and color
   * 
   * @param shapeRenderer displays rectangles
   * @param spriteBatch   displays text
   */
  public void renderButtons(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
    for (UIButton button : buttons) {
      button.render(shapeRenderer, spriteBatch);
    }
  }

  /**
   * renders all screen components
   * 
   * @param shapeRenderer displays button rectangles
   * @param spriteBatch   displays text
   */
  public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 mousePos, float deltaTime) {
    timeOut += deltaTime;
    renderTitle(sb);
    renderButtons(sr, sb);
  }

  /**
   * Gets the correct x position to center a piece of text using the default font
   * 
   * @param text text to center
   * @return x position to start rendering the text
   */
  public float getCenterTextX(String text) {
    GlyphLayout layout = new GlyphLayout();
    layout.setText(getTitleFontHandler().getFont(), text);
    return Gdx.graphics.getWidth() / 2f - layout.width / 2f;
  }

  /**
   * Gets the correct x position to center a piece of text using the passed in
   * font
   * 
   * @param text text to center
   * @param font
   * @return x position to start rendering the text
   */
  public float getCenterTextX(String text, BitmapFont font) {
    GlyphLayout layout = new GlyphLayout();
    layout.setText(font, text);
    return Gdx.graphics.getWidth() / 2f - layout.width / 2f;
  }

  /**
   * gets the screen state depending on the state of the buttons
   * 
   * @return ScreenState redirect of the button pressed, or the default
   *         ScreenState if no buttons are pressed
   */
  public ScreenState getScreenState(Vector2 mousePos) {
    for (UIButton button : buttons) {
      if (button.isClicked(mousePos) && timeOut >= 0.5f) {
        timeOut = 0;
        return button.getScreenState();
      }
    }
    return defaultScreen;
  }

  public FontHandler getTitleFontHandler() {
    return titleFontHandler;
  }

  public HashMap<String, UIButton> getStringButtonHashMap() {
    return stringButtonHashMap;
  }

  public void setTimeOut(float timeOut) {
    this.timeOut = timeOut;
  }

  protected float getTimeOut() {
    return timeOut;
  }

}
