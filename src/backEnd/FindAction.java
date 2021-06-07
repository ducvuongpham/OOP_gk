package backEnd;

import java.util.ArrayList;

import org.graphstream.graph.Node;

import frontEnd.VerticalToolbar;

public class FindAction {
    public static ArrayList<ArrayList<Node>> PathLists;
    public static boolean isFinding;
    public static ArrayList<String> pastNodes;
    public static ArrayList<String> forRedo;
    private static String destination;

    public static void setDestination(String string) {
        pastNodes = new ArrayList<>();
        forRedo = new ArrayList<>();
        destination = string;
    }

    public static String findNext(String currentNode) {

        if (PathLists.size() == 0) {
            System.out.println("ERR");
            VerticalToolbar.ShowError("Can not reach goal with this way");
            return null;
        }

        for (Node node : StoreGraph.getGraph()) {
            if (node.getAttribute("ui.class") != null)
                if (node.getAttribute("ui.class").toString().equals("dekiru"))
                    node.removeAttribute("ui.class");
            if (node.getAttribute("marked") != null)
                node.setAttribute("ui.class", "marked");
        }

        StoreGraph.getGraph().getNode(currentNode).setAttribute("ui.class", "marked");
        StoreGraph.getGraph().getNode(currentNode).setAttribute("marked", "marked");

        if (pastNodes.isEmpty() != true) {
            StoreGraph.getGraph().getEdge(pastNodes.get(pastNodes.size() - 1) + " " + currentNode)
                    .setAttribute("ui.class", "marked");
        }

        for (Node n : StoreGraph.getAdjacency(StoreGraph.getGraph().getNode(currentNode))) {// node 2 chieu cung co the
                                                                                            // dekiru
            if (StoreGraph.getAdjacency(n).contains(StoreGraph.MainGraph.getNode(currentNode))) {
                StoreGraph.getGraph().getNode(n.getId()).setAttribute("ui.class", "dekiru");
                // countDekiru++;
            }
        }

        for (ArrayList<Node> arrayList : PathLists) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getId().equals(currentNode)) {
                    if (i + 1 < arrayList.size()) {
                        StoreGraph.getGraph().getNode(arrayList.get(i + 1).getId()).setAttribute("ui.class", "dekiru");
                    }
                }

            }
        }
        // countDekiru = 0;
        // List<Node> listNodes =
        // StoreGraph.getAdjacency(StoreGraph.getGraph().getNode(currentNode));
        // for (Node node : listNodes) {
        // if (node.getAttribute("ui.class") != null &&
        // node.getAttribute("ui.class").toString().equals("dekiru")) {
        // countDekiru++;
        // }
        // }
        pastNodes.add(currentNode);
        if (currentNode.equals(destination)) {
            return currentNode + "   DONE!!";
        }

        return currentNode;
    }

    public static void stopFind() {
        isFinding = false;
        pastNodes = null;
        // countDekiru = 0;
        for (Node node : StoreGraph.getGraph()) {
            if (node.getAttribute("ui.class") != null)
                node.removeAttribute("ui.class");
            if (node.getAttribute("marked") != null)
                node.removeAttribute("marked");
        }

        for (int i = 0; i < StoreGraph.getGraph().getEdgeCount(); i++) {
            if (StoreGraph.getGraph().getEdge(i).getAttribute("ui.class") != null)
                StoreGraph.getGraph().getEdge(i).removeAttribute("ui.class");
        }
        System.gc();
    }
}
