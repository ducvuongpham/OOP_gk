package backEnd;

import java.util.ArrayList;

import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;

// Truong hop qua nhieu nodes hoac edges se su dung cai nay
public class FindPossible {
    static Object[] edges;
    static ArrayList<Node> possible;

    public static void printPossible(Node destination) {
        FindAction.PathLists = new ArrayList<ArrayList<Node>>();
        printPossibleUtil(destination);
        // System.out.println("\n\n" + FindAction.PathLists.toString());
    }

    private static void printPossibleUtil(Node destination) {
        int check = 0;
        for (ArrayList<Node> edge : FindAction.PathLists) {
            if (edge.get(1).getId().equals(destination.getId())) {
                check++;
                break;
            }
        }
        edges = destination.enteringEdges().toArray();
        if (check == 0 && edges.length > 0)
            for (Object edge : edges) {
                possible = new ArrayList<>();
                possible.add(((Edge) edge).getNode0());
                possible.add(destination);
                FindAction.PathLists.add(possible);
                printPossibleUtil(((Edge) edge).getNode0());
            }
    }
}