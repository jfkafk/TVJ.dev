package com.mygdx.game.Server;


import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Characters.Enemy;
import com.mygdx.game.Characters.GameCharacter;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Finish.Coin;
import com.mygdx.game.Lobbies.Lobby;
import com.mygdx.game.Weapons.Bullet;
import com.mygdx.game.World.Level1;
import com.mygdx.game.World.Level2;
import com.mygdx.game.World.World;
import packets.*;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.*;

public class ServerConnection {

	static Server server;
	static final int tcpPort = 8089;
	private ServerUpdateThread serverUpdateThread;
	private final float playerGameCharacterX = 280f;
	private final float playerGameCharacterY = 250f;
	Map<String, Lobby> availableLobbies = new LinkedHashMap<>();
	Map<String, Lobby> onGoingLobbies = new LinkedHashMap<>();
	List<Integer> connectedPlayers = new ArrayList<>();


	/**
	 * Server connection.
	 */
	public ServerConnection()  {
		try {
			server = new Server(49152, 49152);
			server.start();
			server.bind(tcpPort);

		} catch (IOException exception) {
			JOptionPane.showMessageDialog(null, "Can not start the Server.");
		}

		// Register all packets that are sent over the network.
		server.getKryo().register(Packet.class);
		server.getKryo().register(PacketConnect.class);
		server.getKryo().register(PacketAddCharacter.class);
		server.getKryo().register(PacketUpdateCharacterInformation.class);
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
		server.getKryo().register(PacketBullet.class);
		server.getKryo().register(PacketAddCoin.class);
		server.getKryo().register(PacketWon.class);

		// Add listener to handle receiving objects.
		server.addListener(new Listener() {

			// Receive packets from clients.
			public void received(Connection connection, Object object){
				if (object instanceof PacketConnect) {
					System.out.println("packet recieved");

					// Add connection to list
					connectedPlayers.add(connection.getID());

					PacketConnect packetConnect = (PacketConnect) object;

					// Get lobby's server world
					World serverWorld = onGoingLobbies.get(packetConnect.getLobbyHash()).getServerWorld();
					System.out.println("serverWorld " + serverWorld);

					// Creates new PlayerGameCharacter instance for the connection.
					PlayerGameCharacter newPlayerGameCharacter = PlayerGameCharacter
							.createPlayerGameCharacter(playerGameCharacterX, playerGameCharacterY, serverWorld, connection.getID());

					// Add character to server world
					serverWorld.addGameCharacter(connection.getID(), newPlayerGameCharacter);

					// Add new PlayerGameCharacter instance to all connections.
					addCharacterToClientsGame(connection, newPlayerGameCharacter, packetConnect.getLobbyHash());

					// Send connected player position to other players
					sendUpdatedGameCharacter(connection.getID(), playerGameCharacterX, playerGameCharacterY, GameCharacter.State.IDLE, true, packetConnect.getLobbyHash(), 100);

					// Send other players positions to joined player in lobby
					for (Map.Entry<Integer, PlayerGameCharacter> entry : serverWorld.getClients().entrySet()) {
						if (entry.getKey() != connection.getID()) {
							int id = entry.getKey();
							PlayerGameCharacter player = entry.getValue();
							PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(packetConnect.getLobbyHash(), id, player.getxPosition(), player.getyPosition(), player.getCurrentState(), player.isFacingRight(), player.getHealth());
							server.sendToTCP(connection.getID(), packet);
						}
					}

					// Add enemies to client's world
					for (Enemy enemy : serverWorld.getEnemyMap().values()) {
						addEnemyToClientsGame(enemy.getBotHash(), enemy.getxPosition(), enemy.getyPosition(), connection.getID());
					}

					// Get coin
					Coin coin = serverWorld.getCoin();
					// Send add coin
					sendAddCoin(connection.getID(), coin.getxCoordinate(), coin.getyCoordinate());

				} else if (object instanceof PacketUpdateCharacterInformation) {
					// Packet for updating player position
					PacketUpdateCharacterInformation packet = (PacketUpdateCharacterInformation) object;

					// Get world
					World world = onGoingLobbies.get(packet.getLobbyHash()).getServerWorld();

					if (packet.isDead()) {
						// If player has died, then remove him from server world
						world.removeClient(connection.getID());
						// Send packet that informs other clients in lobby that player is dead
						sendDeadPlayer(packet.getLobbyHash(), packet.getId());
					} else {
						// Update PlayerGameCharacter's coordinates in lobby.
						PlayerGameCharacter player = world.getClients().get(connection.getID());
						player.setxPosition(packet.getX());
						player.setyPosition(packet.getY());
						player.setFacingRight(packet.getFacingRight());
						player.setCurrentState(packet.getCurrentState());

						// Get coin
						Coin coin = world.getCoin();

						// If player got coin, then game is won
						if (packet.getX() < coin.getxCoordinate() + 20 && packet.getX() > coin.getxCoordinate() && packet.getY() < coin.getyCoordinate() + 20 && packet.getY() + 20 > coin.getyCoordinate()) {
							// Game won
							sendPacketWon(packet.getLobbyHash());
							return;
						}

						// Send players new coordinates and direction to all players in lobby
						sendUpdatedGameCharacter(connection.getID(), packet.getX(), packet.getY(), packet.getCurrentState(), packet.getFacingRight(), packet.getLobbyHash(), packet.getHealth());
					}

				} else if (object instanceof PacketUpdateEnemy) {
					// Packet for updating enemy position
					PacketUpdateEnemy packetUpdateEnemy = (PacketUpdateEnemy) object;
                    World world = onGoingLobbies.get(packetUpdateEnemy.getLobbyHash()).getServerWorld();

					if (world.getEnemyMap().containsKey(packetUpdateEnemy.getBotHash())) {
						Enemy enemy = world.getEnemyMap().get(packetUpdateEnemy.getBotHash());
						enemy.setyPosition(packetUpdateEnemy.getyPosition());
					}

				} else if (object instanceof PacketSendNewLobby) {
					// Packet for adding new lobby
					PacketSendNewLobby packetSendNewLobby = (PacketSendNewLobby) object;

					Lobby lobby = new Lobby(connection.getID());
					lobby.addPlayer(connection.getID());

					availableLobbies.put(lobby.getLobbyHash(), lobby);

					packetSendNewLobby.setLobbyHash(lobby.getLobbyHash());
					packetSendNewLobby.setCreatorId(connection.getID());

					server.sendToAllTCP(packetSendNewLobby);
					//System.out.println(availableLobbies);

				} else if (object instanceof PacketGetAvailableLobbies) {
					sendAvailableLobbies(connection.getID());

				} else if (object instanceof PacketLobbyInfo) {
					PacketLobbyInfo packetLobbyInfo = (PacketLobbyInfo) object;

					// Check if host set lobby to started
					if (packetLobbyInfo.isStartGame()) {
						// Get lobby
						//System.out.println("Got path: " + packetLobbyInfo.getMapPath());
						Lobby lobby = availableLobbies.get(packetLobbyInfo.getLobbyHash());
						// Set for each new ongoing lobby new server world
						if (Objects.equals(packetLobbyInfo.getMapPath(), "Maps/level1/level1.tmx")) {
							System.out.println("startGame Packet in Server Connection level1");
							lobby.setServerWorld(new Level1());
						} else if (Objects.equals(packetLobbyInfo.getMapPath(), "Maps/level4/destestsmaller.tmx")) {
							System.out.println("StartGame packet in ServerConnection destestsmaller");
							lobby.setServerWorld(new Level2());
						}
						// Set for each new ongoing lobby new server update thread
						World serverWorld = lobby.getServerWorld();
						serverUpdateThread = new ServerUpdateThread();
						serverUpdateThread.setServerConnection(getServerConnection());
						serverUpdateThread.setServerWorld(serverWorld);
						new Thread(serverUpdateThread).start();
						lobby.setServerUpdateThread(serverUpdateThread);
						// Set lobbyHash to server thread
						lobby.getServerUpdateThread().setLobbyHash(packetLobbyInfo.getLobbyHash());
						// Send packet that lobby has started to all players except host that are in that lobby
						for (Integer playerId : availableLobbies.get(packetLobbyInfo.getLobbyHash()).getPlayers()) {
							// Add player id's to server world with new PlayerGameCharacter instance
							serverWorld.addGameCharacter(playerId, PlayerGameCharacter
									.createPlayerGameCharacter(playerGameCharacterX, playerGameCharacterY, availableLobbies.get(packetLobbyInfo.getLobbyHash()).getServerWorld(), playerId));
							if (playerId != connection.getID()) {
								server.sendToTCP(playerId, packetLobbyInfo);
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
						server.sendToTCP(connection.getID(), packetLobbyInfo);

					} else if (packetLobbyInfo.getPlayerToAdd() != null && availableLobbies.get(packetLobbyInfo.getLobbyHash()) != null) {
						Lobby lobby = availableLobbies.get(packetLobbyInfo.getLobbyHash());
						lobby.addPlayer(packetLobbyInfo.getPlayerToAdd());
						server.sendToAllExceptTCP(connection.getID(), packetLobbyInfo);

					} else if (availableLobbies.get(packetLobbyInfo.getLobbyHash()) != null && packetLobbyInfo.getPlayerToRemove() != null) {
						Lobby lobby = availableLobbies.get(packetLobbyInfo.getLobbyHash());
						lobby.removePlayer(packetLobbyInfo.getPlayerToRemove());
						server.sendToAllExceptTCP(connection.getID(), packetLobbyInfo);

					} else if (packetLobbyInfo.isToDelete()) {
						// Remove lobby from available lobbies
						removeAvailableLobby(packetLobbyInfo.getLobbyHash());
						// Send to all that lobby is to be deleted
						server.sendToAllExceptTCP(connection.getID(), packetLobbyInfo);

					}
				} else if (object instanceof PacketBullet) {
					PacketBullet packetBullet = (PacketBullet) object;

					// Check if arrived packet that informs that enemy wask killed
					if (packetBullet.isHit()) {
						World world = onGoingLobbies.get(packetBullet.getLobbyHash()).getServerWorld();

						// Check if enemy is in server world
						if (world.getEnemyMap().containsKey(packetBullet.getHitEnemy())) {
							Enemy enemy = world.getEnemyMap().get(packetBullet.getHitEnemy());
							// Decrease health
							enemy.updateHealth(-20);
							// Check if dead
							if (enemy.getHealth() <= 0) {
								world.removeEnemy(enemy.getBotHash());
							}
							// Inform other clients that enemy is hit
							sendHitEnemy(packetBullet.getLobbyHash(), packetBullet);
						}

					} else {
						// Create bullet
						Bullet bullet = Bullet.createBullet(packetBullet.getLobbyHash(), packetBullet.getPlayerX(), packetBullet.getPlayerY(), packetBullet.getMouseX(), packetBullet.getMouseY());
						// Add to server world
						onGoingLobbies.get(packetBullet.getLobbyHash()).getServerWorld().addBullet(bullet);
					}
				}
			}

			// Client disconnects from the Server.
			public void disconnected (Connection c) {
				// Remove connection from list
				connectedPlayers.remove(Integer.valueOf(c.getID()));

				// Check if any lobby is empty
				checkIfLobbyEmpty();

				PacketClientDisconnect packetClientDisconnect = PacketCreator.createPacketClientDisconnect(c.getID());
				// Player's lobby if he was in game
				Lobby playerLobby = null;
				// Get lobby where player was if was in game
				for (Lobby lobby : onGoingLobbies.values()) {
					if (lobby.getServerWorld().getClientsIds().contains(c.getID())) {
						playerLobby = lobby;
					}
				}

				// Remove client from the game.
				if (playerLobby != null) {
					playerLobby.getServerWorld().removeClient(c.getID());
					// Send to other players in lobby that client has disconnected from the game.
					for (Integer playerId : playerLobby.getServerWorld().getClientsIds()) {
						if (playerId != c.getID()) {
							server.sendToTCP(playerId, packetClientDisconnect);
						}
					}
				}

				System.out.println("Client " + c.getID() + " disconnected.");
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
			server.sendToTCP(playerId, addCharacter);
		}
	}

	/**
	 * Method for sending updated PlayerGameCharacter instance info to all connections.
	 *
	 * @param id of the PlayerGameCharacter (int)
	 * @param xPos new x coordinate of the PlayerGameCharacter (float)
	 * @param yPos new y coordinate of the PlayerGameCharacter (float)
	 */
	public void sendUpdatedGameCharacter(int id, float xPos, float yPos, GameCharacter.State currentState, boolean facingRight, String lobbyHash, float health) {
		// Create packet
		PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(lobbyHash, id, xPos, yPos, currentState, facingRight, health);
		// Send packet to players in given lobby
		Set<Integer> players = new HashSet<>(onGoingLobbies.get(lobbyHash).getServerWorld().getClientsIds());
		for (Integer playerId : players) {
			if (playerId != id) {
				server.sendToTCP(playerId, packet);
			}
		}
	}

	/**
	 * Send packet to clients in lobby informing that player has died.
	 * @param lobbyHash lobby hash.
	 * @param id died player id.
	 */
	public void sendDeadPlayer(String lobbyHash, int id) {
		// Create packet
		PacketUpdateCharacterInformation packetUpdateCharacterInformation = PacketCreator.createPacketUpdateCharacterInformation(lobbyHash, id, -100, -100, GameCharacter.State.IDLE, true, 0);
		// Set dead
		packetUpdateCharacterInformation.setDead(true);
		// Send packet to players in given lobby
		Set<Integer> players = new HashSet<>(onGoingLobbies.get(lobbyHash).getServerWorld().getClientsIds());
		for (Integer playerId : players) {
			if (playerId != id) {
				server.sendToTCP(playerId, packetUpdateCharacterInformation);
			}
		}
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
		Map<String, Enemy> enemies = new HashMap<>(serverWorld.getEnemyMap());
		for (Map.Entry<String, Enemy> entry : enemies.entrySet()) {
			// Get key and value
			String botHash = entry.getKey();
			Enemy enemy = entry.getValue();
			// Create packet
			PacketUpdateEnemy packetUpdateEnemy = PacketCreator.createPacketUpdateEnemy(botHash, enemy.getxPosition(),
					enemy.getyPosition(), enemy.getCurrentState(), enemy.isFacingRight(), enemy.getHealth());
			packetUpdateEnemy.setCurrentState(entry.getValue().getCurrentState());
			packetUpdateEnemy.setFacingRight(entry.getValue().isFacingRight());
			// Send packet to each player in world
			for (Integer playerId : lobby.getServerWorld().getClientsIds()) {
				server.sendToTCP(playerId, packetUpdateEnemy);
			}
		}
	}

	/**
	 * Method for sending list of available lobbies to clients.
	 */
	public void sendAvailableLobbies(Integer playerId) {
		List<Lobby> lobbies = new ArrayList<>(availableLobbies.values());
		for (Lobby lobby : lobbies) {
			PacketLobbyInfo packetLobbyInfo = PacketCreator.createPacketLobbyInfo(lobby.getLobbyHash());
			packetLobbyInfo.setPlayers(new HashSet<>(lobby.getPlayers()));
			server.sendToTCP(playerId, packetLobbyInfo);
		}
	}

	/**
	 * Method for sending packet that lobby is removed.
	 * @param lobbyHash lobby hash.
	 */
	public void sendRemoveLobby(String lobbyHash) {
		PacketRemoveLobby packetRemoveLobby = PacketCreator.createPacketRemoveLobby(lobbyHash);
		server.sendToAllTCP(packetRemoveLobby);
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
		PacketNewEnemy packetNewEnemy = PacketCreator.createPacketNewEnemy(botHash, xPosition, yPosition);
		server.sendToTCP(connectionId, packetNewEnemy);
	}

	/**
	 * Send updated bullet info.
	 * @param lobbyHash lobby's hash.
	 */
	public void sendUpdatedBullet(String lobbyHash) {

		// To prevent modifying list while iterating
		List<Bullet> bullets = new ArrayList<>(onGoingLobbies.get(lobbyHash).getServerWorld().getBullets());

		for (Bullet bullet : bullets) {
			PacketBullet packetBullet = PacketCreator.createPacketBullet(lobbyHash, bullet.getBulletId(),
					bullet.getBulletX(), bullet.getBulletY());

			for (Integer id : onGoingLobbies.get(lobbyHash).getPlayers()) {
				server.sendToTCP(id, packetBullet);
			}
		}
	}

	/**
	 * Inform players in lobby that given enemy is killed.
	 * @param lobbyHash lobby's hash.
	 * @param packetBullet bullet packet.
	 */
	public void sendHitEnemy(String lobbyHash, PacketBullet packetBullet) {
		Set<Integer> players = new HashSet<>(onGoingLobbies.get(lobbyHash).getPlayers());
		for (Integer id : players) {
			server.sendToTCP(id, packetBullet);
		}
	}

	/**
	 * Send PacketAddCoin to client.
	 * @param connectionId connection id.
	 * @param xCoordinate coin x coordinate.
	 * @param yCoordinate coin y coordinate.
	 */
	public void sendAddCoin(int connectionId, float xCoordinate, float yCoordinate) {
		PacketAddCoin packetAddCoin = PacketCreator.createPacketAddCoin(xCoordinate, yCoordinate);
		server.sendToTCP(connectionId, packetAddCoin);
	}

	/**
	 * Send packetWon to client.
	 * @param lobbyHash lobby hash.
	 */
	public void sendPacketWon(String lobbyHash) {
		PacketWon packetWon = PacketCreator.createPacketWon();
		List<Integer> players = new ArrayList<>(onGoingLobbies.get(lobbyHash).getPlayers());
		for (Integer player : players) {
			server.sendToTCP(player, packetWon);
		}
	}

	/**
	 * Check whether lobby is empty.
	 * If is then remove lobby.
	 */
	public void checkIfLobbyEmpty() {
		List<Lobby> lobbies = new ArrayList<>(availableLobbies.values());
		for (Lobby lobby : lobbies) {
			boolean isEmpty = true;
			for (Integer playerId : lobby.getPlayers()) {
                if (connectedPlayers.contains(playerId)) {
                    isEmpty = false;
                    break;
                }
			}
			if (lobby.getPlayers().isEmpty()) {
				isEmpty = false;
			}
			if (isEmpty) {
				sendRemoveLobby(lobby.getLobbyHash());
			}
		}
	}

	/**
	 * Get server connection.
	 * @return server connection.
	 */
	public ServerConnection getServerConnection() {
		return this;
	}

	public static void main(String[] args) {
		// Runs the main application.
		new ServerConnection();
	}
	// key location.
	// /Users/mactamm/.ssh/id_ed25519
}
