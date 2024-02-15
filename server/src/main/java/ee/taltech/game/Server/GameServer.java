package ee.taltech.game.Server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import packets.Packet;

import java.io.IOException;

public class GameServer {

    private Server server;

    public GameServer() {
        server = new Server();
        server.start();

        server.getKryo().register(Packet.class);

        try {
            server.bind(8080, 8081);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Packet) {
                    Packet packet = (Packet) object;
                    packet.setId(connection.getID());
                    System.out.println(connection.getID());
                    server.sendToAllExceptUDP(packet.getId(), packet);
                }
            }
        });
    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
    }
}
