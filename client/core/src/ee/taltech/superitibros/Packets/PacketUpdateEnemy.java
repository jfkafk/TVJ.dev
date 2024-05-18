package ee.taltech.superitibros.Packets;

import ee.taltech.superitibros.Characters.GameCharacter;

/**
 * Packet for sending a list of updated Enemy instances to all connections.
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

    /**
     * Sets the bot hash.
     *
     * @param botHash the bot hash
     */
    public void setBotHash(String botHash) {
        this.botHash = botHash;
    }

    /**
     * Returns the bot hash.
     *
     * @return the bot hash
     */
    public String getBotHash() {
        return botHash;
    }

    /**
     * Sets the x position of the enemy.
     *
     * @param xPosition the x position
     */
    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Sets the y position of the enemy.
     *
     * @param yPosition the y position
     */
    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Returns the x position of the enemy.
     *
     * @return the x position
     */
    public float getxPosition() {
        return xPosition;
    }

    /**
     * Returns the y position of the enemy.
     *
     * @return the y position
     */
    public float getyPosition() {
        return yPosition;
    }

    /**
     * Sets the lobby hash.
     *
     * @param lobbyHash the lobby hash
     */
    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    /**
     * Returns the lobby hash.
     *
     * @return the lobby hash
     */
    public String getLobbyHash() {
        return lobbyHash;
    }

    /**
     * Sets the current state of the enemy.
     *
     * @param currentState the current state
     */
    public void setCurrentState(GameCharacter.State currentState) {
        this.currentState = currentState;
    }

    /**
     * Returns the current state of the enemy.
     *
     * @return the current state
     */
    public GameCharacter.State getCurrentState() {
        return currentState;
    }

    /**
     * Sets whether the enemy is facing right.
     *
     * @param facingRight true if facing right, false otherwise
     */
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    /**
     * Returns whether the enemy is facing right.
     *
     * @return true if facing right, false otherwise
     */
    public boolean getFacingRight() {
        return this.facingRight;
    }

    /**
     * Returns whether the enemy is dead.
     *
     * @return true if the enemy is dead, false otherwise
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Sets whether the enemy is dead.
     *
     * @param dead true if the enemy is dead, false otherwise
     */
    public void setDead(boolean dead) {
        isDead = dead;
    }

    /**
     * Returns the health of the enemy.
     *
     * @return the health
     */
    public float getHealth() {
        return health;
    }

    /**
     * Sets the health of the enemy.
     *
     * @param health the health
     */
    public void setHealth(float health) {
        this.health = health;
    }
}
