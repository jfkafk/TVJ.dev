package com.mygdx.game.World;

import com.mygdx.game.Characters.Enemy;
import com.mygdx.game.Finish.Coin;
import com.mygdx.game.Server.ServerConnection;

public class Level2 extends World{

    /**
     * Initialize necessary objects.
     */
    public Level2(String lobbyHash, ServerConnection serverConnection) {
        super(lobbyHash, serverConnection);
        // Enemies and their ranges.
        Enemy enemy1 = Enemy.createEnemy(100, 60, 0, 1080, this);
        Enemy enemy2 = Enemy.createEnemy(700, 50, 500, 1090, this);
        Enemy enemy3 = Enemy.createEnemy(1200, 50, 1050, 1380, this);
        Enemy enemy4 = Enemy.createEnemy(1500, 50, 1380, 1700, this);
        Enemy enemy5 = Enemy.createEnemy(1700, 50, 1600, 2300, this);
        addEnemy(enemy1.getBotHash(), enemy1);
        addEnemy(enemy2.getBotHash(), enemy2);
        addEnemy(enemy3.getBotHash(), enemy3);
        addEnemy(enemy4.getBotHash(), enemy4);
        addEnemy(enemy5.getBotHash(), enemy5);
        coin = new Coin(2341, 290);
    }

}
