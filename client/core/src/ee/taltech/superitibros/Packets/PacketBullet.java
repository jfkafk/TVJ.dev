package ee.taltech.superitibros.Packets;

public class PacketBullet extends Packet {

    Integer bulletId;
    String lobbyHash;
    float playerX;
    float playerY;
    float mouseX;
    float mouseY;
    float bulletX;
    float bulletY;
    boolean isHit;
    String hitEnemy;

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

    public boolean isHit() {
        return isHit;
    }

    public void setIsHit(boolean hit) {
        isHit = hit;
    }

    public String getHitEnemy() {
        return hitEnemy;
    }

    public void setHitEnemy(String hitEnemy) {
        this.hitEnemy = hitEnemy;
    }
}
