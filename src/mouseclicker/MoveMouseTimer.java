package mouseclicker;

import java.awt.*;
import java.util.TimerTask;

public class MoveMouseTimer extends TimerTask {
    public void run() {
        mouseMove(new Point(0,0));
    }

    private void mouseMove(Point p) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        for (GraphicsDevice device: gs) {
            GraphicsConfiguration[] configurations = device.getConfigurations();

            for (GraphicsConfiguration config: configurations) {
                Rectangle bounds = config.getBounds();
                if(bounds.contains(p)) {
                    Point b = bounds.getLocation();
                    Point s = new Point(p.x - b.x, p.y - b.y);

                    try {
                        Robot r = new Robot(device);
                        r.mouseMove(s.x, s.y);
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
    }
}