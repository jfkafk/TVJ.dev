package ee.taltech.superitibros.Lobbies;

import java.util.LinkedList;
import java.util.List;

public class Lobby {

    String lobbyHash;
    static Integer nextLobbyNum = 1;

    Integer creatorId;

    List<Integer> players = new LinkedList<>();

    Integer playerCount;

    public Lobby() {
        lobbyHash = "Lobby:" + nextLobbyNum;
        incrementNextLobbyNumber();
        players.add(creatorId);
    }

    public Lobby(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public static void incrementNextLobbyNumber() {
        nextLobbyNum++;
    }

    public void addPlayer(Integer id) {
        players.add(id);
    }

    public List<Integer> getPlayers() {
        return players;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }

    public void setPlayerCount(Integer playerCount) {
        this.playerCount = playerCount;
    }
}
