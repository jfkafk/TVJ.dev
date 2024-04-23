package ee.taltech.superitibros.GameInfo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import ee.taltech.superitibros.Connection.ClientConnection;
import ee.taltech.superitibros.Lobbies.Lobby;
import ee.taltech.superitibros.Screens.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GameClient extends Game {

    private GameScreen gameScreen;
    private ClientConnection clientConnection;
    private ClientWorld clientWorld;
    private MenuScreen menuScreen;
    List<Lobby> availableLobbies = new LinkedList<>();
    Lobby myLobby;
    LobbyScreen lobbyScreen;
    HostLobby hostLobbyScreen;
    Integer connectionId;
    String mapPath;

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
     * Starts a game.
     */
    public void startGame(String path) {
        // Create a new game screen and transition to it
        clientWorld = new ClientWorld(path);
        gameScreen = new GameScreen(clientWorld);
        setScreen(gameScreen);
        menuScreen.stopSound();
        try {
            createClient(gameScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gdx.input.setInputProcessor(gameScreen);
    }

    /**
     * Get client connection.
     * @return client connection.
     */
    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    /**
     * Method for adding lobby to available lobbies.
     * @param lobby lobby to add.
     */
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

    /**
     * Method for removing lobby from available lobbies.
     * @param lobby to remove.
     */
    public void removeAvailableLobby(Lobby lobby) {
        availableLobbies.remove(lobby);
    }

    /**
     * Remove available lobby by lobby hash.
     * @param lobbyHash lobby's hash.
     */
    public void removeAvailableLobby(String lobbyHash) {
        availableLobbies.removeIf(lobby -> Objects.equals(lobby.getLobbyHash(), lobbyHash));
    }

    /**
     * Get lobby by hash.
     * @param lobbyHash lobby's hash.
     * @return lobby.
     */
    public Optional<Lobby> getLobby(String lobbyHash) {
        for (Lobby lobby : availableLobbies) {
            if (Objects.equals(lobby.getLobbyHash(), lobbyHash)) {
                return Optional.of(lobby);
            }
        }
        return Optional.empty();
    }

    /**
     * Method for setting lobby to ready to start.
     * @param mapPath map's path.
     */
    public void readyToStart(String mapPath) {
        lobbyScreen.setReadyToStart(true);
        lobbyScreen.refreshScreen();
        lobbyScreen.setMapPath(mapPath);
    }

    /**
     * Method for setting host left to true.
     */
    public void hostLeft() {
        if (lobbyScreen != null) {
            lobbyScreen.setHostLeft(true);
            lobbyScreen.refreshScreen();
        }
    }

    /**
     * Get available lobbies.
     * @return available lobbies.
     */
    public List<Lobby> getAvailableLobbies() {
        clientConnection.sendGetAvailableLobbies();
        return this.availableLobbies;
    }

    /**
     * Set my lobby.
     * @param myLobby lobby to set.
     */
    public void setMyLobby(Lobby myLobby) {
        this.myLobby = myLobby;
    }

    /**
     * Get my lobby.
     * @return my lobby.
     */
    public Lobby getMyLobby() {
        return myLobby;
    }

    /**
     * Set lobby screen.
     * @param lobbyScreen lobby screen.
     */
    public void setLobbyScreen(LobbyScreen lobbyScreen) {
        this.lobbyScreen = lobbyScreen;
    }

    /**
     * Set host lobby screen.
     * @param hostLobbyScreen host lobby screen.
     */
    public void setHostLobbyScreen(HostLobby hostLobbyScreen) {
        this.hostLobbyScreen = hostLobbyScreen;
    }

    /**
     * Set connection ID.
     * @param connectionId connection ID.
     */
    public void setConnectionId(Integer connectionId) {
        this.connectionId = connectionId;
    }

    /**
     * Get connection ID.
     * @return connection ID.
     */
    public Integer getConnectionId() {
        return connectionId;
    }

    /**
     * Refresh lobby screen.
     */
    public void refreshLobbyScreen() {
        if (lobbyScreen != null) {
            lobbyScreen.refreshScreen();
        }
    }

    /**
     * Refresh host lobby screen.
     */
    public void refreshHostLobbyScreen() {
        if (hostLobbyScreen != null) {
            hostLobbyScreen.refreshPlayers();
        }
    }

    /**
     * Update map path.
     * @param newPath new map path.
     */
    public void updateMapPath(String newPath) {
        this.mapPath = newPath;
    }

    /**
     * Get map path.
     * @return map path.
     */
    public String getMapPath() {
        return mapPath;
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
