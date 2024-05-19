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
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Objects;

public class ServerConnection {

	static Server server;
	static final int tcpPort = 8089;
    private final float playerGameCharacterX = 280f;
	private final float playerGameCharacterY = 250f;
	private final Map<String, Lobby> availableLobbies = new LinkedHashMap<>();
	private final Map<String, Lobby> onGoingLobbies = new LinkedHashMap<>();
	private final List<Integer> connectedPlayers = new ArrayList<>();


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
		server.getKryo().register(PacketRemovePlayer.class);
		server.getKryo().register(PacketNewBullet.class);

		// Add listener to handle receiving objects.
		server.addListener(new Listener() {

			// Receive packets from clients.
			public void received(Connection connection, Object object){
				if (object instanceof PacketConnect) {

					// Add connection to list
					connectedPlayers.add(connection.getID());

					PacketConnect packetConnect = (PacketConnect) object;

					// Get lobby's server world
					World serverWorld = onGoingLobbies.get(packetConnect.getLobbyHash()).getServerWorld();
					// System.out.println("serverWorld " + serverWorld);

					// Creates new PlayerGameCharacter instance for the connection.
					PlayerGameCharacter newPlayerGameCharacter = PlayerGameCharacter
							.createPlayerGameCharacter(playerGameCharacterX, playerGameCharacterY,
									serverWorld, connection.getID());

					// Add character to server world
					serverWorld.addGameCharacter(connection.getID(), newPlayerGameCharacter);

					// Add new PlayerGameCharacter instance to all connections.
					addCharacterToClientsGame(connection, newPlayerGameCharacter, packetConnect.getLobbyHash());

					// Send connected player position to other players
					sendUpdatedGameCharacter(connection.getID(), playerGameCharacterX, playerGameCharacterY,
							GameCharacter.State.IDLE, true, packetConnect.getLobbyHash(), 100);

					// Send other players positions to joined player in lobby
					sendOtherPlayersInfoToClient(serverWorld, packetConnect, connection);

					// Add enemies to client's world
					for (Enemy enemy : serverWorld.getEnemyMap().values()) {
						addEnemyToClientsGame(enemy.getBotHash(), enemy.getxPosition(),
								enemy.getyPosition(), connection.getID());
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
						// Update info
						updateCharacterInfo(world, packet, connection.getID());
					}

				} else if (object instanceof PacketUpdateEnemy) {
					// Packet for updating enemy position
					PacketUpdateEnemy packetUpdateEnemy = (PacketUpdateEnemy) object;
                    World world = onGoingLobbies.get(packetUpdateEnemy.getLobbyHash()).getServerWorld();

					if (world.getEnemyMap().containsKey(packetUpdateEnemy.getBotHash())) {
						Enemy enemy = world.getEnemyMap().get(packetUpdateEnemy.getBotHash());
						enemy.setyPosition(packetUpdateEnemy.getyPosition());
						// System.out.println("Got enemy y: " + packetUpdateEnemy.getyPosition());
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


				} else if (object instanceof PacketGetAvailableLobbies) {
					sendAvailableLobbies(connection.getID());


				} else if (object instanceof PacketLobbyInfo) {
					PacketLobbyInfo packetLobbyInfo = (PacketLobbyInfo) object;

					// Check if host set lobby to started
					if (packetLobbyInfo.isStartGame()) {
						// Start game
						startGame(packetLobbyInfo, connection.getID());

					} else if (packetLobbyInfo.isUpdateInfo() && availableLobbies
							.get(packetLobbyInfo.getLobbyHash()) != null) {
						// If client request info about lobby, send players in lobby
						Lobby lobby = availableLobbies.get(packetLobbyInfo.getLobbyHash());
						Set<Integer> players = lobby.getPlayers();
						packetLobbyInfo.setPlayers(players);
						server.sendToAllExceptTCP(connection.getID(), packetLobbyInfo);

					} else if (packetLobbyInfo.getPlayerToAdd() != null && availableLobbies
							.get(packetLobbyInfo.getLobbyHash()) != null) {
						Lobby lobby = availableLobbies.get(packetLobbyInfo.getLobbyHash());
						lobby.addPlayer(packetLobbyInfo.getPlayerToAdd());
						server.sendToAllExceptTCP(connection.getID(), packetLobbyInfo);

					} else if (packetLobbyInfo.isToDelete()) {
						// Remove lobby from available lobbies
						removeAvailableLobby(packetLobbyInfo.getLobbyHash());
						// Send to all that lobby is to be deleted
						server.sendToAllExceptTCP(connection.getID(), packetLobbyInfo);

					}


				} else if (object instanceof PacketRemovePlayer) {
					// Remove player from lobby and world
					PacketRemovePlayer packetRemovePlayer = (PacketRemovePlayer) object;
					handleRemovingPlayer(packetRemovePlayer, connection.getID());


				} else if (object instanceof PacketBullet) {
					PacketBullet packetBullet = (PacketBullet) object;
					// Check if arrived packet that informs that enemy was killed
					if (packetBullet.isHit()) {
						// Handle bullet hit
						handleBulletHit(packetBullet);
					}


				} else if (object instanceof PacketNewBullet) {
					PacketNewBullet packetNewBullet = (PacketNewBullet) object;
					// Create bullet
					Bullet bullet = Bullet.createBullet(packetNewBullet.getPlayerX(), packetNewBullet.getPlayerY(),
							packetNewBullet.getMouseX(), packetNewBullet.getMouseY(), true);
					// Add to server world
					onGoingLobbies.get(packetNewBullet.getLobbyHash()).getServerWorld().addBullet(bullet);
				}
			}

			// Client disconnects from the Server.
			public void disconnected (Connection c) {
				// Handle disconnect
				handleDisconnect(c);

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
	public void addCharacterToClientsGame(Connection newCharacterConnection,
										  PlayerGameCharacter newPlayerGameCharacter, String lobbyHash) {
		// Get all players from lobby's server world
		List<PlayerGameCharacter> clientsValues = new ArrayList<>(onGoingLobbies.get(lobbyHash)
				.getServerWorld().getClients().values());
		// Add new character to all players client world
		for (PlayerGameCharacter character : clientsValues) {
			// Create a new packet for sending PlayerGameCharacter instance info.
			PacketAddCharacter addCharacter = PacketCreator
					.createPacketAddCharacter(character.getPlayerGameCharacterId(), character.getBoundingBox().getX(),
							character.getBoundingBox().getY());
			// Send packet only to new connection.
			server.sendToTCP(newCharacterConnection.getID(), addCharacter);
		}

		// Add new PlayerGameCharacter instance to all connections.
		// Create a packet to send new PlayerGameCharacter's info.
		PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(newCharacterConnection.getID(),
				newPlayerGameCharacter.getBoundingBox().getX(), newPlayerGameCharacter.getBoundingBox().getY());

		// Send packet to players who are in lobby
		for (Integer playerId : onGoingLobbies.get(lobbyHash).getServerWorld().getClientsIds()) {
			server.sendToTCP(playerId, addCharacter);
		}
	}

	/**
	 * Sends information about other players in the lobby to a newly connected client.
	 *
	 * @param serverWorld the World object representing the game world
	 * @param packetConnect the PacketConnect object containing connection information
	 * @param connection the Connection object representing the newly connected client
	 */
	public void sendOtherPlayersInfoToClient(World serverWorld, PacketConnect packetConnect, Connection connection) {
		for (Map.Entry<Integer, PlayerGameCharacter> entry : serverWorld.getClients().entrySet()) {
			if (entry.getKey() != connection.getID()) {
				int id = entry.getKey();
				PlayerGameCharacter player = entry.getValue();
				PacketUpdateCharacterInformation packet = PacketCreator
						.createPacketUpdateCharacterInformation(packetConnect.getLobbyHash(), id,
								player.getxPosition(), player.getyPosition(), player.getCurrentState(),
								player.isFacingRight(), player.getHealth());
				server.sendToTCP(connection.getID(), packet);
			}
		}
	}

	/**
	 * Updates the character information for a player in the specified world based on the provided packet data,
	 * including position, facing direction, and current state. Checks if the player has collected a coin, signaling
	 * the end of the game if so.
	 *
	 * @param world  the World object containing the player and coin information
	 * @param packet the PacketUpdateCharacterInformation object containing updated character information
	 * @param id the ID of the player whose character information is being updated
	 */
	public void updateCharacterInfo(World world, PacketUpdateCharacterInformation packet, int id) {
		// Update PlayerGameCharacter's coordinates, facing direction, and current state
		PlayerGameCharacter player = world.getClients().get(id);
		player.setxPosition(packet.getX());
		player.setyPosition(packet.getY());
		player.setFacingRight(packet.getFacingRight());
		player.setCurrentState(packet.getCurrentState());

		// Get the coin
		Coin coin = world.getCoin();

		// Check if the player has collected the coin, indicating victory
		if (checkIfCoinCollected(packet, coin)) {
			return;
		}

		// Send updated character information to all players in the lobby
		sendUpdatedGameCharacter(id, packet.getX(), packet.getY(), packet.getCurrentState(),
				packet.getFacingRight(), packet.getLobbyHash(), packet.getHealth());
	}

	/**
	 * Checks if the player represented by the provided packet has collected the coin in the game world.
	 * If the coin is collected, the method signals the end of the game by sending a win packet to the lobby.
	 *
	 * @param packet the PacketUpdateCharacterInformation object containing updated character information
	 * @param coin the Coin object representing the coin in the game world
	 * @return true if the coin is collected by the player, indicating the game is won; false otherwise
	 */
	public boolean checkIfCoinCollected(PacketUpdateCharacterInformation packet, Coin coin) {
		if (packet.getX() < coin.getxCoordinate() + 20 && packet.getX() > coin.getxCoordinate()
				&& packet.getY() < coin.getyCoordinate() + 20 && packet.getY() + 20 > coin.getyCoordinate()) {
			// Signal that the game is won
			sendPacketWon(packet.getLobbyHash());
			return true;
		}
		return false;
	}

	/**
	 * Method for sending updated PlayerGameCharacter instance info to all connections.
	 *
	 * @param id of the PlayerGameCharacter (int)
	 * @param xPos new x coordinate of the PlayerGameCharacter (float)
	 * @param yPos new y coordinate of the PlayerGameCharacter (float)
	 */
	public void sendUpdatedGameCharacter(int id, float xPos, float yPos, GameCharacter.State currentState,
										 boolean facingRight, String lobbyHash, float health) {
		// Create packet
		PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(lobbyHash,
				id, xPos, yPos, currentState, facingRight, health);
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
		PacketUpdateCharacterInformation packetUpdateCharacterInformation = PacketCreator
				.createPacketUpdateCharacterInformation(lobbyHash, id, -100, -100, GameCharacter.State.IDLE,
						true, 0);
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
	 * Handles the removal of a player from the specified lobby, removing the player from the lobby and the server world,
	 * and sending a packet to all players except the removed player.
	 *
	 * @param packetRemovePlayer the PacketRemovePlayer object containing information about the player to be removed
	 * @param id the ID of the player initiating the removal
	 */
	public void handleRemovingPlayer(PacketRemovePlayer packetRemovePlayer, int id) {
		// Get the lobby where the player is to be removed
		Lobby lobby = availableLobbies.get(packetRemovePlayer.getLobbyHash());

		// If the lobby is not found in available lobbies, check ongoing lobbies
		if (lobby == null) {
			lobby = onGoingLobbies.get(packetRemovePlayer.getLobbyHash());
		}

		// If the lobby is found
		if (lobby != null) {
			// Remove the player from the lobby
			lobby.removePlayer(packetRemovePlayer.getPlayerToRemove());

			// Remove the player from the server world associated with the lobby
			lobby.getServerWorld().removeClient(packetRemovePlayer.getPlayerToRemove());
		}

		// Send a packet to all players except the one initiating the removal, indicating the player removal
		server.sendToAllExceptTCP(id, packetRemovePlayer);
	}

	/**
	 * Remove lobby from available lobbies
	 * @param lobbyHash lobby hash.
	 */
	public void removeAvailableLobby(String lobbyHash) {
        availableLobbies.remove(lobbyHash);
	}

	/**
	 * Starts the game for the specified lobby, initializing the game environment, updating thread, and notifying players.
	 *
	 * @param packetLobbyInfo the PacketLobbyInfo object containing lobby information
	 * @param id the ID of the player initiating the game start
	 */
	public void startGame(PacketLobbyInfo packetLobbyInfo, int id) {
		// Get the lobby
		Lobby lobby = availableLobbies.get(packetLobbyInfo.getLobbyHash());

		// Create and set a new server world for the lobby
		createAndSetServerWorld(packetLobbyInfo.getMapPath(), lobby);

		// Create and set a new server update thread for the lobby
		World serverWorld = lobby.getServerWorld();
        ServerUpdateThread serverUpdateThread = new ServerUpdateThread(serverWorld, packetLobbyInfo.getLobbyHash(), getServerConnection());
		serverUpdateThread.setGameOn(true);
		new Thread(serverUpdateThread).start();
		lobby.setServerUpdateThread(serverUpdateThread);

		// Set the lobbyHash to the server thread
		lobby.getServerUpdateThread().setLobbyHash(packetLobbyInfo.getLobbyHash());

		// Inform all players except the host in the lobby that the game has started
		informThatGameStarted(serverWorld, packetLobbyInfo, lobby, id);

		// Add the lobby to ongoing lobbies
		onGoingLobbies.put(packetLobbyInfo.getLobbyHash(), availableLobbies.get(packetLobbyInfo.getLobbyHash()));

		// Remove the lobby from available lobbies
		availableLobbies.remove(packetLobbyInfo.getLobbyHash());

		// Send a packet to all players removing the lobby from available lobbies
		sendRemoveLobby(packetLobbyInfo.getLobbyHash());
	}

	/**
	 * Informs all players in the lobby that the game has started and initializes the game environment.
	 *
	 * @param serverWorld the World object representing the game environment on the server
	 * @param packetLobbyInfo the PacketLobbyInfo object containing lobby information
	 * @param lobby the Lobby object representing the game lobby
	 * @param id the ID of the player initiating the game start
	 */
	public void informThatGameStarted(World serverWorld, PacketLobbyInfo packetLobbyInfo, Lobby lobby, int id) {
		for (Integer playerId : availableLobbies.get(packetLobbyInfo.getLobbyHash()).getPlayers()) {
			// Add player IDs to the server world with new PlayerGameCharacter instances
			if (serverWorld == null) {
				// If serverWorld is null, create a new one and set it for the lobby
				serverWorld = createAndSetServerWorld(packetLobbyInfo.getMapPath(), lobby);
				serverWorld.addGameCharacter(playerId, PlayerGameCharacter
						.createPlayerGameCharacter(playerGameCharacterX, playerGameCharacterY,
								availableLobbies.get(packetLobbyInfo.getLobbyHash()).getServerWorld(), playerId));
			} else {
				// Add players to the existing server world
				serverWorld.addGameCharacter(playerId, PlayerGameCharacter
						.createPlayerGameCharacter(playerGameCharacterX, playerGameCharacterY,
								availableLobbies.get(packetLobbyInfo.getLobbyHash()).getServerWorld(), playerId));
			}
			// Send the lobby information packet to all players except the one initiating the game start
			if (playerId != id) {
				server.sendToTCP(playerId, packetLobbyInfo);
			}
		}
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

			PacketBullet packetBullet;

			if (bullet.isPlayerBullet()) {
				packetBullet = PacketCreator.createPacketBullet(lobbyHash, bullet.getBulletId(),
						bullet.getBulletX(), bullet.getBulletY(), true);
			} else {
				packetBullet = PacketCreator.createPacketBullet(lobbyHash, bullet.getBulletId(),
						bullet.getBulletX(), bullet.getBulletY(), false);
			}

			List<Integer> players = new ArrayList<>(onGoingLobbies.get(lobbyHash).getPlayers());

			for (Integer id : players) {
				server.sendToTCP(id, packetBullet);
			}
		}
	}

	/**
	 * Add new bullet to client game.
	 * @param lobbyHash lobby hash.
	 * @param bullet new bullet.
	 */
	public void sendNewBullet(String lobbyHash, Bullet bullet) {
		System.out.println("Sent new bullet");
		PacketNewBullet packetNewBullet = PacketCreator.createPacketNewBullet(lobbyHash, bullet.getBulletId(),
				bullet.getBulletX(), bullet.getBulletY(), bullet.isPlayerBullet());
		System.out.println("Sent bullet with id: " + packetNewBullet.getBulletId());
		Set<Integer> players = new HashSet<>(onGoingLobbies.get(lobbyHash).getPlayers());
		for (Integer id : players) {
			server.sendToTCP(id, packetNewBullet);
		}
	}

	/**
	 * Handles the event of a bullet hitting an enemy in the game.
	 *
	 * @param packetBullet the PacketBullet object containing information about the bullet hit
	 */
	public void handleBulletHit(PacketBullet packetBullet) {
		// Retrieve the world associated with the lobby hash from ongoing lobbies
		World world = onGoingLobbies.get(packetBullet.getLobbyHash()).getServerWorld();

		// Check if the enemy hit by the bullet exists in the server world
		if (world.getEnemyMap().containsKey(packetBullet.getHitEnemy())) {
			Enemy enemy = world.getEnemyMap().get(packetBullet.getHitEnemy());
			// Decrease the health of the hit enemy
			enemy.updateHealth(-20);
			// Check if the enemy is dead
			if (enemy.getHealth() <= 0) {
				// Remove the enemy from the world if its health is zero or below
				world.removeEnemy(enemy.getBotHash());
			}
			// Inform other clients in the lobby that an enemy has been hit
			sendHitEnemy(packetBullet.getLobbyHash(), packetBullet);
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
				lobby.getServerUpdateThread().setGameOn(false);
				sendRemoveLobby(lobby.getLobbyHash());
			}
		}
	}

	/**
	 * Handles the disconnection of a client.
	 *
	 * @param c the Connection object representing the disconnected client
	 */
	public void handleDisconnect(Connection c) {
		// Remove connection from list
		connectedPlayers.remove(Integer.valueOf(c.getID()));

		// Check if any lobby is empty
		checkIfLobbyEmpty();

		// Create a packet for notifying client disconnect
		PacketClientDisconnect packetClientDisconnect = PacketCreator.createPacketClientDisconnect(c.getID());
		// Player's lobby if they were in a game
		Lobby playerLobby = checkAndGetClientLobby(c);

		// Remove client from the game if they were in a lobby
		removeClientFromGame(playerLobby, c, packetClientDisconnect);
	}

	/**
	 * Checks and retrieves the lobby of the disconnected client.
	 *
	 * @param c the Connection object representing the disconnected client
	 * @return the Lobby object associated with the disconnected client, or null if not found
	 */
	public Lobby checkAndGetClientLobby(Connection c) {
		// Player's lobby if they were in a game
		Lobby playerLobby = null;
		// Get lobby where player was if they were in a game
		for (Lobby lobby : onGoingLobbies.values()) {
			if (lobby.getServerWorld().getClientsIds().contains(c.getID())) {
				playerLobby = lobby;
			}
		}
		return playerLobby;
	}

	/**
	 * Removes the disconnected client from the game.
	 *
	 * @param playerLobby the Lobby object associated with the disconnected client
	 * @param c the Connection object representing the disconnected client
	 * @param packetClientDisconnect the packet to notify other players about the disconnection
	 */
	public void removeClientFromGame(Lobby playerLobby, Connection c, PacketClientDisconnect packetClientDisconnect) {
		if (playerLobby != null) {
			playerLobby.getServerWorld().removeClient(c.getID());
			// Send notification to other players in the lobby about the disconnection
			for (Integer playerId : playerLobby.getServerWorld().getClientsIds()) {
				if (playerId != c.getID()) {
					server.sendToTCP(playerId, packetClientDisconnect);
				}
			}
		}
	}

	/**
	 * Get lobbies that are in game right now.
	 * @return lobbies that in game.
	 */
	public Map<String, Lobby> getOnGoingLobbies() {
		return onGoingLobbies;
	}

	/**
	 * Get server connection.
	 * @return server connection.
	 */
	public ServerConnection getServerConnection() {
		return this;
	}

	/**
	 * Create and set server world to lobby.
	 * @param mapPath map path.
	 * @param lobby lobby.
	 */
	public World createAndSetServerWorld(String mapPath, Lobby lobby) {
		if (Objects.equals(mapPath, "Maps/level1/level1.tmx")) {
			System.out.println("startGame Packet in Server Connection level1");
			lobby.setServerWorld(new Level1(lobby.getLobbyHash(), this));
		} else if (Objects.equals(mapPath, "Maps/level4/destestsmaller.tmx")) {
			System.out.println("StartGame packet in ServerConnection destestsmaller");
			lobby.setServerWorld(new Level2(lobby.getLobbyHash(), this));
		}
		return lobby.getServerWorld();
	}

	public static void main(String[] args) {
		// Runs the main application.
		new ServerConnection();
	}
	// key location.
	// /Users/mactamm/.ssh/id_ed25519
}
