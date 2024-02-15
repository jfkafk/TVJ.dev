package ee.taltech.superitibros;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.superitibros.Screens.GameScreen;
import packets.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SuperItiBros extends Game {
	public static final int V_WIDTH = 2000;
	public static final int V_HEIGHT = 1500;

	public static SpriteBatch batch;
	Texture img;
	public static Texture opponentImg;
	public static HashMap<Integer, ArrayList<Integer>> characters = new HashMap<>();
	private static Client client;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("Characters/TestCharacter.png");
		opponentImg = new Texture("Characters/TestCharacter.png");

		client = new Client();
		client.start();

		client.getKryo().register(Packet.class);

		setScreen(new GameScreen(this));

		try {
			client.connect(5000, "localhost", 8080, 8081);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		client.addListener(new Listener.ThreadedListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof Packet) {
					Packet packet = (Packet) object;
					System.out.println(packet.getId());
					ArrayList<Integer> coordinates = new ArrayList<>();
					coordinates.add(packet.getX());
					coordinates.add(packet.getY());
					characters.put(client.getID(), coordinates);
					System.out.println(characters);
				}
			}
		}));
	}

	@Override
	public void render() {
		super.render(); // This line is missing

	}

	public static void sendPositionInfoToServer(int x, int y) {
		Packet packet = new Packet();
		packet.setX(x);
		packet.setY(y);
        client.sendUDP(packet);
    }

	@Override
	public void dispose() {
		client.close();
		try {
			client.dispose();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		batch.dispose();
		img.dispose();
	}
}
