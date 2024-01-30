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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperItiBros extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture opponentImg;

	private int x = 0, y = 0;

	private Client client;

	private String lastReceived;

	private Map<Integer, SpriteBatch> opponentBatches = new HashMap<>();
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		opponentImg = new Texture("badlogic.jpg");

		client = new Client();
		client.start();
		client.sendTCP("Start");
        try {
            client.connect(5000, "localhost", 8080, 8081);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
		client.addListener(new Listener.ThreadedListener(new Listener() {
			public void received(Connection connection, Object object) {
				System.out.println("received: " + object);
				if (object instanceof String) {
					lastReceived = (String) object;
				}
			}
        }));
    }

	private void sendPositionInfoToServer() {
		client.sendUDP(x + "," + y);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			x -= 10;
			sendPositionInfoToServer();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			x += 10;
			sendPositionInfoToServer();
		}
		batch.begin();
		batch.draw(img, x, y);
		batch.end();

		if (lastReceived != null) {
			// id:x,y
			String[] opponentDatas = lastReceived.split("|");
			for (String opponentData : opponentDatas) {
				String[] parts = opponentData.split(":");
				int id = Integer.parseInt(parts[0]);
				if (!opponentBatches.containsKey(id)) {
					opponentBatches.put(id, new SpriteBatch());
				}
				String[] coordinates = parts[1].split(",");
				opponentBatches.get(id).begin();
				opponentBatches.get(id).draw(opponentImg, Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
				opponentBatches.get(id).end();
			}
			lastReceived = null;
		}
	}
	
	@Override
	public void dispose () {
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
