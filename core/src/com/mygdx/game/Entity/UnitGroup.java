package com.mygdx.game.Entity;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.Selector;

public class UnitGroup {
  ArrayList<Unit> units;
  ArrayList<ArrayList<Unit>> unitSubgroups = new ArrayList<>();

  public UnitGroup(ArrayList<Unit> units) {
    this.units = units;
  }

  public void handlePhysics(Grid grid, Selector selector, Vector2 mousePos, float deltaTime) {
    ArrayList<Unit> selectedUnits = new ArrayList<>();
    for (Unit unit : units) {
      unit.handleSelection(selector, mousePos, deltaTime);
      if (unit.isSelected()) {
        selectedUnits.add(unit);
      }
    }
    /*
     * if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
     * 
     * if (selectedUnits.size() > 1) {
     * float lowestX = Float.MAX_VALUE;
     * float highestX = Float.MIN_VALUE;
     * float lowestY = Float.MAX_VALUE;
     * float highestY = Float.MIN_VALUE;
     * for (Unit unit : selectedUnits) {
     * float x = unit.getPosition().x;
     * float y = unit.getPosition().y;
     * if (x < lowestX)
     * lowestX = x;
     * if (x > highestX)
     * highestX = x;
     * if (y < lowestY)
     * lowestY = y;
     * if (y > highestY)
     * highestY = y;
     * }
     * Rectangle boundingBox = new Rectangle(lowestX, lowestY, highestX - lowestX,
     * highestY - lowestY);
     * Vector2 boundingBoxCenter = new Vector2((lowestX + highestX) / 2f, (lowestY +
     * highestY) / 2f);
     * if (!boundingBox.contains(mousePos)) {
     * Vector2 velocity = mousePos.cpy().sub(boundingBoxCenter);
     * for (Unit unit : selectedUnits) {
     * unit.setFinalDestination(unit.getPosition().cpy().add(velocity));
     * }
     * } else {
     * System.out.println("Contained");
     * float sumWidth = 0;
     * float sumHeight = 0;
     * for (Unit unit : selectedUnits) {
     * sumWidth += unit.getHurtbox().getWidth();
     * sumHeight += unit.getHurtbox().getHeight();
     * }
     * float paddingMultiplier = 0.75f;
     * for (Unit unit : selectedUnits) {
     * Vector2 random = new Vector2((float) (Math.random() - 0.5f) * sumWidth *
     * paddingMultiplier,
     * (float) (Math.random() - 0.5f) * sumHeight * paddingMultiplier);
     * unit.setFinalDestination(mousePos.cpy().add(random));
     * }
     * }
     * }
     * }
     */

    for (Unit unit : units) {
      unit.handleMovement(grid, mousePos, deltaTime);
    }
  }

  public void render(ShapeRenderer sr) {
    for (Unit unit : units) {
      unit.render(sr);
    }
  }

  public ArrayList<Unit> getUnits() {
    return units;
  }
}
