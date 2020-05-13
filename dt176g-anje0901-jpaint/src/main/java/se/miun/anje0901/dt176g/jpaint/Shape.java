package se.miun.anje0901.dt176g.jpaint;

import java.awt.*;
import java.util.ArrayList;

/**
 * Abstract class that shapes of different kinds can inherit. Each shape
 * will have an array of Points and an associated color.
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2018-11-09
 */
public abstract class Shape implements Drawable {
    protected String color;
    private int thickness;

    ArrayList<Point> points = new ArrayList<>();

    Shape(Point p, String color, int thickness) {
        this.points.add(p);
        this.color = color;
        this.thickness = thickness;
    }

    /**
     * Gets current color.
     * @return color of points
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets a new color.
     * @param color string value of color, e.g., #ff0000
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets current points.
     * @return points
     */
    ArrayList<Point> getPoints() {
        return points;
    }

    /**
     * Adds an additional x,y point to the shape. Max 1 additional point.
     * @param point point object to add
     */
    void addEndPoint(Point point) {
        // index specified to comply with previous assignment requirements
        this.points.add(1, point);
    }

    void addPoint(Point point) {
        this.points.add(point);
    }

    /**
     * Sets the color of the shape that a subclass will draw
     * @param g a Graphics object
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        // Make sure color string starts with a '#' so that
        // Color.decode doesn't cause an exception
        String colorToConvert;
        if (color.startsWith("#")) {
            colorToConvert = color;
        } else {
            colorToConvert = "#" + color;
        }
        // Set color, decoded from hexadecimal value
        g2.setPaint(Color.decode(colorToConvert));

        g2.setStroke(new BasicStroke(thickness));
    }
}
