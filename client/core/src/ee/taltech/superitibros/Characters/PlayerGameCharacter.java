package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.superitibros.GameInfo.ClientWorld;

public class PlayerGameCharacter extends GameCharacter{

    public float width;
    public float height;
    // Box2D
    public ClientWorld world;
    private int playerGameCharacterId;
    public GameCharacter.State state = GameCharacter.State.IDLE;
    private boolean facingRight = true;
    float health;


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

    public void setPlayerGameCharacterId(int playerGameCharacterId) {
        this.playerGameCharacterId = playerGameCharacterId;
    }

    public int getPlayerGameCharacterId() {
        return playerGameCharacterId;
    }

    public void setCharacterTexture(TextureRegion texture) {
        // Player sprite
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public boolean getFacingRight() {
        return this.facingRight;
    }

    /**
     * PlayerGameCharacter static method for creating a new PlayerGameCharacter instance.
     *
     * @param x coordinate of the PlayerGameCharacter (float)
     * @param y coordinate of the PlayerGameCharacter (float)
     * @return new PlayerGameCharacter instance
     */
    public static PlayerGameCharacter createPlayerGameCharacter(float x, float y, int id, ClientWorld world) {
        Rectangle playerGameCharacterRectangle = new Rectangle(x, y, 10, 20);
        PlayerGameCharacter newGameCharacter = new PlayerGameCharacter(100f, playerGameCharacterRectangle, x, y, playerGameCharacterRectangle.width, playerGameCharacterRectangle.height, world);
        newGameCharacter.setPlayerGameCharacterId(id);
        return newGameCharacter;
    }

    public GameCharacter.State getState() {
        return state;
    }

    // Dispose
    public void dispose() {
    }
}
