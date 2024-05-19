package com.mygdx.game.Server;

import com.mygdx.game.Characters.Enemy;
import com.mygdx.game.Weapons.Bullet;
import com.mygdx.game.World.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * The ServerUpdateThread class is responsible for updating the game world on the server.
 */
public class ServerUpdateThread implements Runnable {

    /**
     * The server connection associated with this update thread.
     */
    private final ServerConnection serverConnection;

    /**
     * The world that is being updated by this thread.
     */
    private final World serverWorld;

    /**
     * The lobby hash identifying the lobby associated with this update thread.
     */
    String lobbyHash;

    /**
     * Flag indicating whether the game is currently running.
     */
    boolean gameOn;

    /**
     * Constructs a new ServerUpdateThread.
     *
     * @param serverWorld      the world to be updated (World)
     * @param lobbyHash        the lobby hash identifying the lobby (String)
     * @param serverConnection the server connection (ServerConnection)
     */
    public ServerUpdateThread(World serverWorld, String lobbyHash, ServerConnection serverConnection) {
        this.serverWorld = serverWorld;
        this.lobbyHash = lobbyHash;
        this.serverConnection = serverConnection;
    }

    /**
     * Sets the lobby hash associated with this update thread.
     *
     * @param lobbyHash the lobby hash to set (String)
     */
    public void setLobbyHash(String lobbyHash) {
        this.lobbyHash = lobbyHash;
    }

    /**
     * Retrieves the lobby hash associated with this update thread.
     *
     * @return the lobby hash (String)
     */
    public String getLobbyHash() {
        return lobbyHash;
    }

    /**
     * Sets the flag indicating whether the game is running.
     *
     * @param gameOn the flag value to set (boolean)
     */
    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }

    /**
     * Checks if the game is currently running.
     *
     * @return true if the game is running, false otherwise (boolean)
     */
    public boolean isGameOn() {
        return gameOn;
    }

    /**
     * The main execution method of the update thread.
     */
    @Override
    public void run() {
        while (gameOn) {
            try {
                if (serverWorld != null && serverConnection.getOnGoingLobbies().get(lobbyHash) != null
                        && serverConnection.getOnGoingLobbies().get(lobbyHash).getServerWorld() != null) {
                    // Update and send Enemies.
                    if (!serverWorld.getEnemyMap().isEmpty()) {
                        List<Enemy> enemies = new ArrayList<>(serverWorld.getEnemyMap().values());
                        for (Enemy enemy : enemies) {
                            enemy.spin();
                        }
                        serverConnection.sendUpdatedEnemies(lobbyHash);
                    }

                    // Bullets
                    if (!serverWorld.getBullets().isEmpty()) {

                        // Remove unnecessary bullets
                        if (!serverWorld.getBulletsToRemove().isEmpty()) {
                            serverWorld.getBulletsToRemove().clear();
                        }

                        // Update bullets
                        List<Bullet> bullets = new ArrayList<>(serverWorld.getBullets());
                        for (Bullet bullet : bullets) {
                            bullet.updateBullet();

                            if (bullet.getBulletX() > 3999 || bullet.getBulletX() < 0 || bullet.getBulletY() > 299 || bullet.getBulletY() < 0) {
                                serverWorld.addBulletToRemove(bullet);
                            }
                        }
                        serverConnection.sendUpdatedBullet(lobbyHash);
                    }

                    // Sleep to control update rate
                    Thread.sleep(5);
                }

            } catch (InterruptedException e) {
                // Exception handling
                System.out.println("Exception: " + Arrays.toString(e.getStackTrace()));
                System.out.println("Cause: " + e.getCause().toString());
            } catch (ConcurrentModificationException e) {
                // Concurrent modification exception handling
                System.out.println("ConcurrentModificationException occurred: " + e.getMessage());
            }
        }
    }
}
