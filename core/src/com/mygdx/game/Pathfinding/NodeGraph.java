package com.mygdx.game.Pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class NodeGraph implements IndexedGraph<DestinationNode> {

  private NodeHeuristic nodeHeuristic = new NodeHeuristic();
  private Array<DestinationNode> nodes = new Array<>();
  private Array<NodeConnection> connections = new Array<>();

  /** Map of Cities to Streets starting in that City. */
  ObjectMap<DestinationNode, Array<Connection<DestinationNode>>> destinationMap = new ObjectMap<>();

  private int lastNodeIndex = 0;

  public void addNode(DestinationNode node) {
    node.index = lastNodeIndex;
    lastNodeIndex++;

    nodes.add(node);
  }

  public void removeNode(DestinationNode node) {
    Array<NodeConnection> removeableConnections = new Array<>();
    for (NodeConnection connection : connections) {
      if (connection.getFromNode().equals(node) || connection.getToNode().equals(node)) {
        removeableConnections.add(connection);
      }
    }
    connections.removeAll(removeableConnections, true);
    destinationMap.remove(node);
    nodes.removeValue(node, true);
  }

  public void connectNodes(DestinationNode fromNode, DestinationNode toNode) {
    NodeConnection connection = new NodeConnection(fromNode, toNode);
    if (!destinationMap.containsKey(fromNode)) {
      destinationMap.put(fromNode, new Array<Connection<DestinationNode>>());
    }
    destinationMap.get(fromNode).add(connection);
    connections.add(connection);
  }

  public GraphPath<DestinationNode> findPath(DestinationNode startNode, DestinationNode goalNode) {
    GraphPath<DestinationNode> nodePath = new DefaultGraphPath<>();
    new IndexedAStarPathFinder<>(this).searchNodePath(startNode, goalNode, nodeHeuristic, nodePath);
    return nodePath;
  }

  public void render(ShapeRenderer sr) {
    for (DestinationNode node : nodes) {
      node.render(sr, false);
    }
    for (NodeConnection connection : connections) {
      connection.render(sr);
    }
  }

  @Override
  public int getIndex(DestinationNode node) {
    return node.index;
  }

  @Override
  public int getNodeCount() {
    return lastNodeIndex;
  }

  @Override
  public Array<Connection<DestinationNode>> getConnections(DestinationNode fromNode) {
    if (destinationMap.containsKey(fromNode)) {
      return destinationMap.get(fromNode);
    }

    return new Array<>(0);
  }

  public Array<DestinationNode> getNodes() {
    return nodes;
  }

  public Array<NodeConnection> getConnections() {
    return connections;
  }
}
