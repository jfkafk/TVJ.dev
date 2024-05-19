package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ee.taltech.superitibros.GameInfo.ClientWorld;

public class MyPlayerGameCharacter extends GameCharacter {

    public float width;
    public float height;
    int id;
    public ClientWorld world;

    /**
     * Constructor for my player.
     * @param movementSpeed movement speed.
     * @param boundingBox bounding box.
     * @param xPosition x coordinate.
     * @param yPosition y coordinate.
     * @param width width.
     * @param height height.
     * @param id id.
     * @param world world.
     */
    public MyPlayerGameCharacter(float movementSpeed, Rectangle boundingBox, float xPosition,
                                 float yPosition, float width, float height, int id, ClientWorld world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, world);
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.id = id;
        this.world = world;
        defineCharacter();
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
}
