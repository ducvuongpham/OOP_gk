package App;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import backEnd.FindAction;
import backEnd.StoreGraph;
import backEnd.ZoomGraph;
import frontEnd.AppUI;

import java.awt.*;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;

public class App implements ViewerListener {
    protected boolean loop = true;
    JFrame frame;
    public JPanel mainpanel;
    JPanel body;
    public static JPanel waysPath;
    public static JTextArea showWaysPath;
    public static ViewPanel viewPanel;
    ViewerPipe fromViewer;

    public App() {
    }

    public static void main(String args[]) {
        StoreGraph.create("graph.txt");
        new App(StoreGraph.getGraph());
    }

    public App(Graph graph) {
        mainpanel = new JPanel();
        body = new JPanel();
        waysPath = new JPanel();
        showWaysPath = new JTextArea();
        frame = new AppUI(this);

        loadGraph(graph);
        while (loop) {
            fromViewer.pump();
        }
    }

    public void loadGraph(Graph graph) {
        // create body
        body.setLayout(new java.awt.BorderLayout());
        body.add(mainpanel, BorderLayout.CENTER);
        mainpanel.setBorder(
                javax.swing.BorderFactory.createTitledBorder(null, "Graph", javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.TOP, new java.awt.Font("Times New Roman", 1, 14)));
        JScrollPane scroll = new JScrollPane(showWaysPath, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        waysPath.setBorder(
                javax.swing.BorderFactory.createTitledBorder(null, "Ways", javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.TOP, new java.awt.Font("Times New Roman", 1, 14)));
        waysPath.setPreferredSize(new java.awt.Dimension(0, 155));
        waysPath.setLayout(new BorderLayout());
        waysPath.add(scroll, BorderLayout.CENTER);
        showWaysPath.setColumns(0);
        showWaysPath.setRows(0);
        showWaysPath.setMargin(new Insets(2, 2, 2, 2));
        showWaysPath.setFont(new java.awt.Font("Times New Roman", 0, 14));
        showWaysPath.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret) showWaysPath.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        // waysPath.setViewportView(showWaysPath);
        // showWaysPath.setPreferredSize(new java.awt.Dimension(0, 155));

        body.add(waysPath, BorderLayout.SOUTH);

        if (viewPanel != null)
            mainpanel.remove(viewPanel);
        Viewer viewer;
        viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();

        viewPanel = (ViewPanel) viewer.addDefaultView(false);

        viewPanel.addMouseWheelListener(new MouseWheelListener() {// add mouseWheelListener here
            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                ZoomGraph.zoomGraphMouseWheelMoved(mwe, viewPanel);
            }
        });

        mainpanel.add(viewPanel);
        mainpanel.setLayout(new GridLayout());

        frame.add(body);

        fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
        fromViewer.addSink(graph);
        frame.revalidate();
    }

    public void viewClosed(String id) {
        loop = false;
    }

    // From here is about mouse click to Node
    java.awt.Point mouseLocation;

    public void buttonPushed(String id) {
        if (FindAction.isFinding == false)
            return;
        mouseLocation = MouseInfo.getPointerInfo().getLocation();
    }

    public void buttonReleased(String id) {
        if (MouseInfo.getPointerInfo().getLocation().equals(mouseLocation))
            if (StoreGraph.getGraph().getNode(id).getAttribute("ui.class") != null)
                if (StoreGraph.getGraph().getNode(id).getAttribute("ui.class").toString().equals("dekiru")) {
                    showWaysPath.append(FindAction.findNext(id) + "\n");
                }
    }

    public void mouseOver(String id) {
        System.out.println("Need the Mouse Options to be activated");
    }

    public void mouseLeft(String id) {
        System.out.println("Need the Mouse Options to be activated");
    }

}