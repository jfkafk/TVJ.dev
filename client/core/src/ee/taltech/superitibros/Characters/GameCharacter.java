package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import ee.taltech.AudioHelper;
import ee.taltech.superitibros.GameInfo.ClientWorld;

public class GameCharacter {

    // Sounds.
    private final AudioHelper audioHelper = AudioHelper.getInstance();

    public static CreateCharacterFrames skinCreator = new CreateCharacterFrames();
    String nameOfSkin;
    float stateTime;

    // Character characteristics.
    protected float movementSpeed; // world units per second

    public enum State {IDLE, WALKING, JUMPING, FALL}
    boolean facingRight;
    private float stateTimer;

    // Position & dimension.
    public float xPosition;
    public float yPosition; // Lower-left corner

    protected float width, height;
    protected Rectangle boundingBox;

    private Integer playerSize = 50;

    // Health bar properties
    private float maxHealth;
    private float health;
    private float healthBarWidth;
    private float healthBarHeight;
    private Color healthBarColor;
    private float healthBarX;
    private float healthBarY;

    // World where physics are applied
    ClientWorld clientWorld;
    public Body b2body;
    private boolean bodyDefined = false;
    Vector2 newPosition;
    private Integer mapHeight;
    private Integer mapWidth;

    // Textures
    Animation<TextureRegion> walkAnimationRight; // Must declare frame type (TextureRegion)
    Animation<TextureRegion> walkAnimationLeft;
    Animation<TextureRegion> idleAnimationRight;
    Animation<TextureRegion> idleAnimationLeft;
    Animation<TextureRegion> jumpAnimationRight;
    Animation<TextureRegion> jumpAnimationLeft;
    Animation<TextureRegion> fallAnimationRight;
    Animation<TextureRegion> fallAnimationLeft;
    TextureRegion currentFrame;
    public State currentState = State.IDLE;
    // Animation
    boolean animationCreated = false;

    // Jumping sound cooldown.
    private boolean canJump = true;
    private float jumpCooldown = 0.75f;

    /**
     * GameCharacter constructor.
     *
     * @param movementSpeed of the PlayerGameCharacter (float)
     * @param boundingBox   encapsulates a 2D rectangle(bounding box) for the PlayerGameCharacter (Rectangle)
     * @param xPosition     of the PlayerGameCharacter (float)
     * @param yPosition     of the PlayerGameCharacter (float)
     * @param width         of the PlayerGameCharacter (float)
     * @param height        of the PlayerGameCharacter (float)
     */
    public GameCharacter(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width,
                         float height, ClientWorld clientWorld) {
        this.movementSpeed = movementSpeed;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.boundingBox = boundingBox;
        this.clientWorld = clientWorld;
        this.facingRight = true;
        this.stateTimer = 0;
        this.mapHeight = clientWorld.getMapHeight();
        this.mapWidth = clientWorld.getMapWidth();
        defineCharacter();
        // Initialize health bar properties
        maxHealth = 100f;
        health = maxHealth;
        healthBarWidth = 25;
        healthBarHeight = 2;
        healthBarColor = Color.GREEN;
    }

    /**
     *
     * @return the name of the skin
     */
    public String getNameOfSkin() {
        return nameOfSkin;
    }

    /**
     * sets parameter as name of skin
     * @param nameOfSkin
     */
    public void setNameOfSkin(String nameOfSkin) {
        this.nameOfSkin = nameOfSkin;
    }

    /**
     * Making frames for character
     */
    public void createFrames() {
        skinCreator.makeFrames(this);
        this.playerSize = skinCreator.getPlayerSize();
        walkAnimationRight = skinCreator.getWalkAnimationRight();
        walkAnimationLeft = skinCreator.getWalkAnimationLeft();
        idleAnimationRight = skinCreator.getIdleAnimationRight();
        idleAnimationLeft = skinCreator.getIdleAnimationLeft();
        jumpAnimationRight = skinCreator.getJumpAnimationRight();
        jumpAnimationLeft = skinCreator.getJumpAnimationLeft();
        fallAnimationRight = skinCreator.getFallAnimationRight();
        fallAnimationLeft = skinCreator.getFallAnimationLeft();
        animationCreated = true;
    }

    /**
     * Define the GameCharacter's body.
     */
    public void defineCharacter() {
        // Check if the body has already been defined
        if (!bodyDefined) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(boundingBox.getX(), boundingBox.getY());
            bodyDef.type = BodyDef.BodyType.DynamicBody;

            boolean bodyCreated = false;
            while (!bodyCreated) {
                if (!clientWorld.getGdxWorld().isLocked()) {
                    this.b2body = clientWorld.getGdxWorld().createBody(bodyDef);
                    bodyCreated = true;
                }
            }

            FixtureDef fixtureDef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(boundingBox.width, boundingBox.height);
            fixtureDef.shape = shape;

            fixtureDef.filter.categoryBits = CollisionBits.CATEGORY_PLAYER;
            fixtureDef.filter.maskBits = CollisionBits.MASK_PLAYER;

            b2body.createFixture(fixtureDef);
            shape.dispose();

            // Set the flag to true to indicate that the body has been defined
            bodyDefined = true;

            newPosition = new Vector2();
        }
    }

    public float getyPosition() {
        return yPosition;
    }

    public float getxPosition() {
        return xPosition;
    }

    /**
     * Get bounding box.
     * @return bounding box.
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    /**
     * Moves the GameCharacter to a new position.
     *
     * @param xPos of the GameCharacter's new coordinates
     * @param yPos of the GameCharacter's new coordinates
     */
    public void moveToNewPos(float xPos, float yPos) {
        this.boundingBox.set(xPos, yPos, boundingBox.getWidth(), boundingBox.getHeight());
        //System.out.println("Move to y: " + yPos);
        if (b2body != null) {
            // Store the new position for later update
            this.newPosition.set(xPos, yPos);
        }
    }

    /**
     * Update the position of the Box2D body.
     * This method should be called outside the physics simulation loop.
     */
    public void updatePosition() {
        if (b2body != null && newPosition != null && !clientWorld.getGdxWorld().isLocked()) {
            b2body.setTransform(newPosition, b2body.getAngle());
        }
    }


    public void setCurrentState(GameCharacter.State currentState) {
        this.currentState = currentState;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }


    /**
     * Method for jumping.
     */
    public void jump() {
        // Player can't jump if he is already in air
        if (isGrounded() && !clientWorld.getGdxWorld().isLocked()) {
            if (canJump) {
                canJump = false;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        canJump = true;
                    }
                }, jumpCooldown);
                audioHelper.playSound("MusicSounds/jump.mp3");
            }
            // Apply an impulse upwards to simulate the jump
            this.b2body.applyLinearImpulse(0, 1000000000, this.b2body.getWorldCenter().x, this.b2body.getWorldCenter().y, true);
            // System.out.println("jumped");
        }
    }

    /**
     * Method for faster falling.
     */
    public void fallDown() {
        if (!clientWorld.getGdxWorld().isLocked()) {
            this.b2body.setLinearVelocity(this.b2body.getLinearVelocity().x, -movementSpeed * 70);
        }
    }

    /**
     * Method for moving character to right.
     */
    public void moveRight() {
        if (!clientWorld.getGdxWorld().isLocked()) {
            this.b2body.applyForceToCenter(new Vector2(movementSpeed * 70, b2body.getLinearVelocity().y), true);
            facingRight = true;
        }
    }

    /**
     * Method for moving character to left.
     */
    public void moveLeft() {
        if (!clientWorld.getGdxWorld().isLocked()) {
            // Apply a force to the left
            this.b2body.applyForceToCenter(new Vector2(-movementSpeed * 70, b2body.getLinearVelocity().y), true);
            facingRight = false;
        }
    }

    /**
     * Return if character is on the ground.
     * @return true if on the ground, otherwise false.
     */
    public boolean isGrounded() {
        return b2body.getLinearVelocity().y == 0;
    }

    /**
     * Get character state.
     * @return state.
     */
    public State getState() {
        if((b2body.getLinearVelocity().y > 0)) {
            return State.JUMPING;
        }
        //if negative in Y-Axis mario is falling
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALL;
            //if mario is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x != 0 && isGrounded()) {
            return State.WALKING;
        }
        //if none of these return then he must be standing
        return State.IDLE;
    }

    /**
     * Get if player is facing right.
     * @return true if facing right, otherwise false.
     */
    public boolean getFacingRight() {
        return this.facingRight;
    }

    /**
     * Draw game character.
     * @param batch batch.
     */
    public void draw(SpriteBatch batch, Texture whiteTexture) {

        if (!animationCreated) {
            createFrames();
        }

        // Update the animation state time
        stateTime += Gdx.graphics.getDeltaTime();

        // Get which state is player currently in
        State currentState = getState(); // Muuda olekut vastavalt vajadusele

        if (currentState == null) {
            currentState = State.IDLE;
        }

        // Reset x-axis velocity
        b2body.setLinearVelocity(0, clientWorld.getMyPlayerGameCharacter().b2body.getLinearVelocity().y);

        switch (currentState) {
            case IDLE:
                if (getFacingRight()) {
                    currentFrame = idleAnimationRight.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = idleAnimationLeft.getKeyFrame(stateTime, true);
                }
                break;
            case WALKING:
                if (getFacingRight()) {
                    currentFrame = walkAnimationRight.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = walkAnimationLeft.getKeyFrame(stateTime, true);
                }
                break;
            case JUMPING:
                if (getFacingRight()) {
                    currentFrame = jumpAnimationRight.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = jumpAnimationLeft.getKeyFrame(stateTime, true);
                }
                break;
            default:
                if (getFacingRight()) {
                    currentFrame = fallAnimationRight.getKeyFrame(stateTime, true);
                } else {
                    currentFrame = fallAnimationLeft.getKeyFrame(stateTime, true);
                }
                break;
        }

        // Set the position of the current frame to match the position of the Box2D body
        float frameX = (float) (b2body.getPosition().x - boundingBox.getWidth() - 0.1 * playerSize); // Somehow needed -4 to match the sprite.
        float frameY = (b2body.getPosition().y - boundingBox.getHeight());
        //System.out.println("Bounding box height: " + boundingBox.getHeight());
        //System.out.println("Bounding box width: " + boundingBox.getWidth());
        //System.out.println("Position y: " + b2body.getPosition().y);
        //System.out.println("Frame x: " + frameX + " | Frame y: " + frameY);

        // Calculate health bar position
        healthBarX = frameX + boundingBox.width / 2;
        healthBarY = frameY + boundingBox.height;

        // Bounding box
        boundingBox.x = b2body.getPosition().x ;
        boundingBox.y = b2body.getPosition().y;

        // Update coordinates
        xPosition = b2body.getPosition().x;
        yPosition = b2body.getPosition().y;


        // Draw the current frame at the Box2D body position
        if (currentFrame != null) {
            batch.setColor(Color.WHITE); // Reset batch color to default (white)
            batch.draw(currentFrame, frameX, frameY, playerSize, playerSize);
        }

        // Draw health bar
        drawHealthBar(batch, whiteTexture);
    }

    // Draw health bar method
    public void drawHealthBar(Batch batch, Texture whiteTexture) {
        // Calculate health bar fill percentage
        float healthPercentage = health / maxHealth;

        // Set the color of the health bar based on health percentage
        if (healthPercentage > 0.5f) {
            healthBarColor = Color.GREEN;
        } else if (healthPercentage > 0.2f) {
            healthBarColor = Color.YELLOW;
        } else {
            healthBarColor = Color.RED;
        }

        // Calculate the width of the filled portion of the health bar
        float filledWidth = healthBarWidth * healthPercentage;

        // Calculate health bar position
        float healthBarX = xPosition - healthBarWidth / 2;
        float healthBarY = yPosition + height + 5;

        // Draw the health bar outline (border) using the Batch
        batch.setColor(Color.BLACK);
        batch.draw(whiteTexture, (float) (healthBarX - 0.5), (float) (healthBarY - 0.5), healthBarWidth + 1, healthBarHeight + 1);

        // Draw the health bar fill using the Batch
        batch.setColor(healthBarColor);
        batch.draw(whiteTexture, healthBarX, healthBarY, filledWidth, healthBarHeight);
    }

    // Update health method (call this when player takes damage or heals)
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
     * Get game character health.
     * @return health.
     */
    public float getHealth() {
        return health;
    }

    /**
     * Set game character health.
     * @param health health.
     */
    public void setHealth(float health) {
        this.health = health;
    }

    /**
     * Remove the Box2D body from the game world.
     */
    public void removeBodyFromWorld() {
        if (b2body != null) {
            clientWorld.getGdxWorld().destroyBody(b2body);
            b2body = null; // Set the reference to null to indicate that the body has been destroyed
        }
    }
}
