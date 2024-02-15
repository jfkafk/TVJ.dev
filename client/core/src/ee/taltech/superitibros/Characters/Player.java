package ee.taltech.superitibros.Characters;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Player {

    // rows and columns of the sprite sheet
    private static final int frameCols = 4, frameRows = 1;

    // player data

    private int xPosition, yPosition;
    public float width;
    public float height;

    private int movementSpeed;

    private Sprite playerSprite;

    // collision box
    Rectangle collisionBox;

    public Player(Sprite playerSprite,
                  int xPosition, int yPosition,
                  float width, float height,
                  int movementSpeed) {
        this.playerSprite = playerSprite;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.movementSpeed = movementSpeed;

        // initialize collision box
        this.collisionBox = new Rectangle(this.playerSprite.getX(), this.playerSprite.getY(), width, height);
    }

    /**
     * PlayerGameCharacter method for drawing the PlayerCharacter on the Screen.
     *
     * @param batch (Batch) is used to draw 2D rectangles
     */
    public void draw(Batch batch) {
        batch.draw(this.playerSprite, this.xPosition, this.yPosition, this.width, this.height);
    }

    public void moveYPosition() {
        this.yPosition += this.movementSpeed;
        this.collisionBox = new Rectangle(xPosition, yPosition, width, height);
    }

    public void moveYPositionDown() {
        this.yPosition -= this.movementSpeed;
        this.collisionBox = new Rectangle(xPosition, yPosition, width, height);
    }

    public void moveXPosition() {
        this.xPosition += this.movementSpeed;
        this.collisionBox = new Rectangle(xPosition, yPosition, width, height);
    }

    public void moveXPositionBack() {
        this.xPosition -= this.movementSpeed;
        this.collisionBox = new Rectangle(xPosition, yPosition, width, height);
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getYPosition() {
        return this.yPosition;
    }

    public int getXPosition() {
        return this.xPosition;
    }

    public void dispose() {
    }
}
