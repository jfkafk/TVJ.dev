package ee.taltech.superitibros.Weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {

    Integer bulletId;
    float bulletX;
    float bulletY;

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

    public void draw(SpriteBatch batch) {
        Sprite sprite = new Sprite(new Texture("Bullet/bullet.png"));

        sprite.setSize(20, 20);

        // Set the position of the sprite
        sprite.setPosition(bulletX, bulletY);

        // Draw
        sprite.draw(batch);
    }
}
