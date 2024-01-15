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

/**
 * user interface that the user interacts with when playing the game
 **/
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

  /**
   * recalculates the button positions and dimensions when the screen is resized
   **/
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

  /**
   * @param index         order in the array of buttons
   * @param buttonPadding distance to the sides of the button
   * @param padding       distance to the edges of the screen
   * @param sideLength    size of the sqaure button sides
   * @return Rectangle with calculated dimensions and positions
   **/
  private Rectangle generateRectangle(int index, float buttonPadding, float padding, float sideLength) {
    return new Rectangle(Gdx.graphics.getWidth() / 2f - sideLength - padding - index * buttonPadding,
        -Gdx.graphics.getHeight() / 2f + padding,
        sideLength, sideLength);
  }

  /**
   * @param action    ButtonAction to pair with button
   * @param Rectangle dimesions and position of the button
   * @return GameButton
   **/
  public GameButton createButton(ButtonAction action, Rectangle rectangle) {
    return new GameButton(action, rectangle, fontHandler);
  }

  /**
   * @param building  Building to pair with button; used to gather description
   * @param action    ButtonAction to pair with button
   * @param Rectangle dimesions and position of the button
   * @return GameButton
   **/
  public GameButton createBuildingButton(Building building, ButtonAction action, Rectangle rectangle) {
    return new BuildingButton(building, action, rectangle, fontHandler);
  }

  /**
   * displays the ButtonGroup to the screen
   * 
   * @param spriteBatch SpriteBatch used to draw text to the screen
   * @param sr          ShapeRenderer used to draw rectangle for the button
   * @param mousePos    Vector2 position of the mouse on the screen
   **/
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

  /**
   * @param mousePos Vector2 position of the mouse on the screen
   * @return previous action or current action being clicked
   **/
  public ButtonAction getCurrentAction(Vector2 mousePos) {
    for (GameButton button : currentButtons) {
      if (button.isClicked(mousePos)) {
        currentAction = button.getAction();
        return currentAction;
      }
    }
    return currentAction;
  }

  /**
   * @return true if the player is in the build submenu, false otherwise
   **/
  public boolean isInBuildMode() {
    return Arrays.asList(buildActions).contains(currentAction);
  }

  /**
   * @return a list of all GameButtons possible in the ButtonGroup
   **/
  public ArrayList<GameButton> getButtons() {
    ArrayList<GameButton> buttons = new ArrayList<>();
    buttons.addAll(Arrays.asList(defaultButtons));
    buttons.addAll(Arrays.asList(buildButtons));
    return buttons;
  }
}
