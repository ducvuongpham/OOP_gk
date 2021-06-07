package frontEnd;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.graphstream.graph.Node;

import App.App;
import backEnd.FindAction;
import backEnd.FindAllPath;
import backEnd.FindPossible;
import backEnd.StoreGraph;

class MenuListener implements ActionListener {
    public VerticalToolbar obj;
    public App app;
    // showMessageDialog messageDialog = new showMessageDialog();

    public MenuListener(VerticalToolbar obj, App app) {
        this.obj = obj;
        this.app = app;
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        switch (e.getActionCommand()) {
            case "open":
                open();
                break;

            case "saveText":
                try {
                    saveText();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                break;

            case "saveasText":
                saveasText();
                break;

            case "exportImg":
                try {
                    exportImg();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;

            case "exit":
                exit();
                break;

            case "setFont":
                setFont();
                break;

            case "setColor":
                setColor();
                break;

            case "help":
                help();
                break;

            case "about":
                about();
                break;

            case "run":
                run();
                break;
            case "undo":
                undo();
                break;
            case "redo":
                redo();
                break;
            case "reset":
                reset();
                break;
        }
    }

    private void open() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int result = fileChooser.showOpenDialog(fileChooser);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            StoreGraph.create(selectedFile.getAbsolutePath());

            if (StoreGraph.getGraph() == null)
                return;

            ((App) app).loadGraph(StoreGraph.getGraph());
            FindAction.stopFind();
        }

    }

    private void saveText() throws IOException {
    }

    private void saveasText() {

    }

    private void exportImg() throws IOException {

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        int result = fileChooser.showSaveDialog(fileChooser);
        System.out.println(fileChooser.getSelectedFile().getName());
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            BufferedImage image = new BufferedImage(((App) app).mainpanel.getWidth(), ((App) app).mainpanel.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            ((App) app).mainpanel.paint(g2);
            try {
                ImageIO.write(image, "png", new File(selectedFile.getAbsolutePath() + ".png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void exit() {
        System.exit(0);
    }

    private void setFont() {

    }

    private void setColor() {
        // StoreGraph.MainGraph.getNode("0").setAttribute("ui.class", "dekiru");
    }

    private void help() {

    }

    private void about() {

    }

    private void run() {
        try {
            String[] SD = VerticalToolbar.getSD();
            String source = SD[0];
            String destination = SD[1];

            if (source.length() == 0 && destination.length() == 0) {
                destination = StoreGraph.MainGraph.getNode(0).getId();
                source = destination;
                System.out.println(destination);
                for (Node node : StoreGraph.MainGraph) {
                    if (destination.compareToIgnoreCase(node.getId()) < 0)
                        destination = node.getId();
                    if (source.compareToIgnoreCase(node.getId()) > 0)
                        source = node.getId();
                }
            }
            FindAction.stopFind();
            FindAction.setDestination(destination);

            if (StoreGraph.getGraph().getEdgeCount() < 40 && StoreGraph.getGraph().getEdgeCount() < 50) {
                FindAllPath.printAllPaths(source, destination);
                App.showWaysPath.setText("All paths from " + source + " to " + destination + ":\n"
                        + FindAction.PathLists.toString() + "\n\nRoute:\n");
            } else {
                FindPossible.printPossible(StoreGraph.getGraph().getNode(destination));
                App.showWaysPath.setText("Cannot show all paths, navigating..." + "\n\nRoute:\n");
            }

            FindAction.isFinding = true;
            App.showWaysPath.append(FindAction.findNext(source) + "\n");

        } catch (Exception e) {
            // showMessageDialog.showMessage("Node not found");
            VerticalToolbar.deleteText();
        }

    }

    private void reset() {
        FindAction.stopFind();
        VerticalToolbar.sNodeText.setText("");
        VerticalToolbar.dNodeText.setText("");
        App.showWaysPath.setText("");
        App.viewPanel.getCamera().resetView();
    }

    private void redo() {
        if (FindAction.forRedo.size() > 0) {
            FindAction.findNext(FindAction.forRedo.get(FindAction.forRedo.size() - 1));
            FindAction.forRedo.remove(FindAction.forRedo.size() - 1);
        }
    }

    private void undo() {
        if (FindAction.pastNodes.size() <= 1)
            return;
        String currentNode = FindAction.pastNodes.get(FindAction.pastNodes.size() - 1);
        String lastNode = FindAction.pastNodes.get(FindAction.pastNodes.size() - 2);

        FindAction.forRedo.add(currentNode);

        FindAction.pastNodes.remove(FindAction.pastNodes.size() - 1);
        if (!FindAction.pastNodes.contains(currentNode)) {
            StoreGraph.getGraph().getNode(currentNode).removeAttribute("ui.class");
            StoreGraph.getGraph().getNode(currentNode).removeAttribute("marked");
        }

        // StoreGraph.getGraph().getEdge(lastNode + " " +
        // currentNode).removeAttribute("ui.class");

        int checkAlreadyHas = 0;
        for (int i = 0; i < FindAction.pastNodes.size() - 1; i++) {
            if (FindAction.pastNodes.get(i) == lastNode && FindAction.pastNodes.get(i + 1) == currentNode) {
                checkAlreadyHas++;
                break;
            }
        }
        if (checkAlreadyHas == 0)
            StoreGraph.getGraph().getEdge(lastNode + " " + currentNode).removeAttribute("ui.class");

        FindAction.pastNodes.remove(FindAction.pastNodes.size() - 1);
        FindAction.findNext(lastNode);
        String contents = App.showWaysPath.getText();
        String[] lines = contents.split("\\r?\\n");
        contents = "";
        for (int i = 0; i < lines.length - 1; i++) {
            contents = contents + lines[i] + "\n";
        }
        App.showWaysPath.setText(contents);
    }

}
