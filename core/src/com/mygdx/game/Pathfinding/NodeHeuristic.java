package com.mygdx.game.Pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;

public class NodeHeuristic implements Heuristic<DestinationNode> {

  @Override
  public float estimate(DestinationNode currentNode, DestinationNode goalNode) {
    return Vector2.dst(currentNode.x, currentNode.y, goalNode.x, goalNode.y);
  }
}
