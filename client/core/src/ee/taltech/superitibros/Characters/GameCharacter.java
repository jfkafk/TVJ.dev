package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.superitibros.GameInfo.ClientWorld;

public class GameCharacter {

    SpriteBatch batch;
    TextureAtlas textureAtlas;
    Animation<Sprite> animation;
    float stateTime;
    private AssetManager assetManager;
    Sprite sprite;
    Sprite spriteArrow;
    Animation<TextureRegion> runningAnimation;

    // Character characteristics.
    protected float movementSpeed; // world units per second

    private enum State {IDLE, WALKING, JUMPING, FALL}

    private boolean facingRight = true;

    // Position & dimension.
    public float xPosition;
    public float yPosition; // Lower-left corner

    protected float width, height;
    protected Rectangle boundingBox;

    // World where physics are applied
    ClientWorld clientWorld;
    public Body b2body;
    private boolean bodyDefined = false;

    // Textures
    Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    Animation<TextureRegion> idleAnimation;
    Animation<TextureRegion> jumpAnimation;
    Animation<TextureRegion> fallAnimation;

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
        TextureRegion[] idleFrames = new TextureRegion[64];

        // Define the desired width and height for the cropped frames
        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Calculate the index in the 1D array
                int index = i * 8 + j;
                // Crop each frame to the desired size
                TextureRegion croppedRegion = new TextureRegion(tmpIdle[i][j], 90, 0, croppedWidth, croppedHeight);
                idleFrames[index] = croppedRegion;
            }
        }
        // making IDLE ANIMATION
        idleAnimation = new Animation<TextureRegion>(0.025f, idleFrames);
    }

    public void createFramesWalking() {
        // Sprite

        // Walking
        Texture walksheet = new Texture("Characters/Skeleton sprites/WALK 64 frames.png");
        TextureRegion[][] tmpwalk = TextureRegion.split(walksheet, walksheet.getWidth() / 8, walksheet.getHeight() / 8);
        TextureRegion[] walkFrames = new TextureRegion[64];

        // Define the desired width and height for the cropped frames
        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Calculate the index in the 1D array
                int index = i * 8 + j;
                // Crop each frame to the desired size
                TextureRegion croppedRegion = new TextureRegion(tmpwalk[i][j], 90, 0, croppedWidth, croppedHeight);
                walkFrames[index] = croppedRegion;
            }
        }
        walkAnimation = new Animation<TextureRegion>(0.015f, walkFrames);
    }

    public void createFramesFalling() {
        // Sprite
        // Idle
        Texture fallSheet = new Texture("Characters/Skeleton sprites/FALL 64 frames.png");
        TextureRegion[][] tmpFall = TextureRegion.split(fallSheet, fallSheet.getWidth() / 8,
                fallSheet.getHeight() / 8);
        TextureRegion[] fallFrames = new TextureRegion[64];

        // Define the desired width and height for the cropped frames
        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Calculate the index in the 1D array
                int index = i * 8 + j;
                // Crop each frame to the desired size
                TextureRegion croppedRegion = new TextureRegion(tmpFall[i][j], 90, 0, croppedWidth, croppedHeight);
                fallFrames[index] = croppedRegion;
            }
        }
        // making IDLE ANIMATION
        fallAnimation = new Animation<TextureRegion>(0.025f, fallFrames);
    }

    public void createFramesJumping() {
        // Sprite
        // Idle
        Texture jumpSheet = new Texture("Characters/Skeleton sprites/JUMP 64 frames.png");
        TextureRegion[][] tmpJump = TextureRegion.split(jumpSheet, jumpSheet.getWidth() / 8,
                jumpSheet.getHeight() / 8);
        TextureRegion[] jumpFrames = new TextureRegion[64];

        // Define the desired width and height for the cropped frames
        int croppedWidth = 128; // Adjust as needed
        int croppedHeight = 128; // Adjust as needed

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Calculate the index in the 1D array
                int index = i * 8 + j;
                // Crop each frame to the desired size
                TextureRegion croppedRegion = new TextureRegion(tmpJump[i][j], 90, 0, croppedWidth, croppedHeight);
                jumpFrames[index] = croppedRegion;
            }
        }
        // making IDLE ANIMATION
        jumpAnimation = new Animation<TextureRegion>(0.025f, jumpFrames);
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
        }
    }

    public float getMovementSpeed() {
        return this.movementSpeed;
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
            b2body.setTransform(xPos, yPos, b2body.getAngle());
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
    }

    // Move left
    public void moveLeft() {
        // Apply a force to the left
        this.b2body.applyForceToCenter(new Vector2(-movementSpeed * 70, b2body.getLinearVelocity().y), true);
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
        else if(b2body.getLinearVelocity().x != 0 && isGrounded())
            return State.WALKING;
            //if none of these return then he must be standing
        else if (b2body.getLinearVelocity().x == 0)
            return State.IDLE;
        return State.IDLE;
    }


    public void draw(SpriteBatch batch) {
        // Create a sprite with the texture
        stateTime += Gdx.graphics.getDeltaTime();
        Sprite sprite = animation.getKeyFrame(stateTime,true);
//        sprite.setX(stateTime * 250 % (Gdx.graphics.getWidth() + 400) - 200);

        if (!animationCreated) {
            createFrames();
        }

        // Update the animation state time
        stateTime += Gdx.graphics.getDeltaTime();

        // Get which state is player currently in
        State currentState = getState(); // Muuda olekut vastavalt vajadusele

        b2body.setLinearVelocity(0, b2body.getLinearVelocity().y);

        TextureRegion currentFrame;
        // getting the state of the character
        switch (currentState) {
            case IDLE:
                currentFrame = idleAnimation.getKeyFrame(stateTime, true);
                System.out.println("IDLE,IDLE");
                break;
            case WALKING:
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                System.out.println("WALKING");
                break;
            case FALL:
                currentFrame = fallAnimation.getKeyFrame(stateTime, true);
                System.out.println("FALLING");
                break;
            case JUMPING:
                currentFrame = jumpAnimation.getKeyFrame(stateTime, true);
                System.out.println("Jumping");
            default:
                currentFrame = fallAnimation.getKeyFrame(stateTime, true);
                System.out.println("DEFAULT DEFAULT");
        }

        // Set the position of the current frame to match the position of the Box2D body
        float frameX = b2body.getPosition().x - boundingBox.getHeight();
        float frameY = b2body.getPosition().y - boundingBox.getHeight();

        //Bounding box
        boundingBox.x = b2body.getPosition().x;
        boundingBox.y = b2body.getPosition().y;

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

