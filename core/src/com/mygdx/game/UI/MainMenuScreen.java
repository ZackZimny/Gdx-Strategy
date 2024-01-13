package com.mygdx.game.UI;

import java.util.LinkedHashMap;

public class MainMenuScreen extends Screen {
  public MainMenuScreen() {
    super("Verne", ScreenState.MAIN_MENU);
    LinkedHashMap<String, ScreenState> stringStateHashMap = new LinkedHashMap<>();
    stringStateHashMap.put("Play", ScreenState.GAME_LOOP);
    stringStateHashMap.put("Exit", ScreenState.EXIT);
    createButtonColumn(stringStateHashMap);
  }
}
