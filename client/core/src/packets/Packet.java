package packets;

/**
 * Packet superclass.
 */
public class Packet {

    private int id;

    private int x;

    private int y;

    public void setId(int id) {
        this.id = id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {return id;}

    public int getX() {return x;}

    public int getY() {return y;}
}