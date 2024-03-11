package packets;

/**
 * Packet superclass.
 */
public class PacketUpdateCharacterInformation extends Packet {

    private int id;

    private float x;

    private float y;

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
}