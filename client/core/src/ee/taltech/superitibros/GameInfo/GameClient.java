package ee.taltech.superitibros.GameInfo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import ee.taltech.superitibros.Connection.ClientConnection;
import ee.taltech.superitibros.Screens.GameScreen;
import ee.taltech.superitibros.Screens.MainMenu;
import ee.taltech.superitibros.Screens.MenuScreen;

import java.io.IOException;

public class GameClient extends Game {

    private GameScreen gameScreen;
    private ClientConnection clientConnection;
    private ClientWorld clientWorld;
    private MenuScreen menuScreen;

    /**
     * Method creates a new Client who connects to the Server with its ClientWorld and GameScreen.
     */
    public void createClient(GameScreen gameScreen) throws IOException {
        clientConnection = new ClientConnection();
        clientConnection.setGameScreen(gameScreen);
        clientConnection.setClientWorld(clientWorld);
        clientConnection.setGameClient(this);
        clientConnection.sendPacketConnect();
        gameScreen.registerClientConnection(clientConnection);
        clientWorld.registerClient(clientConnection);
    }

    /**
     * Sets the screen to game over screen.
     */
    public void setScreenToGameOver() {
        // Implement your game over screen logic here
    }

    /**
     * Creates the menu screen.
     */
    @Override
    public void create() {
        // Create and set the menu screen as the initial screen
        this.menuScreen = new MenuScreen(this);
        setScreen(menuScreen);
    }

    /**
     * Starts a game and tries to create a new client.
     */
    public void startGame(String path) {
        // Create a new game screen and transition to it
        clientWorld = new ClientWorld(path);
        gameScreen = new GameScreen(clientWorld);
        setScreen(gameScreen);
        try {
            createClient(gameScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gdx.input.setInputProcessor(gameScreen);
    }

    /**
     * Disposes the screen.
     */
    @Override
    public void dispose() {
        super.dispose();
        if (gameScreen != null) {
            gameScreen.dispose();
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
