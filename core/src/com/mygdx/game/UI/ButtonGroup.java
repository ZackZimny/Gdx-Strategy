package com.mygdx.game.UI;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ButtonGroup {
  private Button[] currentButtons;
  private ButtonAction currentAction = ButtonAction.Move;
  private FontHandler fontHandler = new FontHandler(15, Color.BLACK);
  private ButtonAction[] defaultActions = new ButtonAction[] { ButtonAction.Move, ButtonAction.Fight,
      ButtonAction.Build };
  private ButtonAction[] buildActions = new ButtonAction[] { ButtonAction.Apartment, ButtonAction.Generator,
      ButtonAction.Mine, ButtonAction.Factory };
  private Button[] defaultButtons = new Button[defaultActions.length];
  private Button[] buildButtons = new Button[buildActions.length];

  public ButtonGroup() {
    calculateButtons();
    currentButtons = defaultButtons;
  }

  public void calculateButtons() {
    float buttonPadding = Gdx.graphics.getHeight() * 0.15f;
    float padding = Gdx.graphics.getHeight() * 0.05f;
    float sideLength = Gdx.graphics.getHeight() * 0.1f;
    for (int i = 0; i < defaultActions.length; i++) {
      defaultButtons[i] = createButton(defaultActions[i], i, buttonPadding, padding, sideLength);
    }
    for (int i = 0; i < buildActions.length; i++) {
      buildButtons[i] = createButton(buildActions[i], i, buttonPadding, padding, sideLength);
    }
  }

  public Button createButton(ButtonAction action, int index, float buttonPadding, float padding, float sideLength) {
    return new Button(action,
        new Rectangle(Gdx.graphics.getWidth() / 2f - sideLength - padding - index * buttonPadding,
            -Gdx.graphics.getHeight() / 2f + padding,
            sideLength, sideLength),
        fontHandler);
  }

  public void render(SpriteBatch spriteBatch, ShapeRenderer sr, Vector2 mousePos) {
    for (Button button : currentButtons) {
      boolean buildModeStarted = button.getAction().equals(ButtonAction.Build) && button.isClicked(mousePos);
      boolean buildingPlaced = isInBuildMode() && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT);
      if (buildModeStarted) {
        currentButtons = buildButtons;
      } else if (buildingPlaced) {
        currentButtons = defaultButtons;
        currentAction = ButtonAction.Move;
      }
    }
    Rectangle firstRectangle = currentButtons[0].getRectangle();
    Rectangle lastRectangle = currentButtons[currentButtons.length - 1].getRectangle();
    float center = (firstRectangle.x + lastRectangle.x + lastRectangle.width) / 2f;
    String text = getCurrentAction(mousePos).toString();
    float textX = center - fontHandler.getTextWidth(text) / 2f;
    float yPadding = 10f;
    float textY = firstRectangle.getY() + firstRectangle.getHeight() + yPadding + fontHandler.getTextHeight(text);
    spriteBatch.begin();
    fontHandler.getFont().draw(spriteBatch, text, textX, textY);
    spriteBatch.end();
    for (Button button : currentButtons) {
      button.render(spriteBatch, sr, mousePos);
    }
  }

  public ButtonAction getCurrentAction(Vector2 mousePos) {
    for (Button button : currentButtons) {
      if (button.isClicked(mousePos)) {
        currentAction = button.getAction();
        return currentAction;
      }
    }
    return currentAction;
  }

  public boolean isInBuildMode() {
    return Arrays.asList(buildActions).contains(currentAction);
  }
}
