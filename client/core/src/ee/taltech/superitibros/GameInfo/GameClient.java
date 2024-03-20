package ee.taltech.superitibros.GameInfo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import ee.taltech.superitibros.Connection.ClientConnection;
import ee.taltech.superitibros.Lobbies.Lobby;
import ee.taltech.superitibros.Screens.GameScreen;
import ee.taltech.superitibros.Screens.MainMenu;
import ee.taltech.superitibros.Screens.MenuScreen;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GameClient extends Game {

    private GameScreen gameScreen;
    private ClientConnection clientConnection;
    private ClientWorld clientWorld;
    private MenuScreen menuScreen;
    List<Lobby> availableLobbies = new LinkedList<>();

    /**
     * Method creates a new Client who connects to the Server with its ClientWorld and GameScreen.
     */
    public void createClient(GameScreen gameScreen) throws IOException {
        clientConnection.setGameScreen(gameScreen);
        clientConnection.setClientWorld(clientWorld);
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
        clientConnection = new ClientConnection();
        clientConnection.setGameClient(this);
        // Create and set the menu screen as the initial screen
        this.menuScreen = new MenuScreen(this);
        setScreen(menuScreen);
    }

    /**
     * Starts a game and tries to create a new client.
     */
    public void startGame() {
        // Create a new game screen and transition to it
        clientWorld = new ClientWorld();
        gameScreen = new GameScreen(clientWorld);
        setScreen(gameScreen);
        try {
            createClient(gameScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gdx.input.setInputProcessor(gameScreen);
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public void addAvailableLobby(Lobby lobby) {
        boolean lobbyExists = false;
        for (Lobby existingLobby : availableLobbies) {
            if (existingLobby.getLobbyHash().equals(lobby.getLobbyHash())) {
                lobbyExists = true;
                break;
            }
        }
        if (!lobbyExists) {
            availableLobbies.add(lobby);
        }
        System.out.println("added lobby: " + availableLobbies);
    }

    public List<Lobby> getAvailableLobbies() {
        return this.availableLobbies;
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
