package com.mygdx.game.Server;


import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Characters.GameCharacter;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.World.World;
import packets.*;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


public class ServerConnection {

	static com.esotericsoftware.kryonet.Server server;
	static final int udpPort = 5007, tcpPort = 5008;
	private World serverWorld;

	private float playerGameCharacterX = 280f;
	private float playerGameCharacterY = 250f;
	private int playerCount = 0;

	/**
	 * Server connection.
	 */
	public ServerConnection()  {
		try {
			server = new Server();
			server.start();
			server.bind(tcpPort, udpPort);

			// Starts the game (create a new World instance for the game).
			this.serverWorld = new World();

		} catch (IOException exception) {
			JOptionPane.showMessageDialog(null, "Can not start the Server.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Register all packets that are sent over the network.
		server.getKryo().register(Packet.class);
		server.getKryo().register(PacketConnect.class);
		server.getKryo().register(PacketAddCharacter.class);
		server.getKryo().register(GameCharacter.class);
		server.getKryo().register(PacketUpdateCharacterInformation.class);
		server.getKryo().register(PacketCreator.class);
		server.getKryo().register(ArrayList.class);
		server.getKryo().register(Rectangle.class);
		server.getKryo().register(HashMap.class);

		// Add listener to handle receiving objects.
		server.addListener(new Listener() {

			// Receive packets from clients.
			public void received(Connection connection, Object object){
				if (object instanceof PacketConnect) {
					PacketConnect packetConnect = (PacketConnect) object;

					// Creates new PlayerGameCharacter instance for the connection.
					PlayerGameCharacter newPlayerGameCharacter = PlayerGameCharacter
							.createPlayerGameCharacter(playerGameCharacterX, playerGameCharacterY, serverWorld, connection.getID());

					// Add new PlayerGameCharacter instance to all connections.
					addCharacterToClientsGame(connection, newPlayerGameCharacter);

				} else if (object instanceof PacketUpdateCharacterInformation) {
					System.out.println("got packet update");
					PacketUpdateCharacterInformation packet = (PacketUpdateCharacterInformation) object;
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
		PlayerGameCharacter character = serverWorld.getGameCharacter(Id);
		System.out.println(character);
		// Send updated PlayerGameCharacter's info to all connections.
		System.out.println("sent " + xPos);
		PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(Id, xPos, yPos);
		server.sendToAllExceptUDP(Id, packet);
	}

	/**
	 * Reset Server variable playerGameCharacterX.
	 */
	public void restartServer() {
		playerGameCharacterX = 280f;
	}

	public World getServerWorld() {
		return serverWorld;
	}

	public static void main(String[] args) {
		// Runs the main application.
		new ServerConnection();
	}
}
