package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.Objects;


public class GameCharacter {

    // Character characteristics.
    protected float movementSpeed; // world units per second
    protected int health;

    // Position & dimension.
    protected float xPosition, yPosition; // Lower-left corner
    protected float width, height;
    protected Rectangle boundingBox;

    // Textures. Seda pole vaja?
    private TextureRegion characterTexture;

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
                         float height) {
        this.movementSpeed = movementSpeed;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.boundingBox = boundingBox;
    }

    /**
     * GameCharacter constructor without parameters.
     */
    public GameCharacter() { }

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

    @Override
    public int hashCode() {
        return Objects.hash(movementSpeed, health, boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight(), characterTexture);
    }
}
