package ee.taltech.game.Server;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.math.Rectangle;
import ee.taltech.game.Characters.PlayerGameCharacter;
import ee.taltech.game.World.Headless;
import ee.taltech.game.World.World;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import ee.taltech.game.Characters.GameCharacter;
import packets.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameServer  extends ApplicationAdapter {

    private World serverWorld;
    private float playerGameCharacterX = 280f;
    private float playerGameCharacterY = 250f;
    private int playerCount = 0;

    private final Server server;

    private static final float INCREASE_X_COORDINATE = 30f;

    public GameServer() {

        server = new Server();

        // Start server
        server.start();

        // Register all packets that are sent over the network.
        server.getKryo().register(Packet.class);
        server.getKryo().register(PacketConnect.class);
        server.getKryo().register(PacketAddCharacter.class);
        server.getKryo().register(GameCharacter.class);
        server.getKryo().register(PacketMoveCharacter.class);
        server.getKryo().register(PacketCreator.class);
        server.getKryo().register(GameCharacter.class);
        server.getKryo().register(PlayerGameCharacter.class);

        // Connect to ports
        try {
            server.bind(8080, 8081);
            // Starts the game (create a new World instance for the game).
            this.serverWorld = new World();
            Headless.loadHeadless(serverWorld);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Add listener to handle receiving objects.
        server.addListener(new Listener() {

            // Receive packets from clients.
            public void received(Connection connection, Object object){
                if (object instanceof PacketConnect && serverWorld.getClients().size() < 3) {
                    System.out.println("PacketConnect arrived");
                    PacketConnect packetConnect = (PacketConnect) object;
                    playerCount += 1;

                    // Creates new PlayerGameCharacter instance for the connection.
                    PlayerGameCharacter newPlayerGameCharacter = PlayerGameCharacter
                            .createPlayerGameCharacter(playerGameCharacterX, playerGameCharacterY, serverWorld, connection.getID());

                    // Add new PlayerGameCharacter instance to all connections.
                    addCharacterToClientsGame(connection, newPlayerGameCharacter);
                    // Each PlayerGameCharacter instance has a different x coordinate.
                    if (playerCount <= 3) {
                        playerGameCharacterX += INCREASE_X_COORDINATE;
                    } else {
                        playerGameCharacterX = 280f;
                        playerCount = 0;
                    }

                } else if (object instanceof PacketMoveCharacter) {
                    System.out.println("PacketMoveCharacter arrived");
                    PacketMoveCharacter packet = (PacketMoveCharacter) object;
                    // Update PlayerGameCharacter's coordinates and direction.
                    // Send PlayerGameCharacter's new coordinate and direction to all connections.
                    sendUpdatedGameCharacter(connection.getID(), packet.getX(), packet.getY());

                }
            }
        });

        System.out.println("Server is on!");

    }

    /**
     * Method for sending new PlayerGameCharacter instance info to all connections and sending existing characters
     * to the new connection.
     *
     * @param newCharacterConnection new connection (Connection)
     * @param newPlayerGameCharacter new PlayerGameCharacter instance that was created for new connection (PlayerGameCharacter)
     */
    public void addCharacterToClientsGame(Connection newCharacterConnection, PlayerGameCharacter newPlayerGameCharacter) {
        // Add existing PlayerGameCharacter instances to new connection.
        List<PlayerGameCharacter> clientsValues = new ArrayList<>(serverWorld.getClients().values());
        for (PlayerGameCharacter character : clientsValues) {
            // Create a new packet for sending PlayerGameCharacter instance info.
            PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(character.getPlayerGameCharacterId(), character.getBoundingBox().getX(), character.getBoundingBox().getY());
            // Send packet only to new connection.
            server.sendToTCP(newCharacterConnection.getID(), addCharacter);
        }

        // Add new PlayerGameCharacter instance to Server's world.
        serverWorld.addGameCharacter(newCharacterConnection.getID(), newPlayerGameCharacter);

        // Add new PlayerGameCharacter instance to all connections.
        // Create a packet to send new PlayerGameCharacter's info.
        PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(newCharacterConnection.getID(), newPlayerGameCharacter.getBoundingBox().getX(), newPlayerGameCharacter.getBoundingBox().getY());
        server.sendToAllTCP(addCharacter);  // Send packet to all connections.
    }

    /**
     * Method for sending updated PlayerGameCharacter instance info to all connections.
     *
     * @param Id of the PlayerGameCharacter (int)
     * @param xPos new x coordinate of the PlayerGameCharacter (float)
     * @param yPos new y coordinate of the PlayerGameCharacter (float)
     */
    public void sendUpdatedGameCharacter(int Id, float xPos, float yPos) {
        serverWorld.movePlayerGameCharacter(Id, xPos, yPos);  // Update given PlayerGameCharacter.
        PlayerGameCharacter character = serverWorld.getGameCharacter(Id);
        System.out.println(character);
        // Send updated PlayerGameCharacter's info to all connections.
        PacketMoveCharacter packet = PacketCreator.createPacketUpdateCharacterInformation(Id, character.getBoundingBox().getX(), character.getBoundingBox().getY());
        server.sendToAllUDP(packet);
    }

    public static void main(String[] args) {
        // Runs the main application.
        new GameServer();
    }
}

