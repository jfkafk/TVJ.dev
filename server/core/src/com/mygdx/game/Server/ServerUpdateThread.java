package com.mygdx.game.Server;

import com.mygdx.game.Characters.Enemy;
import com.mygdx.game.World.World;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import static java.lang.Thread.sleep;


/**
 * ServerUpdateThread class is used for updating World (Zombies, PistolBullets etc).
 */
public class ServerUpdateThread implements Runnable {

    private ServerConnection serverConnection;
    private World serverWorld;
    String lobbyHash;

    // TODO create constructor

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public void setServerWorld(World serverWorld) {
        this.serverWorld = serverWorld;
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Update and send Enemies.
                if (!serverWorld.getEnemyMap().isEmpty()) {
                    for (Enemy enemy : serverWorld.getEnemyMap().values()) {
                        enemy.spin();
                    }
                    serverConnection.sendUpdatedEnemies(lobbyHash);
                }

                sleep(5);

            } catch (InterruptedException e) {
                System.out.println("Exception: " + Arrays.toString(e.getStackTrace()));
                System.out.println("Cause: " + e.getCause().toString());
            }
        }
    }
}