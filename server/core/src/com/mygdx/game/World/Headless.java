package com.mygdx.game.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;


public class Headless {

    /**
     * This method loads the necessary libGDX components to servers instance so that they could be used.
     *
     * The main thing that is used is TMXmaploader that helps loading the map to Server instance.
     * @param world is the world that game is set in.
     */
    public static void loadHeadless(World world) {
        LwjglNativesLoader.load();
        Gdx.files = new LwjglFiles();
    }
}
