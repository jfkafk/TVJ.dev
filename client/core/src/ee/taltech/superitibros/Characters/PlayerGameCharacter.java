package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.math.Rectangle;
import ee.taltech.superitibros.GameInfo.ClientWorld;

/**
 * Represents a player character in the game.
 */
public class PlayerGameCharacter extends GameCharacter {

    /**
     * Width of the player character.
     */
    public float width;

    /**
     * Height of the player character.
     */
    public float height;

    /**
     * The game world in which the character exists.
     */
    public ClientWorld world;

    /**
     * The current state of the character.
     */
    public GameCharacter.State state = GameCharacter.State.IDLE;

    /**
     * Indicates whether the character is facing right.
     */
    private boolean facingRight = true;

    /**
     * The health of the character.
     */
    float health;

    /**
     * Constructs a new PlayerGameCharacter with the specified parameters.
     *
     * @param movementSpeed the movement speed of the character
     * @param boundingBox the bounding box of the character
     * @param xPosition the x-coordinate of the character
     * @param yPosition the y-coordinate of the character
     * @param width the width of the character
     * @param height the height of the character
     * @param world the game world in which the character exists
     */
    public PlayerGameCharacter(float movementSpeed, Rectangle boundingBox, float xPosition,
                               float yPosition, float width, float height, ClientWorld world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, world);
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.world = world;
        defineCharacter();
        this.health = 100;
    }

    /**
     * Sets the ID of the player character.
     *
     * @param playerGameCharacterId the ID to set
     */
    public void setPlayerGameCharacterId(int playerGameCharacterId) {
    }

    /**
     * Sets whether the character is facing right.
     *
     * @param facingRight true if the character is facing right, false otherwise
     */
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    /**
     * Returns whether the character is facing right.
     *
     * @return true if the character is facing right, false otherwise
     */
    public boolean getFacingRight() {
        return this.facingRight;
    }

    /**
     * Creates a new PlayerGameCharacter instance.
     *
     * @param x the x-coordinate of the character
     * @param y the y-coordinate of the character
     * @param id the ID of the character
     * @param world the game world in which the character exists
     * @return a new PlayerGameCharacter instance
     */
    public static PlayerGameCharacter createPlayerGameCharacter(float x, float y, int id, ClientWorld world) {
        Rectangle playerGameCharacterRectangle = new Rectangle(x, y, 10, 20);
        PlayerGameCharacter newGameCharacter = new PlayerGameCharacter(100f,
                playerGameCharacterRectangle, x, y, playerGameCharacterRectangle.width,
                playerGameCharacterRectangle.height, world);
        newGameCharacter.setPlayerGameCharacterId(id);
        return newGameCharacter;
    }

    /**
     * Returns the state of the character.
     *
     * @return the state of the character
     */
    public GameCharacter.State getState() {
        return state;
    }
}
