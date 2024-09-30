package space.zchazc.gamejudgement.operator;

import com.almasb.fxgl.core.EngineService;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.event.Subscriber;
import com.almasb.fxgl.logging.Logger;
import space.zchazc.gamejudgement.GoBangApp;
import space.zchazc.gamejudgement.components.Player;
import space.zchazc.gamejudgement.config.GameConfig;
import space.zchazc.gamejudgement.event.PlayerEvent;
import space.zchazc.gamejudgement.view.TileViewComponent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GoBangEngineService extends EngineService {

    private Subscriber eventListener;

    private final static Logger logger =  Logger.get(GoBangEngineService.class);

    @Override
    public void onInit() {
        eventListener = FXGL.getEventBus().addEventHandler(PlayerEvent.PLAYER_WAITING, event -> {
            Entity[][] board = FXGL.<GoBangApp>getAppCast().getBoard();
            int[][] intBoard = new int[GameConfig.ROW_SIZE][GameConfig.COL_SIZE];

            for (int x = 0; x < GameConfig.COL_SIZE; x++) {
                for (int y = 0; y  < GameConfig.ROW_SIZE; y++) {
                    Player thatPlayer = board[x][y].getComponent(TileViewComponent.class).getPlayer();
                    intBoard[x][y] = thatPlayer == null ? 0 : thatPlayer.id;
                }
            }

            CompletableFuture<int[]> calcStep = new CompletableFuture<>();
            calcStep.completeAsync(() -> event.player.getOperator().makeMove(intBoard));
            int[] step = null;
            boolean isTimeout = false;
            try {
                step = calcStep.get(GameConfig.TIMEOUT, TimeUnit.MILLISECONDS);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                isTimeout = true;
                logger.infof("%s 超时，跳过操作",event.player.name);
            }

            if(!isTimeout && step != null
                    && 0 <= step[0] && step[0] < GameConfig.COL_SIZE
                    && 0 <= step[1] && step[1] < GameConfig.ROW_SIZE){
                board[step[0]][step[1]].getComponent(TileViewComponent.class).mark(event.player);
                logger.infof("%s 移动到 %d,%d",event.player.name,step[0],step[1]);
            }
            FXGL.getEventBus().fireEvent(new PlayerEvent(PlayerEvent.PLAYER_MOVED,event.player));
        });
    }

    @Override
    public void onExit() {
        eventListener.unsubscribe();
    }

}
