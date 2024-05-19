package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.World.World;

import java.util.Objects;

public class GameCharacter {

    public enum State {IDLE, WALKING, JUMPING, FALL}

    // Character characteristics.
    protected float movementSpeed; // World units per second.
    protected float health;

    // Position & dimension.
    protected float xPosition;
    protected float yPosition; // Lower-left corner
    protected float width, height;
    protected Rectangle boundingBox;
    private World world;
    private State currentState;
    private boolean facingRight = true;

    /**
     * GameCharacter constructor.
     *
     * @param movementSpeed the movement speed of the GameCharacter (in world units per second)
     * @param boundingBox the bounding box that encapsulates the GameCharacter (Rectangle)
     * @param xPosition the initial x position of the GameCharacter (float)
     * @param yPosition the initial y position of the GameCharacter (float)
     * @param width the width of the GameCharacter (float)
     * @param height the height of the GameCharacter (float)
     * @param world the game world (World)
     */
    public GameCharacter(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition,
                         float width, float height, World world) {
        this.movementSpeed = movementSpeed;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.boundingBox = boundingBox;
        this.world = world;
    }

    /**
     * Gets the x position of the GameCharacter.
     *
     * @return the x position of the GameCharacter (float)
     */
    public float getxPosition() {
        return xPosition;
    }

    /**
     * Gets the y position of the GameCharacter.
     *
     * @return the y position of the GameCharacter (float)
     */
    public float getyPosition() {
        return yPosition;
    }

    /**
     * Gets the bounding box of the GameCharacter.
     *
     * @return the bounding box of the GameCharacter (Rectangle)
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    /**
     * Sets the game world.
     *
     * @param world the game world (World)
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Gets the game world.
     *
     * @return the game world (World)
     */
    public World getWorld() {
        return world;
    }

    /**
     * Sets the x position of the GameCharacter.
     *
     * @param xPosition the new x position of the GameCharacter (float)
     */
    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Sets the y position of the GameCharacter.
     *
     * @param yPosition the new y position of the GameCharacter (float)
     */
    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Checks if the GameCharacter is facing right.
     *
     * @return true if the GameCharacter is facing right, false otherwise
     */
    public boolean isFacingRight() {
        return facingRight;
    }

    /**
     * Sets the facing direction of the GameCharacter.
     *
     * @param facingRight true if the GameCharacter is facing right, false otherwise
     */
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    /**
     * Sets the current state of the GameCharacter.
     *
     * @param currentState the current state of the GameCharacter (State)
     */
    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    /**
     * Gets the current state of the GameCharacter.
     *
     * @return the current state of the GameCharacter (State)
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * Gets the health of the GameCharacter.
     *
     * @return the health of the GameCharacter (float)
     */
    public float getHealth() {
        return health;
    }

    /**
     * Checks if this GameCharacter is equal to another object.
     *
     * @param o the object to compare to
     * @return true if this GameCharacter is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCharacter that = (GameCharacter) o;
        return Float.compare(that.movementSpeed, movementSpeed) == 0 && health == that.health
                && Float.compare(that.xPosition, xPosition) == 0 && Float.compare(that.yPosition, yPosition) == 0
                && Float.compare(that.width, width) == 0 && Float.compare(that.height, height) == 0;
    }

    /**
     * Computes a hash code for this GameCharacter.
     *
     * @return a hash code for this GameCharacter
     */
    @Override
    public int hashCode() {
        return Objects.hash(movementSpeed, health, xPosition, yPosition, width, height);
    }
}
