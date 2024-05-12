package ee.taltech.superitibros.Packets;

/**
 * Packet for sending a list of updated Zombie instances to all connections.
 */
public class PacketNewEnemy extends Packet {

    private String botHash;

    private float xPosition;

    private float yPosition;

    public void setBotHash(String botHash) {
        this.botHash = botHash;
    }

    public String getBotHash() {
        return botHash;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getxPosition() {
        return xPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public float getyPosition() {
        return yPosition;
    }
}