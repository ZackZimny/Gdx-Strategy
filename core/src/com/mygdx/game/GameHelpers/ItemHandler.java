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

  private final String[] ITEMS = { "Money", "Steel", "Energy" };
  private FontHandler fontHandler = new FontHandler();
  private OrderedMap<String, Item> itemMap;

  public ItemHandler() {
    itemMap = new OrderedMap<>();
    for (String itemName : ITEMS) {
      ArrayList<ItemCounter> itemCounters = new ArrayList<>();
      itemCounters.add(new ItemCounter(0.5f, 2));
      itemMap.put(itemName, new Item(itemName, itemCounters));
    }
  }

  public void update(float deltaTime) {
    for (String itemName : ITEMS) {
      itemMap.get(itemName).updateCount(deltaTime);
    }
  }

  public void addItemCounter(String name, ItemCounter itemCounter) {
    itemMap.get(name).addItemCounter(itemCounter);
  }

  public int getItemCount(String name) {
    return itemMap.get(name).getCount();
  }

  public void consumeItem(String name, int amount) {
    itemMap.get(name).consumeItem(amount);
  }

  public void render(SpriteBatch sb) {
    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    float padding = 10f;
    String text = "";
    String longestKey = "";
    for (String item : itemMap.keys()) {
      text += String.format("%s: %d\n", item, itemMap.get(item).getCount());
      if (item.length() > longestKey.length()) {
        longestKey = item;
      }
    }
    float textWidth = fontHandler.getTextWidth(longestKey + "0000000");
    sb.begin();
    fontHandler.getFont().draw(sb, text, screenWidth / 2f - padding - textWidth, screenHeight / 2f - padding);
    sb.end();
  }

}
