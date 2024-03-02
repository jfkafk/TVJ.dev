package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.superitibros.Screens.GameScreen;

public class Player {

    // States
    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD }
    public State currentState;
    public State previousState;

    public float width;
    public float height;
    private final int movementSpeed;
    private boolean broIsDead;

    // Player sprite
    private final Sprite playerSprite;

    // Box2D
    public World world;
    public Body b2body;

    public Player(Sprite playerSprite,
                  int xPosition, int yPosition,
                  float width, float height,
                  int movementSpeed, GameScreen screen) {
        // GameScreen reference
        this.world = screen.getWorld();
        this.playerSprite = playerSprite;
        // Player data
        this.width = width;
        this.height = height;
        this.movementSpeed = movementSpeed;
        currentState = State.STANDING;
        previousState = State.STANDING;

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
        fdef.density = 10f;
        fdef.friction = .0f;
        fdef.restitution = .0f;

        // Body in the world
        b2body = world.createBody(bdef);
        b2body.createFixture(fdef);
        b2body.setUserData(playerSprite);

        // Dispose
        shape.dispose();
    }

    public State getState(){
        //Test to Box2D for velocity on the X and Y-Axis
        //if mario is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        if(broIsDead)
            return State.DEAD;
        else if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
            //if negative in Y-Axis mario is falling
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
            //if mario is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
            //if none of these return then he must be standing
        else
            return State.STANDING;
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
        // Player can't jump if he is already in air
        if (currentState != State.JUMPING) {
            // Apply an impulse upwards to simulate the jump
            float jumpImpulse = 500000.0F; // Define your jump impulse here
            this.b2body.applyLinearImpulse(0, jumpImpulse, this.b2body.getWorldCenter().x, this.b2body.getWorldCenter().y, true);
            currentState = State.JUMPING;
        }
    }

    // Update state
    public void updateState() {
        if (b2body.getLinearVelocity().y == 0 && currentState == State.JUMPING) {
            currentState = State.FALLING;
        }
    }

    // Fall faster
    public void moveYPositionDown() {
        this.b2body.setLinearVelocity(this.b2body.getLinearVelocity().x, -movementSpeed);
    }

    // Move right
    public void moveXPosition() {
        // Apply a force to the right
        this.b2body.applyForceToCenter(new Vector2(9999999, 0), true);
    }

    // Move left
    public void moveXPositionBack() {
        // Apply a force to the left
        this.b2body.applyForceToCenter(new Vector2(-9999999, 0), true);
    }

    // Set X coordinate
    public void setXPosition(int xPosition) {
        this.b2body.setTransform(xPosition, this.b2body.getPosition().y, this.b2body.getAngle());
    }

    // Set Y coordinate
    public void setYPosition(int yPosition) {
        this.b2body.setTransform(this.b2body.getPosition().x, yPosition, this.b2body.getAngle());
    }

    // Get Y coordinate
    public int getYPosition() {
        return (int) this.b2body.getPosition().y;
    }

    // Get X coordinate
    public int getXPosition() {
        return (int) this.b2body.getPosition().x;
    }

    // Dispose
    public void dispose() {
        playerSprite.getTexture().dispose();
    }
}
