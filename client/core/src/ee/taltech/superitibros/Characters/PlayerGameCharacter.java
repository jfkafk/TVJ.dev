package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.superitibros.Screens.GameScreen;

public class PlayerGameCharacter extends GameCharacter{

    // States
    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD }
    public State currentState;
    public State previousState;

    public float width;
    public float height;
    private final float movementSpeed;
    private boolean broIsDead;

    // Player sprite
    private TextureRegion characterTexture;

    // Box2D
    public World world;
    public Body b2body;
    private final PolygonShape shape;
    private int playerGameCharacterId;


    public PlayerGameCharacter(float movementSpeed, Rectangle boundingBox, float xPosition,
                               float yPosition, float width, float height) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height);
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;

        // Player data
        currentState = State.STANDING;
        previousState = State.STANDING;

        // Body definition
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(xPosition, yPosition);
        bdef.fixedRotation = true; // Set fixedRotation to true to prevent rotation

        // Player shape
        shape = new PolygonShape();
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
    public static PlayerGameCharacter createPlayerGameCharacter(float x, float y, int id) {
        Rectangle playerGameCharacterRectangle = new Rectangle(x, y, 10f, 10f);
        PlayerGameCharacter newGameCharacter = new PlayerGameCharacter(2f, playerGameCharacterRectangle, x, y, 10f, 10f);
        newGameCharacter.setPlayerGameCharacterId(id);
        return newGameCharacter;
    }


    private boolean isGrounded() {
        // Implement your logic to check if the player is on the ground
        // For example, you can check if the player's Y velocity is 0 or if it's colliding with the ground
        // You can use Box2D methods like getLinearVelocity() or check collisions with fixtures

        // Update isGrounded based on whether the player's Y velocity is 0
        return Math.abs(b2body.getLinearVelocity().y) < 0.01; // Adjust the threshold as needed
    }

    public State getState(){
        //Test to Box2D for velocity on the X and Y-Axis
        //if mario is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        // Update the grounded status based on current conditions
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
        batch.draw(characterTexture, boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight());
    }


    public void jump() {
<<<<<<< HEAD:client/core/src/ee/taltech/superitibros/Characters/Player.java
        // Player can't jump if he is already in air
        if (currentState != State.JUMPING) {
            // Apply an impulse upwards to simulate the jump
=======
        if (isGrounded()) {
            // Apply the jump impulse only if the player is grounded
>>>>>>> 87a3f93 (Gradle build error.):client/core/src/ee/taltech/superitibros/Characters/PlayerGameCharacter.java
            int jumpImpulse = 50000000; // Define your jump impulse here
            this.b2body.applyLinearImpulse(0, jumpImpulse, this.b2body.getWorldCenter().x, this.b2body.getWorldCenter().y, true);
            currentState = State.JUMPING;
            // Reset the grounded status to prevent consecutive jumps until the player hits the ground again
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
        shape.dispose(); // Dispose the PolygonShape object
    }
}
