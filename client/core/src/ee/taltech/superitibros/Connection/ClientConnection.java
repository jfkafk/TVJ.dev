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
import java.util.*;


public class ClientConnection {

	private GameScreen gameScreen;
	private ClientWorld clientWorld;
	private GameClient gameClient;
	private static Client client;

	/**
	 * Client connection.
	 */
	public ClientConnection() {

		String ip = "127.0.0.1";
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
		client.getKryo().register(PacketCreator.class);
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

		// Add a listener to handle receiving objects.
		client.addListener(new Listener.ThreadedListener(new Listener()) {

			// Receive packets from the Server.
			public void received(Connection connection, Object object) {

				gameClient.setConnectionId(connection.getID());

				if (object instanceof Packet) {

					if (object instanceof PacketAddCharacter) {
						if (gameScreen != null && clientWorld != null) {
							// Packet for adding player to game.
							PacketAddCharacter packetAddCharacter = (PacketAddCharacter) object;
							if (!clientWorld.getWorldGameCharactersMap().containsKey(packetAddCharacter.getId())) {
								if (connection.getID() == ((PacketAddCharacter) object).getId()) {
									MyPlayerGameCharacter newGameCharacter = MyPlayerGameCharacter.createMyPlayerGameCharacter(packetAddCharacter.getX(), packetAddCharacter.getY(), packetAddCharacter.getId(), clientWorld);
									// Add new PlayerGameCharacter to client's game.
									clientWorld.addGameCharacter(packetAddCharacter.getId(), newGameCharacter);
								} else {
									// Create a new PlayerGameCharacter instance from received info.
									PlayerGameCharacter newGameCharacter = PlayerGameCharacter.createPlayerGameCharacter(packetAddCharacter.getX(), packetAddCharacter.getY(), packetAddCharacter.getId(), clientWorld);
									// Add new PlayerGameCharacter to client's game.
									clientWorld.addGameCharacter(packetAddCharacter.getId(), newGameCharacter);
								}
							}
						}

					} else if (object instanceof PacketUpdateCharacterInformation && clientWorld != null) {
						// Packet for updating player position.
						PacketUpdateCharacterInformation packetUpdateCharacterInformation = (PacketUpdateCharacterInformation) object;
						if (gameScreen != null && clientWorld.getWorldGameCharactersMap().containsKey(packetUpdateCharacterInformation.getId()) && connection.getID() != packetUpdateCharacterInformation.getId()) {
							// Update PlayerGameCharacter's coordinates.
							PlayerGameCharacter gameCharacter = (PlayerGameCharacter) clientWorld.getGameCharacter(packetUpdateCharacterInformation.getId());
							gameCharacter.state = packetUpdateCharacterInformation.getCurrentState();
							// System.out.println(packetUpdateCharacterInformation.getCurrentState());
							System.out.println("Got y: " + packetUpdateCharacterInformation.getY());
							gameCharacter.setFacingRight(packetUpdateCharacterInformation.getFacingRight());
							clientWorld.movePlayerGameCharacter(packetUpdateCharacterInformation.getId(),
									packetUpdateCharacterInformation.getX(), packetUpdateCharacterInformation.getY());
						}

					} else if (object instanceof PacketClientDisconnect && clientWorld != null) {
						// Packet for removing player from world if disconnected.
						PacketClientDisconnect packetClientDisconnect = (PacketClientDisconnect) object;
						System.out.println("Client " + packetClientDisconnect.getId() + " disconnected.");
						clientWorld.getGameCharacter(packetClientDisconnect.getId()).removeBodyFromWorld();
						clientWorld.getWorldGameCharactersMap().remove(packetClientDisconnect.getId());

					} else if (object instanceof PacketNewEnemy && clientWorld != null) {
						// Packet for adding enemy to game.
						PacketNewEnemy packetNewEnemy = (PacketNewEnemy) object;
						Enemy enemy = Enemy.createEnemy(packetNewEnemy.getBotHash(), packetNewEnemy.getxPosition(), packetNewEnemy.getyPosition(), clientWorld);
						clientWorld.addEnemy(enemy);

					} else if (object instanceof PacketUpdateEnemy && clientWorld != null) {
						// Packet for updating enemy position.
						PacketUpdateEnemy packetUpdateEnemy = (PacketUpdateEnemy) object;
						if (clientWorld.getEnemyMap().containsKey(packetUpdateEnemy.getBotHash())) {
							Enemy enemy = clientWorld.getEnemy(packetUpdateEnemy.getBotHash());
							enemy.xPosition = packetUpdateEnemy.getxPosition();
							enemy.setCurrentState(packetUpdateEnemy.getCurrentState());
							enemy.setFacingRight(packetUpdateEnemy.getFacingRight());
							//System.out.println("Health from packet: " + packetUpdateEnemy.getHealth());
							enemy.setHealth(packetUpdateEnemy.getHealth());

							// clientWorld.getEnemy(packetUpdateEnemy.getBotHash()).yPosition = packetUpdateEnemy.getyPosition();
							// System.out.println("Enemy Y: " + packetUpdateEnemy.getyPosition());
						}
					} else if (object instanceof PacketLobbyInfo) {
						// Packet for updating available lobby info
						PacketLobbyInfo packetLobbyInfo = (PacketLobbyInfo) object;
						Optional<Lobby> lobby = gameClient.getLobby(packetLobbyInfo.getLobbyHash());

						// Start game if startGame true
						if (packetLobbyInfo.isStartGame()) {
							gameClient.readyToStart(packetLobbyInfo.getMapPath());

						} else if (packetLobbyInfo.isToDelete()) {
							gameClient.removeAvailableLobby(packetLobbyInfo.getLobbyHash());
							gameClient.hostLeft();

						} else if (packetLobbyInfo.isUpdateInfo()) {
							lobby.get().setPlayers(packetLobbyInfo.getPlayers());
							gameClient.refreshLobbyScreen();
							gameClient.refreshHostLobbyScreen();

						} else if (packetLobbyInfo.getPlayerToAdd() != null) {
							lobby.get().addPLayer(packetLobbyInfo.getPlayerToAdd());
							gameClient.refreshLobbyScreen();
							gameClient.refreshHostLobbyScreen();

						} else if (packetLobbyInfo.getPlayerToRemove() != null) {
							lobby.get().removePlayer(packetLobbyInfo.getPlayerToRemove());
							gameClient.refreshLobbyScreen();
							gameClient.refreshHostLobbyScreen();
						}

					} else if (object instanceof PacketSendNewLobby) {
						PacketSendNewLobby packetSendNewLobby = (PacketSendNewLobby) object;
						Lobby lobby = new Lobby(packetSendNewLobby.getLobbyHash());
						lobby.addPLayer(packetSendNewLobby.getCreatorId());
						gameClient.addAvailableLobby(lobby);
						if (packetSendNewLobby.getCreatorId() == connection.getID()) {
							gameClient.setMyLobby(lobby);
						}

					} else if (object instanceof PacketRemoveLobby) {
						PacketRemoveLobby packetRemoveLobby = (PacketRemoveLobby) object;
						gameClient.removeAvailableLobby(gameClient.getLobby(packetRemoveLobby.getLobbyHash()).get());

					} else if (object instanceof PacketBullet && clientWorld != null) {
						PacketBullet packetBullet = (PacketBullet) object;

						// If bullet collided with enemy, then remove enemy from game
						if (packetBullet.isKilled()) {
							if (clientWorld.getEnemyMap().containsKey(packetBullet.getKilledBot())) {
								clientWorld.removeEnemy(packetBullet.getKilledBot());
							}
						} else {

							// Check if bullet is already in client world
							if (clientWorld.isBulletInWorld(packetBullet.getBulletId())) {
								// System.out.println("got existing bullet");
								// If is then update coordinates
								Bullet bullet = clientWorld.getBulletById(packetBullet.getBulletId());
								bullet.setBulletX(packetBullet.getBulletX());
								bullet.setBulletY(packetBullet.getBulletY());

							} else {
								// System.out.println("got new bullet");
								// If not, create new bullet and add to client world
								// Check is bullet has collided already with solid object
								if (!clientWorld.getCollidedBullets().contains(packetBullet.getBulletId())) {
									Bullet bullet = new Bullet(packetBullet.getBulletId());
									bullet.setBulletX(packetBullet.getBulletX());
									bullet.setBulletY(packetBullet.getBulletY());
									clientWorld.addBulletToAdd(bullet);
								}
							}
						}
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
	 *
	 * This is sent when a client wants to connect to the server.
	 */
	public void sendPacketConnect() {
		PacketConnect packetConnect = PacketCreator.createPacketConnect();
		packetConnect.setLobbyHash(gameClient.getMyLobby().getLobbyHash());
		client.sendTCP(packetConnect);
	}

	/**
	 * Send client's PlayerGameCharacter new coordinates and direction to the server.
	 *
	 * @param x of the PlayerGameCharacters x coordinate (float)
	 * @param y of the PlayerGameCharacters y coordinate (float)
	 */
	public void sendPlayerInformation(float x, float y, GameCharacter.State currentState, boolean isFacingRight) {
		PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(client.getID(), x, y);
		packet.setLobbyHash(gameClient.getMyLobby().getLobbyHash());
		// System.out.println("send update character with lobby hash: " + gameClient.getMyLobby().getLobbyHash());
		System.out.println("Sent y: " + y);
		//System.out.println("My player y: " + clientWorld.getMyPlayerGameCharacter().yPosition);
		packet.setCurrentState(currentState);
		packet.setFacingRight(isFacingRight);
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
		PacketLobbyInfo packetLobbyInfo = PacketCreator.createPacketLobbyInfo(lobbyHash);
		packetLobbyInfo.setPlayerToRemove(gameClient.getConnectionId());
		client.sendTCP(packetLobbyInfo);
	}

	/**
	 * Send packet that informs that host started the game.
	 * @param lobbyHash lobby's hash.
	 * @param mapPath chosen map's path.
	 */
	public void sendLobbyStartGame(String lobbyHash, String mapPath) {
		PacketLobbyInfo packetLobbyInfo = PacketCreator.createPacketLobbyInfo(lobbyHash);
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
	public void sendBullet(String lobbyHash, float playerX, float playerY, float mouseX, float mouseY) {
		PacketBullet packetBullet = PacketCreator.createPacketBullet(lobbyHash);
		packetBullet.setPlayerX(playerX);
		packetBullet.setPlayerY(playerY);
		packetBullet.setMouseX(mouseX);
		packetBullet.setMouseY(mouseY);
		client.sendTCP(packetBullet);
	}

	/**
	 * Send packet that informs that enemy is killed.
	 * @param lobbyHash lobby's hash.
	 * @param botHash bot's hash.
	 */
	public void sendKilledEnemy(String lobbyHash, String botHash) {
		PacketBullet packetBullet = PacketCreator.createPacketBullet(lobbyHash);
		packetBullet.setKilled(true);
		packetBullet.setKilledBot(botHash);
		client.sendTCP(packetBullet);
	}

	/**
	 * Set game screen.
	 * @param gameScreen game screen.
	 */
	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	/**
	 * Get game screen.
	 * @return game screen.
	 */
	public GameScreen getGameScreen() {
		return gameScreen;
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
