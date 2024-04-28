package packets;
import com.mygdx.game.Characters.GameCharacter;

/**
 * Packet for sending a list of updated Zombie instances to all connections.
 */
public class PacketUpdateEnemy extends Packet {

    private String lobbyHash;
    private String botHash;
    private float xPosition;
    private float yPosition;
    private boolean isDead;
    private GameCharacter.State currentState;
    private boolean facingRight;
    private float health;

    public void setBotHash(String botHash) {
        this.botHash = botHash;
    }

    public String getBotHash() {
        return botHash;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public float getxPosition() {
        return xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }

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

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }
}
