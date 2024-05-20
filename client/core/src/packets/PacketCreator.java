package packets;

/**
 * Class that is used to create new Packets to keep the code readable in ClientConnection and ServerConnection.
 */
public class PacketCreator {

    /**
     * Create a PacketUpdateCharacterInformation.
     *
     * @return new PacketUpdateCharacterInformation
     */
    public static PacketUpdateCharacterInformation createPacketUpdateCharacterInformation(int id, float xPos, float yPos) {
        PacketUpdateCharacterInformation packetUpdateCharacterInformation = new PacketUpdateCharacterInformation();
        packetUpdateCharacterInformation.setId(id);
        packetUpdateCharacterInformation.setX(xPos);
        packetUpdateCharacterInformation.setY(yPos);
        return packetUpdateCharacterInformation;
    }

    /**
     * Create a PacketConnect.
     *
     * @return new PacketConnect
     */
    public static PacketConnect createPacketConnect() { return new PacketConnect(); }

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

    /**
     * Create a PacketClientDisconnect.
     *
     * @return new PacketClientDisconnect
     */
    public static PacketClientDisconnect createPacketClientDisconnect(int id) {
        PacketClientDisconnect packetClientDisconnect = new PacketClientDisconnect();
        packetClientDisconnect.setId(id);
        return packetClientDisconnect;
    }

    /**
     * Create a PacketUpdateEnemy.
     *
     * @param botHash npc's botHash
     * @return new PacketUpdateEnemy
     */
    public static PacketUpdateEnemy createPacketUpdateEnemy(String botHash, float xPosition, float yPosition) {
        PacketUpdateEnemy packetEnemy = new PacketUpdateEnemy();
        packetEnemy.setBotHash(botHash);
        packetEnemy.setxPosition(xPosition);
        packetEnemy.setyPosition(yPosition);
        return packetEnemy;
    }

    /**
     * Create a PacketNewEnemy.
     *
     * @param botHash botHash
     * @param xPosition x coordinate
     * @param yPosition y coordinate
     * @return new PacketNewEnemy
     */
    public static PacketNewEnemy createPacketNewEnemy(String botHash, float xPosition, float yPosition) {
        PacketNewEnemy packetNewEnemy = new PacketNewEnemy();
        packetNewEnemy.setBotHash(botHash);
        packetNewEnemy.setxPosition(xPosition);
        packetNewEnemy.setyPosition(yPosition);
        return packetNewEnemy;
    }

    /**
     * Create a PacketSendNewLobby.
     * @return new PacketSendNewLobby.
     */
    public static PacketSendNewLobby createPacketSendNewLobby() {
        return new PacketSendNewLobby();
    }

    /**
     * Create a PacketLobbyInfo.
     * @param lobbyHash lobby's hash.
     * @return new PacketLobbyInfo.
     */
    public static PacketLobbyInfo createPacketLobbyInfo(String lobbyHash) {
        PacketLobbyInfo packetLobbyInfo = new PacketLobbyInfo();
        packetLobbyInfo.setLobbyHash(lobbyHash);
        System.out.println("packetLobbyInfo in Creator -> " + packetLobbyInfo.getLobbyHash());
        return packetLobbyInfo;
    }

    /**
     * Create PacketBullet.
     * @param lobbyHash lobby hash.
     * @return new PacketBullet.
     */
    public static PacketBullet createPacketBullet(String lobbyHash) {
        PacketBullet packetBullet = new PacketBullet();
        packetBullet.setLobbyHash(lobbyHash);
        return packetBullet;
    }

    /**
     * Create PacketRemovePlayer.
     * @param lobbyHash lobby hash.
     * @param playerToRemove player to remove.
     * @return new PacketRemovePlayer.
     */
    public static PacketRemovePlayer createPacketRemovePlayer(String lobbyHash, int playerToRemove) {
        PacketRemovePlayer packetRemovePlayer = new PacketRemovePlayer();
        packetRemovePlayer.setLobbyHash(lobbyHash);
        packetRemovePlayer.setPlayerToRemove(playerToRemove);
        return  packetRemovePlayer;
    }

    /**
     * Create PacketNewBullet.
     * @param lobbyHash lobby hash.
     * @param mouseX mouse x.
     * @param mouseY mouse y.
     * @param playerX player x.
     * @param playerY player y.
     * @return new PacketNewBullet.
     */
    public static PacketNewBullet createPacketNewBullet(String lobbyHash, float mouseX, float mouseY,
                                                        float playerX, float playerY) {
        PacketNewBullet packetNewBullet = new PacketNewBullet();
        packetNewBullet.setLobbyHash(lobbyHash);
        packetNewBullet.setMouseX(mouseX);
        packetNewBullet.setMouseY(mouseY);
        packetNewBullet.setPlayerX(playerX);
        packetNewBullet.setPlayerY(playerY);
        packetNewBullet.setPlayerBullet(true);
        return packetNewBullet;
    }
}
