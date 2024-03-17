package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.superitibros.GameInfo.ClientWorld;

import java.util.Objects;


public class GameCharacter {

    // Character characteristics.
    protected float movementSpeed; // world units per second

    // Position & dimension.
    public float xPosition;
    public float yPosition; // Lower-left corner
    protected float width, height;
    protected Rectangle boundingBox;

    // Textures
    private TextureRegion characterTexture;

    // World where physics are applied
    ClientWorld clientWorld;
    public Body b2body;

    private boolean bodyDefined = false;
    Vector2 newPosition;

    /**
     * GameCharacter constructor.
     *
     * @param movementSpeed of the PlayerGameCharacter (float)
     * @param boundingBox encapsulates a 2D rectangle(bounding box) for the PlayerGameCharacter (Rectangle)
     * @param xPosition of the PlayerGameCharacter (float)
     * @param yPosition of the PlayerGameCharacter (float)
     * @param width of the PlayerGameCharacter (float)
     * @param height of the PlayerGameCharacter (float)
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

    public float getMovementSpeed() {
        return this.movementSpeed;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
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

    /**
     * Method for jumping.
     */
    public void jump() {
        // Player can't jump if he is already in air
        if (isGrounded()) {
            // Apply an impulse upwards to simulate the jump
            this.b2body.applyLinearImpulse(0, 7000, this.b2body.getWorldCenter().x, this.b2body.getWorldCenter().y, true);
        }
    }

    /**
     * Method for faster falling.
     */
    public void fallDown() {
        this.b2body.setLinearVelocity(this.b2body.getLinearVelocity().x, -movementSpeed);
    }

    /**
     * Method for moving right.
     */
    // Move right
    public void moveRight() {
        this.b2body.applyForceToCenter(new Vector2(movementSpeed * 70, b2body.getLinearVelocity().y), true);
    }

    /**
     * Method for moving left.
     */
    public void moveLeft() {
        // Apply a force to the left
        this.b2body.applyForceToCenter(new Vector2(-movementSpeed * 70, b2body.getLinearVelocity().y), true);
    }

    /**
     * Method for checking if player is grounded.
     * @return true if grounded, otherwise false.
     */
    public boolean isGrounded() {
        return b2body.getLinearVelocity().y == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movementSpeed, boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight(), characterTexture);
    }

    public void draw(SpriteBatch batch) {
        // Create a sprite with the texture
        Sprite sprite = new Sprite(new Texture("Characters/TestCharacter.png"));
        sprite.setSize((boundingBox.width) * 2, (boundingBox.height) * 2);

        // Set the position of the sprite to match the physics body
        sprite.setPosition((b2body.getPosition().x - boundingBox.width), (b2body.getPosition().y - boundingBox.height));

        xPosition = b2body.getPosition().x;
        yPosition = b2body.getPosition().y;

        boundingBox.x = b2body.getPosition().x;
        boundingBox.y = b2body.getPosition().y;

        // Draw the sprite
        sprite.draw(batch);
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
