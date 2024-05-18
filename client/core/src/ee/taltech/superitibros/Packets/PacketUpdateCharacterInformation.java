package ee.taltech.superitibros.Packets;

import ee.taltech.superitibros.Characters.GameCharacter;

/**
 * Packet for updating player info.
 */
public class PacketUpdateCharacterInformation extends Packet {

    private int id;
    private float x;
    private float y;
    private GameCharacter.State currentState;
    private boolean facingRight;
    private String lobbyHash;
    private float health;
    private boolean isDead;

    /**
     * Sets the ID of the character.
     *
     * @param id the ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the X position of the character.
     *
     * @param x the X position
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the Y position of the character.
     *
     * @param y the Y position
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Returns the ID of the character.
     *
     * @return the ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the X position of the character.
     *
     * @return the X position
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the Y position of the character.
     *
     * @return the Y position
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the current state of the character.
     *
     * @param currentState the current state
     */
    public void setCurrentState(GameCharacter.State currentState) {
        this.currentState = currentState;
    }

    /**
     * Returns the current state of the character.
     *
     * @return the current state
     */
    public GameCharacter.State getCurrentState() {
        return currentState;
    }

    /**
     * Sets whether the character is facing right.
     *
     * @param facingRight true if facing right, false otherwise
     */
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    /**
     * Returns whether the character is facing right.
     *
     * @return true if facing right, false otherwise
     */
    public boolean getFacingRight() {
        return this.facingRight;
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
     * Sets the health of the character.
     *
     * @param health the health
     */
    public void setHealth(float health) {
        this.health = health;
    }

    /**
     * Returns the health of the character.
     *
     * @return the health
     */
    public float getHealth() {
        return health;
    }

    /**
     * Sets whether the character is dead.
     *
     * @param dead true if dead, false otherwise
     */
    public void setDead(boolean dead) {
        isDead = dead;
    }

    /**
     * Returns whether the character is dead.
     *
     * @return true if dead, false otherwise
     */
    public boolean isDead() {
        return isDead;
    }
}
