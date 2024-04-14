package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import ee.taltech.superitibros.GameInfo.ClientWorld;

public class Enemy extends GameCharacter {

    String botHash;
    public Body b2body;
    private boolean bodyDefined = false;
    private Integer playerSize = 50;

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
    public Enemy(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width, float height, ClientWorld world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, world);
    }

    /**
     * Define the Enemy's body.
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

    /**
     * Enemy static method for creating a new Enemy instance.
     *
     * @param x coordinate of the PlayerGameCharacter (float)
     * @param y coordinate of the PlayerGameCharacter (float)
     * @return new Enemy instance
     */
    public static Enemy createEnemy(String botHash, float x, float y, ClientWorld world) {
        Rectangle enemyRectangle = new Rectangle(x, y, 10f, 20f);
        Enemy enemy = new Enemy(10f, enemyRectangle, x, y, enemyRectangle.width, enemyRectangle.height, world);
        enemy.setBotHash(botHash);
        return enemy;
    }

    /**
     * Set bot hash.
     * @param botHash bot hash to set.
     */
    public void setBotHash(String botHash) {
        this.botHash = botHash;
    }

    /**
     * Get bot hash.
     * @return bot hash.
     */
    public String getBotHash() {
        return botHash;
    }

    /**
     * Moves the Enemy to a new position.
     *
     * @param xPos of the GameCharacter's new coordinates
     */
    public void moveToNewPos(float xPos) {
        this.boundingBox.set(xPos, b2body.getPosition().y, boundingBox.getWidth(), boundingBox.getHeight());
        if (b2body != null) {
            // Store the new position for later update
            this.newPosition.set(xPos, b2body.getPosition().y);
        }
    }

    public State getState() {
        return State.IDLE;
    }

    public void draw(SpriteBatch batch) {

        if (!animationCreated) {
            createFrames();
        }

        // Update the animation state time
        stateTime += Gdx.graphics.getDeltaTime();

        // Get which state is player currently in
        State currentState = getState();

        if (currentState == null) {
            currentState = State.IDLE;
        }

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
        float frameX = (float) (b2body.getPosition().x - boundingBox.getWidth() * 1.5); // Somehow needed -4 to match the sprite.
        float frameY = (b2body.getPosition().y - boundingBox.getHeight());

        b2body.setTransform(new Vector2(xPosition, b2body.getPosition().y), b2body.getAngle());

        // Bounding box
        boundingBox.x = b2body.getPosition().x ;
        boundingBox.y = b2body.getPosition().y;

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
            System.out.println("isnt null");
            clientWorld.getGdxWorld().destroyBody(b2body);
            b2body = null; // Set the reference to null to indicate that the body has been destroyed
        }

    }

}