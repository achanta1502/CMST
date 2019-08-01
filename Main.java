/*
author : Achanta pavan kumar
copyright 2019
*/
package kruskal;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        //Slide example graph
        int [][] temp = new int[][] {
            {0, 5, 6, 9, 12, 15},
            {5, 0, 4, 3, 8, 10},
            {6, 4, 0, 8, 5, 12},
            {9, 3, 8, 0, 6, 6},
            {12, 8, 5, 6, 0, 7},
            {15, 10, 12, 6, 7, 0}
        };
        TwoAlgos(6, temp, 0, 3);

        int[][] temp1 = new int[][]{
            {0, 5, 6, 9, 12, 15, 9, 7},
            {5, 0, 4, 3, 8, 10, 14, 16},
            {6, 4, 0, 8, 5, 12, 11, 24},
            {9, 3, 8, 0, 6, 6, 19, 17},
            {12, 8, 5, 6, 0, 7, 20, 31},
            {15, 10, 12, 6, 7, 0, 21, 44},
            {9, 14, 11, 19, 20, 21, 0, 22},
            {7, 16, 24, 17, 31, 44, 22, 0}
        };
        TwoAlgos(8, temp1, 0, 3);

        int[][] temp2 = new int[][] {
            {0, 50, 60, 90, 120, 150},
            {50, 0, 40, 30, 80, 100},
            {60, 40, 0, 80, 50, 120},
            {90, 30, 80, 0, 60, 60},
            {120, 80, 50, 60, 0, 70},
            {150, 100, 120, 60, 70, 0}
        };

        TwoAlgos(6, temp2, 0, 4);
        int[][] temp3 = new int[][] {
            {0, 5, 29, 17, 0, 0, 0, 0},
            {5, 0, 0, 31, 17, 0, 0, 0},
            {29, 0, 0, 23, 0, 13, 0, 0},
            {17, 31, 23, 0, 11, 3, 2, 0},
            {0, 17, 0, 11, 0, 0, 7, 0},
            {0, 0, 13, 3, 0, 0, 13, 0},
            {0, 0, 0, 2, 7, 13, 0, 19},
            {0, 0, 0, 0, 0, 0, 19, 0}
        };
        TwoAlgos(8, temp3, 6, 3);


        int[][] temp4 = new int[][] {
            { 0, 5, 6, 9, 12, 15,6,4 },
            { 5, 0, 4, 3, 8, 10,3,8 },
            { 6, 4, 0, 8, 5, 12, 0,2 },
            { 9, 3, 8, 0, 6, 6,10,4 },
            { 12, 8, 5, 6, 0, 7,5,2 },
            { 15, 10, 12, 6, 7, 0,12,13 },
            {6, 3, 0, 10, 5, 12, 0, 6},
            {4, 8, 2, 4, 2, 3, 6, 0}
        };
        TwoAlgos(8, temp4, 0, 3);

    }

    public static void TwoAlgos(int nodes, int[][]temp, int root, int max_nodes) {
        Graph graph = new Graph(nodes);
        graph.setGraph(temp);
        System.out.println("Graph:");
        System.out.println(graph.toString());
        System.out.println("*****CMST Modified Kruskal*******");
        HashMap<Integer, Integer> wts = new HashMap<>();
        for(int i = 0; i < graph.nodes(); i++) {
            wts.put(i, 1);
        }
        CMSTModifiedKruskal cmstKrus = new CMSTModifiedKruskal(graph);
        System.out.println("CMST Modified kruskal: " + cmstKrus.modifiedKruskal(root, wts, max_nodes));
        System.out.println("*****CMST Esau Williams*******");
        CMSTEsauWilliams cmstEW = new CMSTEsauWilliams(graph);
        System.out.println("CMST EsauWilliams: " + cmstEW.esauWilliams(root, max_nodes, wts));
    }
}
