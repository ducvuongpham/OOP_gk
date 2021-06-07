package backEnd;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.graphstream.graph.Node;

public class FindAllPath {
    public static void printAllPaths(String s, String d) {
        if (FindAction.PathLists != null)
            FindAction.PathLists.clear();
        System.gc();

        FindAction.PathLists = new ArrayList<ArrayList<Node>>();
        for (Node node : StoreGraph.MainGraph) {
            node.removeAttribute("isVisited");
        }

        printAllPathsUtil(s, d);
        System.out.println("\nAll printed! :D");
        System.out.println("\n\n");
    }

    private static void printAllPathsUtil(String s, String d) {
        Node source = StoreGraph.getGraph().getNode(s);
        Node destination = StoreGraph.getGraph().getNode(d);

        Queue<List<Node>> queue = new LinkedList<>();
        List<Node> path = new ArrayList<>();
        path.add(source);
        queue.offer(path);
        while (!queue.isEmpty()) {
            path = queue.poll();
            Node last = path.get(path.size() - 1);
            if (last.equals(destination)) {
                printPath((ArrayList<Node>) path);
            }
            List<Node> lastNode = StoreGraph.getAdjacency(last);
            for (int i = 0; i < lastNode.size(); i++) {
                if (isNotVisited(lastNode.get(i), path)) {
                    List<Node> newpath = new ArrayList<>(path);
                    newpath.add(lastNode.get(i));
                    queue.offer(newpath);
                }
            }
        }
    }

    private static boolean isNotVisited(Node x, List<Node> path) {
        int size = path.size();
        for (int i = 0; i < size; i++)
            if (path.get(i).getId().equals(x.getId()))
                return false;

        return true;
    }

    private static void printPath(ArrayList<Node> path) {
        // FindAction.PathLists.add((ArrayList<String>) path.clone());
        ArrayList<Node> clone = new ArrayList<>();
        for (Node string : path) {
            clone.add(string);
        }
        FindAction.PathLists.add(clone);
    }
}