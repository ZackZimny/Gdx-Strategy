package com.mygdx.game.Pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class NodeConnection implements Connection<DestinationNode> {
  private DestinationNode fromNode;
  private DestinationNode toNode;
  private float cost;

  public NodeConnection(DestinationNode fromNode, DestinationNode toNode) {
    this.fromNode = fromNode;
    this.toNode = toNode;
    cost = Vector2.dst(fromNode.x, fromNode.y, toNode.x, toNode.y);
  }

  public void render(ShapeRenderer shapeRenderer) {
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.setColor(0, 0, 0, 1);
    shapeRenderer.rectLine(fromNode.x, fromNode.y, toNode.x, toNode.y, 2);
    shapeRenderer.end();
  }

  @Override
  public float getCost() {
    return cost;
  }

  @Override
  public DestinationNode getFromNode() {
    return fromNode;
  }

  @Override
  public DestinationNode getToNode() {
    return toNode;
  }
}
