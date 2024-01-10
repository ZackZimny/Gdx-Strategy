package com.mygdx.game.GameHelpers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.OrderedMap;
import com.mygdx.game.UI.FontHandler;

public class ItemHandler {
  private class Item {
    String name;
    ArrayList<ItemCounter> itemCounters;
    int count = 0;

    public Item(String name, ArrayList<ItemCounter> itemCounters) {
      this.name = name;
      this.itemCounters = itemCounters;
    }

    public void updateCount(float deltaTime) {
      for (ItemCounter itemCounter : itemCounters) {
        itemCounter.update(deltaTime);
        count += itemCounter.consumeItem();
      }
    }

    public int getCount() {
      return count;
    }

    public void addItemCounter(ItemCounter itemCounter) {
      itemCounters.add(itemCounter);
    }

    public void consumeItem(int amount) {
      if (count > amount) {
        count -= amount;
      }
    }
  }

  private FontHandler fontHandler = new FontHandler();
  private OrderedMap<ItemType, Item> itemMap;

  public ItemHandler() {
    itemMap = new OrderedMap<>();
    for (ItemType item : ItemType.values()) {
      itemMap.put(item, new Item(item.toString(), new ArrayList<>()));
    }
  }

  public void update(float deltaTime) {
    for (ItemType item : ItemType.values()) {
      itemMap.get(item).updateCount(deltaTime);
    }
  }

  public void addItemCounter(ItemType type, ItemCounter itemCounter) {
    itemMap.get(type).addItemCounter(itemCounter);
  }

  public int getItemCount(ItemType itemType) {
    return itemMap.get(itemType).getCount();
  }

  public void consumeItem(ItemType type, int amount) {
    itemMap.get(type).consumeItem(amount);
  }

  public void render(SpriteBatch sb) {
    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    float padding = 10f;
    String text = "";
    String longestKey = "";
    for (ItemType type : itemMap.keys()) {
      text += String.format("%s: %d\n", type.toString(), getItemCount(type));
      if (type.toString().length() > longestKey.length()) {
        longestKey = type.toString();
      }
    }
    float textWidth = fontHandler.getTextWidth(longestKey + "0000000");
    sb.begin();
    fontHandler.getFont().draw(sb, text, screenWidth / 2f - padding - textWidth, screenHeight / 2f - padding);
    sb.end();
  }

}
