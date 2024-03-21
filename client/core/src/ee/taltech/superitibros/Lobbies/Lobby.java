package ee.taltech.superitibros.Lobbies;

import java.util.*;

public class Lobby {

    String lobbyHash;

    Integer creatorId;

    Set<String> players = new LinkedHashSet<>();

    Integer playerCount;

    public Lobby(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public void setPlayers(Set<String> players) {
        this.players = players;
    }

    public Set<String> getPlayers() {
        return players;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }

    public void setPlayerCount(Integer playerCount) {
        this.playerCount = playerCount;
    }

    public void addPLayer(String playerName) {
        players.add(playerName);
    }
}
