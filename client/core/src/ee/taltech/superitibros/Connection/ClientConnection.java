package ee.taltech.superitibros.Connection;

import com.badlogic.gdx.Game;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.superitibros.Screens.GameScreen;
import packets.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientConnection extends Game {
	// Game camera size
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;

	// Other players
	public static HashMap<Integer, ArrayList<Integer>> characters = new HashMap<>();

	private static Client client;

	@Override
	public void create() {
		// Create new client
		client = new Client();
		client.start();
		client.getKryo().register(Packet.class);

		// Set game screen
		setScreen(new GameScreen(this));

		// Connect to ports
		try {
			client.connect(5000, "localhost", 8080, 8081);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Listen for incoming packets from server
		client.addListener(new Listener.ThreadedListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof Packet) {
					Packet packet = (Packet) object;
					System.out.println(packet.getId());
					// Add other player coordinates to array
					ArrayList<Integer> coordinates = new ArrayList<>();
					coordinates.add(packet.getX());
					coordinates.add(packet.getY());
					// Put other player id and coordinates to hashmap
					characters.put(packet.getId(), coordinates);
					System.out.println(characters);
				}
			}
		}));
	}

	// Render
	@Override
	public void render() {
		super.render();
	}

	// Send player position info to server
	public static void sendPositionInfoToServer(int x, int y) {
		Packet packet = new Packet();
		packet.setX(x);
		packet.setY(y);
		// Use playerId generated for this client
		client.sendUDP(packet);
	}

	// Dispose
	@Override
	public void dispose() {
		client.close();
		try {
			client.dispose();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
