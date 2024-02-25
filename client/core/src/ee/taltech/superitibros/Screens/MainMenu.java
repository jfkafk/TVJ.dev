package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import ee.taltech.superitibros.Connection.ClientConnection;

public class MainMenu extends Game {
    GameScreen gameScreen;
    ClientConnection clientConnection;

    @Override
    public void create() {
        MenuScreen menuScreen = new MenuScreen();
        setScreen(menuScreen);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
    }
}
