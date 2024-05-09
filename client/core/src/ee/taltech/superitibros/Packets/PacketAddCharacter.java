package ee.taltech.superitibros.Packets;

/**
 * Packet that is used to add existing PlayerGameCharacters to the new player's world and to add the new PlayerGameCharacter to existing players' worlds.
 */
public class PacketAddCharacter extends Packet {

    private int id;  // Connection id.
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