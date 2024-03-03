package ee.taltech.superitibros.GameInfo;

import ee.taltech.superitibros.Connection.ClientConnection;
import com.badlogic.gdx.math.Rectangle;
import ee.taltech.superitibros.Characters.PlayerGameCharacter;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;


public class ClientWorld {

    private ClientConnection clientConnection;
    private PlayerGameCharacter myPlayerGameCharacter;
    private final HashMap<Integer, PlayerGameCharacter> worldGameCharactersMap = new HashMap<>();
    private int score = 0;
    private int waveCount = 0;

    /**
     * This adds the instance of ClientConnection to this class.
     */
    public void registerClient(ClientConnection clientConnection){
        this.clientConnection = clientConnection;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setWaveCount(int waveCount) {
        this.waveCount = waveCount;
    }

    public int getWaveCount() {
        return waveCount;
    }

    public void setMyPlayerGameCharacter(PlayerGameCharacter myPlayerGameCharacter) {
        this.myPlayerGameCharacter = myPlayerGameCharacter;
    }

    public PlayerGameCharacter getMyPlayerGameCharacter() {
        return myPlayerGameCharacter;
    }

    /**
     * Map of clients and their PlayerGameCharacters.
     *
     * Key: id, value: PlayerGameCharacter
     */
    public HashMap<Integer, PlayerGameCharacter> getWorldGameCharactersMap() {
        return worldGameCharactersMap;
    }

    public PlayerGameCharacter getGameCharacter(Integer id){
        return worldGameCharactersMap.get(id);
    }

    /**
     * Add a PlayerGameCharacter to the characters map.
     *
     * @param id of the PlayerGameCharacter
     * @param newCharacter PlayerGameCharacter
     */
    public void addGameCharacter(Integer id, PlayerGameCharacter newCharacter) {
        worldGameCharactersMap.put(id, newCharacter);
    }

    /**
     * This moves the PlayerGameCharacter by changing  x and y coordinates of set character.
     *
     * Also updates PlayerGameCharacter's weapons coordinates.
     * @param id of the moving character - id is key in worldGameCharactersMap.
     * @param xPosChange the change of x
     * @param yPosChange the change of y
     */
    public void movePlayerGameCharacter(Integer id, float xPosChange, float yPosChange) {
        getGameCharacter(id).moveToNewPos(xPosChange, yPosChange);
    }
}