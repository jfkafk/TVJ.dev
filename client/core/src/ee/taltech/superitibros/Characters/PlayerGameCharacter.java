package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.superitibros.GameInfo.ClientWorld;

public class PlayerGameCharacter extends GameCharacter{

    public float width;
    public float height;
    private final float movementSpeed;
    private boolean broIsDead;

    // Player sprite
    private TextureRegion characterTexture;

    // Box2D
    public ClientWorld world;
    public Body b2body;
    private int playerGameCharacterId;


    public PlayerGameCharacter(float movementSpeed, Rectangle boundingBox, float xPosition,
                               float yPosition, float width, float height, ClientWorld world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, world);
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.world = world;
        defineCharacter();
    }

    public void setPlayerGameCharacterId(int playerGameCharacterId) {
        this.playerGameCharacterId = playerGameCharacterId;
    }

    public int getPlayerGameCharacterId() {
        return playerGameCharacterId;
    }

    public void setCharacterTexture(TextureRegion texture) {
        this.characterTexture = texture;
    }

    /**
     * PlayerGameCharacter static method for creating a new PlayerGameCharacter instance.
     *
     * @param x coordinate of the PlayerGameCharacter (float)
     * @param y coordinate of the PlayerGameCharacter (float)
     * @return new PlayerGameCharacter instance
     */
    public static PlayerGameCharacter createPlayerGameCharacter(float x, float y, int id, ClientWorld world) {
        Rectangle playerGameCharacterRectangle = new Rectangle(x, y, 10f, 20f);
        PlayerGameCharacter newGameCharacter = new PlayerGameCharacter(10f, playerGameCharacterRectangle, x, y, playerGameCharacterRectangle.width, playerGameCharacterRectangle.height, world);
        newGameCharacter.setPlayerGameCharacterId(id);
        return newGameCharacter;
    }

    // Dispose
    public void dispose() {
    }
}
