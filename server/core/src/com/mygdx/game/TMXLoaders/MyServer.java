package com.mygdx.game.TMXLoaders;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;

/**
 * THIS IS AN EXAMPLE CLASS
 */
public class MyServer {


    /**
     * Modified filehandle that is built on libGDX's filehandler.
     */
    public static class MyFileHandleResolver implements  FileHandleResolver{
        @Override
        public FileHandle resolve(String fileName) {
            return new FileHandle(new File(fileName));
        }
    }
}
