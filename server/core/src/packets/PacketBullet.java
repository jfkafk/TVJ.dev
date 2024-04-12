package packets;

public class PacketBullet extends Packet {

    Integer bulletId;
    String lobbyHash;
    float playerX;
    float playerY;
    float mouseX;
    float mouseY;
    float bulletX;
    float bulletY;
    boolean isKilled;
    String killedBot;

    public void setBulletId(Integer bulletId) {
        this.bulletId = bulletId;
    }

    public Integer getBulletId() {
        return bulletId;
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public void setBulletX(float bulletX) {
        this.bulletX = bulletX;
    }

    public void setBulletY(float bulletY) {
        this.bulletY = bulletY;
    }

    public void setPlayerX(float playerX) {
        this.playerX = playerX;
    }

    public void setPlayerY(float playerY) {
        this.playerY = playerY;
    }

    public void setMouseX(float mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(float mouseY) {
        this.mouseY = mouseY;
    }

    public float getBulletX() {
        return bulletX;
    }

    public float getBulletY() {
        return bulletY;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }

    public float getPlayerX() {
        return playerX;
    }

    public float getPlayerY() {
        return playerY;
    }

    public float getMouseX() {
        return mouseX;
    }

    public float getMouseY() {
        return mouseY;
    }

    public boolean isKilled() {
        return isKilled;
    }

    public void setKilled(boolean killed) {
        isKilled = killed;
    }

    public String getKilledBot() {
        return killedBot;
    }

    public void setKilledBot(String killedBot) {
        this.killedBot = killedBot;
    }
}
