package ee.taltech.game.Server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import packets.Packet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameServer {

    private final Server server;
    Map<Integer, Packet> recentPackets = new HashMap<>();

    public GameServer() {
        server = new Server();

        // Start server
        server.start();

        // Register packets
        server.getKryo().register(Packet.class);

        // Connect to ports
        try {
            server.bind(8080, 8081);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Listen for incoming packets from clients
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Packet packet) {
                    // Send packet to all clients except one who sent it to server
                    packet.setId(connection.getID());
                    System.out.println(connection.getID());
                    // Check if new connection
                    if (!recentPackets.containsKey(connection.getID())) {
                        for (Packet pack : recentPackets.values()) {
                            server.sendToUDP(connection.getID(), pack);
                        }
                    }
                    // Add recent packet to hashmap
                    recentPackets.put(connection.getID(), packet);
                    server.sendToAllExceptUDP(connection.getID(), packet);
                }
            }
            public void disconnected(Connection connection) {
                // Remove disconnected client from the map
                recentPackets.remove(connection.getID());
            }
        });
    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
    }
}
