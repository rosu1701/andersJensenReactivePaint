package se.miun.anje0901.dt176g.jpaint;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import io.reactivex.Observable;
import reactor.core.publisher.Mono;

/**
 * Server for painting program
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2019-12-01
 */
public class PaintServer {
    private static final int PORT = 23999;

    private Server server = new Server();
    private List<DrawingEvent> allEvents = new ArrayList<>();

    private PaintServer() throws IOException {
        server.start();
        server.bind(PORT);
        System.out.println("Server started, listening on " + PORT);

        Kryo kryo = server.getKryo();
        kryo.register(DrawingEvent.class);

        // Incoming DrawingEvents
        Observable<DrawingEvent> incomingObservable = Observable.create(e -> server.addListener(new Listener() {
            @Override
            public void received (Connection connection, Object object) {
                if (object instanceof DrawingEvent) {
                    e.onNext((DrawingEvent)object);
                }
            }
        }));
        // If a client has pressed Clear then clear list of DrawingEvents
        incomingObservable.subscribe(e -> {
            if (e.eventType.equals("CLEAR")) {
                allEvents.clear();
            }
        });
        // Broadcast each incoming DrawingEvent to every client
        incomingObservable.subscribe(server::sendToAllTCP);
        // Add each incoming DrawingEvent to local history
        incomingObservable.subscribe(allEvents::add);

        // New connections
        Observable<Connection> newClientObservable = Observable.create(e -> server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                e.onNext(connection);
            }
        }));
        // When a client connects, send all previously recorded DrawingEvents
        newClientObservable.subscribe(c -> {
            System.out.println("Client " + c.getID() + " (" + c.getRemoteAddressTCP() + ") connected");
            for (DrawingEvent de : allEvents) {
                c.sendTCP(de);
            }
        });

        // Disconnections
        Observable<Connection> disconnectedClientObservable = Observable.create(e -> server.addListener(new Listener() {
            @Override
            public void disconnected(Connection connection) {
                e.onNext(connection);
            }
        }));
        disconnectedClientObservable.subscribe(c -> {
            System.out.println("Client " + c.getID() + " disconnected");
        });
    }

    public static void main(String[] args) throws IOException {
     /*   Mono.delay(Duration.ofSeconds(1))
                .doOnNext(it -> {
                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .block(); */
        System.out.println("Server starting");
        new PaintServer();
    }
}
