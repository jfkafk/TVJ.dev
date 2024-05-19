package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.World.World;

public class PlayerGameCharacter extends GameCharacter {

    private int playerGameCharacterId;

    /**
     * Class PlayerGameCharacter constructor.
     *
     * @param movementSpeed the movement speed of the PlayerGameCharacter (in world units per second)
     * @param boundingBox the bounding box that encapsulates the PlayerGameCharacter (Rectangle)
     * @param xPosition the initial x position of the PlayerGameCharacter (float)
     * @param yPosition the initial y position of the PlayerGameCharacter (float)
     * @param width the width of the PlayerGameCharacter (float)
     * @param height the height of the PlayerGameCharacter (float)
     * @param world the game world where the PlayerGameCharacter exists (World)
     */
    public PlayerGameCharacter(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width,
                               float height, World world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, world);
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.height = height;
        this.width = width;
        this.health = 100;
    }

    /**
     * Sets the unique ID of the PlayerGameCharacter.
     *
     * @param playerGameCharacterId the unique ID of the PlayerGameCharacter (int)
     */
    public void setPlayerGameCharacterId(int playerGameCharacterId) {
        this.playerGameCharacterId = playerGameCharacterId;
    }

    /**
     * Gets the unique ID of the PlayerGameCharacter.
     *
     * @return the unique ID of the PlayerGameCharacter (int)
     */
    public int getPlayerGameCharacterId() {
        return playerGameCharacterId;
    }

    /**
     * PlayerGameCharacter static method for creating a new PlayerGameCharacter instance.
     *
     * @param x the initial x coordinate of the PlayerGameCharacter (float)
     * @param y the initial y coordinate of the PlayerGameCharacter (float)
     * @param world the game world where the PlayerGameCharacter exists (World)
     * @param id the unique ID of the PlayerGameCharacter (int)
     * @return a new PlayerGameCharacter instance
     */
    public static PlayerGameCharacter createPlayerGameCharacter(float x, float y, World world, int id) {
        Rectangle playerGameCharacterRectangle = new Rectangle(x, y, 10f, 10f);
        PlayerGameCharacter newGameCharacter = new PlayerGameCharacter(2f, playerGameCharacterRectangle, x, y, 10f, 10f, world);
        newGameCharacter.setPlayerGameCharacterId(id);
        return newGameCharacter;
    }

}
