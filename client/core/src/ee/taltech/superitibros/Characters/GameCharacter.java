package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.superitibros.GameInfo.ClientWorld;


public class GameCharacter {

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

    // World where physics are applied
    ClientWorld clientWorld;
    public Body b2body;
    private boolean bodyDefined = false;
    Vector2 newPosition;

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

    Texture walkSheet;
    Texture idleSheet;
    Texture fallSheet;
    Texture jumpSheet;
    SpriteBatch spriteBatch;

    // A variable for tracking elapsed time for the animation
    float stateTime;

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
        facingRight = true;
        stateTimer = 0;
        defineCharacter();
    }

    public void createFrames() {
        createFramesIdle();
        createFramesWalking();
        createFramesFalling();
        createFramesJumping();
        animationCreated = true;

    }

    public void createFramesIdle() {
        // Sprite
        // Idle
        Texture idlesheet = new Texture("Characters/Skeleton sprites/IDLE 64 frames.png");
        TextureRegion[][] tmpIdle = TextureRegion.split(idlesheet, idlesheet.getWidth() / 8,
                idlesheet.getHeight() / 8);
        TextureRegion[] idleFramesRight = new TextureRegion[64];
        TextureRegion[] idleFramesLeft = new TextureRegion[64];

        // Define the desired width and height for the cropped frames
        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Calculate the index in the 1D array
                int index = i * 8 + j;
                // Crop each frame to the desired size
                TextureRegion croppedRegionRight = new TextureRegion(tmpIdle[i][j], 90, 0, croppedWidth, croppedHeight);
                idleFramesRight[index] = croppedRegionRight;

                TextureRegion croppedRegionLeft = new TextureRegion(tmpIdle[i][j], 40, 0, croppedWidth, croppedHeight);
                croppedRegionLeft.flip(true, false); // Flip horizontally
                idleFramesLeft[index] = croppedRegionLeft;
            }
        }
        // making IDLE ANIMATION
        idleAnimationRight = new Animation<TextureRegion>(0.025f, idleFramesRight);
        idleAnimationLeft = new Animation<TextureRegion>(0.025f, idleFramesLeft);
    }

    public void createFramesWalking() {
        // Sprite

        // Walking
        Texture walksheet = new Texture("Characters/Skeleton sprites/WALK 64 frames.png");
        TextureRegion[][] tmpwalk = TextureRegion.split(walksheet, walksheet.getWidth() / 8, walksheet.getHeight() / 8);

        TextureRegion[] walkFramesRight = new TextureRegion[64];
        TextureRegion[] walkFramesLeft = new TextureRegion[64];

        // Define the desired width and height for the cropped frames
        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Calculate the index in the 1D array
                int index = i * 8 + j;
                // Crop each frame to the desired size
                TextureRegion croppedRegionRight = new TextureRegion(tmpwalk[i][j], 90, 0, croppedWidth, croppedHeight);
                walkFramesRight[index] = croppedRegionRight;

                TextureRegion croppedRegionLeft = new TextureRegion(tmpwalk[i][j], 40, 0, croppedWidth, croppedHeight);
                croppedRegionLeft.flip(true, false); // Flip horizontally
                walkFramesLeft[index] = croppedRegionLeft;
            }
        }

        walkAnimationRight = new Animation<TextureRegion>(0.015f, walkFramesRight);
        walkAnimationLeft = new Animation<TextureRegion>(0.015f, walkFramesLeft);
    }

    public void createFramesFalling() {
        // Sprite
        // Idle
        Texture fallSheet = new Texture("Characters/Skeleton sprites/FALL 64 frames.png");
        TextureRegion[][] tmpFall = TextureRegion.split(fallSheet, fallSheet.getWidth() / 8,
                fallSheet.getHeight() / 8);
        TextureRegion[] fallFramesRight = new TextureRegion[64];
        TextureRegion[] fallFramesLeft = new TextureRegion[64];

        // Define the desired width and height for the cropped frames
        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Calculate the index in the 1D array
                int index = i * 8 + j;
                // Crop each frame to the desired size
                TextureRegion croppedRegionRight = new TextureRegion(tmpFall[i][j], 90, 0, croppedWidth, croppedHeight);
                fallFramesRight[index] = croppedRegionRight;

                TextureRegion croppedRegionLeft = new TextureRegion(tmpFall[i][j], 40, 0, croppedWidth, croppedHeight);
                croppedRegionLeft.flip(true, false); // Flip horizontally
                fallFramesLeft[index] = croppedRegionLeft;
            }
        }
        // making IDLE ANIMATION
        fallAnimationRight = new Animation<TextureRegion>(0.025f, fallFramesRight);
        fallAnimationLeft = new Animation<TextureRegion>(0.025f, fallFramesLeft);
    }

    public void createFramesJumping() {
        // Sprite
        // Idle
        Texture jumpSheet = new Texture("Characters/Skeleton sprites/JUMP 64 frames.png");
        TextureRegion[][] tmpJump = TextureRegion.split(jumpSheet, jumpSheet.getWidth() / 8,
                jumpSheet.getHeight() / 8);
        TextureRegion[] jumpFramesRight = new TextureRegion[64];
        TextureRegion[] jumpFramesLeft = new TextureRegion[64];

        // Define the desired width and height for the cropped frames
        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Calculate the index in the 1D array
                int index = i * 8 + j;
                // Crop each frame to the desired size
                TextureRegion croppedRegionRight = new TextureRegion(tmpJump[i][j], 90, 0, croppedWidth, croppedHeight);
                jumpFramesRight[index] = croppedRegionRight;

                TextureRegion croppedRegionLeft = new TextureRegion(tmpJump[i][j], 40, 0, croppedWidth, croppedHeight);
                croppedRegionLeft.flip(true, false); // Flip horizontally
                jumpFramesLeft[index] = croppedRegionLeft;
            }
        }
        // making IDLE ANIMATION
        jumpAnimationRight = new Animation<TextureRegion>(0.025f, jumpFramesRight);
        jumpAnimationLeft = new Animation<TextureRegion>(0.025f, jumpFramesLeft);
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
        this.boundingBox.set(xPos, yPos, boundingBox.getWidth(), boundingBox.getHeight());
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
            this.b2body.applyLinearImpulse(0, 1000000000f, this.b2body.getWorldCenter().x, this.b2body.getWorldCenter().y, true);
        }
    }

    // Fall faster
    public void fallDown() {
        this.b2body.setLinearVelocity(this.b2body.getLinearVelocity().x, -movementSpeed);
    }

    // Move right
    public void moveRight() {
        this.b2body.applyForceToCenter(new Vector2(movementSpeed * 70, b2body.getLinearVelocity().y), true);
        setFacingRight(true);
    }

    // Move left
    public void moveLeft() {
        // Apply a force to the left
        this.b2body.applyForceToCenter(new Vector2(-movementSpeed * 70, b2body.getLinearVelocity().y), true);
        setFacingRight(false);
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

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
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
                    System.out.println("IDLE, RIGHT");
                } else {
                    currentFrame = idleAnimationLeft.getKeyFrame(stateTime, true);
                    System.out.println("IDLE LEFT");
                }
                break;
            case WALKING:
                if (getFacingRight()) {
                    currentFrame = walkAnimationRight.getKeyFrame(stateTime, true);
                    System.out.println("WALKING RIGHT");
                } else {
                    currentFrame = walkAnimationLeft.getKeyFrame(stateTime, true);
                    System.out.println("WALKING LEFT");
                }
                break;
            case JUMPING:
                if (getFacingRight()) {
                    currentFrame = jumpAnimationRight.getKeyFrame(stateTime, true);
                    System.out.println("JUMPING RIGHT");
                } else {
                    currentFrame = jumpAnimationLeft.getKeyFrame(stateTime, true);
                    System.out.println("JUMPING LEFT");
                }
                break;
            default:
                if (getFacingRight()) {
                    currentFrame = fallAnimationRight.getKeyFrame(stateTime, true);
                    System.out.println("FALLING RIGHT");
                } else {
                    currentFrame = fallAnimationLeft.getKeyFrame(stateTime, true);
                    System.out.println("FALLING LEFT");
                }
                break;
        }

        // Set the position of the current frame to match the position of the Box2D body
        float frameX = b2body.getPosition().x - boundingBox.getHeight();
        float frameY = b2body.getPosition().y - boundingBox.getHeight();

        // Bounding box
        boundingBox.x = b2body.getPosition().x;
        boundingBox.y = b2body.getPosition().y;

        // Update coordinates
        xPosition = b2body.getPosition().x;
        yPosition = b2body.getPosition().y;


        // Draw the current frame at the Box2D body position
        if (currentFrame != null) {
            batch.draw(currentFrame, frameX + 5, frameY, 50, 50);
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

