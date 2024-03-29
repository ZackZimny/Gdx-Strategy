package com.mygdx.game.UI;

import java.util.LinkedHashMap;

/**
 * First screen the user boots into on application startup
 **/
public class MainMenuScreen extends Screen {
  public MainMenuScreen() {
    super("Verne", ScreenState.MAIN_MENU);
    LinkedHashMap<String, ScreenState> stringStateHashMap = new LinkedHashMap<>();
    stringStateHashMap.put("Play", ScreenState.GAME_LOOP);
    stringStateHashMap.put("Tutorial", ScreenState.TUTORIAL);
    stringStateHashMap.put("Records", ScreenState.RECORDS);
    stringStateHashMap.put("Options", ScreenState.OPTIONS);
    stringStateHashMap.put("Exit", ScreenState.EXIT);
    createButtonColumn(stringStateHashMap);
  }
}
