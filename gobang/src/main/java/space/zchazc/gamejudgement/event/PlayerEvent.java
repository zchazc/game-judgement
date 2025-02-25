package space.zchazc.gamejudgement.event;

import space.zchazc.gamejudgement.components.Player;
import javafx.event.Event;
import javafx.event.EventType;

public class PlayerEvent extends Event {
    public static final EventType<PlayerEvent> PLAYER_ANY = new EventType<>(Event.ANY, "PLAYER_EVENT");
    public static final EventType<PlayerEvent> PLAYER_WAITING = new EventType<>(PLAYER_ANY, "PLAYER_EVENT_WAITING");
    public static final EventType<PlayerEvent> PLAYER_MOVING = new EventType<>(PLAYER_ANY, "PLAYER_EVENT_MOVING");
    public static final EventType<PlayerEvent> PLAYER_MOVED = new EventType<>(PLAYER_ANY, "PLAYER_EVENT_MOVED");

    public final Player player;

    public PlayerEvent(EventType<? extends Event> eventType, Player player) {
        super(player,null,eventType);
        this.player = player;
    }

}
