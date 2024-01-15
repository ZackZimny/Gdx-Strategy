package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * displays how many enemies the player has beat to the screen, which is their
 * score
 **/
public class EnemiesBeatDisplay {
  private int enemiesBeat = 0;
  private FontHandler fontHandler;

  public EnemiesBeatDisplay() {
    fontHandler = new FontHandler();
  }

  /**
   * displays the EnemiesBeatDisplay to the screen
   * 
   * @param sb displays the text related to the score
   **/
  public void render(SpriteBatch sb) {
    String text = "Enemies Beat: " + enemiesBeat;
    String dummyText = "Enemies Beat: 00000";
    float height = fontHandler.getTextHeight(dummyText);
    float leftPad = 10;
    sb.begin();
    fontHandler.getFont().draw(sb, text, -Gdx.graphics.getWidth() / 2f + leftPad,
        Gdx.graphics.getHeight() / 2f - height);
    sb.end();
  }

  /**
   * @param enemiesBeat updates the value for the score
   **/
  public void setEnemiesBeat(int enemiesBeat) {
    this.enemiesBeat = enemiesBeat;
  }
}
