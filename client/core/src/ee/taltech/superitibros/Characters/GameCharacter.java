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
    protected int health;

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
        defineCharacter();
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
        // Apply the jump impulse only if the player is grounded
        if (isGrounded()) {
            b2body.applyLinearImpulse(0, 100f, b2body.getWorldCenter().x, b2body.getWorldCenter().y, true);
        }
    }

    // Fall faster
    public void fallDown() {
        this.b2body.setLinearVelocity(this.b2body.getLinearVelocity().x, -movementSpeed);
    }

    // Move right
    public void moveRight() {
        // Apply a force to the right
        b2body.applyForceToCenter(new Vector2(movementSpeed * 1000, b2body.getLinearVelocity().y), true);
    }

    // Move left
    public void moveLeft() {
        // Apply a force to the left
        b2body.applyForceToCenter(new Vector2(-movementSpeed * 1000, b2body.getLinearVelocity().y), true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCharacter that = (GameCharacter) o;
        return Float.compare(that.movementSpeed, movementSpeed) == 0 && health == that.health && Float.compare(that.xPosition, xPosition) == 0
                && Float.compare(that.yPosition, yPosition) == 0 && Float.compare(that.width, width) == 0
                && Float.compare(that.height, height) == 0 && Objects.equals(characterTexture, that.characterTexture);
    }

    public boolean isGrounded() {
        return b2body.getLinearVelocity().y == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movementSpeed, health, boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight(), characterTexture);
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
}
