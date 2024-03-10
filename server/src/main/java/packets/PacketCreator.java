package packets;

/**
 * Class that is used to create new Packets to keep the code readable in ClientConnection and ServerConnection.
 */
public class PacketCreator {

    /**
     * Create a PacketUpdateCharacterInformation.
     *
     * @param id of the player's connection (int)
     * @param xPos change of player's x coordinate (float)
     * @param yPos change of player's y coordinate (float)
     * @return new PacketUpdateCharacterInformation
     */
    public static PacketMoveCharacter createPacketUpdateCharacterInformation(int id, float xPos, float yPos) {
        PacketMoveCharacter packetMoveCharacter = new PacketMoveCharacter();
        packetMoveCharacter.setId(id);
        packetMoveCharacter.setX(xPos);
        packetMoveCharacter.setY(yPos);
        return packetMoveCharacter;
    }

    /**
     * Create a PacketConnect.
     *
     * @return new PacketConnect
     */
    public static PacketConnect createPacketConnect() {
        return new PacketConnect();
    }

    /**
     * Create a PacketAddCharacter.
     *
     * @param id of the player's connection (int)
     * @param xPos of the player (float)
     * @param yPos of the player (float)
     * @return new PacketAddCharacter
     */
    public static PacketAddCharacter createPacketAddCharacter(int id, float xPos, float yPos) {
        PacketAddCharacter packetAddCharacter = new PacketAddCharacter();
        packetAddCharacter.setId(id);
        packetAddCharacter.setX(xPos);
        packetAddCharacter.setY(yPos);
        return packetAddCharacter;
    }
}
