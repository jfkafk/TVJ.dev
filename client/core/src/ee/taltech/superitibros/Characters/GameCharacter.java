package ee.taltech.superitibros.Characters;

import ee.taltech.superitibros.Characters.CreateCharacterFrames;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.superitibros.GameInfo.ClientWorld;
import ee.taltech.superitibros.Screens.GameScreen;

public class GameCharacter {


    public static CreateCharacterFrames skinCreator = new CreateCharacterFrames();
    String temporarySkin = "Skeleton";
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
    private final Integer possiblyDealingWithSheetSize = 8;
    private final Integer boundingBoxHeight = 128;
    private final Integer boundingBoxWidth = 128;
    private Integer playerSize = 64;

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
        if (clientWorld.getPath().equals("Maps/level4/gameart2d-desert.tmx")) {
            playerSize = 256;
        }
        defineCharacter();
    }


    /**
     * Making frames for character
     */
    public void createFrames() {
        skinCreator.makeFrames();
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

            this.b2body = clientWorld.getGdxWorld().createBody(bodyDef);

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
        this.boundingBox.set(xPos, yPos, boundingBoxWidth, boundingBoxHeight);
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
        if (b2body != null && newPosition != null) {
            b2body.setTransform(newPosition, b2body.getAngle());
        }
    }


    public void jump() {
        // Player can't jump if he is already in air
        if (isGrounded()) {
            // Apply an impulse upwards to simulate the jump
            this.b2body.applyLinearImpulse(0, 1000000000, this.b2body.getWorldCenter().x, this.b2body.getWorldCenter().y, true);
        }
    }

    // Fall faster
    public void fallDown() {
        this.b2body.setLinearVelocity(this.b2body.getLinearVelocity().y, -movementSpeed * 70);
    }

    // Move right
    public void moveRight() {
        this.b2body.applyForceToCenter(new Vector2(movementSpeed * 70, b2body.getLinearVelocity().y), true);
        facingRight = true;
    }

    // Move left
    public void moveLeft() {
        // Apply a force to the left
        this.b2body.applyForceToCenter(new Vector2(-movementSpeed * 70, b2body.getLinearVelocity().y), true);
        facingRight = false;
    }

    public boolean isGrounded() {
        return b2body.getLinearVelocity().y == 0;
    }

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

    public boolean getFacingRight() {
        return this.facingRight;
    }

    public void draw(SpriteBatch batch) {

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
        float frameX = (b2body.getPosition().x - boundingBox.getHeight()); // Somehow needed -4 to match the sprite.
        float frameY = (b2body.getPosition().y - boundingBox.getHeight());

        // Bounding box
        boundingBox.x = b2body.getPosition().x + ((float) playerSize / 50);
        boundingBox.y = b2body.getPosition().y  + ((float) playerSize / 50);

        // Update coordinates
        xPosition = b2body.getPosition().x + ((float) playerSize / 50);
        yPosition = b2body.getPosition().y + ((float) playerSize / 50);


        // Draw the current frame at the Box2D body position
        if (currentFrame != null) {
            batch.draw(currentFrame, frameX, frameY, playerSize, playerSize);
        }
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
