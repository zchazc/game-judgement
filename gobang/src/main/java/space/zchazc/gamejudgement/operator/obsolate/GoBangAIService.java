package space.zchazc.gamejudgement.operator.obsolate;

import com.almasb.fxgl.core.EngineService;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.event.Subscriber;
import space.zchazc.gamejudgement.components.Player;
import space.zchazc.gamejudgement.event.PlayerEvent;


/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public abstract class GoBangAIService extends EngineService {

    private Subscriber eventListener;



    @Override
    public void onInit() {
        eventListener = FXGL.getEventBus().addEventHandler(PlayerEvent.PLAYER_WAITING, event -> {
            makeMove(event.player);
            FXGL.getEventBus().fireEvent(new PlayerEvent(PlayerEvent.PLAYER_MOVED,event.player));
        });
    }

    @Override
    public void onExit() {
        eventListener.unsubscribe();
    }

    public abstract void makeMove(Player player);
}
