package packets;

/**
 * Packet for sending a list of updated Zombie instances to all connections.
 */
public class PacketUpdateEnemy extends Packet {

    private String botHash;

    float xPosition;

    float yPosition;

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
}