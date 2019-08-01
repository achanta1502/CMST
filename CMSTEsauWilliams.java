/*
author : Achanta pavan kumar
copyright 2019
*/
package kruskal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CMSTEsauWilliams {
  private Graph graph;
  private Map<Integer, Integer> rootMap;
  private List<HashMap<Character, Integer>> edgeMaps;
  private Map<Integer, Set<Integer>> traverse;
  private Map<Integer, Integer> tradeMap;
  private int esauTotal = 0;
  private ArrayList<Map<Character, Integer>> cmst;

  public CMSTEsauWilliams(Graph graph) {
    this.graph = graph;
    rootMap = new HashMap<>();
    tradeMap = new HashMap<>();
    cmst = new ArrayList<>();
  }

  //calculate the tradeoff value
  private Map<Integer, Integer> tradeOff() {
        for(int vertex = 0; vertex < graph.nodes(); vertex++) {
          int min = Integer.MAX_VALUE;
          for (HashMap<Character, Integer> edge : edgeMaps) {
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

  //minimum edge for vertex and its other vertex
  private int[] min(int vertex, int root) {
      int[] min = new int[]{Integer.MAX_VALUE, 0};
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

  //root weights to vertexes need to be updated
  private void rootUpdate(int root, List<Integer> parent) {
      for(int i = 0; i < graph.nodes(); i++) {
          if(i == root) continue;
          int val = find(parent, i);
          parent.set(i, val);
          if(rootMap.getOrDefault(val, 0) != rootMap.getOrDefault(i, 0) && rootMap.getOrDefault(val,0) > 0) {
            rootMap.put(i, rootMap.get(val));
          }
      }
  }

  //Initial root weight calculation
  private void rootSet(int root) {
    for(HashMap<Character, Integer> edge: edgeMaps) {
        if(edge.get('s') == root) {
            cmst.add(edge);
            rootMap.put(edge.get('d'), edge.get('w'));
        }
    }
  }

  //Print tradeoff values
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

  //check for the loop between two nodes
  private boolean isLoopExist(List<Integer> parent, int node1, int node2) {
    return find(parent, node1) == find(parent, node2);
  }

  //check loop for the given edge
  private boolean isLoopExist(List<Integer> parent, HashMap<Character, Integer> edge) {
    return find(parent, edge.get('s')) == find(parent, edge.get('d'));
  }

  //Is tradeoff positive to cut off or negative
  private boolean isTradeOff(Map<Integer, Integer> tradeMap) {
    int count = 0;
    for(Integer i: tradeMap.keySet()) {
      if(tradeMap.get(i) >= 0) {
        count++;
      }
    }
    return count != tradeMap.size();
  }

  //trade off sorting
  private List<Integer> tradeMapMin(Map<Integer,Integer> tradeMap) {
    Set<Integer> wts = new TreeSet<>(tradeMap.values());
    List<Integer> nodes = new ArrayList<>();
    for(Integer wt: wts) {
        for(Integer i:tradeMap.keySet()) {
            if(tradeMap.get(i).equals(wt)) {
              nodes.add(i);
            }
        }

    }
    return nodes;
  }

  //subtree node weights used for constraints
  private void subtree_weight(List<ArrayList<Integer>> st_list, int vertex,
      Map<Integer,Integer> weights, int st_wt) {
    st_wt += weights.get(vertex);
    for(ArrayList<Integer> st: st_list) {
      if(st.contains(vertex)) {
        for(int v: st) {
          st_wt += weights.get(v);
        }
        st_wt -= weights.get(vertex);
        break;
      }
    }
  }

  //To check constfraints based on sum of weights of nodes <= total weight
  private boolean isConstraints(int root, List<ArrayList<Integer>> st_list,
      Map<Integer,Integer> weights, int max_nodes, int min_node, int min_node2) {
      int st_wt = 0;
        for (ArrayList<Integer> st : st_list) {
          if(!st.contains(min_node) && !st.contains(min_node2)) {
            continue;
          }
          for(int v: st) {
            st_wt += weights.getOrDefault(v, 1);
          }

          if(!st.contains(min_node)) {
            subtree_weight(st_list, min_node, weights, st_wt);
          }
          if(!st.contains(min_node2)) {
            subtree_weight(st_list, min_node2, weights, st_wt);
          }
          if(st_wt >= max_nodes) {
            System.out.printf("max nodes reached. sum of node weights %d >= total node weights %d\n", st_wt, max_nodes);
            return false;
          }
          if(!st.contains(min_node)) {
            st.add(min_node);
          }
          if(!st.contains(min_node2)) {
            st.add(min_node2);
          }
          return true;
        }
      ArrayList<Integer> new_st = new ArrayList<>();
      new_st.add(min_node);
      new_st.add(min_node2);
      st_list.add(new_st);
      return true;
  }

  //Add edge to CMST graph
  private void cmstAddandRemove(int root, int node1, int node2, int wt) {
    Map<Character, Integer> edge = new HashMap<>();
    edge.put('s', node1);
    edge.put('d', node2);
    edge.put('w', wt);
    cmst.add(edge);
    for(Map<Character, Integer> map: cmst) {
        if(map.get('s') == root && map.get('d') == node1) {
            cmst.remove(map);
            break;
        }
    }
  }

  //esauWilliams algorithm to calculate minimum spanning tree
  protected int esauWilliams(int root, int max_nodes, HashMap<Integer,Integer> wts) {
      edgeMaps = graph.getEdgesWeights();
      Set<Integer> set = new HashSet<>();
      traverse = new HashMap<>();
      rootSet(root);
      List<Integer> parent = new ArrayList<>();
      for(int i = 0; i < graph.nodes(); i++) {
        parent.add(i, i);
      }
      //int[] min;
      int min = 0;
      List<ArrayList<Integer>> subtrees = new ArrayList<>();
      int count = 0;
      tradeMap = tradeOff();
      printMap(tradeMap);
      boolean flag = true;
      while(flag && isTradeOff(tradeMap)) {
        List<Integer> trade_sort = tradeMapMin(tradeMap);
        int i = 0;
        while(i < graph.nodes() - 1) {
          int min_node = trade_sort.get(i);
          int[] min1 = min(min_node, root);
          int min_node2 = min1[1];
          min = min1[0];
          System.out.println("vertex_min:" + min_node);
          if(!isLoopExist(parent, min_node, min_node2)) {
            if (isConstraints(root, subtrees, wts, max_nodes, min_node, min_node2)) {
              parent.set(find(parent, min_node), find(parent, min_node2));
              esauTotal += min;
              set.add(min_node);
              cmstAddandRemove(root, min_node, min_node2, min);
              rootUpdate(root, parent);
              count++;
              break;
            } else {
              System.out.println("Skipping as there are constraints");
            }
          }else {
            System.out.println("loop exists");
            count++;
          }
          i++;
          if(i == graph.nodes() - 1) {
            flag = false;
          }
      }
        tradeMap = tradeOff();
        printMap(tradeMap);
        if(count == graph.nodes()-1) {
          break;
        }
      }
    for(Map<Character, Integer> map: cmst) {
      System.out.println(map.get('s') + "-->" + map.get('d') + "-->" + map.get('w'));
    }
    for(int i = 0; i < graph.nodes(); i++) {
      if(i == root || set.contains(i)) continue;
        esauTotal += rootMap.getOrDefault(i,0);
    }
      return esauTotal;
  }
}
