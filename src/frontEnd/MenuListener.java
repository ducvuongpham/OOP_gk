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
            FindAllPath.printAllPaths(source, destination);

            FindAction.stopFind();
            FindAction.isFinding = true;
            FindAction.findNext(source);
            // System.out.println(FindAction.PathLists.toString());
            // App app = new App();
            App.showWaysPath.setText(FindAction.PathLists.toString());
            FindAction.setDestination(destination);
        } catch (Exception e) {
            // showMessageDialog.showMessage("Node not found");
            VerticalToolbar.deleteText();
        }

    }
}
