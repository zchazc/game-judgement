package space.zchazc.gamejudgement.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import space.zchazc.gamejudgement.GoBangApp;
import space.zchazc.gamejudgement.view.TileViewComponent;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class GoBangFactory implements EntityFactory {

    @Spawns("tile")
    public Entity newTile(SpawnData data) {
        var tile = entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(GoBangApp.CELL_SIZE, GoBangApp.CELL_SIZE)))
                .with(new TileViewComponent())
                .build();
        return tile;
    }

    @Spawns("playerInfoBoard")
    public Entity newPlayer(SpawnData data){

        var player = entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(GoBangApp.CELL_SIZE, GoBangApp.CELL_SIZE)))
//                .with(new Player())
                .build();
        return player;
    }
}
