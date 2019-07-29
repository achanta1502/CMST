package kruskal;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph(6);
        int[][] temp = new int[][]{
            {0, 5, 6, 9, 12, 15, 9, 7},
            {5, 0, 4, 3, 8, 10, 14, 16},
            {6, 4, 0, 8, 5, 12, 11, 24},
            {9, 3, 8, 0, 6, 6, 19, 17},
            {12, 8, 5, 6, 0, 7, 20, 31},
            {15, 10, 12, 6, 7, 0, 21, 44},
            {9, 14, 11, 19, 20, 21, 0, 22},
            {7, 16, 24, 17, 31, 44, 22, 0}
        };
        int [][] temp1 = new int[][] {
            {0, 5, 6, 9, 12, 15},
            {5, 0, 4, 3, 8, 10},
            {6, 4, 0, 8, 5, 12},
            {9, 3, 8, 0, 6, 6},
            {12, 8, 5, 6, 0, 7},
            {15, 10, 12, 6, 7, 0}
        };
        graph.setGraph(temp1);
        System.out.println(graph.toString());
        HashMap<Integer,Integer> wts = new HashMap<>();
        for(int i = 0; i < graph.nodes(); i++) {
            wts.put(i, 1);
        }
        CMSTModifiedKruskal cmstKrus = new CMSTModifiedKruskal(graph);
        //System.out.println(cmstKrus.modifiedKruskal(0, wts, 3));
        CMSTEsauWilliams cmstEW = new CMSTEsauWilliams(graph);
        System.out.println(cmstEW.esauWilliams(0, 3));
    }
}
