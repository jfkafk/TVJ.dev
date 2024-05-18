package packets;

/**
 * Packet used to add existing PlayerGameCharacters to a new player's world and to add the new PlayerGameCharacter to existing players' worlds.
 */
public class PacketAddCharacter extends Packet {

    /**
     * The ID of the character being added.
     */
    private int id;

    /**
     * The X-coordinate of the character being added.
     */
    private float x;

    /**
     * The Y-coordinate of the character being added.
     */
    private float y;

    /**
     * Sets the ID of the character being added.
     * @param id The ID of the character being added.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the X-coordinate of the character being added.
     * @param x The X-coordinate of the character being added.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the Y-coordinate of the character being added.
     * @param y The Y-coordinate of the character being added.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Retrieves the ID of the character being added.
     * @return The ID of the character being added.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the X-coordinate of the character being added.
     * @return The X-coordinate of the character being added.
     */
    public float getX() {
        return x;
    }

    /**
     * Retrieves the Y-coordinate of the character being added.
     * @return The Y-coordinate of the character being added.
     */
    public float getY() {
        return y;
    }
}
