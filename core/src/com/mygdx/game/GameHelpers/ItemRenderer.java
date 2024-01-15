package com.mygdx.game.GameHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.OrderedMap;
import com.mygdx.game.UI.FontHandler;

/**
 * Displays Item information at the top left of the screen
 **/
public class ItemRenderer {

  private FontHandler fontHandler = new FontHandler();
  private OrderedMap<ItemType, Integer> itemMap = new OrderedMap<>();

  /**
   * @param sb SpriteBatch used to render text
   **/
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
    // origin is in the center of the screen
    sb.begin();
    fontHandler.getFont().draw(sb, text, screenWidth / 2f - padding - textWidth, screenHeight / 2f - padding);
    sb.end();
  }

  /**
   * @return ItemMap<ItemType, Integer> which displays to the screen when the
   *         render method is used
   **/
  public void setItemMap(OrderedMap<ItemType, Integer> itemMap) {
    this.itemMap = itemMap;
  }

}
