package ee.taltech.superitibros.Weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {

    Integer bulletId;
    float bulletX;
    float bulletY;

    Rectangle boundingBox = new Rectangle(bulletX, bulletY, 20, 20);

    public Bullet(Integer bulletId) {
        this.bulletId = bulletId;
    }

    public void setBulletX(float bulletX) {
        this.bulletX = bulletX;
    }

    public void setBulletY(float bulletY) {
        this.bulletY = bulletY;
    }

    public Integer getBulletId() {
        return bulletId;
    }

    public float getBulletX() {
        return bulletX;
    }

    public float getBulletY() {
        return bulletY;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

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
