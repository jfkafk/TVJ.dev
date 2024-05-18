package ee.taltech.superitibros.Finish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ee.taltech.superitibros.Helpers.GifDecoder;

/**
 * Represents a coin object in the game.
 */
public class Coin {

    private float xCoordinate;
    private float yCoordinate;
    private final Animation<TextureRegion> animation;
    private float elapsed;

    /**
     * Constructs a new Coin object with the specified coordinates.
     * @param x The x-coordinate of the coin.
     * @param y The y-coordinate of the coin.
     */
    public Coin(float x, float y) {
        xCoordinate = x;
        yCoordinate = y;
        animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("Coin/coin.gif").read());
    }

    /**
     * Draws the coin animation on the batch.
     * @param batch The batch to draw the coin on.
     */
    public void draw(Batch batch) {
        elapsed += Gdx.graphics.getDeltaTime();
        // Set the batch's color to white before drawing the coin animation
        batch.setColor(Color.WHITE);
        batch.draw(animation.getKeyFrame(elapsed), xCoordinate, yCoordinate, 20, 20);
    }

    /**
     * Sets the x-coordinate of the coin.
     * @param xCoordinate The new x-coordinate.
     */
    public void setxCoordinate(float xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    /**
     * Sets the y-coordinate of the coin.
     * @param yCoordinate The new y-coordinate.
     */
    public void setyCoordinate(float yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
