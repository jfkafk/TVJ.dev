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

    int count = 0;

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public void setServerWorld(World serverWorld) {
        this.serverWorld = serverWorld;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(serverWorld.getEnemyMap());
                System.out.println(serverWorld.getClients());
                // Update and send Enemies.
                if (!serverWorld.getEnemyMap().isEmpty()) {
                    for (Enemy enemy : serverWorld.getEnemyMap().values()) {
                        enemy.spin();
                    }
                    serverConnection.sendUpdatedEnemies();
                }

                sleep(5);

            } catch (InterruptedException e) {
                System.out.println("Exception: " + Arrays.toString(e.getStackTrace()));
                System.out.println("Cause: " + e.getCause().toString());
            }
        }
    }
}