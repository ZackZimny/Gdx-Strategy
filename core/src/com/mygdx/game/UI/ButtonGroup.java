package com.mygdx.game.UI;

import java.util.InputMismatchException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ButtonGroup {
  private Button[] buttons;
  private String[] actions;
  private String currentAction;
  private FontHandler fontHandler = new FontHandler(15, Color.BLACK);

  public ButtonGroup(String[] actions, String defaultAction) {
    this.actions = actions;
    buttons = new Button[actions.length];
    calculateButtons();
    currentAction = defaultAction;
  }

  public void calculateButtons() {
    float buttonPadding = Gdx.graphics.getHeight() * 0.15f;
    float padding = Gdx.graphics.getHeight() * 0.05f;
    float sideLength = Gdx.graphics.getHeight() * 0.1f;
    for (int i = 0; i < actions.length; i++) {
      buttons[i] = createButton(i, buttonPadding, padding, sideLength);
    }
  }

  public Button createButton(int index, float buttonPadding, float padding, float sideLength) {
    return new Button(actions[index],
        new Rectangle(Gdx.graphics.getWidth() / 2f - sideLength - padding - index * buttonPadding,
            -Gdx.graphics.getHeight() / 2f + padding,
            sideLength, sideLength),
        fontHandler);
  }

  public void render(SpriteBatch spriteBatch, ShapeRenderer sr, Vector2 mousePos) {
    Rectangle firstRectangle = buttons[0].getRectangle();
    Rectangle lastRectangle = buttons[buttons.length - 1].getRectangle();
    float center = (firstRectangle.x + lastRectangle.x + lastRectangle.width) / 2f;
    String text = getCurrentAction(mousePos);
    float textX = center - fontHandler.getTextWidth(text) / 2f;
    float yPadding = 10f;
    float textY = firstRectangle.getY() + firstRectangle.getHeight() + yPadding + fontHandler.getTextHeight(text);
    spriteBatch.begin();
    fontHandler.getFont().draw(spriteBatch, text, textX, textY);
    spriteBatch.end();
    for (Button button : buttons) {
      button.render(spriteBatch, sr, mousePos);
    }
  }

  public String getCurrentAction(Vector2 mousePos) {
    for (Button button : buttons) {
      if (button.isClicked(mousePos)) {
        currentAction = button.getText();
        return currentAction;
      }
    }
    return currentAction;
  }
}
