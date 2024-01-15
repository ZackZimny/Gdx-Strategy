package com.mygdx.game.UI;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Entity.Apartment;
import com.mygdx.game.Entity.Building;
import com.mygdx.game.Entity.Factory;
import com.mygdx.game.Entity.Generator;
import com.mygdx.game.Entity.Mine;
import com.mygdx.game.GameHelpers.Grid;

public class ButtonGroup {
  private GameButton[] currentButtons;
  private ButtonAction currentAction = ButtonAction.Move;
  private FontHandler fontHandler = new FontHandler(15, Color.BLACK);
  private ButtonAction[] defaultActions = new ButtonAction[] { ButtonAction.Move, ButtonAction.Fight,
      ButtonAction.Build };
  private ButtonAction[] buildActions = new ButtonAction[] { ButtonAction.Apartment, ButtonAction.Generator,
      ButtonAction.Mine, ButtonAction.Factory };
  private Grid dummyGrid = new Grid(1, 1);
  private Building[] dummyBuildings = new Building[] {
      new Apartment(dummyGrid, 0, 0),
      new Generator(dummyGrid, 0, 0),
      new Mine(dummyGrid, 0, 0),
      new Factory(dummyGrid, 0, 0)
  };
  private GameButton[] defaultButtons = new GameButton[defaultActions.length];
  private GameButton[] buildButtons = new GameButton[buildActions.length];

  public ButtonGroup() {
    calculateButtons();
    currentButtons = defaultButtons;
  }

  public void calculateButtons() {
    float buttonPadding = Gdx.graphics.getHeight() * 0.3f;
    float padding = Gdx.graphics.getHeight() * 0.05f;
    float sideLength = fontHandler.getFont().getLineHeight() * 7f;
    for (int i = 0; i < defaultActions.length; i++) {
      defaultButtons[i] = createButton(defaultActions[i], generateRectangle(i, buttonPadding, padding, sideLength));
    }
    for (int i = 0; i < buildActions.length; i++) {
      buildButtons[i] = createBuildingButton(dummyBuildings[i], buildActions[i],
          generateRectangle(i, buttonPadding, padding, sideLength));
    }
  }

  private Rectangle generateRectangle(int index, float buttonPadding, float padding, float sideLength) {
    return new Rectangle(Gdx.graphics.getWidth() / 2f - sideLength - padding - index * buttonPadding,
        -Gdx.graphics.getHeight() / 2f + padding,
        sideLength, sideLength);
  }

  public GameButton createButton(ButtonAction action, Rectangle rectangle) {
    return new GameButton(action, rectangle, fontHandler);
  }

  public GameButton createBuildingButton(Building building, ButtonAction action, Rectangle rectangle) {
    return new BuildingButton(building, action, rectangle, fontHandler);
  }

  public void render(SpriteBatch spriteBatch, ShapeRenderer sr, Vector2 mousePos) {
    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
      currentButtons = defaultButtons;
      currentAction = ButtonAction.Move;
    }
    for (GameButton button : currentButtons) {
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
    for (GameButton button : currentButtons) {
      button.render(spriteBatch, sr, mousePos);
    }
  }

  public ButtonAction getCurrentAction(Vector2 mousePos) {
    for (GameButton button : currentButtons) {
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

  public ArrayList<GameButton> getButtons() {
    ArrayList<GameButton> buttons = new ArrayList<>();
    buttons.addAll(Arrays.asList(defaultButtons));
    buttons.addAll(Arrays.asList(buildButtons));
    return buttons;
  }
}
