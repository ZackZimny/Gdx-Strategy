package com.mygdx.game.GameHelpers;

public class ItemCounter {
  private float timeElapsed = 0;
  private float creationTime;
  private int amountOnConumption;
  private boolean isConsumable = false;

  public ItemCounter(float creationTime, int amountOnConumption) {
    this.creationTime = creationTime;
    this.amountOnConumption = amountOnConumption;
  }

  public void update(float deltaTime) {
    timeElapsed += deltaTime;
    if (timeElapsed >= creationTime) {
      timeElapsed = 0;
      isConsumable = true;
    }
  }

  public int consumeItem() {
    if (isConsumable) {
      isConsumable = false;
      return amountOnConumption;
    }
    return 0;
  }
}
