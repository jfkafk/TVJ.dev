package ee.taltech.superitibros.Characters;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {

    // rows and columns of the sprite sheet
    private static final int frameCols = 4, frameRows = 1;

    // player data

    private float xPosition, yPosition;
    private float width, height;
    private int health;
    private int movementSpeed;

    private Sprite playerSprite;

    // collision box
    Rectangle collisionBox;

    public Player(String name, Sprite playerSprite,
                  float xCentre, float yCentre,
                  float width, float height,
                  int movementSpeed, int health) {
        this.playerSprite = playerSprite;
        this.xPosition = xCentre - width / 2f;
        this.yPosition = yCentre + height / 2f;
        this.width = width;
        this.height = height;
        this.movementSpeed = movementSpeed;
        this.health = health;

        // initialize collision box
        this.collisionBox = new Rectangle(this.playerSprite.getX(), this.playerSprite.getY(), width, height);
    }

    public void draw(Batch batch) {
        System.out.println("Drawing character");
        System.out.println("char x: " + this.getXPosition() + ", char y: " + this.getYPosition());

        this.playerSprite.setPosition(this.xPosition, this.yPosition);
        Vector2 spriteCenter = new Vector2(playerSprite.getX() + playerSprite.getWidth() / 2, playerSprite.getY() + playerSprite.getHeight() / 2);

        playerSprite.draw(batch);

        // update collision box position
        collisionBox.setPosition(xPosition, yPosition);
    }

    public void moveYPosition() {
        this.yPosition += this.movementSpeed;
        this.collisionBox = new Rectangle(xPosition, yPosition, width, height);
    }

    public void moveXPosition() {
        this.xPosition += this.movementSpeed;
        this.collisionBox = new Rectangle(xPosition, yPosition, width, height);
    }

    public void setXPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public void setYPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean stillAlive() {
        return this.health > 0;
    }

    public float getYPosition() {
        return this.yPosition;
    }

    public float getXPosition() {
        return this.xPosition;
    }

    public int getHealth() {
        return this.health;
    }

    public void dispose() {
    }
}
