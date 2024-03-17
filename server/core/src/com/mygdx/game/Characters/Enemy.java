package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.World.World;

public class Enemy extends GameCharacter {

<<<<<<< HEAD
    // Bot hash
    String botHash;
    // Unique number for botHash
=======
    String botHash;
>>>>>>> 7e80772 (Some initial npc is added. Now need to add multiple states. Also deleted wrong server repo. And fixed bug that after last input was done, three packets are sent more.)
    static int nextBotHashNumber = 0;

    // Enemy (AI) states
    enum State {IDLE, RUNNING_LEFT, RUNNING_RIGHT, ATTACKING}

    private static final float MOVEMENT_SPEED = 0.1f;
    private static final float DETECTION_RANGE = 100f;

    private State currentState = State.IDLE;
    private long elapsedTime = 0;
    private long lastUpdateTime;

    /**
     * GameCharacter constructor.
     *
     * @param boundingBox   encapsulates a 2D rectangle(bounding box) for the PlayerGameCharacter (Rectangle)
     * @param xPosition     of the PlayerGameCharacter (float)
     * @param yPosition     of the PlayerGameCharacter (float)
     * @param width         of the PlayerGameCharacter (float)
     * @param height        of the PlayerGameCharacter (float)
     * @param world         game world (World)
     */
    public Enemy(Rectangle boundingBox, float xPosition, float yPosition, float width, float height, World world) {
        super(MOVEMENT_SPEED, boundingBox, xPosition, yPosition, width, height, world);
        botHash = "Bot" + nextBotHashNumber;
        lastUpdateTime = System.currentTimeMillis();
        nextBotHashNumber++; // Incrementing here
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
        return new Enemy(enemyRectangle, x, y, enemyRectangle.width, enemyRectangle.height, world);
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

    private void sense() {
        float minDistance = DETECTION_RANGE;
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
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastUpdateTime;
        lastUpdateTime = currentTime;
        elapsedTime += deltaTime;

        switch (currentState) {
            case RUNNING_LEFT:
                xPosition -= MOVEMENT_SPEED;
                break;
            case RUNNING_RIGHT:
                xPosition += MOVEMENT_SPEED;
                break;
            case IDLE:
                break;
        }
    }
}
