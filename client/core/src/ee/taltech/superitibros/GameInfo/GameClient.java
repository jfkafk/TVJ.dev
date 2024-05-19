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
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient extends Game {

    private GameScreen gameScreen;
    private ClientConnection clientConnection;
    private ClientWorld clientWorld;
    private MultiplayerMenu multiplayerMenu;
    private final List<Lobby> availableLobbies = new LinkedList<>();
    private Lobby myLobby;
    private LobbyScreen lobbyScreen;
    private HostLobby hostLobbyScreen;
    private Integer connectionId;
    private String mapPath;
    private boolean gameStart;
    private boolean gameWon;

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
     * Creates the menu screen.
     */
    @Override
    public void create() {
        clientConnection = new ClientConnection();
        clientConnection.setGameClient(this);
        // Create and set the menu screen as the initial screen
        MenuScreen menuScreen = new MenuScreen(this);
        setScreen(menuScreen);
    }

    /**
     * Starts a game.
     */
    public void startGame(String path) {
        Logger logger = Logger.getLogger(getClass().getName());

        // Create a new game screen and transition to it
        clientWorld = new ClientWorld(path);
        gameScreen = new GameScreen(clientWorld);
        gameWon = false;
        setScreen(gameScreen);

        try {
            createClient(gameScreen);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred in startGame of GameClient", e);
        }

        gameStart = false;
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
        System.out.println("added lobby: " + availableLobbies + "\n");
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
     * Set map path.
     * @param mapPath map's path.
     */
    public void setMapPath(String mapPath) {
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
     * Set multiplayer menu.
     * @param multiplayerMenu multiplayer menu.
     */
    public void setMultiplayerMenu(MultiplayerMenu multiplayerMenu) {
        this.multiplayerMenu = multiplayerMenu;
    }

    /**
     * Get multiplayer menu.
     * @return multiplayer menu.
     */
    public MultiplayerMenu getMultiplayerMenu() {
        return multiplayerMenu;
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
     * Set status to game won.
     */
    public void setGameWon(boolean won) {
        this.gameWon = won;
    }

    /**
     * Return if game won.
     * @return boolean if game won.
     */
    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * Set game to start.
     * @param gameStart is game start.
     */
    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }

    /**
     * Return if game start.
     * @return true if game to start.
     */
    public boolean isGameStart() {
        return gameStart;
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
