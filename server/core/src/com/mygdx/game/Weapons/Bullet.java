package com.mygdx.game.Weapons;

/**
 * Represents a bullet in the game.
 */
public class Bullet {

    private final boolean isPlayerBullet;
    private final Integer bulletId;
    private static Integer nextBulletId = 1;
    private float bulletX;
    private float bulletY;
    private final float addX;
    private final float addY;

    /**
     * Constructs a new Bullet instance with specified position and direction.
     *
     * @param bulletX the initial x-coordinate of the bullet.
     * @param bulletY the initial y-coordinate of the bullet.
     * @param addX the x-component of the bullet's velocity.
     * @param addY the y-component of the bullet's velocity.
     */
    public Bullet(float bulletX, float bulletY, float addX, float addY, boolean isPlayerBullet) {
        bulletId = nextBulletId;
        incrementNextBulletId();
        this.bulletX = bulletX;
        this.bulletY = bulletY;
        this.addX = addX;
        this.addY = addY;
        this.isPlayerBullet = isPlayerBullet;
    }

    /**
     * Increments the next bullet ID.
     */
    public static void incrementNextBulletId() {
        nextBulletId++;
    }

    /**
     * Creates a new Bullet instance with calculated direction based on player and target positions.
     *
     * @param playerX the x-coordinate of the player.
     * @param playerY the y-coordinate of the player.
     * @param mouseX the x-coordinate of the target (e.g., mouse position).
     * @param mouseY the y-coordinate of the target (e.g., mouse position).
     * @param isPlayerBullet is bullet shot by player.
     * @return a new Bullet instance.
     */
    public static Bullet createBullet(float playerX, float playerY, float mouseX, float mouseY, boolean isPlayerBullet) {
        // Calculate the direction vector from player to mouse
        float directionX = mouseX - playerX;
        float directionY = mouseY - playerY;

        // Normalize the direction vector to ensure consistent speed
        float length = (float) Math.sqrt(directionX * directionX + directionY * directionY);
        directionX /= length;
        directionY /= length;

        // Define the maximum speed of the bullet
        float maxSpeed = 2.0f; // You can adjust this value as needed

        // Calculate the final movement values for the bullet
        float addX = directionX * maxSpeed;
        float addY = directionY * maxSpeed;

        // Create a new instance of Bullet with calculated values and return it
        return new Bullet(playerX, playerY, addX, addY, isPlayerBullet);
    }

    /**
     * Updates the bullet's position based on its velocity.
     */
    public void updateBullet() {
        bulletX += addX;
        bulletY += addY;
    }

    /**
     * Gets the bullet ID.
     *
     * @return the bullet ID.
     */
    public Integer getBulletId() {
        return bulletId;
    }

    /**
     * Gets the x-coordinate of the bullet.
     *
     * @return the x-coordinate of the bullet.
     */
    public float getBulletX() {
        return bulletX;
    }

    /**
     * Gets the y-coordinate of the bullet.
     *
     * @return the y-coordinate of the bullet.
     */
    public float getBulletY() {
        return bulletY;
    }

    /**
     * Get whether bullet is shot by player.
     * @return boolean whether bullet is shot by player.
     */
    public boolean isPlayerBullet() {
        return isPlayerBullet;
    }
}
