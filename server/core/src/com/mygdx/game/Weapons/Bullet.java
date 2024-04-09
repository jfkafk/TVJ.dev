package com.mygdx.game.Weapons;

public class Bullet {

    Integer bulletId;
    static Integer nextBulletId = 1;
    String lobbyHash;
    float bulletX;
    float bulletY;
    float addX;
    float addY;

    public Bullet(String lobbyHash, float bulletX, float bulletY, float addX, float addY) {
        bulletId = nextBulletId;
        incrementNextBulletId();
        this.lobbyHash = lobbyHash;
        this.bulletX = bulletX;
        this.bulletY = bulletY;
        this.addX = addX;
        this.addY = addY;
    }

    public static void incrementNextBulletId() {
        nextBulletId++;
    }

    public static Bullet createBullet(String lobbyHash, float playerX, float playerY, float mouseX, float mouseY) {
        // Calculate the direction vector from player to mouse
        float directionX = mouseX - playerX;
        float directionY = mouseY - playerY;

        // Normalize the direction vector to ensure consistent speed
        float length = (float) Math.sqrt(directionX * directionX + directionY * directionY);
        directionX /= length;
        directionY /= length;

        System.out.println("Player X: " + playerX + " | Mouse X: " + mouseX);
        System.out.println("Player Y: " + playerY + " | Mouse Y: " + mouseY);

        // Define the maximum speed of the bullet
        float maxSpeed = 2.0f; // You can adjust this value as needed

        // Calculate the final movement values for the bullet
        float addX = directionX * maxSpeed;
        float addY = directionY * maxSpeed;
        System.out.println("Add X: " + addX);
        System.out.println("Add Y: " + addY);

        // Create a new instance of Bullet with calculated values and return it
        return new Bullet(lobbyHash, playerX, playerY, addX, addY);
    }

    public void updateBullet() {
        bulletX += addX;
        bulletY += addY;
    }

    public Integer getBulletId() {
        return bulletId;
    }

    public float getAddX() {
        return addX;
    }

    public float getAddY() {
        return addY;
    }

    public float getBulletX() {
        return bulletX;
    }

    public float getBulletY() {
        return bulletY;
    }

    public void setBulletX(float bulletX) {
        this.bulletX = bulletX;
    }

    public void setBulletY(float bulletY) {
        this.bulletY = bulletY;
    }
}
