package se.miun.anje0901.dt176g.jpaint;

/**
 * Represents a point (x,y) in a coordinate system.
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2018-11-07
 */
public class Point {
    private double x;
    private double y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets x coordinate.
     * @return x coordinate of point
     */
    double getX() {
        return x;
    }

    /**
     * Gets y coordinate.
     * @return y coordinate of point
     */
    double getY() {
        return y;
    }

    /**
     * @return string formatted as "x, y", e.g.: 10, 5
     */
    @Override
    public String toString() {
        return x + ", " + y;
    }
}
