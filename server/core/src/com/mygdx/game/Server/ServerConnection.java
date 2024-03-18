package com.mygdx.game.Server;


import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Characters.Enemy;
import com.mygdx.game.Characters.GameCharacter;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.World.World;
import packets.*;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class ServerConnection {

	static com.esotericsoftware.kryonet.Server server;
	static final int udpPort = 5007, tcpPort = 5008;
	private World serverWorld;
	private ServerUpdateThread serverUpdateThread;

	private float playerGameCharacterX = 280f;
	private float playerGameCharacterY = 250f;
	private int playerCount = 0;
	int x = 0;

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
		server.getKryo().register(PacketClientDisconnect.class);
		server.getKryo().register(PacketNewEnemy.class);
		server.getKryo().register(PacketUpdateEnemy.class);
		server.getKryo().register(GameCharacter.State.class);

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

					// Send connected player position to other players
					sendUpdatedGameCharacter(connection.getID(), playerGameCharacterX, playerGameCharacterY, GameCharacter.State.IDLE, true);

					// Send other players positions to joined player
					for (Map.Entry<Integer, PlayerGameCharacter> entry : serverWorld.getClients().entrySet()) {
						if (entry.getKey() != connection.getID()) {
							PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(entry.getKey(), entry.getValue().xPosition, entry.getValue().yPosition);
							server.sendToUDP(connection.getID(), packet);
						}
					}

					// Send enemies info to joined player
					for (Map.Entry<String, Enemy> entry : serverWorld.getEnemyMap().entrySet()) {
						addEnemyToClientsGame(entry.getKey(), entry.getValue().getxPosition(), entry.getValue().getyPosition(), connection.getID());
					}

				} else if (object instanceof PacketUpdateCharacterInformation) {
					// Packet for updating player position
					PacketUpdateCharacterInformation packet = (PacketUpdateCharacterInformation) object;
					// Update PlayerGameCharacter's coordinates.
					serverWorld.getClients().get(connection.getID()).xPosition = packet.getX();
					serverWorld.getClients().get(connection.getID()).yPosition = packet.getY();
					System.out.println("got state: " + packet.getCurrentState());
					// Send PlayerGameCharacter's new coordinate and direction to all connections.
					sendUpdatedGameCharacter(connection.getID(), packet.getX(), packet.getY(), packet.getCurrentState(), packet.getFacingRight());

				} else if (object instanceof PacketUpdateEnemy) {
					// Packet for updating enemy position
					PacketUpdateEnemy packetUpdateEnemy = (PacketUpdateEnemy) object;
					serverWorld.getEnemyMap().get(packetUpdateEnemy.getBotHash()).xPosition = packetUpdateEnemy.getxPosition();
					serverWorld.getEnemyMap().get(packetUpdateEnemy.getBotHash()).yPosition = packetUpdateEnemy.getyPosition();
				}
			}

			// Client disconnects from the Server.
			public void disconnected (Connection c) {
				PacketClientDisconnect packetClientDisconnect = PacketCreator.createPacketClientDisconnect(c.getID());
				System.out.println("Client " + c.getID() + " disconnected.");
				// Remove client from the game.
				serverWorld.removeClient(c.getID());
				// Send to other connections that client has disconnected from the game.
				server.sendToAllExceptTCP(c.getID(), packetClientDisconnect);
			}
		});

		System.out.println("Server is on!");

		// Start ServerUpdateThread.
		serverUpdateThread = new ServerUpdateThread();
		serverUpdateThread.setServerConnection(this);
		serverUpdateThread.setServerWorld(serverWorld);
		new Thread(serverUpdateThread).start();
		System.out.println("Thread is on!");

		addEnemyToGame(100, 35, serverWorld);
		addEnemyToGame(150, 35, serverWorld);
		addEnemyToGame(200, 35, serverWorld);
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
	public void sendUpdatedGameCharacter(int Id, float xPos, float yPos, GameCharacter.State state, boolean isFacingRight) {
		PlayerGameCharacter character = serverWorld.getGameCharacter(Id);
		character.xPosition = xPos;
		character.yPosition = yPos;
		// Send updated PlayerGameCharacter's info to all connections.
		System.out.println("sent " + xPos);
		PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(Id, xPos, yPos);
		packet.setCurrentState(state);
		packet.setFacingRight(isFacingRight);
		server.sendToAllExceptUDP(Id, packet);
	}

	/**
	 * Add enemy to server world.
	 * @param xPosition x coordinate.
	 * @param yPosition y coordinate.
	 * @param world server world.
	 */
	public void addEnemyToGame(float xPosition, float yPosition, World world) {
		Enemy enemy = Enemy.createEnemy(xPosition, yPosition, world);
		world.addEnemy(enemy.getBotHash(), enemy);
	}

	/**
	 * Add enemy to client's game.
	 * @param botHash bot hash.
	 * @param xPosition x coordinate.
	 * @param yPosition y coordinate.
	 * @param connectionId player connection id.
	 */
	public void addEnemyToClientsGame(String botHash, float xPosition, float yPosition, Integer connectionId) {
		PacketNewEnemy packetNewEnemy = PacketCreator.createPacketNewZombies(botHash, xPosition, yPosition);
		server.sendToTCP(connectionId, packetNewEnemy);
	}

	/**
	 * Method for sending serverWorld's updated Enemy instances info to all connections.
	 */
	public void sendUpdatedEnemies() {
		serverWorld.updateEnemyInTheWorldEnemyMap();
		// Enemy instance id (key) and new coordinates (value) are sent.
		for (Map.Entry<String, Enemy> entry : serverWorld.getEnemyMap().entrySet()) {
			PacketUpdateEnemy packetUpdateEnemy = PacketCreator.createPacketUpdateZombies(entry.getKey(), entry.getValue().getxPosition(), entry.getValue().getyPosition());
			server.sendToAllUDP(packetUpdateEnemy);
		}
	}

	/**
	 * Add enemy to server world.
	 * @param xPosition x coordinate.
	 * @param yPosition y coordinate.
	 * @param world server world.
	 */
	public void addEnemyToGame(float xPosition, float yPosition, World world) {
		Enemy enemy = Enemy.createEnemy(xPosition, yPosition, world);
		world.addEnemy(enemy.getBotHash(), enemy);
	}

	/**
	 * Add enemy to client's game.
	 * @param botHash bot hash.
	 * @param xPosition x coordinate.
	 * @param yPosition y coordinate.
	 * @param connectionId player connection id.
	 */
	public void addEnemyToClientsGame(String botHash, float xPosition, float yPosition, Integer connectionId) {
		PacketNewEnemy packetNewEnemy = PacketCreator.createPacketNewZombies(botHash, xPosition, yPosition);
		server.sendToTCP(connectionId, packetNewEnemy);
	}

	/**
	 * Method for sending serverWorld's updated Enemy instances info to all connections.
	 */
	public void sendUpdatedEnemies() {
		serverWorld.updateEnemyInTheWorldEnemyMap();
		// Enemy instance id (key) and new coordinates (value) are sent.
		for (Map.Entry<String, Enemy> entry : serverWorld.getEnemyMap().entrySet()) {
			PacketUpdateEnemy packetUpdateEnemy = PacketCreator.createPacketUpdateZombies(entry.getKey(), entry.getValue().getxPosition(), entry.getValue().getyPosition());
			server.sendToAllUDP(packetUpdateEnemy);
		}
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
