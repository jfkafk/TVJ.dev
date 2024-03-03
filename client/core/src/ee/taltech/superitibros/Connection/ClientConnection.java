package ee.taltech.superitibros.Connection;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import ee.taltech.superitibros.Characters.GameCharacter;
import ee.taltech.superitibros.Characters.PlayerGameCharacter;
import ee.taltech.superitibros.Screens.GameScreen;
import ee.taltech.superitibros.GameInfo.GameClient;
import ee.taltech.superitibros.GameInfo.ClientWorld;
import packets.*;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class ClientConnection {

	private GameScreen gameScreen;
	private ClientWorld clientWorld;
	private GameClient gameClient;
	private static Client client;
	private String playerName;

	private static final int SCORE_COEFFICIENT = 100;

	/**
	 * Client connection.
	 */
	public ClientConnection() {

		String ip = "127.0.0.1";
		// Server 193.40.255.23
		// local  127.0.0.1
		int udpPort = 8081, tcpPort = 8080;


		client = new Client(49152, 49152);
		client.start();

		// Register all packets that are sent over the network.
		client.getKryo().register(Packet.class);
		client.getKryo().register(PacketConnect.class);
		client.getKryo().register(PacketAddCharacter.class);
		client.getKryo().register(GameCharacter.class);
		client.getKryo().register(PacketMoveCharacter.class);
		client.getKryo().register(PacketCreator.class);
		client.getKryo().register(GameCharacter.class);
		client.getKryo().register(PlayerGameCharacter.class);


		// Add a listener to handle receiving objects.
		client.addListener(new Listener.ThreadedListener(new Listener()) {

			// Receive packets from the Server.
			public void received(Connection connection, Object object) {
				if (object instanceof Packet) {
					if (object instanceof PacketAddCharacter) {
						PacketAddCharacter packetAddCharacter = (PacketAddCharacter) object;
						// Create a new PlayerGameCharacter instance from received info.
						PlayerGameCharacter newGameCharacter = PlayerGameCharacter.createPlayerGameCharacter(packetAddCharacter.getX(), packetAddCharacter.getY(), packetAddCharacter.getId());
						// Add new PlayerGameCharacter to client's game.
						clientWorld.addGameCharacter(packetAddCharacter.getId(), newGameCharacter);
						if (packetAddCharacter.getId() == connection.getID()) {
							// If new PlayerGameCharacter is client's PlayerGameCharacter.
							clientWorld.setMyPlayerGameCharacter(newGameCharacter);
						}

					} else  if (object instanceof PacketMoveCharacter) {
						PacketMoveCharacter packetUpdateCharacterInformation = (PacketMoveCharacter) object;
						if (clientWorld.getWorldGameCharactersMap().containsKey(packetUpdateCharacterInformation.getId())) {
							// Update PlayerGameCharacter's coordinates, direction and health.
							clientWorld.movePlayerGameCharacter(packetUpdateCharacterInformation.getId(),
									packetUpdateCharacterInformation.getX(), packetUpdateCharacterInformation.getY());
						}

					}
				}
			}
		});

		try {
			// Connected to the server - wait 5000ms before failing.
			client.connect(5000, ip, tcpPort, udpPort);
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
		client.sendTCP(packetConnect);
	}

	/**
	 * Send client's PlayerGameCharacter new coordinates and direction to the server.
	 *
	 * @param xChange of the PlayerGameCharacters x coordinate (float)
	 * @param yChange of the PlayerGameCharacters y coordinate (float)
	 */
	public void sendPlayerInformation(float xChange, float yChange) {
		System.out.println("Send player info");
		PacketMoveCharacter packet = PacketCreator.createPacketUpdateCharacterInformation(client.getID(), xChange, yChange);
		client.sendUDP(packet);
	}


	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setClientWorld(ClientWorld clientWorld){
		this.clientWorld = clientWorld;
	}

	public void setGameClient(GameClient gameClient) {
		this.gameClient = gameClient;
	}

	public GameClient getGameClient() {
		return gameClient;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public static void main(String[] args) {
		new ClientConnection();  // Runs the main application.
	}
}
