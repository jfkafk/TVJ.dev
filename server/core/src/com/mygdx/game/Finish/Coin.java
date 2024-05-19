package com.mygdx.game.Finish;

/**
 * Represents a Coin in the game world.
 */
public class Coin {

    /**
     * The x coordinate of the Coin.
     */
    float xCoordinate;

    /**
     * The y coordinate of the Coin.
     */
    float yCoordinate;

    /**
     * Constructs a new Coin instance.
     *
     * @param x the x coordinate of the Coin (float)
     * @param y the y coordinate of the Coin (float)
     */
    public Coin(float x, float y) {
        xCoordinate = x;
        yCoordinate = y;
    }

    /**
     * Gets the x coordinate of the Coin.
     *
     * @return the x coordinate (float)
     */
    public float getxCoordinate() {
        return xCoordinate;
    }

    /**
     * Gets the y coordinate of the Coin.
     *
     * @return the y coordinate (float)
     */
    public float getyCoordinate() {
        return yCoordinate;
    }
}
