package backEnd;

import java.util.ArrayList;

import org.graphstream.graph.Node;

import frontEnd.VerticalToolbar;

public class FindAction {
    public static ArrayList<ArrayList<String>> PathLists;
    public static boolean isFinding;
    private static int step = 0;

    private static String pastNode;
    private static String destination;

    public static void setDestination(String string) {
        destination = string;
    }

    public static void findNext(String currentNode) {
        if (PathLists.size() == 0) {
            System.out.println("ERR");
            VerticalToolbar.ShowError("Can not reach goal with this way");
            return;
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

        if (pastNode != null) {

            StoreGraph.getGraph().getEdge(pastNode + currentNode).setAttribute("ui.class", "marked");
        }

        if (currentNode.equals(destination))
            return;

        step++;
        // for (ArrayList<String> arrayList : PathLists) {
        //     if (step < arrayList.size() && arrayList.get(step - 1).equals(currentNode)) {
        //         StoreGraph.getGraph().getNode(arrayList.get(step)).setAttribute("ui.class", "dekiru");
        //     }
        // }
        
        for(Node n : StoreGraph.getAdjacency(currentNode)) {// node 2 chieu cung co the dekiru
            if(StoreGraph.getAdjacency(n).contains(StoreGraph.MainGraph.getNode(currentNode)))
            StoreGraph.getGraph().getNode(n.getId()).setAttribute("ui.class", "dekiru");
            
        }

  
        for (ArrayList<String> arrayList : PathLists) {
            for(int i = 0; i < arrayList.size(); i++){
                if(arrayList.get(i) == currentNode) {
                    int k = i+1;
                    StoreGraph.
                    getGraph().getNode(arrayList.get(k)).setAttribute("ui.class", "dekiru");
                }
            }
        }

        pastNode = currentNode;

    }

    public static void stopFind() {
        isFinding = false;
        step = 0;
        pastNode = null;
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
    }
}
