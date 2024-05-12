package ee.taltech.superitibros.Finish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ee.taltech.superitibros.Helpers.GifDecoder;

public class Coin {

    float xCoordinate;
    float yCoordinate;
    Animation<TextureRegion> animation;
    float elapsed;

    public Coin(float x, float y) {
        xCoordinate = x;
        yCoordinate = y;
        animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Coin/coin.gif").read());
    }

    public void draw(Batch batch) {
        elapsed += Gdx.graphics.getDeltaTime();
        // Set the batch's color to white before drawing the health bar fill
        batch.setColor(Color.WHITE);
        batch.draw(animation.getKeyFrame(elapsed), xCoordinate, yCoordinate, 20, 20);
    }

    public void setxCoordinate(float xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setyCoordinate(float yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public float getyCoordinate() {
        return yCoordinate;
    }

    public float getxCoordinate() {
        return xCoordinate;
    }
}
