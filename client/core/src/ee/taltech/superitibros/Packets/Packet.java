package ee.taltech.superitibros.Packets;


/**
 * Superclass for packet objects.
 */
public class Packet {

    /**
     * The message contained in the packet.
     */
    private String message;

    /**
     * Sets the message of the packet.
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Retrieves the message contained in the packet.
     * @return The message contained in the packet.
     */
    public String getMessage() {
        return message;
    }
}
