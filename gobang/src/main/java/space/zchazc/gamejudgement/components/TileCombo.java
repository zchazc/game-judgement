package space.zchazc.gamejudgement.components;

import com.almasb.fxgl.entity.Entity;
import space.zchazc.gamejudgement.view.TileViewComponent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class TileCombo {

    private List<Entity> tiles;


    public TileCombo(Entity ...tiles){
        this.tiles = Arrays.asList(tiles);
    }

    public Entity getFirstTile() {
        return tiles.get(0);
    }

    public Entity getLastTile() {
        return tiles.get(tiles.size()-1);
    }

    public boolean isComplete() {
        List<Player> distinctTiles = tiles.stream().map(this::getPlayerOf)
                .distinct()
                .collect(Collectors.toList());
        return distinctTiles.size() == 1 && distinctTiles.get(0) != null;
    }
    /**
     * @return true if all tiles are empty
     */
    public boolean isOpen() {
        return tiles.stream()
                .allMatch(this::isEmpty);
    }
    /**
     * @return first empty tile or null if no empty tiles
     */
    public Entity getFirstEmpty() {
        return tiles.stream()
                .filter(this::isEmpty)
                .findAny()
                .orElse(null);
    }
    private Player getPlayerOf(Entity tile) {
        return tile.getComponent(TileViewComponent.class).getPlayer();
    }

    private boolean isEmpty(Entity tile) {
        return tile.getComponent(TileViewComponent.class).isEmpty();
    }

    public Player getWinner() {
        return getPlayerOf(tiles.get(0));
    }
}
