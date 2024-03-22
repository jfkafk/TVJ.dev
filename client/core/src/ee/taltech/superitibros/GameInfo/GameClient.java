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
    String clientName;
    LobbyScreen lobbyScreen;
    HostLobby hostLobbyScreen;

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

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setLobbyScreen(LobbyScreen lobbyScreen) {
        this.lobbyScreen = lobbyScreen;
    }

    public void setHostLobbyScreen(HostLobby hostLobbyScreen) {
        this.hostLobbyScreen = hostLobbyScreen;
    }

    public void refreshLobbyScreen() {
        if (lobbyScreen != null) {
            lobbyScreen.refreshPlayers();
        }
    }

    public void refreshHostLobbyScreen() {
        if (hostLobbyScreen != null) {
            hostLobbyScreen.refreshPlayers();
        }
    }

    public String getClientName() {
        return clientName;
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
