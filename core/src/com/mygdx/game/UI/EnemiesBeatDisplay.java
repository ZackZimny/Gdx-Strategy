package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EnemiesBeatDisplay {
  private int enemiesBeat = 0;
  private FontHandler fontHandler;

  public EnemiesBeatDisplay() {
    fontHandler = new FontHandler();
  }

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

  public void setEnemiesBeat(int enemiesBeat) {
    this.enemiesBeat = enemiesBeat;
  }
}
