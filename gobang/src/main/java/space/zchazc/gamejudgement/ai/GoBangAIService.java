package space.zchazc.gamejudgement.ai;

import com.almasb.fxgl.core.EngineService;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.event.Subscriber;
import space.zchazc.gamejudgement.GoBangApp;
import space.zchazc.gamejudgement.Player;
import space.zchazc.gamejudgement.TileViewComponent;
import space.zchazc.gamejudgement.event.PlayerEvent;
import javafx.util.Pair;


/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public abstract class GoBangAIService extends EngineService {

    private Subscriber eventListener;



    @Override
    public void onInit() {

        eventListener = FXGL.getEventBus().addEventHandler(PlayerEvent.PLAYER_WAITING, event -> {
            Entity[][] board = FXGL.<GoBangApp>getAppCast().getBoard();
            int[][] intBoard = new int[GoBangApp.ROW_SIZE][GoBangApp.COL_SIZE];

            for (int x = 0; x < GoBangApp.COL_SIZE; x++) {
                for (int y = 0; y  < GoBangApp.ROW_SIZE; y++) {
                    Player thatPlayer = board[x][y].getComponent(TileViewComponent.class).getPlayer();
                    intBoard[x][y] = thatPlayer == null ? 0 : thatPlayer.id;
                }
            }
            Pair<Integer, Integer> step = event.player.operator.makeMove(intBoard, GoBangApp.COL_SIZE, GoBangApp.ROW_SIZE, event.player.id);
            if(step != null
                   && 0 <= step.getKey() && step.getKey() < GoBangApp.COL_SIZE
            && 0 <= step.getValue() && step.getValue() < GoBangApp.ROW_SIZE){
                board[step.getKey()][step.getValue()].getComponent(TileViewComponent.class).mark(event.player);
            }
            System.out.println(event.player + " MOVING " + step);
            FXGL.getEventBus().fireEvent(new PlayerEvent(PlayerEvent.PLAYER_MOVED,event.player));
        });
    }

    @Override
    public void onExit() {
        eventListener.unsubscribe();
    }

    public abstract void makeMove(Player player);
}
