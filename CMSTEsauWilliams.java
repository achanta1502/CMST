package kruskal;

import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac.IntegrityHmacSHA384;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CMSTEsauWilliams {
  private Graph graph;
  public CMSTEsauWilliams(Graph graph) {
    this.graph = graph;
  }

  private int tradeOff(int root, List<HashMap<Character, Integer>> edgeMaps, int vertex) {
        int min = Integer.MAX_VALUE;
        int root_wt = 0;
        boolean set = false;
        for(HashMap<Character, Integer> edge: edgeMaps) {
            if(edge.get('s') == root) {
              if(edge.get('d') == vertex) {
                root_wt = edge.get('w');
                set = true;
              }
            }
            if(edge.get('s') == vertex || edge.get('d') == vertex) {
              if (edge.get('w') < min) {
                min = edge.get('w');
              }
            }
            if(set && edge.get('s') > vertex) {
              break;
            }
        }
        return min - root_wt;
  }

  private int min(List<HashMap<Character, Integer>> edgeMaps, int vertex) {
      int min = Integer.MAX_VALUE;
      for(HashMap<Character, Integer> edge: edgeMaps) {
        if(edge.get('s') == vertex || edge.get('d') == vertex) {
          if (min > edge.get('w')) {
            min = edge.get('w');
          }
        }
      }
      return min;
  }

  private int rootTotal(List<HashMap<Character, Integer>> edgeMaps, int root) {
    int total = 0;
    for(HashMap<Character, Integer> edge: edgeMaps) {
        if(edge.get('s') == root) {
            total += edge.get('w');
        }
        if(edge.get('s') > root) {
          break;
        }
    }
    return total;
  }

  protected int esauWilliams(int root, int max_nodes) {
      List<HashMap<Character, Integer>> edgeMaps = graph.getEdgesWeights();
      Set<Integer> set = new HashSet<>();
      int trade_min = Integer.MAX_VALUE;
      int vertex_min = -1, tradeOff;
      int select_nodes = 0;
      int esauTotal = rootTotal(edgeMaps, root);
      System.out.println(esauTotal);
      while(select_nodes < max_nodes) {
        for (int vertex = 0; vertex < graph.nodes(); vertex++) {
          tradeOff = tradeOff(root, edgeMaps, vertex);
          if (trade_min > tradeOff) {
            trade_min = tradeOff;
            vertex_min = vertex;
          }
        }
        set.add(vertex_min);
        esauTotal += min(edgeMaps, vertex_min);
        select_nodes++;
      }
    System.out.println(esauTotal);
    for(HashMap<Character, Integer> edge: edgeMaps) {
      if(edge.get('s') == root && set.contains(edge.get('d'))) {
        System.out.println("edge delete" + edge.get('d'));
        System.out.println("edge delete" + edge.get('w'));
          esauTotal -= edge.get('w');
          set.remove(edge.get('d'));
          continue;
      }
      if(edge.get('d') == root && set.contains(edge.get('s'))) {
        System.out.println("edge delete" + edge.get('s'));
        System.out.println("edge delete" + edge.get('w'));
        esauTotal -= edge.get('w');
        set.remove(edge.get('s'));
      }
    }
      return esauTotal;
  }
}
