package com.mygdx.game.UI;

import java.util.LinkedHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TutorialScreen extends Screen {
  private int screenNumber = 0;
  private ScreenState screenState = ScreenState.TUTORIAL;
  String[] textureStrings = new String[] { "MainMenu.png", "Options.png", "Goal.png", "CommandingTroops.png",
      "CommandingTroops2.png", "BuildingTutorial.png", "BuildingTutorial2.png", "BuildingTutorial3.png" };
  private Texture[] textures = new Texture[textureStrings.length];
  private UIButton backButton;
  private UIButton nextButton;

  public TutorialScreen() {
    super("Tutorial", ScreenState.TUTORIAL);
  }

  public void loadImages(AssetManager assetManager) {
    int i = 0;
    for (String path : textureStrings) {
      textures[i] = assetManager.get(path, Texture.class);
      i++;
    }
  }

  @Override
  public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 mousePos, float deltaTime) {
    screenState = ScreenState.TUTORIAL;
    float leftPad = 20;
    float y = -Gdx.graphics.getHeight() / 2f + 20;
    float buttonWidth = Gdx.graphics.getWidth() / 2f - leftPad / 2f;
    float buttonHeight = Gdx.graphics.getHeight() * 0.1f;
    backButton = new UIButton(new Rectangle(leftPad - buttonWidth, y, buttonWidth, buttonHeight),
        "Back", Color.RED, ScreenState.TUTORIAL);
    nextButton = new UIButton(new Rectangle(-leftPad / 2f, y, buttonWidth, buttonHeight), "Next",
        Color.BLUE, ScreenState.TUTORIAL);
    if (nextButton.isClicked(mousePos)) {
      screenNumber++;
    } else if (backButton.isClicked(mousePos)) {
      screenNumber--;
    }
    if (screenNumber < 0 || screenNumber >= textureStrings.length) {
      screenState = ScreenState.MAIN_MENU;
      screenNumber = 0;
      return;
    }
    Texture currentTexture = textures[screenNumber];
    sb.begin();
    sb.draw(currentTexture, -currentTexture.getWidth() / 2f,
        Gdx.graphics.getHeight() / 2f - 100 - currentTexture.getHeight());
    sb.end();
    backButton.render(sr, sb);
    nextButton.render(sr, sb);
    super.render(sb, sr, mousePos, deltaTime);
  }

  @Override
  public ScreenState getScreenState(Vector2 mousePos) {
    return screenState;
  }
}
