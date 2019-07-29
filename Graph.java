package kruskal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Graph {
  private int nodes;
  private int[][] graph;
  public Graph(int nodes) {
    this.nodes = nodes;
    graph = new int[nodes][nodes];
  }
  //Create a Random graph with the given
  //number of edges and vertices
  protected void createGraph() {
    Random random = new Random();
    for(int edge = 0; edge < nodes; edge++) {
      int node1 = random.nextInt(nodes);
      int node2 = random.nextInt(nodes);
      if(node1 == node2) {
        node1 = random.nextInt(nodes);
      }
      graph[node1][node2] = graph[node1][node2] + 1;
      graph[node2][node1] = graph[node1][node2] + 1;
    }
  }

  //Number of nodes in the graph
  protected void setNodes(int nodes) {
    this.nodes = nodes;
  }

  //Set the nodes in the graph
  protected int nodes() {
    return nodes;
  }

  //Set the graph
  protected void setGraph(int[][] graph) {
    this.graph = graph;
  }

  //Returns the graph
  protected int[][] getGraph() {
    return graph;
  }

  //Set the value at particular position
  protected void setValue(int row, int col, int value) {
    graph[row][col] = value;
  }

  //Get value at position
  protected int getValue(int row, int col) {
    return graph[row][col];
  }

  protected ArrayList<HashMap<Character, Integer>> getEdgesWeights() {
    ArrayList<HashMap<Character,Integer>> edgesMap = new ArrayList<>();
    for(int i = 0; i < nodes; i++) {
      for(int j = i; j < nodes; j++) {
          if (graph[i][j] == 0) {
            continue;
          }
          HashMap<Character, Integer> edge = new HashMap<>();
          edge.put('s', i);
          edge.put('d', j);
          edge.put('w', graph[i][j]);
          edgesMap.add(edge);
      }
    }
    return edgesMap;
  }

  //Print the graph
  @Override
  public String toString() {
    StringBuilder graphPrint = new StringBuilder();
    for(int i = 0; i < nodes; i++) {
      for(int j = 0; j<nodes; j++) {
        graphPrint.append(graph[i][j] + "\t");
      }
      graphPrint.append("\n");
    }
    return graphPrint.toString();
  }
}
