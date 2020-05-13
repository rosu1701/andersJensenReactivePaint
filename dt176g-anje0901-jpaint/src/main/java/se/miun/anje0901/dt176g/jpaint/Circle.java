package se.miun.anje0901.dt176g.jpaint;

import java.awt.*;

/**
 * Represents a circle that's drawable, and for which various properties can be queried.
 * Start (first) point is the center of the circle; end point is its perimeter.
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2018-11-09
 */
public class Circle extends Shape implements Drawable {
    Circle(Point p, String color, int thickness) {
        super(p, color, thickness);
    }

    /**
     * Gets radius of circle, i.e., the distance between the two points:
     * the square root of (x2-x1)² + (y2-y1)².
     * @return radius of circle if end point exists, otherwise throw exception
     */
    private double getRadius() throws MissingPointException {
        // Checks whether the second point exists. Could also use try/catch list.get(1) but that'd
        // supposedly be much slower.
        if (points.size() > 1) {
            return Math.hypot((points.get(0).getX() - points.get(1).getX()), (points.get(0).getY() - points.get(1).getY()));
        } else {
            throw new MissingPointException("The radius cannot be calculated, end point is missing!");
        }
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
        try {
            if (points.size() > 1) {
                int topLeftX = (int) points.get(0).getX() - (int) getRadius();
                int topLeftY = (int) points.get(0).getY() - (int) getRadius();
                int width = (int) getRadius() * 2;
                g2.drawOval(topLeftX, topLeftY, width, width);
            }
        } catch (MissingPointException e) {
            System.err.println("Can't draw shape, point missing.");
        }
    }
}
