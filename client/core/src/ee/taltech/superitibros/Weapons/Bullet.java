package ee.taltech.superitibros.Weapons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {

    // Bullet data
    private final Integer bulletId;
    private float bulletX;
    private float bulletY;
    private final Rectangle boundingBox = new Rectangle(bulletX, bulletY, 20, 20);

    /**
     * Bullet constructor.
     * @param bulletId bullet's id.
     */
    public Bullet(Integer bulletId) {
        this.bulletId = bulletId;
    }

    /**
     * Set bullet x and y coordinate.
     * @param x coordinate.
     * @param y coordinate.
     */
    public void setBulletCoordinates(float x, float y) {
        this.bulletX = x;
        this.bulletY = y;
    }

    /**
     * Get bullet id.
     * @return id.
     */
    public Integer getBulletId() {
        return bulletId;
    }

    /**
     * Return bullet x coordinate.
     * @return x coordinate.
     */
    public float getBulletX() {
        return bulletX;
    }

    /**
     * Return bullet y coordinate.
     * @return y coordinate.
     */
    public float getBulletY() {
        return bulletY;
    }

    /**
     * Get bullet bounding box.
     * @return bounding box.
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    /**
     * Method for drawing bullet.
     * @param batch batch.
     * @param sprite bullet sprite.
     */
    public void draw(SpriteBatch batch, Sprite sprite) {

        sprite.setSize(20, 20);

        boundingBox.x = bulletX;
        boundingBox.y = bulletY;

        // Set the position of the sprite
        sprite.setPosition(bulletX, bulletY);

        // Draw
        sprite.draw(batch);
    }
}
