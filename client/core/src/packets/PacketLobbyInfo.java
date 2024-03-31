package packets;

import java.util.LinkedHashSet;
import java.util.Set;

public class PacketLobbyInfo extends Packet {

    String lobbyHash;
    Set<Integer> players = new LinkedHashSet<>();
    Integer playerToAdd;
    Integer playerToRemove;
    boolean updateInfo;
    boolean startGame;
    boolean toDelete;
    String mapPath;

    public String getLobbyHash() {
        return lobbyHash;
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public void setPlayers(Set<Integer> players) {
        this.players = players;
    }

    public Set<Integer> getPlayers() {
        return players;
    }

    public void setPlayerToAdd(Integer playerToAdd) {
        this.playerToAdd = playerToAdd;
    }

    public Integer getPlayerToAdd() {
        return playerToAdd;
    }

    public void setPlayerToRemove(Integer playerToRemove) {
        this.playerToRemove = playerToRemove;
    }

    public Integer getPlayerToRemove() {
        return playerToRemove;
    }

    public void setUpdateInfo(boolean updateInfo) {
        this.updateInfo = updateInfo;
    }

    public boolean isUpdateInfo() {
        return updateInfo;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getMapPath() {
        return mapPath;
    }
}
