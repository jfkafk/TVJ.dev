package ee.taltech.superitibros.Packets;

/**
 * Packet superclass.
 */
public class Packet {

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}