package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.superitibros.Screens.GameScreen;

public class Player {

    // Player data
    private int xPosition, yPosition;
    public float width;
    public float height;

    private int movementSpeed;

    private Sprite playerSprite;

    // GameScreen reference
    private GameScreen screen;

    // 2D Box
    public World world;
    public Body b2body;

    public Player(Sprite playerSprite,
                  int xPosition, int yPosition,
                  float width, float height,
                  int movementSpeed, GameScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.playerSprite = playerSprite;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.movementSpeed = movementSpeed;

        // Body definition
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(xPosition, yPosition);
        bdef.fixedRotation = true; // Set fixedRotation to true to prevent rotation

        // Player shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 4f, height / 2f);

        // Fixture definition
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 30f;
        fdef.friction = .0f;
        fdef.restitution = .0f;

        b2body = world.createBody(bdef);
        b2body.createFixture(fdef);
        b2body.setUserData(playerSprite);

        shape.dispose();
    }

    /**
     * PlayerGameCharacter method for drawing the PlayerCharacter on the Screen.
     *
     * @param batch (Batch) is used to draw 2D rectangles
     */
    public void draw(Batch batch) {
        // Update sprite position based on the body's position
        playerSprite.setSize(width, height);
        playerSprite.setOrigin(width / 2, height / 2); // Set the origin to the center of the sprite
        playerSprite.setPosition(b2body.getPosition().x - width / 2, b2body.getPosition().y - height / 2); // Adjust the position based on the center of the sprite
        playerSprite.setRotation((float) Math.toDegrees(b2body.getAngle()));
        playerSprite.draw(batch);
    }


    public void jump() {
        this.b2body.setLinearVelocity(this.b2body.getLinearVelocity().x, movementSpeed);
    }

    public void moveYPosition() {
        this.b2body.setLinearVelocity(this.b2body.getLinearVelocity().x, movementSpeed);
    }

    public void moveYPositionDown() {
        this.b2body.setLinearVelocity(this.b2body.getLinearVelocity().x, -movementSpeed);
    }

    public void moveXPosition() {
        this.b2body.setLinearVelocity(movementSpeed, this.b2body.getLinearVelocity().y);
    }

    public void moveXPositionBack() {
        this.b2body.setLinearVelocity(-movementSpeed, this.b2body.getLinearVelocity().y);
    }

    public void setXPosition(int xPosition) {
        this.b2body.setTransform(xPosition, this.b2body.getPosition().y, this.b2body.getAngle());
    }

    public void setYPosition(int yPosition) {
        this.b2body.setTransform(this.b2body.getPosition().x, yPosition, this.b2body.getAngle());
    }

    public int getYPosition() {
        return (int) this.b2body.getPosition().y;
    }

    public int getXPosition() {
        return (int) this.b2body.getPosition().x;
    }

    public void dispose() {
        playerSprite.getTexture().dispose();
    }
}
