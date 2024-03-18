package ee.taltech.superitibros.Connection;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import ee.taltech.superitibros.Characters.GameCharacter;
import ee.taltech.superitibros.Characters.MyPlayerGameCharacter;
import ee.taltech.superitibros.Characters.PlayerGameCharacter;
import ee.taltech.superitibros.Screens.GameScreen;
import ee.taltech.superitibros.GameInfo.GameClient;
import ee.taltech.superitibros.GameInfo.ClientWorld;
import packets.*;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
		int udpPort = 5007, tcpPort = 5008;


		client = new Client(49152, 49152);
		client.start();

		// Register all packets that are sent over the network.
		client.getKryo().register(Packet.class);
		client.getKryo().register(PacketConnect.class);
		client.getKryo().register(PacketAddCharacter.class);
		client.getKryo().register(GameCharacter.class);
		client.getKryo().register(PacketUpdateCharacterInformation.class);
		client.getKryo().register(PacketCreator.class);
		client.getKryo().register(ArrayList.class);
		client.getKryo().register(Rectangle.class);
		client.getKryo().register(HashMap.class);
		client.getKryo().register(PacketClientDisconnect.class);
		client.getKryo().register(GameCharacter.State.class);

		// Add a listener to handle receiving objects.
		client.addListener(new Listener.ThreadedListener(new Listener()) {

			// Receive packets from the Server.
			public void received(Connection connection, Object object) {
				if (object instanceof Packet) {
					if (object instanceof PacketAddCharacter) {
						PacketAddCharacter packetAddCharacter = (PacketAddCharacter) object;
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
					} else  if (object instanceof PacketUpdateCharacterInformation) {
						PacketUpdateCharacterInformation packetUpdateCharacterInformation = (PacketUpdateCharacterInformation) object;
						if (clientWorld.getWorldGameCharactersMap().containsKey(packetUpdateCharacterInformation.getId()) && connection.getID() != packetUpdateCharacterInformation.getId()) {
							// Update PlayerGameCharacter's coordinates.
							System.out.println("client connection x pos: " + packetUpdateCharacterInformation.getX());
							System.out.println("packet update character pos");
							PlayerGameCharacter gameCharacter = (PlayerGameCharacter) clientWorld.getGameCharacter(packetUpdateCharacterInformation.getId());
							gameCharacter.state = packetUpdateCharacterInformation.getCurrentState();
							System.out.println(packetUpdateCharacterInformation.getCurrentState());
							System.out.println("got state: " + packetUpdateCharacterInformation.getCurrentState());
							gameCharacter.setFacingRight(packetUpdateCharacterInformation.getFacingRight());
							clientWorld.movePlayerGameCharacter(packetUpdateCharacterInformation.getId(),
									packetUpdateCharacterInformation.getX(), packetUpdateCharacterInformation.getY());
						}

					} else if (object instanceof PacketClientDisconnect) {
						PacketClientDisconnect packetClientDisconnect = (PacketClientDisconnect) object;
						System.out.println("Client " + packetClientDisconnect.getId() + " disconnected.");
						clientWorld.getGameCharacter(packetClientDisconnect.getId()).removeBodyFromWorld();
						clientWorld.getWorldGameCharactersMap().remove(packetClientDisconnect.getId());
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
	 * @param x of the PlayerGameCharacters x coordinate (float)
	 * @param y of the PlayerGameCharacters y coordinate (float)
	 */
	public void sendPlayerInformation(float x, float y, GameCharacter.State currentState, boolean isFacingRight) {
		System.out.println("Send player info");
		System.out.println("sent: " + x);
		PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(client.getID(), x, y);
		System.out.println("sent state: " + currentState);
		packet.setCurrentState(currentState);
		packet.setFacingRight(isFacingRight);
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
