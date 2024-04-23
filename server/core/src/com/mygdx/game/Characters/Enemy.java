package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.World.World;

public class Enemy extends GameCharacter {

    // Bot hash
    String botHash;
    // Unique number for botHash
    static int nextBotHashNumber = 0;

    // Enemy (AI) states
    enum State {IDLE, RUNNING_LEFT, RUNNING_RIGHT, JUMPING, FALL, WALKING, ATTACKING}

    private static final float MOVEMENT_SPEED = 0.1f;
    private static final float DETECTION_RANGE = 100f;
    private long lastUpdateTime;
    // Health
    private float maxHealth;
    private float health;

    private State currentState;

    // Health
    private float maxHealth;
    private float health;

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
        maxHealth = 100f;
        health = maxHealth;
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

    /**
     * Get bot hash.
     * @return bot hash.
     */
    public String getBotHash() {
        return botHash;
    }

    /**
     * Update enemy health.
     * @param amount health.
     */
    public void updateHealth(float amount) {
        health += amount;

        // Ensure health doesn't exceed maximum
        if (health > maxHealth) {
            health = maxHealth;
        }

        // Ensure health doesn't go below 0
        if (health < 0) {
            health = 0;
        }
    }

    /**
     * Get enemy health.
     * @return health.
     */
    public float getHealth() {
        return health;
    }

    /**
     * Main method for looping.
     */
    public void spin() {
        sense();
        //System.out.println("State: " + currentState);
        act();
    }

    /**
     * Sense method for npc sensing.
     */
    private void sense() {
        float minDistance = DETECTION_RANGE;
        for (GameCharacter player : getWorld().getClients().values()) {
            //System.out.println("Player x: " + player.xPosition);
            float distance = Math.abs(xPosition - player.xPosition) + Math.abs(yPosition - player.yPosition);
            //System.out.println("Enemy x: " + xPosition + " | Player x: " + player.xPosition);
            //System.out.println("Enemy y: " + yPosition + " | Player y: " + player.yPosition);
            //System.out.println("Min distance: " + minDistance + " | Distance: " + distance);
            if (distance < minDistance) {
                minDistance = distance;
                //System.out.println("Setting state to run");
                currentState = (player.xPosition < xPosition) ? State.RUNNING_LEFT : State.RUNNING_RIGHT;
            }
        }
        if (minDistance == DETECTION_RANGE) {
            currentState = State.IDLE;
        }
    }

    /**
     * Return if character is on the ground.
     * @return true if on the ground, otherwise false.
     */


    /**
     * Act method for acting.
     */
    private void act() {
        switch (currentState) {
            case RUNNING_LEFT:
                setFacingRight(false);
                xPosition -= MOVEMENT_SPEED;
                break;
            case RUNNING_RIGHT:
                setFacingRight(true);
                xPosition += MOVEMENT_SPEED;
                break;
            case IDLE:
                break;
        }
    }

    @Override
    public GameCharacter.State getCurrentState() {
        if (currentState == State.RUNNING_LEFT || currentState == State.RUNNING_RIGHT) {
            return GameCharacter.State.WALKING;
        } else {
            return GameCharacter.State.IDLE;
        }
    }
}
