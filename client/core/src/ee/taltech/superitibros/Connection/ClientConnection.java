package ee.taltech.superitibros.Connection;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import ee.taltech.superitibros.Characters.Enemy;
import ee.taltech.superitibros.Characters.GameCharacter;
import ee.taltech.superitibros.Characters.MyPlayerGameCharacter;
import ee.taltech.superitibros.Characters.PlayerGameCharacter;
import ee.taltech.superitibros.Lobbies.Lobby;
import ee.taltech.superitibros.Screens.GameScreen;
import ee.taltech.superitibros.GameInfo.GameClient;
import ee.taltech.superitibros.GameInfo.ClientWorld;
import ee.taltech.superitibros.Weapons.Bullet;
import packets.*;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;


public class ClientConnection {

	private GameScreen gameScreen;
	private ClientWorld clientWorld;
	private GameClient gameClient;
	private static Client client;

	/**
	 * Client connection.
	 */
	public ClientConnection() {

		String ip = "193.40.255.30";
		// Server 193.40.255.30
		// local 127.0.0.1
		int tcpPort = 8089;


		client = new Client(49152, 49152);
		client.start();

		// Register all packets that are sent over the network.
		client.getKryo().register(Packet.class);
		client.getKryo().register(PacketConnect.class);
		client.getKryo().register(PacketAddCharacter.class);
		client.getKryo().register(PacketUpdateCharacterInformation.class);
		client.getKryo().register(ArrayList.class);
		client.getKryo().register(Rectangle.class);
		client.getKryo().register(HashMap.class);
		client.getKryo().register(PacketClientDisconnect.class);
		client.getKryo().register(PacketNewEnemy.class);
		client.getKryo().register(PacketUpdateEnemy.class);
		client.getKryo().register(GameCharacter.State.class);
		client.getKryo().register(PacketSendNewLobby.class);
		client.getKryo().register(PacketLobbyInfo.class);
		client.getKryo().register(PacketGetAvailableLobbies.class);
		client.getKryo().register(HashSet.class);
		client.getKryo().register(LinkedHashSet.class);
		client.getKryo().register(PacketRemoveLobby.class);
		client.getKryo().register(PacketBullet.class);
		client.getKryo().register(PacketAddCoin.class);
		client.getKryo().register(PacketWon.class);
		client.getKryo().register(PacketRemovePlayer.class);
		client.getKryo().register(PacketNewBullet.class);

		// Add a listener to handle receiving objects.
		client.addListener(new Listener.ThreadedListener(new Listener()) {

			// Receive packets from the Server.
			public void received(Connection connection, Object object) {

				gameClient.setConnectionId(connection.getID());

				if (object instanceof Packet) {

					if (object instanceof PacketAddCharacter) {
						System.out.println("Got packet add character");
						if (gameScreen != null && clientWorld != null) {
							// Packet for adding player to game.
							PacketAddCharacter packetAddCharacter = (PacketAddCharacter) object;
							if (!clientWorld.getWorldGameCharactersMap().containsKey(packetAddCharacter.getId())) {

								if (connection.getID() == ((PacketAddCharacter) object).getId()) {
									MyPlayerGameCharacter newGameCharacter = MyPlayerGameCharacter
											.createMyPlayerGameCharacter(packetAddCharacter.getX(),
													packetAddCharacter.getY(), packetAddCharacter.getId(), clientWorld);
									// Add new PlayerGameCharacter to client's game.
									clientWorld.addGameCharacter(packetAddCharacter.getId(), newGameCharacter);
								} else {
									// Create a new PlayerGameCharacter instance from received info.
									PlayerGameCharacter newGameCharacter = PlayerGameCharacter
											.createPlayerGameCharacter(packetAddCharacter.getX(),
													packetAddCharacter.getY(), packetAddCharacter.getId(), clientWorld);
									// Add new PlayerGameCharacter to client's game.
									clientWorld.addGameCharacter(packetAddCharacter.getId(), newGameCharacter);
								}
							}
						}

					} else if (object instanceof PacketUpdateCharacterInformation && clientWorld != null) {
						System.out.println("Got packet update character info");
						// Packet for updating player position.
						PacketUpdateCharacterInformation packetUpdateCharacterInformation = (PacketUpdateCharacterInformation) object;

						if (gameScreen != null && clientWorld.getWorldGameCharactersMap()
								.containsKey(packetUpdateCharacterInformation.getId())
								&& connection.getID() != packetUpdateCharacterInformation.getId()) {

                            PlayerGameCharacter gameCharacter = (PlayerGameCharacter) clientWorld
									.getGameCharacter(packetUpdateCharacterInformation.getId());
                            if (packetUpdateCharacterInformation.isDead()) {
                                clientWorld.removeClient(packetUpdateCharacterInformation.getId());
								gameCharacter.removeBodyFromWorld();
							} else {
								// Update PlayerGameCharacter's coordinates.
                                gameCharacter.state = packetUpdateCharacterInformation.getCurrentState();
								gameCharacter.setFacingRight(packetUpdateCharacterInformation.getFacingRight());
								gameCharacter.setHealth(packetUpdateCharacterInformation.getHealth());
								clientWorld.movePlayerGameCharacter(packetUpdateCharacterInformation.getId(),
										packetUpdateCharacterInformation.getX(), packetUpdateCharacterInformation.getY());
							}
						}

					} else if (object instanceof PacketClientDisconnect && clientWorld != null) {
						System.out.println("Got packet client disconnected");
						// Packet for removing player from world if disconnected.
						PacketClientDisconnect packetClientDisconnect = (PacketClientDisconnect) object;
						System.out.println("Client " + packetClientDisconnect.getId() + " disconnected." + "\n");
						clientWorld.getGameCharacter(packetClientDisconnect.getId()).removeBodyFromWorld();
						clientWorld.getWorldGameCharactersMap().remove(packetClientDisconnect.getId());

					} else if (object instanceof PacketNewEnemy && clientWorld != null) {
						System.out.println("Got packet new enemy");
						// Packet for adding enemy to game.
						PacketNewEnemy packetNewEnemy = (PacketNewEnemy) object;
						Enemy enemy = Enemy.createEnemy(packetNewEnemy.getBotHash(), packetNewEnemy.getxPosition(),
								packetNewEnemy.getyPosition(), clientWorld);
						clientWorld.addEnemy(enemy);

					} else if (object instanceof PacketUpdateEnemy && clientWorld != null) {
						// Packet for updating enemy position.
						PacketUpdateEnemy packetUpdateEnemy = (PacketUpdateEnemy) object;
						if (clientWorld.getEnemyMap().containsKey(packetUpdateEnemy.getBotHash())) {
							Enemy enemy = clientWorld.getEnemy(packetUpdateEnemy.getBotHash());
							enemy.xPosition = packetUpdateEnemy.getxPosition();
							enemy.setCurrentState(packetUpdateEnemy.getCurrentState());
							enemy.setFacingRight(packetUpdateEnemy.getFacingRight());
							enemy.setHealth(packetUpdateEnemy.getHealth());
						}
					} else if (object instanceof PacketLobbyInfo) {
						System.out.println("Got packet lobby info");
						// Packet for updating available lobby info
						PacketLobbyInfo packetLobbyInfo = (PacketLobbyInfo) object;
						Optional<Lobby> lobby = gameClient.getLobby(packetLobbyInfo.getLobbyHash());

						if (lobby.isPresent()) {
							if (packetLobbyInfo.isStartGame()) {
								gameClient.setGameWon(false);
								gameClient.setGameStart(true);
								gameClient.setMapPath(packetLobbyInfo.getMapPath());

							} else if (packetLobbyInfo.isToDelete()) {
								gameClient.removeAvailableLobby(packetLobbyInfo.getLobbyHash());
								gameClient.hostLeft();

							} else if (packetLobbyInfo.isUpdateInfo()) {
								System.out.println("Got " + packetLobbyInfo.getPlayers().size() + " players.");
								lobby.get().setPlayers(packetLobbyInfo.getPlayers());
								gameClient.refreshLobbyScreen();
								gameClient.refreshHostLobbyScreen();

							} else if (packetLobbyInfo.getPlayerToAdd() != null) {
								System.out.println("Got player to add.");
								lobby.get().addPlayer(packetLobbyInfo.getPlayerToAdd());
								System.out.println("Added player");
								System.out.println(lobby.get().getPlayers());
								gameClient.refreshLobbyScreen();
								gameClient.refreshHostLobbyScreen();

							}
						} else {
							Lobby newLobby = new Lobby(packetLobbyInfo.getLobbyHash());
							newLobby.setPlayers(packetLobbyInfo.getPlayers());
							gameClient.addAvailableLobby(newLobby);
							}

					} else if (object instanceof PacketSendNewLobby) {
						System.out.println("Got packet send new lobby");
						PacketSendNewLobby packetSendNewLobby = (PacketSendNewLobby) object;
						Lobby lobby = new Lobby(packetSendNewLobby.getLobbyHash());
						lobby.addPlayer(packetSendNewLobby.getCreatorId());
						gameClient.addAvailableLobby(lobby);
						if (packetSendNewLobby.getCreatorId() == connection.getID()) {
							gameClient.setMyLobby(lobby);
							gameClient.refreshHostLobbyScreen();
						}

					} else if (object instanceof PacketRemoveLobby) {
						System.out.println("Got packet remove lobby");
						PacketRemoveLobby packetRemoveLobby = (PacketRemoveLobby) object;
						if (gameClient.getLobby(packetRemoveLobby.getLobbyHash()).isPresent()) {
							gameClient.removeAvailableLobby(gameClient.getLobby(packetRemoveLobby.getLobbyHash()).get());
						}

					} else if (object instanceof PacketRemovePlayer) {
						System.out.println("Got packet Remove player");
						PacketRemovePlayer packetRemovePlayer = (PacketRemovePlayer) object;
						Optional<Lobby> lobby = gameClient.getLobby(packetRemovePlayer.getLobbyHash());

						if (lobby.isPresent() && packetRemovePlayer.getPlayerToRemove() != null) {
							lobby.get().removePlayer(packetRemovePlayer.getPlayerToRemove());
							gameClient.refreshLobbyScreen();
							gameClient.refreshHostLobbyScreen();
						}

					} else if (object instanceof PacketBullet && clientWorld != null
							&& clientWorld.getMyPlayerGameCharacter() != null) {
						// System.out.println("Got packet bullet");
						PacketBullet packetBullet = (PacketBullet) object;

						// If bullet collided with enemy, then remove enemy from game
						if (packetBullet.isHit()) {
							if (clientWorld.getEnemyMap().containsKey(packetBullet.getHitEnemy())) {
								Bullet bullet = clientWorld.getBulletById(packetBullet.getBulletId());
								Enemy enemy = clientWorld.getEnemy(packetBullet.getHitEnemy());
								clientWorld.handleBulletCollisionWithEnemy(bullet, enemy);
							}
						} else {
							// Check if bullet is already in client world
							if (clientWorld.isBulletInWorld(packetBullet.getBulletId())
									&& clientWorld.getMyPlayerGameCharacter() != null) {
								// System.out.println("got existing bullet");
								// If is then update coordinates
								Bullet bullet = clientWorld.getBulletById(packetBullet.getBulletId());
								bullet.setBulletCoordinates(packetBullet.getBulletX(), packetBullet.getBulletY());
							}
						}

					} else if (object instanceof PacketNewBullet) {
						System.out.println("Got packet new bullet");
						PacketNewBullet packetNewBullet = (PacketNewBullet) object;

						if (!clientWorld.getCollidedBullets().contains(packetNewBullet.getBulletId())) {
							System.out.println(packetNewBullet.getBulletId());
							System.out.println("Created new bullet with id: "+ packetNewBullet.getBulletId());
							Bullet bullet = new Bullet(packetNewBullet.getBulletId(), packetNewBullet.isPlayerBullet());
							bullet.setBulletCoordinates(packetNewBullet.getPlayerX(), packetNewBullet.getPlayerY());
							clientWorld.addBulletToAdd(bullet);
							gameScreen.makeShootingSound();
						}

					} else if (object instanceof PacketAddCoin) {
						System.out.println("Got packet add coin");
						PacketAddCoin packetAddCoin = (PacketAddCoin) object;
						// Create coin object
						gameScreen.setCoinCoords(packetAddCoin.getxCoordinate(), packetAddCoin.getyCoordinate());

					} else if (object instanceof PacketWon) {
						System.out.println("Got packet won");
						gameClient.setGameWon(true);
					}
				}
			}
		});

		try {
			// Connected to the server - wait 5000ms before failing.
			client.connect(5000, ip, tcpPort);
		} catch (IOException exception) {
			JOptionPane.showMessageDialog(null, "Can not connect to the Server.");
			return;
		}
	}

	/**
	 * Send PacketConnect to the server.
	 * This is sent when a client wants to connect to the server.
	 */
	public void sendPacketConnect() {
		System.out.println("sent packet connect!");
		PacketConnect packetConnect = PacketCreator.createPacketConnect();
		packetConnect.setLobbyHash(gameClient.getMyLobby().getLobbyHash());
		client.sendTCP(packetConnect);
		// System.out.println("packetConnect message -> " + packetConnect.getLobbyHash());
	}

	/**
	 * Send client's PlayerGameCharacter new coordinates and direction to the server.
	 *
	 * @param x of the PlayerGameCharacters x coordinate (float)
	 * @param y of the PlayerGameCharacters y coordinate (float)
	 */
	public void sendPlayerInformation(float x, float y, GameCharacter.State currentState, boolean isFacingRight, float health) {
		PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(client.getID(), x, y);
		packet.setLobbyHash(gameClient.getMyLobby().getLobbyHash());
		packet.setCurrentState(currentState);
		packet.setFacingRight(isFacingRight);
		packet.setHealth(health);
		client.sendTCP(packet);
	}

	/**
	 * Send packet for creating new lobby.
	 */
	public void sendCreateNewLobby() {
		PacketSendNewLobby packetSendNewLobby = PacketCreator.createPacketSendNewLobby();
		client.sendTCP(packetSendNewLobby);
	}

	/**
	 * Method for sending info to server that client is searching available lobbies.
	 */
	public void sendGetAvailableLobbies() {
		PacketGetAvailableLobbies packetGetAvailableLobbies = new PacketGetAvailableLobbies();
		client.sendTCP(packetGetAvailableLobbies);
	}

	/**
	 * Send packet that requests info about the lobby from server.
	 * @param lobbyHash lobby's hash.
	 */
	public void sendUpdateLobbyInfo(String lobbyHash) {
		PacketLobbyInfo packetLobbyInfo = PacketCreator.createPacketLobbyInfo(lobbyHash);
		packetLobbyInfo.setUpdateInfo(true);
		client.sendTCP(packetLobbyInfo);
	}

	/**
	 * Send packet that informs that player joined the lobby.
	 * @param lobbyHash lobby's hash.
	 */
	public void sendAddPlayerToLobby(String lobbyHash) {
		PacketLobbyInfo packetLobbyInfo = PacketCreator.createPacketLobbyInfo(lobbyHash);
		packetLobbyInfo.setPlayerToAdd(gameClient.getConnectionId());
		client.sendTCP(packetLobbyInfo);
	}

	/**
	 * Send packet that informs that player left the lobby.
	 * @param lobbyHash lobby's hash.
	 */
	public void sendRemovePlayerFromLobby(String lobbyHash) {
		PacketRemovePlayer packetRemovePlayer = PacketCreator.createPacketRemovePlayer(lobbyHash, gameClient.getConnectionId());
		client.sendTCP(packetRemovePlayer);
	}

	/**
	 * Send packet that informs that host started the game.
	 * @param lobbyHash lobby's hash.
	 * @param mapPath chosen map's path.
	 */
	public void sendLobbyStartGame(String lobbyHash, String mapPath) {
		PacketLobbyInfo packetLobbyInfo = PacketCreator.createPacketLobbyInfo(lobbyHash);
		System.out.println("sendLobbyStartGame in ClientConnection -> " + packetLobbyInfo.getLobbyHash());
		packetLobbyInfo.setStartGame(true);
		packetLobbyInfo.setMapPath(mapPath);
		client.sendTCP(packetLobbyInfo);
	}

	/**
	 * Send packet that informs that lobby is going to be deleted.
	 * @param lobbyHash lobby's hash.
	 */
	public void sendDeleteLobby(String lobbyHash) {
		PacketLobbyInfo packetLobbyInfo = PacketCreator.createPacketLobbyInfo(lobbyHash);
		packetLobbyInfo.setToDelete(true);
		client.sendTCP(packetLobbyInfo);
	}

	/**
	 * Send info that bullet was shot.
	 * @param lobbyHash lobby's hash.
	 * @param playerX player x coordinate.
	 * @param playerY player y coordinate.
	 * @param mouseX mouse x coordinate.
	 * @param mouseY mouse y coordinate.
	 */
	public void sendNewBullet(String lobbyHash, float playerX, float playerY, float mouseX, float mouseY) {
		PacketNewBullet packetNewBullet = PacketCreator.createPacketNewBullet(lobbyHash, mouseX, mouseY, playerX, playerY);
		client.sendTCP(packetNewBullet);
	}

	/**
	 * Send packet that informs that enemy is killed.
	 * @param lobbyHash lobby's hash.
	 * @param botHash bot hash.
	 */
	public void sendEnemyHit(String lobbyHash, String botHash, int bulletId) {
		PacketBullet packetBullet = PacketCreator.createPacketBullet(lobbyHash);
		packetBullet.setIsHit(true);
		packetBullet.setHitEnemy(botHash);
		packetBullet.setBulletId(bulletId);
		client.sendTCP(packetBullet);
	}

	/**
	 * Send updated enemy info.
	 * @param lobbyHash lobby hash.
	 * @param botHash bot hash.
	 */
	public void sendUpdatedEnemy(String lobbyHash, String botHash) {
		PacketUpdateEnemy packetUpdateEnemy = new PacketUpdateEnemy();
		packetUpdateEnemy.setBotHash(botHash);
		packetUpdateEnemy.setLobbyHash(lobbyHash);
		packetUpdateEnemy.setyPosition(clientWorld.getEnemyMap().get(botHash).yPosition);
		client.sendTCP(packetUpdateEnemy);
	}

	/**
	 * Method for sending info that player has died.
	 * @param lobbyHash lobby hash.
	 * @param id died player id.
	 */
	public void sendPlayerDead(String lobbyHash, int id) {
		PacketUpdateCharacterInformation packetUpdateCharacterInformation = new PacketUpdateCharacterInformation();
		packetUpdateCharacterInformation.setLobbyHash(lobbyHash);
		packetUpdateCharacterInformation.setId(id);
		packetUpdateCharacterInformation.setDead(true);
		client.sendTCP(packetUpdateCharacterInformation);
	}

	/**
	 * Set game screen.
	 * @param gameScreen game screen.
	 */
	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	/**
	 * Set client world.
	 * @param clientWorld client world.
	 */
	public void setClientWorld(ClientWorld clientWorld){
		this.clientWorld = clientWorld;
	}

	/**
	 * Set game client.
	 * @param gameClient game client.
	 */
	public void setGameClient(GameClient gameClient) {
		this.gameClient = gameClient;
	}

	/**
	 * Get game client.
	 * @return game client.
	 */
	public GameClient getGameClient() {
		return gameClient;
	}

	public static void main(String[] args) {
		new ClientConnection();  // Runs the main application.
	}
}
