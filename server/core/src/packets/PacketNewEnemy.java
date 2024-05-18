package packets;

/**
 * Packet for sending information about a new enemy.
 */
public class PacketNewEnemy extends Packet {

    private String botHash;
    private float xPosition;
    private float yPosition;

    /**
     * Sets the hash of the new enemy.
     *
     * @param botHash the hash of the new enemy
     */
    public void setBotHash(String botHash) {
        this.botHash = botHash;
    }

    /**
     * Gets the hash of the new enemy.
     *
     * @return the hash of the new enemy
     */
    public String getBotHash() {
        return botHash;
    }

    /**
     * Sets the x-coordinate of the new enemy.
     *
     * @param xPosition the x-coordinate of the new enemy
     */
    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Gets the x-coordinate of the new enemy.
     *
     * @return the x-coordinate of the new enemy
     */
    public float getxPosition() {
        return xPosition;
    }

    /**
     * Sets the y-coordinate of the new enemy.
     *
     * @param yPosition the y-coordinate of the new enemy
     */
    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Gets the y-coordinate of the new enemy.
     *
     * @return the y-coordinate of the new enemy
     */
    public float getyPosition() {
        return yPosition;
    }
}
