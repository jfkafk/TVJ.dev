package packets;

import com.mygdx.game.Characters.GameCharacter;

/**
 * Packet for updating player info.
 */
public class PacketUpdateCharacterInformation extends Packet {

    private int id;
    private float x;
    private float y;
    private enum State {IDLE, WALKING, JUMPING, FALL}
    private GameCharacter.State currentState;
    private boolean facingRight;
    String lobbyHash;
    float health;
    boolean isDead;


    public void setId(int id) {
        this.id = id;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getId() {return id;}

    public float getX() {return x;}

    public float getY() {return y;}

    public void setCurrentState(GameCharacter.State currentState) {
        this.currentState = currentState;
    }

    public GameCharacter.State getCurrentState() {
        return currentState;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public boolean getFacingRight() {
        return  this.facingRight;
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getHealth() {
        return health;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isDead() {
        return isDead;
    }
}
