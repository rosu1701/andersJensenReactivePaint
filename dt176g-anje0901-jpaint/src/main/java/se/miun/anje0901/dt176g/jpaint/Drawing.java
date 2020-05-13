package se.miun.anje0901.dt176g.jpaint;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a drawing that can contain several shapes, and information about
 * the drawing's name and author.
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2018-11-09
 */
public class Drawing implements Drawable {
    private ConcurrentHashMap<String, Shape> shapes;

    Drawing() {
        this.shapes = new ConcurrentHashMap<>();
    }

    /**
     * Adds shape to drawing.
     * @param shape the shape to add
     */
    void addShape(String id, Shape shape) {
        if (shape != null) {
            //this.shapes.add(shape);
            this.shapes.put(id, shape);
        }
    }

    Shape getShapeById(String id) {
        return shapes.get(id);
    }

    /**
     * Draws each shape
     *
     * @param g a Graphics object
     */
    @Override
    public void draw(Graphics g) {
        for (Shape shape : shapes.values()) {
            shape.draw(g);
        }
    }

    /**
     * Removes all shapes and resets name and author
     */
    void clear() {
        shapes.clear();
    }
}
