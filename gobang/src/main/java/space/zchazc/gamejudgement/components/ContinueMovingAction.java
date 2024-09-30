package space.zchazc.gamejudgement.components;

import com.almasb.fxgl.input.UserAction;
import org.jetbrains.annotations.NotNull;

public class ContinueMovingAction extends UserAction {

    @FunctionalInterface
    public interface Action{
        void act();
    }

    private long interval;

    private long lastTriggerTime;

    private Action action;

    public ContinueMovingAction(@NotNull String name, Action action, long interval) {
        super(name);
        this.action = action;
        this.interval = interval;
    }



    @Override
    protected void onAction() {
        if(System.currentTimeMillis() - lastTriggerTime > interval){
            action.act();
            lastTriggerTime = System.currentTimeMillis();
        }
    }
}
