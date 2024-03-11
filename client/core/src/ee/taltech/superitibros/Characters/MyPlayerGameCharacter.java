package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ee.taltech.superitibros.GameInfo.ClientWorld;

public class MyPlayerGameCharacter extends GameCharacter {

    public float width;

    public float height;
    private final float movementSpeed;
    int id;

    // Player sprite
    private TextureRegion characterTexture;

    // Box2D
    public ClientWorld world;

    private int myPlayerGameCharacterId;

    public MyPlayerGameCharacter(float movementSpeed, Rectangle boundingBox, float xPosition,
                                 float yPosition, float width, float height, int id, ClientWorld world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, world);
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.id = id;
        this.world = world;
        defineCharacter();
    }

    public void setMyPlayerGameCharacterId(int playerGameCharacterId) {
        this.myPlayerGameCharacterId = playerGameCharacterId;
    }

    public int getMyPlayerGameCharacterId() {
        return myPlayerGameCharacterId;
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
    public static MyPlayerGameCharacter createMyPlayerGameCharacter(float x, float y, int id, ClientWorld world) {
        Rectangle myPlayerGameCharacterRectangle = new Rectangle(x, y, 10, 20);
        return new MyPlayerGameCharacter(100f, myPlayerGameCharacterRectangle, x, y, myPlayerGameCharacterRectangle.width, myPlayerGameCharacterRectangle.height, id, world);
    }

    // Dispose
    public void dispose() {
    }
}
