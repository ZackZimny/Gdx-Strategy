package com.mygdx.game.GameHelpers;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.OrderedMap;
import com.mygdx.game.UI.FontHandler;

public class ItemRenderer {

  private FontHandler fontHandler = new FontHandler();
  private OrderedMap<ItemType, Integer> itemMap = new OrderedMap<>();

  public void render(SpriteBatch sb) {
    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    float padding = 10f;
    String text = "";
    String longestKey = "";
    for (ItemType type : itemMap.keys()) {
      text += String.format("%s: %d\n", type.toString(), itemMap.get(type));
      if (type.toString().length() > longestKey.length()) {
        longestKey = type.toString();
      }
    }
    float textWidth = fontHandler.getTextWidth(longestKey + "0000000");
    sb.begin();
    fontHandler.getFont().draw(sb, text, screenWidth / 2f - padding - textWidth, screenHeight / 2f - padding);
    sb.end();
  }

  public void setItemMap(OrderedMap<ItemType, Integer> itemMap) {
    this.itemMap = itemMap;
  }

}
