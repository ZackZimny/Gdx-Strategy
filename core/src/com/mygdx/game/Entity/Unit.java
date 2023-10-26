package com.mygdx.game.Entity;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameHelpers.Grid;
import com.mygdx.game.GameHelpers.Selector;
import com.mygdx.game.Pathfinding.DestinationNode;
import com.mygdx.game.Pathfinding.NodeGraph;
import java.util.Iterator;

public class Unit extends Entity {

  private Color color;
  private Vector2 currentDestination;
  private boolean stationary;
  private float speed = 60;
  private boolean isSelected = false;
  private boolean isClicked = false;
  private GraphPath<DestinationNode> path;
  private Iterator<DestinationNode> pathIterator;

  public Unit(Rectangle hurtbox) {
    super(hurtbox);
    currentDestination = getPosition().cpy();
  }

  public void handleSelection(Selector selector, Vector2 mousePos, float deltaTime) {
    boolean isBounded = isBounded(selector);

    if (isBounded) {
      isSelected = true;
    }

    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
      if (getHurtbox().contains(mousePos)) {
        isClicked = true;
        isSelected = true;
      } else {
        isClicked = false;
        isSelected = false;
      }
    }

    if (selector.getBound() != null && !isBounded && !isClicked) {
      isSelected = false;
    }

    if (isSelected && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
      stationary = false;
    }

    color = isSelected ? Color.RED : Color.BLUE;
  }

  private void generatePathToDestination(Grid grid, Vector2 mousePos) {
    NodeGraph nodeGraph = grid.getNodeGraph();
    Vector2 startingGridCoordinates = grid.getGridCoordinates(getPosition());
    startingGridCoordinates.add(10, 10);
    Vector2 finalGridCoordinates = grid.getGridCoordinates(mousePos);
    finalGridCoordinates.add(10, 10);
    System.out.println("Final Grid coords " + finalGridCoordinates);
    DestinationNode startNode = grid.getNode((int) startingGridCoordinates.x, (int) startingGridCoordinates.y);
    DestinationNode finalNode = grid.getNode((int) finalGridCoordinates.x, (int) finalGridCoordinates.y);
    System.out.println(startingGridCoordinates);
    nodeGraph.connectNodes(startNode, grid.getNode((int) startingGridCoordinates.x, (int) startingGridCoordinates.y));
    nodeGraph.connectNodes(finalNode, grid.getNode((int) finalGridCoordinates.x, (int) finalGridCoordinates.y));
    path = nodeGraph.findPath(startNode, finalNode);
    pathIterator = path.iterator();
    System.out.println(path);
  }

  public void handleMovement(Grid grid, Vector2 mousePos, float deltaTime) {
    if (!stationary && Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && isSelected) {
      path = null;
      pathIterator = null;
      generatePathToDestination(grid, mousePos);
      currentDestination = pathIterator.next().getPosition();
    }
    if (!stationary) {
      Vector2 velocity = currentDestination.cpy().sub(getPosition().cpy()).nor().scl(speed).scl(deltaTime);
      changePosition(velocity);
    }

    if (getPosition().epsilonEquals(currentDestination, 2)) {
      setPosition(currentDestination.cpy());
      if (path == null || !pathIterator.hasNext()) {
        path = null;
        stationary = true;
        return;
      }
      currentDestination = pathIterator.next().getPosition();
    }
  }

  public boolean isBounded(Selector selector) {
    if (selector.getBound() == null) {
      return false;
    }
    Vector2[] points = {
        new Vector2(getHurtbox().getX(), getHurtbox().getY()),
        new Vector2(getHurtbox().getX() + getHurtbox().getWidth(), getHurtbox().getY()),
        new Vector2(getHurtbox().getX() + getHurtbox().getWidth(), getHurtbox().getY() + getHurtbox().getHeight()),
        new Vector2(getHurtbox().getX(), getHurtbox().getY() + getHurtbox().getHeight())
    };
    boolean isBounded = false;
    for (int i = 0; i < points.length; i++) {
      if (selector.getBound().contains(points[i])) {
        isBounded = true;
      }
    }
    return isBounded;
  }

  public void render(ShapeRenderer sr) {

    // if (nodeGraph != null)
    // nodeGraph.render(sr);
    sr.begin(ShapeType.Filled);
    sr.setColor(color);
    sr.rect(getHurtbox().getX(), getHurtbox().getY(), getHurtbox().getWidth(), getHurtbox().getHeight());
    sr.end();
  }

  public boolean isSelected() {
    return isSelected;
  }
}
