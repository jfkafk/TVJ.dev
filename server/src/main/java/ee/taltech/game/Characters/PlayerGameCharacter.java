package ee.taltech.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import ee.taltech.game.World.World;
import ee.taltech.game.Characters.GameCharacter;

public class PlayerGameCharacter extends GameCharacter {

    private int playerGameCharacterId;

    private World world;

    private int timeBetweenZombieCollision = 0;

    private static final int TIME_BETWEEN_COLLISIONS = 100;
    private static final int ZOMBIE_DAMAGE = 10;
    private static final int BULLET_DAMAGE_MULTIPLIER = 5;

    /**
     * Class PlayerGameCharacter constructor.
     *
     * @param movementSpeed of the PlayerGameCharacter (float)
     * @param boundingBox encapsulates a 2D rectangle(bounding box) for the PlayerGameCharacter (Rectangle)
     * @param xPosition of the PlayerGameCharacter (float)
     * @param yPosition of the PlayerGameCharacter (float)
     * @param width of the PlayerGameCharacter (float)
     * @param height of the PlayerGameCharacter (float)
     * @param world where the PlayerGameCharacter is
     */
    public PlayerGameCharacter(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width,
                               float height, World world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, world);
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.height = height;
        this.width = width;
        this.world = world;
    }

    public void setPlayerGameCharacterId(int playerGameCharacterId) {
        this.playerGameCharacterId = playerGameCharacterId;
    }

    public int getPlayerGameCharacterId() {
        return playerGameCharacterId;
    }

    /**
     * PlayerGameCharacter static method for creating a new PlayerGameCharacter instance.
     *
     * @param x coordinate of the PlayerGameCharacter (float)
     * @param y coordinate of the PlayerGameCharacter (float)
     * @param world where the PlayerGameCharacter is (World)
     * @param id unique id (int)
     * @return new PlayerGameCharacter instance
     */
    public static PlayerGameCharacter createPlayerGameCharacter(float x, float y, World world, int id) {
        Rectangle playerGameCharacterRectangle = new Rectangle(x, y, 10f, 10f);
        PlayerGameCharacter newGameCharacter = new PlayerGameCharacter(2f, playerGameCharacterRectangle, x, y, 10f, 10f, world);
        newGameCharacter.setPlayerGameCharacterId(id);
        return newGameCharacter;
    }

}