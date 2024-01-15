package com.mygdx.game.UI;

import java.util.LinkedHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class RecordsScreen extends Screen {
  private String recordsString;

  public RecordsScreen() {
    super("Records", ScreenState.RECORDS);
    LinkedHashMap<String, ScreenState> stateScreenMap = new LinkedHashMap<>();
    stateScreenMap.put("Return to Main Menu", ScreenState.MAIN_MENU);
    createButtonColumn(stateScreenMap);
  }

  public void render(SpriteBatch sb, ShapeRenderer sr, Vector2 mousePos, float deltaTime) {
    sb.begin();
    getTitleFontHandler().getSmallFont().draw(sb, recordsString, -Gdx.graphics.getWidth() / 2f + 100,
        Gdx.graphics.getHeight() / 2f - 100);
    sb.end();
    super.render(sb, sr, mousePos, deltaTime);
  }

  public void setRecordString(String recordsString) {
    this.recordsString = recordsString;
  }
}
