package se.miun.anje0901.dt176g.jpaint;

import java.awt.*;

/**
 * Represents a line that's drawable.
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2019-12-01
 */
public class Line extends Shape {
    Line(Point p, String color, int thickness) {
        super(p, color, thickness);
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

        if (getPoints().size() > 1) {
            Point p1 = getPoints().get(0);
            Point p2 = getPoints().get(1);
            g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
        }
    }
}
