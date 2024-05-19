package com.mygdx.game.Server;

import com.mygdx.game.Characters.Enemy;
import com.mygdx.game.Weapons.Bullet;
import com.mygdx.game.World.World;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import static java.lang.Thread.sleep;


/**
 * ServerUpdateThread class is used for updating World (Zombies, PistolBullets etc).
 */
public class ServerUpdateThread implements Runnable {

    private final ServerConnection serverConnection;
    private final World serverWorld;
    String lobbyHash;
    boolean gameOn;

    public ServerUpdateThread(World serverWorld, String lobbyHash, ServerConnection serverConnection) {
        this.serverWorld = serverWorld;
        this.lobbyHash = lobbyHash;
        this.serverConnection = serverConnection;
    }

    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    public String getLobbyHash() {
        return lobbyHash;
    }

    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }

    public boolean isGameOn() {
        return gameOn;
    }

    @Override
    public void run() {
        while (gameOn) {
            try {
                if (serverWorld != null && serverConnection.getOnGoingLobbies().get(lobbyHash) != null
                        && serverConnection.getOnGoingLobbies().get(lobbyHash).getServerWorld() != null) {
                    // Update and send Enemies.
                    if (!serverWorld.getEnemyMap().isEmpty()) {
                        for (Enemy enemy : serverWorld.getEnemyMap().values()) {
                            enemy.spin();
                        }
                        serverConnection.sendUpdatedEnemies(lobbyHash);
                    }

                    // Bullets
                    if(!serverWorld.getBullets().isEmpty()) {

                        // Remove unnecessary bullets
                        if (!serverWorld.getBulletsToRemove().isEmpty()) {
                            serverWorld.getBulletsToRemove().clear();
                        }

                        // Update bullets
                        for (Bullet bullet : serverWorld.getBullets()) {
                            bullet.updateBullet();

                            if (bullet.getBulletX() > 3999 || bullet.getBulletX() < 0 || bullet.getBulletY() > 299 || bullet.getBulletY() < 0) {
                                serverWorld.addBulletToRemove(bullet);
                            }
                        }
                        serverConnection.sendUpdatedBullet(lobbyHash);
                    }

                    sleep(5);
                }

            } catch (InterruptedException e) {
                System.out.println("Exception: " + Arrays.toString(e.getStackTrace()));
                System.out.println("Cause: " + e.getCause().toString());
            }
        }
    }
}