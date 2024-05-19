package packets;

/**
 * Packet for adding a coin to the game world.
 */
public class PacketAddCoin extends Packet {

    /**
     * The X-coordinate of the coin.
     */
    float xCoordinate;

    /**
     * The Y-coordinate of the coin.
     */
    float yCoordinate;

    /**
     * Sets the X-coordinate of the coin.
     * @param xCoordinate The X-coordinate of the coin.
     */
    public void setxCoordinate(float xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    /**
     * Sets the Y-coordinate of the coin.
     * @param yCoordinate The Y-coordinate of the coin.
     */
    public void setyCoordinate(float yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    /**
     * Retrieves the X-coordinate of the coin.
     * @return The X-coordinate of the coin.
     */
    public float getxCoordinate() {
        return xCoordinate;
    }

    /**
     * Retrieves the Y-coordinate of the coin.
     * @return The Y-coordinate of the coin.
     */
    public float getyCoordinate() {
        return yCoordinate;
    }
}
