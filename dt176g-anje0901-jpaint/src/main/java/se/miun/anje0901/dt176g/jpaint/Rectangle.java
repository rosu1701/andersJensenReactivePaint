package se.miun.anje0901.dt176g.jpaint;

import java.awt.*;

/**
 * Represents a rectangle that's drawable, and for which various properties can be queried.
 * Start (first) point is the upper left corner; end point is the bottom right corner.
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2018-11-09
 */
public class Rectangle extends Shape {
    Rectangle(Point p, String color, int thickness) {
        super(p, color, thickness);
    }

    /**
     * Gets width of rectangle (absolute value of difference between x1 and x2).
     * @return width of rectangle, if end point exists, otherwise throw exception
     */
    private double getWidth() throws MissingPointException {
        if (points.size() > 1) {
            return Math.abs(points.get(1).getX() - points.get(0).getX());
        } else {
            throw new MissingPointException("The width cannot be calculated, end point is missing!");
        }
    }

    /**
     * Gets height of rectangle (absolute value of difference between y1 and y2).
     * @return height of rectangle, if end point exists, otherwise throw exception
     */
    private double getHeight() throws MissingPointException {
        if (points.size() > 1) {
            return Math.abs(points.get(0).getY() - points.get(1).getY());
        } else {
            throw new MissingPointException("The height cannot be calculated, end point is missing!");
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

        try {
            if (points.size() > 1) {
                int rX = Math.min((int) points.get(0).getX(), (int) points.get(1).getX());
                int rY = Math.min((int) points.get(0).getY(), (int) points.get(1).getY());
                g2.drawRect(rX, rY, (int) getWidth(), (int) getHeight());
            }
        } catch (MissingPointException e) {
            System.err.println("Can't draw shape, point missing.");
        }
    }
}
