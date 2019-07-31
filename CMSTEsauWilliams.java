package kruskal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CMSTEsauWilliams {
  private Graph graph;
  private Map<Integer, Integer> rootMap;
  private List<HashMap<Character, Integer>> edgeMaps;
  private Map<Integer, Set<Integer>> traverse;
  private Map<Integer, Integer> tradeMap;
  private int esauTotal = 0;
  public CMSTEsauWilliams(Graph graph) {
    this.graph = graph;
    rootMap = new HashMap<>();
    tradeMap = new HashMap<>();
  }

  private Map<Integer, Integer> tradeOff(List<Integer> parent) {
        for(int vertex = 0; vertex < graph.nodes(); vertex++) {
          int min = Integer.MAX_VALUE;
          for (HashMap<Character, Integer> edge : edgeMaps) {
            if(isLoopExist(parent, edge)) {
                continue;
            }
            if (edge.get('s') == vertex) {
              if (edge.get('w') < min && !traverse.getOrDefault(vertex, new HashSet<>()).contains(edge.get('d'))) {
                min = edge.get('w');
              }
            }
            if (edge.get('d') == vertex) {
              if (edge.get('w') < min && !traverse.getOrDefault(vertex, new HashSet<>()).contains(edge.get('s'))) {
                min = edge.get('w');
              }
            }
            if (edge.get('s') > vertex) {
              break;
            }
          }
          tradeMap.put(vertex, min - rootMap.getOrDefault(vertex, 0));
        }
        return tradeMap;
  }

  private int[] min(int vertex, int root) {
      int[] min = new int[]{Integer.MAX_VALUE, -1};
      int tv1 = -1, tv2 = -1;
      for(HashMap<Character, Integer> edge: edgeMaps) {
        if(edge.get('s') == vertex || edge.get('d') == vertex) {
          if (min[0] > edge.get('w')) {
            min[0] = edge.get('w');
            if(edge.get('s') == vertex && edge.get('d') != root) {
              min[1] = edge.get('d');
              tv1 = vertex;
              tv2 = edge.get('d');
            }
            if (edge.get('d') == vertex && edge.get('s') != root){
              min[1] = edge.get('s');
              tv1 = vertex;
              tv2 = edge.get('s');
            }
          }
        }
      }
      Set<Integer> set = traverse.getOrDefault(tv1, new HashSet<>());
      set.add(tv2);
      traverse.put(tv1, set);
      set = traverse.getOrDefault(tv2, new HashSet<>());
      set.add(tv1);
      traverse.put(tv2, set);
      return min;
  }

  private void rootUpdate(int root, List<Integer> parent) {
      for(int i = 0; i < graph.nodes(); i++) {
          if(i == root) continue;
          int val = find(parent, i);
          parent.set(i, val);
          if(rootMap.get(val) != rootMap.get(i)) {
            rootMap.put(i, rootMap.get(val));
          }
      }
  }

  private void rootSet(int root) {
    for(HashMap<Character, Integer> edge: edgeMaps) {
        if(edge.get('s') == root) {
            rootMap.put(edge.get('d'), edge.get('w'));
        }
        if(edge.get('s') > root) {
          break;
        }
    }
  }

  private void printMap(Map<Integer, Integer> map) {
      for(Integer v: map.keySet()) {
          System.out.println("vertex: " + v +", tradeoff: " + map.get(v));
      }
  }

  //find the parent vertex of the graph
  private int find(List<Integer> parent, int vertex) {
    if(parent.get(vertex) == vertex) {
      return vertex;
    }
    return find(parent, parent.get(vertex));
  }

  //check for the loop in the path
  private boolean isLoopExist(List<Integer> parent, HashMap<Character, Integer> edge) {
    return find(parent, edge.get('s')) == find(parent, edge.get('d'));
  }

  protected int esauWilliams(int root, int max_nodes) {
      edgeMaps = graph.getEdgesWeights();
      Set<Integer> set = new HashSet<>();
      traverse = new HashMap<>();
      int select_nodes = 0;
      rootSet(root);
      List<Integer> parent = new ArrayList<>();
      boolean[] track = new boolean[graph.nodes()];
      for(int i = 0; i < graph.nodes(); i++) {
        parent.add(i, i);
      }
      int[] min;
      int vertex_min = -1;
    while(select_nodes < max_nodes) {
        int trade_min = Integer.MAX_VALUE;
        tradeMap = tradeOff(parent);
        printMap(tradeMap);
        for (Integer vertex: tradeMap.keySet()) {
          if (trade_min > tradeMap.get(vertex)) {
            trade_min = tradeMap.get(vertex);
            vertex_min = vertex;
          }
        }
        set.add(vertex_min);
        track[vertex_min] = true;
        System.out.println("vertex_min: " + vertex_min);
        min = min(vertex_min, root);
        esauTotal += min[0];
        parent.set(find(parent, vertex_min), find(parent, min[1]));
        System.out.println();
        rootUpdate(root, parent);
        select_nodes++;
      }
System.out.println(parent);
    for(int i = 0; i < graph.nodes(); i++) {
      if(i == root || set.contains(i)) continue;
        esauTotal += rootMap.get(i);
    }
      return esauTotal;
  }
}
