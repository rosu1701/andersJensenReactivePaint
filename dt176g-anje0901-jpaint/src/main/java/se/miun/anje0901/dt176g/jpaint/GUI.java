package se.miun.anje0901.dt176g.jpaint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;

/**
 * Provides all GUI functionality for the program.
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2018-12-08
 */
class GUI {
    private JFrame jframe = new JFrame();
    private JLabel statusLabel = new JLabel("Status: disconnected");
    private Color currentColor = Color.GREEN;
    private JPanel topPanel = new JPanel();
    private DrawingPanel drawingArea = new DrawingPanel();
    private JPanel statusPanel = new JPanel();
    private JComboBox<String> thicknessSelect;
    private JComboBox<String> shapeSelect;
    private JButton joinButton;
    private JButton clearButton = new JButton("Clear");
    private boolean connected = false;

    private Client client = new Client();
    private String clientId;
    private long shapeNo = 0;

    private Observable<DrawingEvent> drawingEvent;
    private Observable<DrawingEvent> commandEvent;
    private Disposable localDrawingEventDisposable;
    private Disposable localCommandEventDisposable;
    private Disposable netDrawingEventDisposable;

    GUI() {
        clientId = UUID.randomUUID().toString();

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(true);
        jframe.setPreferredSize(new Dimension(600,480));
        jframe.setLayout(new BorderLayout());
        jframe.setTitle("Reactive JPaint");

        ImageIcon img = new ImageIcon("icon32.png");
        jframe.setIconImage(img.getImage());

        setupToolbar();
        setupDrawingArea();
        setupStatusPanel();

        jframe.add(topPanel, BorderLayout.PAGE_START);
        jframe.add(drawingArea, BorderLayout.CENTER);
        jframe.add(statusPanel, BorderLayout.PAGE_END);
    }

    private void setupClient() {
        if (!connected) {
            System.out.println("Setting up client");
            Kryo kryo = client.getKryo();
            kryo.register(DrawingEvent.class);
            client.start();

            drawingEvent.subscribe(e -> client.sendTCP(e));
            commandEvent.subscribe(e -> client.sendTCP(e));

            Observable<DrawingEvent> incomingObservable = Observable.create(e -> client.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    if (object instanceof DrawingEvent) {
                        e.onNext((DrawingEvent) object);
                    }
                }
            }));
            netDrawingEventDisposable = incomingObservable.subscribe(this::handleDrawingEvent);

            Observable<Connection> disconnectedObservable = Observable.create(e -> client.addListener(new Listener() {
                @Override
                public void disconnected(Connection connection) {
                    super.disconnected(connection);
                }
            }));
            disconnectedObservable.subscribe(this::disconnect);
            
            localDrawingEventDisposable.dispose();
            localCommandEventDisposable.dispose();
            try {
                client.connect(5000, "localhost", 23999);
                connected = true;
                System.out.println("Connected to " + client.getRemoteAddressTCP());
                drawingArea.getDrawing().clear();
                drawingArea.repaint();
                joinButton.setText("Leave");
                statusLabel.setText("Status: connected to " + client.getRemoteAddressTCP());
            } catch (IOException e) {
                System.err.println("Connection to server failed: " + e.getMessage());
                disconnect();
            }
        } else {
            disconnect();
        }
    }

    private void disconnect(Connection connection) {
        disconnect();
    }

    private void disconnect() {
        netDrawingEventDisposable.dispose();
        client.stop();
        connected = false;
        setupDrawingArea();
        joinButton.setText("Join");
        statusLabel.setText("Status: disconnected");
    }

    private void setupDrawingArea() {
        Observable<MouseEvent> mouseEventObservable = Observable.create(subscriber -> {
            drawingArea.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    subscriber.onNext(e);
                }
            });

            drawingArea.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    subscriber.onNext(e);
                }
            });
        });

        drawingEvent = mouseEventObservable.map(this::handleMouseEvent);
        localDrawingEventDisposable = drawingEvent.subscribe(this::handleDrawingEvent);

        Observable<String> clearButtonObservable = Observable.create(e -> clearButton.addActionListener(event -> e.onNext("CLEAR")));
        commandEvent = clearButtonObservable.map(this::handleCommand);
        localCommandEventDisposable = commandEvent.subscribe(this::handleDrawingEvent);
    }

    private DrawingEvent handleMouseEvent(MouseEvent e) {
        DrawingEvent de = new DrawingEvent();
        de.x = e.getX();
        de.y = e.getY();
        de.shape = shapeSelect.getSelectedItem().toString();

        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            de.eventType = "NEW";
            ++shapeNo;
            de.id = clientId + "-" + shapeNo;
            de.color = "#" + Integer.toHexString(currentColor.getRGB()).substring(2);
            de.thickness = thicknessSelect.getSelectedIndex() + 1;
        } else if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
            de.eventType = "UPDATE";
            de.id = clientId + "-" + shapeNo;
        }
        return de;
    }

    private void handleDrawingEvent(DrawingEvent de) {
        if (de.eventType.equals("NEW")) {
            switch (de.shape) {
                case "Rectangle":
                    drawingArea.getDrawing().addShape(de.id, new Rectangle(new Point(de.x, de.y), de.color, de.thickness));
                    break;
                case "Circle":
                    drawingArea.getDrawing().addShape(de.id, new Circle(new Point(de.x, de.y), de.color, de.thickness));
                    break;
                case "Line":
                    drawingArea.getDrawing().addShape(de.id, new Line(new Point(de.x, de.y), de.color, de.thickness));
                    break;
                case "Freehand":
                    drawingArea.getDrawing().addShape(de.id, new Freehand(new Point(de.x, de.y), de.color, de.thickness));
                    break;
            }
        } else if (de.eventType.equals("UPDATE")) {
            Shape shape = drawingArea.getDrawing().getShapeById(de.id);
            if (shape != null) {
                if (de.shape.equals("Freehand")) {
                    shape.addPoint(new Point(de.x, de.y));
                } else {
                    shape.addEndPoint(new Point(de.x, de.y));
                }
            }
        } else if (de.eventType.equals("CLEAR")) {
            drawingArea.getDrawing().clear();
        }
        drawingArea.repaint();
    }

    private DrawingEvent handleCommand(String s) {
        DrawingEvent de = new DrawingEvent();
        de.eventType = s;
        return de;
    }

    private void setupToolbar() {
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(1, 5));

        List<Color> colorList = new ArrayList<>(Arrays.asList(Color.GREEN, Color.BLUE, Color.BLACK, Color.WHITE, Color.RED, Color.YELLOW));
        // For each color, make a new JPanel and add a listener to it, and then add it to the color panel
        for (Color color : colorList) {
            JPanel newPanel = new JPanel();
            newPanel.setBackground(color);

            Observable<MouseEvent> colorButtonObservable = Observable.create(e -> newPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    e.onNext(event);
                }
            }));
            colorButtonObservable.subscribe(e -> currentColor = color);
            colorPanel.add(newPanel);
        }

        joinButton = new JButton("Join");
        Observable<ActionEvent> joinButtonObservable = Observable.create(e -> joinButton.addActionListener(e::onNext));
        joinButtonObservable.subscribe(e -> setupClient());

        String[] thicknessChoices = {"Thickness: 1", "Thickness: 2", "Thickness: 3", "Thickness: 4", "Thickness: 5"};
        thicknessSelect = new JComboBox<>(thicknessChoices);
        thicknessSelect.setPrototypeDisplayValue("Thickness: 5");
        thicknessSelect.setMaximumSize(thicknessSelect.getPreferredSize());

        String[] shapeChoices = {"Freehand", "Line", "Rectangle", "Circle"};
        shapeSelect = new JComboBox<>(shapeChoices);
        shapeSelect.setPrototypeDisplayValue("Rectangle"); // Sets a value used to calculate the size of the box
        shapeSelect.setMaximumSize(shapeSelect.getPreferredSize());

        topPanel.add(colorPanel);
        topPanel.add(joinButton);
        topPanel.add(clearButton);
        topPanel.add(thicknessSelect);
        topPanel.add(shapeSelect);
    }

    /**
     * Sets up the bottom status panel
     */
    private void setupStatusPanel() {
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.LINE_AXIS));
        statusPanel.add(statusLabel);
    }

    /**
     * Sets visibility of the frame
     * @param visible If true, make frame visible
     */
    void setVisible(boolean visible) {
        jframe.setVisible(visible);
    }
}
