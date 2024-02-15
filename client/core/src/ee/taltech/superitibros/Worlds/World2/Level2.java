package ee.taltech.superitibros.Worlds.World2;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Level2 {
    public TiledMap tiledMap;
    public final TiledMapRenderer tiledMapRenderer;
    public MapObjects collisionBoxes;

    public Level2() {
        this.tiledMap = new TmxMapLoader().load("Maps/level2/level2.tmx");
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }
}
