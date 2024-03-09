package packets;

/**
 * Packet for sending info about updated PlayerGameCharacter instance to all connections.
 */
public class PacketUpdateCharacterInformation extends Packet {

    private int id;

    private float x;

    private float y;

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}