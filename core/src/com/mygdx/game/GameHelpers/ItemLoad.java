package com.mygdx.game.GameHelpers;

public class ItemLoad {
  private ItemType item;
  private int amount;

  public ItemLoad(ItemType item, int amount) {
    this.item = item;
    this.amount = amount;
  }

  public ItemType getItem() {
    return item;
  }

  public int getAmount() {
    return amount;
  }

  public void setItem(ItemType item) {
    this.item = item;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

}
