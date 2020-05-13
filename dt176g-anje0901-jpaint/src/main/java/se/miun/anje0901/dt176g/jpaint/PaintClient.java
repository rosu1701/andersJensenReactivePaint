package se.miun.anje0901.dt176g.jpaint;

import javax.swing.SwingUtilities;

/**
 * Client for painting program
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2019-12-01
 */
public class PaintClient {
    public static void main(String[] args) {
        // Make sure GUI is created on the event dispatching thread
        // This will be explained in the lesson about threads
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }
}