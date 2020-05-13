package se.miun.anje0901.dt176g.jpaint;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a panel that can be drawn on, with a Drawing used to
 * contain shapes.
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2018-12-19
 */
public class DrawingPanel extends JPanel {
    protected Drawing drawing;

    DrawingPanel() {
        this(new Drawing());
    }

    private DrawingPanel(Drawing drawing) {
        this.drawing = drawing;
        setBackground(Color.WHITE);
        repaint();
    }

    /**
     * Gets current drawing
     * @return current drawing
     */
    Drawing getDrawing() {
        return drawing;
    }

    /**
     * Makes sure all shapes are drawn
     * @param g Graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (drawing != null) {
            super.paintComponent(g);
            drawing.draw(g);
        }
    }
}
