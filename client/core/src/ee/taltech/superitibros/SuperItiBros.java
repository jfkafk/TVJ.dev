package ee.taltech.superitibros;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import packets.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SuperItiBros extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture opponentImg;

	private int x = 0, y = 0;
	private int id;
	private HashMap<Integer, ArrayList<Integer>> characters = new HashMap<>();
	private Client client;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("Characters/TestCharacter.png");
		opponentImg = new Texture("Characters/TestCharacter.png");

		client = new Client();
		client.start();

		client.getKryo().register(Packet.class);

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
		ScreenUtils.clear(1, 0, 0, 1);
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			x -= 10;
			sendPositionInfoToServer(new Packet());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			x += 10;
			sendPositionInfoToServer(new Packet());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			y += 10;
			sendPositionInfoToServer(new Packet());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			y -= 10;
			sendPositionInfoToServer(new Packet());
		}
		batch.begin();
		batch.draw(img, x, y);
		for (Map.Entry<Integer, ArrayList<Integer>> entry : characters.entrySet()) {
			ArrayList<Integer> coordinates = entry.getValue();
			int characterX = coordinates.get(0);
			int characterY = coordinates.get(1);
			batch.draw(opponentImg, characterX, characterY);
		}
		batch.end();
	}

	private void sendPositionInfoToServer(Packet info) {
		info.setX(x);
		info.setY(y);
		System.out.println(client.getID());
		client.sendUDP(info);
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
