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

    public void removeAvailableLobby(Lobby lobby) {
        availableLobbies.remove(lobby);
    }

    public void removeAvailableLobby(String lobbyHash) {
        availableLobbies.removeIf(lobby -> Objects.equals(lobby.getLobbyHash(), lobbyHash));
    }

    public Optional<Lobby> getLobby(String lobbyHash) {
        for (Lobby lobby : availableLobbies) {
            if (Objects.equals(lobby.getLobbyHash(), lobbyHash)) {
                return Optional.of(lobby);
            }
        }
        return Optional.empty();
    }

    public void readyToStart() {
        lobbyScreen.setReadyToStart(true);
        lobbyScreen.refreshScreen();
    }

    public void hostLeft() {
        lobbyScreen.setHostLeft(true);
        lobbyScreen.refreshScreen();
    }

    public List<Lobby> getAvailableLobbies() {
        clientConnection.sendGetAvailableLobbies();
        return this.availableLobbies;
    }

    public void setMyLobby(Lobby myLobby) {
        this.myLobby = myLobby;
    }

    public Lobby getMyLobby() {
        return myLobby;
    }

    public void setLobbyScreen(LobbyScreen lobbyScreen) {
        this.lobbyScreen = lobbyScreen;
    }

    public void setHostLobbyScreen(HostLobby hostLobbyScreen) {
        this.hostLobbyScreen = hostLobbyScreen;
    }

    public void setConnectionId(Integer connectionId) {
        this.connectionId = connectionId;
    }

    public Integer getConnectionId() {
        return connectionId;
    }

    public void refreshLobbyScreen() {
        if (lobbyScreen != null) {
            lobbyScreen.refreshScreen();
        }
    }

    public void refreshHostLobbyScreen() {
        if (hostLobbyScreen != null) {
            hostLobbyScreen.refreshPlayers();
        }
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
