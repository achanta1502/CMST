/*
author : Achanta pavan kumar
copyright 2019
*/
package kruskal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CMSTModifiedKruskal {
    private Graph graph;

    public CMSTModifiedKruskal(Graph graph) {
        this.graph = graph;
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

    //sort the edges based on weights
    private ArrayList<HashMap<Character, Integer>> sort(ArrayList<HashMap<Character, Integer>>edgesMap) {
        for(int i = 0; i < edgesMap.size(); i++) {
          for(int j = i; j < edgesMap.size(); j++) {
              if(edgesMap.get(i).get('w') > edgesMap.get(j).get('w')) {
                HashMap<Character, Integer> tmp = edgesMap.get(j);
                edgesMap.set(j, edgesMap.get(i));
                edgesMap.set(i, tmp);
              }
          }
        }
        return edgesMap;
    }
    //check for constraints. sum of weight of nodes less than max_weight
    private boolean isConstraints(int root, List<ArrayList<Integer>> st_list,
        HashMap<Integer,Integer> weights, int max_nodes,
        HashMap<Character,Integer> edge) {
        int st_weight = 0;
        if(edge.get('s') == root || edge.get('d') == root) {
            return true;
        }
        for(ArrayList<Integer> st : st_list) {
            if(!st.contains(edge.get('s')) && !st.contains(edge.get('d'))) {
                continue;
            }
            for(int v: st) {
                st_weight += weights.get(v);
            }
            if(!st.contains(edge.get('s'))) {
                subtree_weight(st_list, edge.get('s'), weights, st_weight);
            }
            if(!st.contains(edge.get('d'))) {
                subtree_weight(st_list, edge.get('d'), weights, st_weight);
            }
            if(st_weight >= max_nodes) {
                System.out.println(edge.get('s') + " <--> " + edge.get('d') + " weight: "
                    + edge.get('w') + " | Rejected since subtree weight " + st_weight + " >= "
                    + max_nodes + " (Max allowed subtree weight)");
                return false;
            }
            if(!st.contains(edge.get('s'))) {
                st.add(edge.get('s'));
            }
            if(!st.contains(edge.get('d'))) {
                st.add(edge.get('d'));
            }
            return true;
        }
        ArrayList<Integer> new_st = new ArrayList<>();
        new_st.add(edge.get('s'));
        new_st.add(edge.get('d'));
        st_list.add(new_st);
        return true;
    }

    //subtree total weight
    private void subtree_weight(List<ArrayList<Integer>> st_list, int vertex,
        HashMap<Integer,Integer> weights, int st_wt) {
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

    //kruskal algorithm to get minimum spanning tree
    protected int modifiedKruskal(int root, HashMap<Integer,Integer> wts, int max_nodes) {
        //keep track of subtree graphs connected
        List<ArrayList<Integer>> subtrees = new ArrayList<>();
        //parent list for union-find
        List<Integer> parent = new ArrayList<>();
        //CMST edges for the minimum span tree
        ArrayList<HashMap<Character, Integer>> cmst = new ArrayList<>();
        int edge_count = 0;
        int cmst_weight = 0;
        for(int i = 0; i < graph.nodes(); i++) {
            parent.add(i, i);
        }
        ArrayList<HashMap<Character, Integer>> edgesMap = graph.getEdgesWeights();
        edgesMap = sort(edgesMap);
        for(HashMap<Character, Integer> edge: edgesMap) {
            if(!isLoopExist(parent, edge)) {
                if (isConstraints(root, subtrees, wts, max_nodes, edge)) {
                    edge_count++;
                    parent.set(find(parent, edge.get('d')), find(parent, edge.get('s')));
                    cmst_weight += edge.get('w');
                    cmst.add(edge);
                    System.out.println(edge.get('s') + " <--> " + edge.get('d') + " weight: "
                        + edge.get('w') + " | Selected");
                }
            }
            else {
                System.out.println(edge.get('s') + " <--> " + edge.get('d') + " weight: "
                    + edge.get('w') + " | Already connected");
            }
            if(edge_count == graph.nodes() - 1) {
                break;
            }
        }
        return cmst_weight;
    }
}
