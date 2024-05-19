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
     * @param movementSpeed of the PlayerGameCharacter (float)
     * @param boundingBox encapsulates a 2D rectangle(bounding box) for the PlayerGameCharacter (Rectangle)
     * @param xPosition of the PlayerGameCharacter (float)
     * @param yPosition of the PlayerGameCharacter (float)
     * @param width of the PlayerGameCharacter (float)
     * @param height of the PlayerGameCharacter (float)
     * @param world game world (World)
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

    public float getxPosition() {
        return xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    /**
     * Moves the GameCharacter to a new position.
     *
     * @param xPos change of the x coordinate
     * @param yPos change of the y coordinate
     */
    public void moveToNewPos(float xPos, float yPos) {
        this.boundingBox.set(boundingBox.getX() + xPos, boundingBox.getY() + yPos, boundingBox.getWidth(), boundingBox.getHeight());
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Set y coordinate.
     * @param yPosition y coordinate.
     */
    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public float getHealth() {
        return health;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCharacter that = (GameCharacter) o;
        return Float.compare(that.movementSpeed, movementSpeed) == 0 && health == that.health
                && Float.compare(that.xPosition, xPosition) == 0 && Float.compare(that.yPosition, yPosition) == 0
                && Float.compare(that.width, width) == 0 && Float.compare(that.height, height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movementSpeed, health, xPosition, yPosition, width, height);
    }
}
