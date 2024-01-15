package com.mygdx.game.GameHelpers;

/**
 * contains information on ItemType and item amount
 **/
public class ItemLoad {
  private ItemType item;
  private int amount;

  /**
   * @param item   ItemType resource
   * @param amount amount extracted
   **/
  public ItemLoad(ItemType item, int amount) {
    this.item = item;
    this.amount = amount;
  }

  /**
   * @return ItemType resource
   **/
  public ItemType getItem() {
    return item;
  }

  /**
   * @return amoun extracted
   **/
  public int getAmount() {
    return amount;
  }

  /**
   * @param item overrides current ItemType
   **/
  public void setItem(ItemType item) {
    this.item = item;
  }

  /**
   * @param amount overrides current amount
   **/
  public void setAmount(int amount) {
    this.amount = amount;
  }

}
