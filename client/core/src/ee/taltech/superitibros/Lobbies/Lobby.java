package ee.taltech.superitibros.Lobbies;

import java.util.*;

public class Lobby {

    String lobbyHash;

    Set<Integer> players = new LinkedHashSet<>();

    public Lobby(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public void setPlayers(Set<Integer> players) {
        this.players = players;
    }

    public Set<Integer> getPlayers() {
        return players;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }

    public void addPlayer(Integer playerId) {
        players.add(playerId);
    }

    public void removePlayer(Integer playerId) {
        players.remove(playerId);
    }
}
