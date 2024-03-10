package ee.taltech.game.Characters;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import ee.taltech.game.World.World;

import java.util.ArrayList;
import java.util.Objects;


public class GameCharacter {

    // Character characteristics.
    protected float movementSpeed; // World units per second.
    protected int health;

    // Position & dimension.
    protected float xPosition, yPosition; // Lower-left corner
    protected float width, height;
    protected Rectangle boundingBox;

    private String characterDirection;

    private World world;

    /**
     * GameCharacter constructor.
     *
     * @param movementSpeed of the PlayerGameCharacter (float)
     * @param boundingBox encapsulates a 2D rectangle(bounding box) for the PlayerGameCharacter (Rectangle)
     * @param xPosition of the PlayerGameCharacter (float)
     * @param yPosition of the PlayerGameCharacter (float)
     * @param width of the PlayerGameCharacter (float)
     * @param height of the PlayerGameCharacter (float)
     * @param world game world (World)
     */
    public GameCharacter(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition,
                         float width, float height, World world) {
        this.movementSpeed = movementSpeed;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.boundingBox = boundingBox;
        this.world = world;
        defineCharacter();
    }

    /**
     * Define the GameCharacter's body.
     */
    public void defineCharacter() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(boundingBox.getX(), boundingBox.getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body b2bdy = world.getGdxWorld().createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(boundingBox.getWidth() / 2);

        fixtureDef.shape = shape;
        b2bdy.createFixture(fixtureDef);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public int getHealth() {
        return health;
    }

    public void setCharacterDirection(String direction) {
        this.characterDirection = direction;
    }

    public String getCharacterDirection() {
        return characterDirection;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    /**
     * Moves the GameCharacter to a new position.
     *
     * @param xPos change of the x coordinate
     * @param yPos change of the y coordinate
     */
    public void moveToNewPos(float xPos, float yPos) {
        this.boundingBox.set(boundingBox.getX() + xPos, boundingBox.getY() + yPos, boundingBox.getWidth(), boundingBox.getHeight());
        if (collidesWithWalls(world.getMapLayer())) {
            // If character intersects with wall object/rectangle than moves the character back.
            this.boundingBox.set(boundingBox.getX() - xPos, boundingBox.getY() - yPos, boundingBox.getWidth(), boundingBox.getHeight());
        }
    }

    /**
     * Checks if GameCharacter has collided with walls.
     *
     * @param mapLayer world map
     * @return boolean describing whether GameCharacter collides with a wall
     */
    public boolean collidesWithWalls(MapLayer mapLayer) {
        Array<RectangleMapObject> objects = mapLayer.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            RectangleMapObject obj = objects.get(i);
            Rectangle rect = obj.getRectangle();
            if (boundingBox.overlaps(rect)) {
                return true;
            }
        }
        return false;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCharacter that = (GameCharacter) o;
        return Float.compare(that.movementSpeed, movementSpeed) == 0 && health == that.health
                && Float.compare(that.xPosition, xPosition) == 0 && Float.compare(that.yPosition, yPosition) == 0
                && Float.compare(that.width, width) == 0 && Float.compare(that.height, height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(movementSpeed, health, xPosition, yPosition, width, height);
    }
}