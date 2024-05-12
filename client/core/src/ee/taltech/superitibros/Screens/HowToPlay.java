package ee.taltech.superitibros.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.AudioHelper;
import ee.taltech.superitibros.GameInfo.GameClient;

import java.util.ArrayList;

public class HowToPlay implements Screen {
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    GameClient gameClient;

    private Sprite background;

    // Helper for audio
    private AudioHelper audioHelper = AudioHelper.getInstance();

    // List for tutorial.
    private static ArrayList<String> contents = new ArrayList<>();
    private static ArrayList<String> labels = new ArrayList<>();

    private int contentID = 0;
    // Tutorial contents.
    private Label tutorialContents;
    private Label labelsLabel;


    public HowToPlay(GameClient gameClient) {
        this.gameClient = gameClient;
        int worldWidth = 1600;
        int worldHeight = 1000;
        background = new Sprite(new Texture(Gdx.files.internal("Images/forest2.png")));
        atlas = new TextureAtlas("Skins/pixthulhu/skin/pixthulhu-ui.atlas");
        skin = new Skin(Gdx.files.internal("Skins/pixthulhu/skin/pixthulhu-ui.json"), atlas);

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply();
        camera.update();

        stage = new Stage(viewport, batch);

        // Tutorials
        String howToPlayContent = "Choose map and character. Seek and collect\nthe golden coin. For the best experience\nuse fullscreen.";
        String enemiesContent = "Enemies deal damage by standing on top of\nthe character.Deal with them before they\ngot you!";
        String buttonsContent = "A, W, S, D and space or LEFT, UP, DOWN,\nRIGHT to move around. Right click to shoot.\nESC-button closes the ongoing game.";
        String multiplayerContent = "Create or join the lobby to play with your\nfriends.";

        String whatToDo = "What To Do?";
        String enemies = "Enemy";
        String buttons = "Buttons";
        String multiplayer = "Multiplayer";

        contents.add(howToPlayContent);
        contents.add(enemiesContent);
        contents.add(buttonsContent);
        contents.add(multiplayerContent);

        labels.add(whatToDo);
        labels.add(enemies);
        labels.add(buttons);
        labels.add(multiplayer);

        labelsLabel = new Label(labels.get(contentID), skin, "subtitle", Color.GREEN);
        tutorialContents = new Label(contents.get(contentID), skin, "subtitle");
    }

    @Override
    public void show() {
        //Stage should check input:
        Gdx.input.setInputProcessor(stage);

        // Parent table for different instructions.
        Table mainTable = new Table();

        Table whatToDoTable = new Table();

        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();

        // Tutorial labels.
        Label howToPlayLabel = new Label("How To Play", skin, "title", Color.CYAN);

        TextButton reverse = new TextButton("<", skin);
        TextButton forward = new TextButton(">", skin);
        reverse.pad(10);
        forward.pad(10);
        TextButton back = new TextButton("back", skin);

        forward.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                contentID = (contents.indexOf(tutorialContents.getText().toString()) + 1) % 4;
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                updateInstructions();
            }
        });
        reverse.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                contentID = (contents.indexOf(tutorialContents.getText().toString()) - 1);
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                if (contentID == -1) contentID = 3;
                updateInstructions();
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioHelper.playSound("MusicSounds/buttonClick.mp3");
                MenuScreen menuScreen = new MenuScreen(gameClient);
                ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
                dispose();
            }
        });

        mainTable.add(howToPlayLabel).colspan(3);
        mainTable.row();
        mainTable.add(reverse).uniform(true).width(100).padBottom(20).left();
        mainTable.add(labelsLabel);
        mainTable.add(forward).uniform(true).width(100).padBottom(20).right();
        mainTable.row();
        mainTable.add(tutorialContents).colspan(3).width(1000).height(150);
        mainTable.row();
        mainTable.add(back).colspan(3).padTop(50);
        stage.addActor(mainTable);
    }

    // Set new content from list.
    public void updateInstructions() {
        tutorialContents.setText(contents.get(contentID));
        labelsLabel.setText(labels.get(contentID));
    }

    @Override
    public void render(float delta) {
        batch.begin();
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        background.draw(batch);
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
