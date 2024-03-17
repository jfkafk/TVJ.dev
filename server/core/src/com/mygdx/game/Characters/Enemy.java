package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.World.World;

public class Enemy extends GameCharacter {

    // Bot hash
    String botHash;
    // Unique number for botHash
    static int nextBotHashNumber = 0;

    // Enemy (AI) states
    enum State {IDLE, RUNNING_LEFT, RUNNING_RIGHT, ATTACKING}

    private static final float MOVEMENT_SPEED = 0.1f;
    private static final float DETECTION_RANGE = 100f;

    private State currentState = State.IDLE;
    private long lastUpdateTime;

    /**
     * GameCharacter constructor.
     *
     * @param movementSpeed of the PlayerGameCharacter (float)
     * @param boundingBox   encapsulates a 2D rectangle(bounding box) for the PlayerGameCharacter (Rectangle)
     * @param xPosition     of the PlayerGameCharacter (float)
     * @param yPosition     of the PlayerGameCharacter (float)
     * @param width         of the PlayerGameCharacter (float)
     * @param height        of the PlayerGameCharacter (float)
     * @param world         game world (World)
     */
    public Enemy(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width, float height, World world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, world);
        botHash = "Bot" + nextBotHashNumber;
        incrementNextBotHashNumber();
    }

    /**
     * Enemy static method for creating a new Enemy instance.
     *
     * @param x coordinate of the PlayerGameCharacter (float)
     * @param y coordinate of the PlayerGameCharacter (float)
     * @return new Enemy instance
     */
    public static Enemy createEnemy(float x, float y, World world) {
        Rectangle enemyRectangle = new Rectangle(x, y, 10f, 20f);
        Enemy enemy = new Enemy(10f, enemyRectangle, x, y, enemyRectangle.width, enemyRectangle.height, world);
        return enemy;
    }

    private static void incrementNextBotHashNumber() {
        nextBotHashNumber++;
    }

    public String getBotHash() {
        return botHash;
    }

    /**
     * Main method for looping.
     */
    public void spin() {
        sense();
        act();
    }

    public void sense() {
        float minDistance = Float.MAX_VALUE;
        GameCharacter closestPlayer = null;

        for (GameCharacter player : getWorld().getClients().values()) {
            float distance = Math.abs(xPosition - player.xPosition) + Math.abs(yPosition - player.yPosition);
            if (distance < minDistance) {
                minDistance = distance;
                currentState = (player.xPosition < xPosition) ? State.RUNNING_LEFT : State.RUNNING_RIGHT;
            }
        }
        if (minDistance == DETECTION_RANGE) {
            currentState = State.IDLE;
        }
    }

    private void act() {
        if (currentState == State.RUNNING_LEFT) {
            xPosition -= 0.1f;
        } else if (currentState == State.RUNNING_RIGHT) {
            xPosition += 0.1f;
        }
    }
}
