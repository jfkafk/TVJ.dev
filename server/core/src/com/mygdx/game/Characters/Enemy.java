package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Weapons.Bullet;
import com.mygdx.game.World.World;

public class Enemy extends GameCharacter {

    // Bot hash
    String botHash;
    // Unique number for botHash
    static int nextBotHashNumber = 0;

    // Enemy (AI) states
    enum State {IDLE, RUNNING_LEFT, RUNNING_RIGHT, JUMPING, FALL, WALKING, ATTACKING}

    private static final float MOVEMENT_SPEED = 0.15f; // At this speed enemy can climb
    private static final float DETECTION_RANGE = 300f;
    private static final float SHOOT_COOLDOWN = 1.0f; // 1 second cooldown

    // Health
    private final float maxHealth;
    private float health;

    // State
    private State currentState;

    // Enemy borders
    float minX;
    float maxX;

    // Last shot time
    private long lastShotTime;

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
    public static Enemy createEnemy(float x, float y, float minX, float maxX, World world) {
        Rectangle enemyRectangle = new Rectangle(x, y, 10f, 20f);
        Enemy enemy = new Enemy(enemyRectangle, x, y, enemyRectangle.width, enemyRectangle.height, world);
        enemy.setMinMax(minX, maxX);
        return enemy;
    }

    /**
     * Set min and max coordinate of enemy.
     * @param minX minimum x coordinate.
     * @param maxX maximum x coordinate.
     */
    public void setMinMax(float minX, float maxX) {
        this.minX = minX;
        this.maxX = maxX;
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
        boolean foundPlayer = false;
        for (GameCharacter player : getWorld().getClients().values()) {
            float distance = Math.abs(xPosition - player.getxPosition()) + Math.abs(yPosition - player.getyPosition());
            if (distance < minDistance) {
                minDistance = distance;
                foundPlayer = true;

                // Check if enough time has passed since the last shot
                if (TimeUtils.timeSinceNanos(lastShotTime) > SHOOT_COOLDOWN * 1_000_000_000L) {
                    // Create bullet for shooting
                    Bullet bullet = Bullet.createBullet(xPosition - width, yPosition,
                            player.getxPosition(), player.getyPosition(), false);
                    // Add to server world
                    getWorld().addBullet(bullet);

                    // Update the last shot time
                    lastShotTime = TimeUtils.nanoTime();
                }

                if ((xPosition <= minX && player.getxPosition() < minX) || (xPosition >= maxX && player.getxPosition() > maxX)) {
                    currentState = State.IDLE;
                } else {
                    currentState = (player.xPosition < xPosition) ? State.RUNNING_LEFT : State.RUNNING_RIGHT;
                }
            }
        }
        if (!foundPlayer) {
            if (xPosition <= minX) {
                currentState = State.RUNNING_RIGHT;
            } else if (xPosition >= maxX) {
                currentState = State.RUNNING_LEFT;
            } else {
                currentState = State.IDLE;
            }
        }
    }

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
