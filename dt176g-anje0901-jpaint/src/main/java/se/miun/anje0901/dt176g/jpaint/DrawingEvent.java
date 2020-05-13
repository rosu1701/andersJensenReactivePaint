package se.miun.anje0901.dt176g.jpaint;

/**
 * Represents individual drawing events (new drawing or update of existing drawing)
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2019-12-01
 */
public class DrawingEvent {
    String id;
    int x;
    int y;
    String color;
    int thickness;
    String shape;
    String eventType;

    DrawingEvent() {
    }

    public DrawingEvent(String id, int x, int y, String color, int thickness, String shape, String eventType) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.color = color;
        this.thickness = thickness;
        this.shape = shape;
        this.eventType = eventType;
    }
}
