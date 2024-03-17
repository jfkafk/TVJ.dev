package ee.taltech.superitibros.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.superitibros.GameInfo.ClientWorld;

import java.util.Objects;

public class Enemy extends GameCharacter {

    String botHash;

    /**
     * GameCharacter constructor.
     *
     * @param movementSpeed of the PlayerGameCharacter (float)
     * @param boundingBox   encapsulates a 2D rectangle(bounding box) for the PlayerGameCharacter (Rectangle)
     * @param xPosition     of the PlayerGameCharacter (float)
     * @param yPosition     of the PlayerGameCharacter (float)
     * @param width         of the PlayerGameCharacter (float)
     * @param height        of the PlayerGameCharacter (float)
     * @param world         game world (World)
     */
    public Enemy(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width, float height, ClientWorld world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, world);
    }

    @Override
    public void defineCharacter() {
    }

    /**
     * Enemy static method for creating a new Enemy instance.
     *
     * @param x coordinate of the PlayerGameCharacter (float)
     * @param y coordinate of the PlayerGameCharacter (float)
     * @return new Enemy instance
     */
    public static Enemy createEnemy(String botHash, float x, float y, ClientWorld world) {
        Rectangle enemyRectangle = new Rectangle(x, y, 10f, 20f);
        Enemy enemy = new Enemy(10f, enemyRectangle, x, y, enemyRectangle.width, enemyRectangle.height, world);
        enemy.setBotHash(botHash);
        return enemy;
    }

    public void setBotHash(String botHash) {
        this.botHash = botHash;
    }

    public String getBotHash() {
        return botHash;
    }

    public void draw(SpriteBatch batch) {
        // Create a sprite with the texture
        Sprite sprite = new Sprite(new Texture("Characters/TestCharacter.png"));
        sprite.setSize((boundingBox.width) * 2, (boundingBox.height) * 2);

        // Set the position of the sprite to match the physics body
        sprite.setPosition((xPosition - boundingBox.width), (yPosition - boundingBox.height));

        // Draw the sprite
        sprite.draw(batch);
    }

}