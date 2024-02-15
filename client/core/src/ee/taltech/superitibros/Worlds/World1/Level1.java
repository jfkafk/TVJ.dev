package ee.taltech.superitibros.Worlds.World1;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Level1 {
    public TiledMap tiledMap;
    public final TiledMapRenderer tiledMapRenderer;
    public MapObjects collisionBoxes;

    public Level1() {
        this.tiledMap = new TmxMapLoader().load("Maps/level1/level1.tmx");
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }
}
