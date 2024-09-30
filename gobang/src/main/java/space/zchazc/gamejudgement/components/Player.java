package space.zchazc.gamejudgement.components;

import javafx.scene.paint.Color;
import space.zchazc.gamejudgement.operator.GoBangOperator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Player {
    private final static AtomicInteger SEQ_GENERATOR = new AtomicInteger(1);

    private final static Map<String,Color> NAMED_COLORS = Arrays.stream(Color.class.getDeclaredFields())
            .filter(field -> field.getType().equals(Color.class))
            .collect(Collectors.toMap(Field::getName,field -> {
                try {
                    return (Color) field.get(null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }));

    private final static List<String> ORDERED_COLOR = new ArrayList<>(NAMED_COLORS.keySet());



    private GoBangOperator operator;

    public final int id;

    public final String name;
    public final Color color;

    public Player() {
        this.id = SEQ_GENERATOR.getAndIncrement();
        this.color = NAMED_COLORS.get(ORDERED_COLOR.get(id % ORDERED_COLOR.size()));
        this.name = "PLAYER-"+id;
    }

    public Player(GoBangOperator operator) {
        this.id = SEQ_GENERATOR.getAndIncrement();
        this.color = NAMED_COLORS.get(ORDERED_COLOR.get(id % ORDERED_COLOR.size()));
        this.name = "PLAYER-"+id+"["+operator.name+"]";
        this.operator = operator;
        operator.setPlayerId(id);
    }

    public static void resetSeq(){
        SEQ_GENERATOR.set(1);
    }

    public GoBangOperator getOperator() {
        return operator;
    }

    public void setOperator(GoBangOperator operator) {
        operator.resetPlayerId(id);
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                '}';
    }

    public static void main(String[] args) {
        CompletableFuture<String> cf = new CompletableFuture<>();
        cf.completeAsync(()->{
            try {
                Thread.sleep(2999);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "SUC";
        });

        try {
            System.out.println(cf.get(3000, TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

    }

}
