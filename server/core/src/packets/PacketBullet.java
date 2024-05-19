package packets;

/**
 * Packet for sending bullet information between client and server.
 */
public class PacketBullet extends Packet {

    /**
     * The ID of the bullet.
     */
    Integer bulletId;

    /**
     * The lobby hash associated with the bullet.
     */
    String lobbyHash;

    /**
     * The X-coordinate of the bullet.
     */
    float bulletX;

    /**
     * The Y-coordinate of the bullet.
     */
    float bulletY;

    /**
     * The X-coordinate of the player shooting the bullet.
     */
    float playerX;

    /**
     * The Y-coordinate of the player shooting the bullet.
     */
    float playerY;

    /**
     * The X-coordinate of the mouse position when the bullet was shot.
     */
    float mouseX;

    /**
     * The Y-coordinate of the mouse position when the bullet was shot.
     */
    float mouseY;

    /**
     * Flag indicating whether the bullet has hit something.
     */
    boolean isHit;

    /**
     * The enemy hit by the bullet.
     */
    String hitEnemy;

    /**
     * Is bullet shot by player or not.
     */
    boolean isPlayerBullet;

    /**
     * Sets the ID of the bullet.
     * @param bulletId The ID of the bullet.
     */
    public void setBulletId(Integer bulletId) {
        this.bulletId = bulletId;
    }

    /**
     * Retrieves the ID of the bullet.
     * @return The ID of the bullet.
     */
    public Integer getBulletId() {
        return bulletId;
    }

    /**
     * Sets the lobby hash associated with the bullet.
     * @param lobbyHash The lobby hash to set.
     */
    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    /**
     * Sets the X-coordinate of the bullet.
     * @param bulletX The X-coordinate of the bullet.
     */
    public void setBulletX(float bulletX) {
        this.bulletX = bulletX;
    }

    /**
     * Sets the Y-coordinate of the bullet.
     * @param bulletY The Y-coordinate of the bullet.
     */
    public void setBulletY(float bulletY) {
        this.bulletY = bulletY;
    }

    /**
     * Sets the X-coordinate of the player shooting the bullet.
     * @param playerX The X-coordinate of the player.
     */
    public void setPlayerX(float playerX) {
        this.playerX = playerX;
    }

    /**
     * Sets the Y-coordinate of the player shooting the bullet.
     * @param playerY The Y-coordinate of the player.
     */
    public void setPlayerY(float playerY) {
        this.playerY = playerY;
    }

    /**
     * Sets the X-coordinate of the mouse position when the bullet was shot.
     * @param mouseX The X-coordinate of the mouse.
     */
    public void setMouseX(float mouseX) {
        this.mouseX = mouseX;
    }

    /**
     * Sets the Y-coordinate of the mouse position when the bullet was shot.
     * @param mouseY The Y-coordinate of the mouse.
     */
    public void setMouseY(float mouseY) {
        this.mouseY = mouseY;
    }

    /**
     * Retrieves the X-coordinate of the bullet.
     * @return The X-coordinate of the bullet.
     */
    public float getBulletX() {
        return bulletX;
    }

    /**
     * Retrieves the Y-coordinate of the bullet.
     * @return The Y-coordinate of the bullet.
     */
    public float getBulletY() {
        return bulletY;
    }

    /**
     * Retrieves the lobby hash associated with the bullet.
     * @return The lobby hash.
     */
    public String getLobbyHash() {
        return lobbyHash;
    }

    /**
     * Retrieves the X-coordinate of the player shooting the bullet.
     * @return The X-coordinate of the player.
     */
    public float getPlayerX() {
        return playerX;
    }

    /**
     * Retrieves the Y-coordinate of the player shooting the bullet.
     * @return The Y-coordinate of the player.
     */
    public float getPlayerY() {
        return playerY;
    }

    /**
     * Retrieves the X-coordinate of the mouse position when the bullet was shot.
     * @return The X-coordinate of the mouse.
     */
    public float getMouseX() {
        return mouseX;
    }

    /**
     * Retrieves the Y-coordinate of the mouse position when the bullet was shot.
     * @return The Y-coordinate of the mouse.
     */
    public float getMouseY() {
        return mouseY;
    }

    /**
     * Checks if the bullet has hit something.
     * @return True if the bullet has hit something, false otherwise.
     */
    public boolean isHit() {
        return isHit;
    }

    /**
     * Sets the flag indicating whether the bullet has hit something.
     * @param hit True if the bullet has hit something, false otherwise.
     */
    public void setIsHit(boolean hit) {
        isHit = hit;
    }

    /**
     * Retrieves the enemy hit by the bullet.
     * @return The enemy hit by the bullet.
     */
    public String getHitEnemy() {
        return hitEnemy;
    }

    /**
     * Sets the enemy hit by the bullet.
     * @param hitEnemy The enemy hit by the bullet.
     */
    public void setHitEnemy(String hitEnemy) {
        this.hitEnemy = hitEnemy;
    }

    /**
     * Set if bullet is shot by player.
     * @param playerBullet boolean whether bullet is shot by player.
     */
    public void setPlayerBullet(boolean playerBullet) {
        isPlayerBullet = playerBullet;
    }

    /**
     * Get if bullet is shot by player.
     * @return boolean whether bullet is shot by player.
     */
    public boolean isPlayerBullet() {
        return isPlayerBullet;
    }
}
