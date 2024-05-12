package packets;

import com.mygdx.game.Characters.GameCharacter;

/**
 * Class that is used to create new Packets to keep the code readable in ClientConnection and ServerConnection.
 */
public class PacketCreator {

    /**
     * Create a PacketUpdateCharacterInformation.
     *
     * @return new PacketUpdateCharacterInformation
     */
    public static PacketUpdateCharacterInformation createPacketUpdateCharacterInformation(String lobbyHash, int id, float xPos, float yPos, GameCharacter.State currentState, boolean facingRight, float health) {
        PacketUpdateCharacterInformation packetUpdateCharacterInformation = new PacketUpdateCharacterInformation();
        packetUpdateCharacterInformation.setLobbyHash(lobbyHash);
        packetUpdateCharacterInformation.setId(id);
        packetUpdateCharacterInformation.setX(xPos);
        packetUpdateCharacterInformation.setY(yPos);
        packetUpdateCharacterInformation.setCurrentState(currentState);
        packetUpdateCharacterInformation.setFacingRight(facingRight);
        packetUpdateCharacterInformation.setHealth(health);
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
    public static PacketUpdateEnemy createPacketUpdateEnemy(String botHash, float xPosition, float yPosition, GameCharacter.State currentState, boolean facingRight, float health) {
        PacketUpdateEnemy packetNewEnemy = new PacketUpdateEnemy();
        packetNewEnemy.setBotHash(botHash);
        packetNewEnemy.setxPosition(xPosition);
        packetNewEnemy.setyPosition(yPosition);
        packetNewEnemy.setCurrentState(currentState);
        packetNewEnemy.setFacingRight(facingRight);
        packetNewEnemy.setHealth(health);
        return packetNewEnemy;
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
     * Create PacketRemoveLobby.
     * @param lobbyHash lobby hash.
     * @return PacketRemoveLobby.
     */
    public static PacketRemoveLobby createPacketRemoveLobby(String lobbyHash) {
        PacketRemoveLobby packetRemoveLobby = new PacketRemoveLobby();
        packetRemoveLobby.setLobbyHash(lobbyHash);
        return packetRemoveLobby;
    }

    /**
     * Create  PacketLobbyInfo.
     * @param lobbyHash lobby's hash.
     * @return new PacketLobbyInfo.
     */
    public static PacketLobbyInfo createPacketLobbyInfo(String lobbyHash) {
        PacketLobbyInfo packetLobbyInfo = new PacketLobbyInfo();
        packetLobbyInfo.setLobbyHash(lobbyHash);
        return packetLobbyInfo;
    }

    /**
     * Create PacketBullet.
     * @param lobbyHash lobby hash.
     * @param bulletId bullet id.
     * @param bulletX bullet x coordinate.
     * @param bulletY bullet y coordinate.
     * @return new PacketBullet.
     */
    public static PacketBullet createPacketBullet(String lobbyHash, int bulletId, float bulletX, float bulletY) {
        PacketBullet packetBullet = new PacketBullet();
        packetBullet.setLobbyHash(lobbyHash);
        packetBullet.setBulletId(bulletId);
        packetBullet.setBulletX(bulletX);
        packetBullet.setBulletY(bulletY);
        return packetBullet;
    }

    /**
     * Create PacketAddCoin.
     * @param xCoordinate coin x coordinate.
     * @param yCoordinate coin y coordinate.
     * @return new PacketAddCoin
     */
    public static PacketAddCoin createPacketAddCoin(float xCoordinate, float yCoordinate) {
        PacketAddCoin packetAddCoin = new PacketAddCoin();
        packetAddCoin.setxCoordinate(xCoordinate);
        packetAddCoin.setyCoordinate(yCoordinate);
        return packetAddCoin;
    }

    /**
     * Create PacketWon.
     * @return PacketWon.
     */
    public static PacketWon createPacketWon() {
        return new PacketWon();
    }
}
