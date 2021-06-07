package backEnd;

import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.camera.Camera;

import java.awt.event.MouseWheelEvent;

public class ZoomGraph {

    public ZoomGraph() {
    }

    // the methode that will zoom the graph
    public static void zoomGraphMouseWheelMoved(MouseWheelEvent mwe, ViewPanel view_panel) {
        int i = mwe.getWheelRotation();
        double factor = Math.pow(1.25, i);
        Camera cam = view_panel.getCamera();
        double zoom = cam.getViewPercent() * factor;

        Point2 pxCenter = cam.transformGuToPx(cam.getViewCenter().x, cam.getViewCenter().y, 0);
        Point3 guClicked = cam.transformPxToGu(mwe.getX(), mwe.getY());
        double newRatioPx2Gu = cam.getMetrics().ratioPx2Gu / factor;
        double x = guClicked.x + (pxCenter.x - mwe.getX()) / newRatioPx2Gu;
        double y = guClicked.y - (pxCenter.y - mwe.getY()) / newRatioPx2Gu;
        cam.setViewCenter(x, y, 0);
        cam.setViewPercent(zoom);

        // if (Event.ALT_MASK != 0) {
        // if (mwe.getWheelRotation() > 0) {
        // double new_view_percent = view_panel.getCamera().getViewPercent() + 0.05;
        // cam.setViewPercent(new_view_percent);

        // } else if (mwe.getWheelRotation() < 0) {
        // double current_view_percent = view_panel.getCamera().getViewPercent();
        // if (current_view_percent > 0.05) {
        // cam.setViewPercent(current_view_percent - 0.05);
        // }

        // }
        // }
    }

}
