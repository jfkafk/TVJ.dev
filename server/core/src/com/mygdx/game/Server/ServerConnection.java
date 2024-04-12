package com.mygdx.game.Server;


import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Characters.Enemy;
import com.mygdx.game.Characters.GameCharacter;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Lobbies.Lobby;
import com.mygdx.game.World.World;
import packets.*;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.*;

public class ServerConnection {

	static Server server;
	static final int udpPort = 8081, tcpPort = 8082;

	private ServerUpdateThread serverUpdateThread;

	private float playerGameCharacterX = 280f;
	private float playerGameCharacterY = 250f;
	private int playerCount = 0;

	Map<String, Lobby> availableLobbies = new LinkedHashMap<>();

	Map<String, Lobby> onGoingLobbies = new LinkedHashMap<>();

	/**
	 * Server connection.
	 */
	public ServerConnection()  {
		try {
			server = new Server();
			server.start();
			server.bind(tcpPort, udpPort);

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
		server.getKryo().register(PacketSendNewLobby.class);
		server.getKryo().register(PacketLobbyInfo.class);
		server.getKryo().register(PacketGetAvailableLobbies.class);
		server.getKryo().register(HashSet.class);
		server.getKryo().register(LinkedHashSet.class);
		server.getKryo().register(PacketRemoveLobby.class);

		// Add listener to handle receiving objects.
		server.addListener(new Listener() {

			// Receive packets from clients.
			public void received(Connection connection, Object object){
				if (object instanceof PacketConnect) {

					PacketConnect packetConnect = (PacketConnect) object;

					// Get lobby's server world
					World serverWorld = onGoingLobbies.get(packetConnect.getLobbyHash()).getServerWorld();

					// Creates new PlayerGameCharacter instance for the connection.
					PlayerGameCharacter newPlayerGameCharacter = PlayerGameCharacter
							.createPlayerGameCharacter(playerGameCharacterX, playerGameCharacterY, serverWorld, connection.getID());

					// Add new PlayerGameCharacter instance to all connections.
					addCharacterToClientsGame(connection, newPlayerGameCharacter, packetConnect.getLobbyHash());

					// Send connected player position to other players
					sendUpdatedGameCharacter(connection.getID(), playerGameCharacterX, playerGameCharacterY, GameCharacter.State.IDLE, true, packetConnect.getLobbyHash());

					// Send other players positions to joined player in lobby
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

					// Update PlayerGameCharacter's coordinates in lobby.
					onGoingLobbies.get(packet.getLobbyHash()).getServerWorld().getClients().get(connection.getID()).xPosition = packet.getX();
					onGoingLobbies.get(packet.getLobbyHash()).getServerWorld().getClients().get(connection.getID()).xPosition = packet.getX();

					// Send players new coordinates and direction to all players in lobby
					sendUpdatedGameCharacter(connection.getID(), packet.getX(), packet.getY(), packet.getCurrentState(), packet.getFacingRight(), packet.getLobbyHash());

				} else if (object instanceof PacketUpdateEnemy) {
					// Packet for updating enemy position
					PacketUpdateEnemy packetUpdateEnemy = (PacketUpdateEnemy) object;

				} else if (object instanceof PacketSendNewLobby) {
					// Packet for adding new lobby
					PacketSendNewLobby packetSendNewLobby = (PacketSendNewLobby) object;

					Lobby lobby = new Lobby(connection.getID());
					lobby.addPlayer(connection.getID());

					availableLobbies.put(lobby.getLobbyHash(), lobby);

					packetSendNewLobby.setLobbyHash(lobby.getLobbyHash());
					packetSendNewLobby.setCreatorId(connection.getID());

					server.sendToAllUDP(packetSendNewLobby);
					System.out.println(availableLobbies);

				} else if (object instanceof PacketGetAvailableLobbies) {
					sendAvailableLobbies(connection.getID());

				} else if (object instanceof PacketLobbyInfo) {
					PacketLobbyInfo packetLobbyInfo = (PacketLobbyInfo) object;

					// Check if host set lobby to started
					if (packetLobbyInfo.isStartGame()) {
						// Get lobby
						Lobby lobby = availableLobbies.get(packetLobbyInfo.getLobbyHash());
						// Set for each new ongoing lobby new server world
						lobby.setServerWorld(new World());
						// Set for each new ongoing lobby new server update thread
						serverUpdateThread = new ServerUpdateThread();
						serverUpdateThread.setServerConnection(getServerConnection());
						serverUpdateThread.setServerWorld(lobby.getServerWorld());
						new Thread(serverUpdateThread).start();
						lobby.setServerUpdateThread(serverUpdateThread);
						// Set lobbyHash to server thread
						lobby.getServerUpdateThread().setLobbyHash(packetLobbyInfo.getLobbyHash());
						// Send packet that lobby has started to all players except host that are in that lobby
						for (Integer playerId : availableLobbies.get(packetLobbyInfo.getLobbyHash()).getPlayers()) {
							// Add player id's to server world with new PlayerGameCharacter instance
							availableLobbies.get(packetLobbyInfo.getLobbyHash()).getServerWorld().addGameCharacter(playerId, PlayerGameCharacter
									.createPlayerGameCharacter(playerGameCharacterX, playerGameCharacterY, availableLobbies.get(packetLobbyInfo.getLobbyHash()).getServerWorld(), playerId));
							if (playerId != connection.getID()) {
								server.sendToUDP(playerId, packetLobbyInfo);
							}
						}
						// Add lobby to ongoing lobbies
						onGoingLobbies.put(packetLobbyInfo.getLobbyHash(), availableLobbies.get(packetLobbyInfo.getLobbyHash()));
						// Remove lobby from available lobbies
						availableLobbies.remove(packetLobbyInfo.getLobbyHash());
						// Send to all packet that removes lobby from available lobbies
						sendRemoveLobby(packetLobbyInfo.getLobbyHash());

					} else if (packetLobbyInfo.isUpdateInfo() && availableLobbies.get(packetLobbyInfo.getLobbyHash()) != null) {
						// If client request info about lobby, send players in lobby
						Lobby lobby = availableLobbies.get(packetLobbyInfo.getLobbyHash());
						Set<Integer> players = lobby.getPlayers();
						packetLobbyInfo.setPlayers(players);
						server.sendToUDP(connection.getID(), packetLobbyInfo);

					} else if (packetLobbyInfo.getPlayerToAdd() != null && availableLobbies.get(packetLobbyInfo.getLobbyHash()) != null) {
						Lobby lobby = availableLobbies.get(packetLobbyInfo.getLobbyHash());
						lobby.addPlayer(packetLobbyInfo.getPlayerToAdd());
						// TODO dont send to all
						server.sendToAllExceptUDP(connection.getID(), packetLobbyInfo);

					} else if (availableLobbies.get(packetLobbyInfo.getLobbyHash()) != null && packetLobbyInfo.getPlayerToRemove() != null) {
						Lobby lobby = availableLobbies.get(packetLobbyInfo.getLobbyHash());
						lobby.removePlayer(packetLobbyInfo.getPlayerToRemove());
						server.sendToAllExceptUDP(connection.getID(), packetLobbyInfo);

					} else if (packetLobbyInfo.isToDelete()) {
						// Remove lobby from available lobbies
						removeAvailableLobby(packetLobbyInfo.getLobbyHash());
						// Send to all that lobby is to be deleted
						server.sendToAllExceptUDP(connection.getID(), packetLobbyInfo);

					}
				}
			}

			// Client disconnects from the Server.
			public void disconnected (Connection c) {
				PacketClientDisconnect packetClientDisconnect = PacketCreator.createPacketClientDisconnect(c.getID());
				// Player's lobby if he was in game
				Lobby playerLobby = null;
				// Get lobby where player was if was in game
				for (Lobby lobby : onGoingLobbies.values()) {
					if (lobby.getServerWorld().getClientsIds().contains(c.getID())) {
						playerLobby = lobby;
					}
				}

				System.out.println("Client " + c.getID() + " disconnected.");

				// Remove client from the game.
				if (playerLobby != null) {
					playerLobby.getServerWorld().removeClient(c.getID());
					// Send to other players in lobby that client has disconnected from the game.
					for (Integer playerId : playerLobby.getServerWorld().getClientsIds()) {
						if (playerId != c.getID()) {
							server.sendToUDP(playerId, packetClientDisconnect);
						}
					}
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
	public void addCharacterToClientsGame(Connection newCharacterConnection, PlayerGameCharacter newPlayerGameCharacter, String lobbyHash) {
		// Get all players from lobby's server world
		List<PlayerGameCharacter> clientsValues = new ArrayList<>(onGoingLobbies.get(lobbyHash).getServerWorld().getClients().values());
		// Add new character to all players client world
		for (PlayerGameCharacter character : clientsValues) {
			// Create a new packet for sending PlayerGameCharacter instance info.
			PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(character.getPlayerGameCharacterId(), character.getBoundingBox().getX(), character.getBoundingBox().getY());
			// Send packet only to new connection.
			server.sendToTCP(newCharacterConnection.getID(), addCharacter);
		}

		// Add new PlayerGameCharacter instance to all connections.
		// Create a packet to send new PlayerGameCharacter's info.
		PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(newCharacterConnection.getID(), newPlayerGameCharacter.getBoundingBox().getX(), newPlayerGameCharacter.getBoundingBox().getY());

		// Send packet to players who are in lobby
		for (Integer playerId : onGoingLobbies.get(lobbyHash).getServerWorld().getClientsIds()) {
			server.sendToUDP(playerId, addCharacter);
		}
	}

	/**
	 * Method for sending updated PlayerGameCharacter instance info to all connections.
	 *
	 * @param id of the PlayerGameCharacter (int)
	 * @param xPos new x coordinate of the PlayerGameCharacter (float)
	 * @param yPos new y coordinate of the PlayerGameCharacter (float)
	 */
	public void sendUpdatedGameCharacter(int id, float xPos, float yPos, GameCharacter.State state, boolean isFacingRight, String lobbyHash) {
		// Create packet
		PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(id, xPos, yPos);
		// Set player's state and if he's facing right
		packet.setCurrentState(state);
		packet.setFacingRight(isFacingRight);
		// Send packet to players in given lobby
		for (Integer playerId : onGoingLobbies.get(lobbyHash).getServerWorld().getClientsIds()) {
			if (playerId != id) {
				server.sendToUDP(playerId, packet);
			}
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
	 * Method for sending serverWorld's updated Enemy instances info to all connections.
	 */
	public void sendUpdatedEnemies(String lobbyHash) {
		// Get server world
		World serverWorld = onGoingLobbies.get(lobbyHash).getServerWorld();
		// Get lobby
		Lobby lobby = onGoingLobbies.get(lobbyHash);
		// Update enemy positions
		serverWorld.updateEnemyInTheWorldEnemyMap();
		// Enemy instance id (key) and new coordinates (value) are sent.
		for (Map.Entry<String, Enemy> entry : serverWorld.getEnemyMap().entrySet()) {
			PacketUpdateEnemy packetUpdateEnemy = PacketCreator.createPacketUpdateEnemy(entry.getKey(), entry.getValue().getxPosition(), entry.getValue().getyPosition());
			// Send enemy info to all players in lobby
			for (Integer playerId : lobby.getServerWorld().getClientsIds()) {
				server.sendToUDP(playerId, packetUpdateEnemy);
			}
		}
	}

	/**
	 * Method for sending list of available lobbies to clients.
	 */
	public void sendAvailableLobbies(Integer playerId) {
		for (Lobby lobby : availableLobbies.values()) {
			PacketLobbyInfo packetLobbyInfo = PacketCreator.createPacketLobbyInfo(lobby.getLobbyHash());
			packetLobbyInfo.setPlayers(new HashSet<>(lobby.getPlayers()));
			server.sendToUDP(playerId, packetLobbyInfo);
		}
	}

	/**
	 * Method for sending packet that lobby is removed.
	 * @param lobbyHash lobby hash.
	 */
	public void sendRemoveLobby(String lobbyHash) {
		PacketRemoveLobby packetRemoveLobby = new PacketRemoveLobby();
		packetRemoveLobby.setLobbyHash(lobbyHash);
		server.sendToAllUDP(packetRemoveLobby);
	}

	/**
	 * Remove lobby from available lobbies
	 * @param lobbyHash lobby hash.
	 */
	public void removeAvailableLobby(String lobbyHash) {
        availableLobbies.remove(lobbyHash);
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
	 * Reset Server variable playerGameCharacterX.
	 */
	public void restartServer() {
		playerGameCharacterX = 280f;
	}

	public ServerConnection getServerConnection() {
		return this;
	}

	public static void main(String[] args) {
		// Runs the main application.
		new ServerConnection();
	}
}
