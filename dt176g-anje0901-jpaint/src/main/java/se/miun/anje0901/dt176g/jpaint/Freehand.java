package se.miun.anje0901.dt176g.jpaint;

import java.awt.*;

/**
 * Represents a set of freely drawn points
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2019-12-01
 */
public class Freehand extends Shape implements Drawable {
    Freehand(Point p, String color, int thickness) {
        super(p, color, thickness);
        addPoint(p);
    }

    /**
     * Draws the shape
     * @param g a Graphics object
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        // Parent class sets color
        super.draw(g2);
        // Turn on anti-aliasing, if possible
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 1; i < getPoints().size(); ++i) {
            Point p1 = getPoints().get(i - 1);
            Point p2 = getPoints().get(i);
            g.drawLine((int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
        }
    }
}
