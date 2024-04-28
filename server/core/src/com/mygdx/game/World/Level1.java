package com.mygdx.game.World;

import com.mygdx.game.Characters.Enemy;
import com.mygdx.game.Finish.Coin;

public class Level1 extends World {

    /**
     * Initialize necessary objects.
     */
    public Level1() {
        // At enemies in these x coordinate ranges: 0 -> 1090; 1140 -> 1380; 1450 -> 300
        Enemy enemy1 = Enemy.createEnemy(600, 50, 0, 1090, this);
        Enemy enemy2 = Enemy.createEnemy(1000, 50, 0, 1090, this);
        Enemy enemy3 = Enemy.createEnemy(2000, 50, 1450, 3000, this);
        Enemy enemy4 = Enemy.createEnemy(1100, 50, 1140, 1380, this);
        Enemy enemy5 = Enemy.createEnemy(400, 50, 0, 1090, this);
        addEnemy(enemy1.getBotHash(), enemy1);
        addEnemy(enemy2.getBotHash(), enemy2);
        addEnemy(enemy3.getBotHash(), enemy3);
        addEnemy(enemy4.getBotHash(), enemy4);
        addEnemy(enemy5.getBotHash(), enemy5);
        coin = new Coin(3450, 25);
    }
}
