package space.zchazc.gamejudgement;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.reflections.Reflections;
import space.zchazc.gamejudgement.components.ContinueMovingAction;
import space.zchazc.gamejudgement.components.GoBangFactory;
import space.zchazc.gamejudgement.components.Player;
import space.zchazc.gamejudgement.components.TileCombo;
import space.zchazc.gamejudgement.config.GameConfig;
import space.zchazc.gamejudgement.event.PlayerEvent;
import space.zchazc.gamejudgement.operator.GoBangEngineService;
import space.zchazc.gamejudgement.operator.GoBangOperator;
import space.zchazc.gamejudgement.view.TileViewComponent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static com.almasb.fxgl.dsl.FXGL.*;
import static org.reflections.scanners.Scanners.SubTypes;

public class GoBangApp extends GameApplication {

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 600;
    public static final int EDGE_SIZE = Math.min(APP_HEIGHT,APP_WIDTH);
    public static final double CELL_SIZE = ((double) EDGE_SIZE )/Math.max(GameConfig.ROW_SIZE, GameConfig.COL_SIZE);
    public static final double BOARD_WIDTH = CELL_SIZE* GameConfig.COL_SIZE;
    public static final double BOARD_HEIGHT = CELL_SIZE* GameConfig.ROW_SIZE;

    private int currentPlayer;

    private int firstPlayer = 0;
    private Player[] players = null;

    private GoBangOperator[] availableOperators;

    private AtomicBoolean winning = new AtomicBoolean();
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("GoBang");
        settings.setVersion("1.0");
        settings.setWidth(APP_WIDTH);
        settings.setHeight(APP_HEIGHT);
        settings.addEngineService(GoBangEngineService.class);
        settings.setFileSystemWriteAllowed(false);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);


    }

    private Entity[][] board = new Entity[GameConfig.ROW_SIZE][GameConfig.COL_SIZE];
    private List<TileCombo> combos = new ArrayList<>();
    public Entity[][] getBoard() {
        return board;
    }

    public List<TileCombo> getCombos() {
        return combos;
    }

    @Override
    protected void initInput() {

        getInput()
                .addAction(new ContinueMovingAction("ContinueMoving",
                        this::nextPlayerMove,GameConfig.INTERVAL),MouseButton.PRIMARY);

    }

    @Override
    protected void onPreInit() {

        Reflections reflections = new Reflections("space.zchazc.gamejudgement.operator");
        Set<Class<?>> classes = reflections.get(SubTypes.of(GoBangOperator.class).asClass());
        availableOperators = classes.stream().map(clz->{
            try {
                return (GoBangOperator)clz.getConstructor(int.class,int.class)
                        .newInstance(GameConfig.COL_SIZE,GameConfig.ROW_SIZE);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }).toArray(GoBangOperator[]::new);

        players = Arrays.stream(availableOperators).map(Player::new)
                        .toArray(Player[]::new);

        getEventBus().addEventHandler(PlayerEvent.PLAYER_MOVED, event -> {
            if(!winning.get()){
                checkGameFinished();
            }
//            boolean over = checkGameFinished();
        });

    }

    @Override
    protected void initGame() {
        winning.set(false);
        getGameWorld().addEntityFactory(new GoBangFactory());

        for (int y = 0; y < GameConfig.ROW_SIZE; y++) {
            for (int x = 0; x < GameConfig.COL_SIZE; x++) {
                board[x][y] = spawn("tile", x * CELL_SIZE, y * CELL_SIZE);
            }
        }

        for (int i = 0; i < players.length; i++) {
//            spawn("playerInfoBoard");
        }

        currentPlayer = (firstPlayer++ % players.length) ;
        combos.clear();

        // horizontal
        for (int y = 0; y < GameConfig.ROW_SIZE; y++) {
            for (int x = 0; x + GameConfig.WIN_COMBOS_SIZE <= GameConfig.COL_SIZE; x++) {
                Entity[] combo = new Entity[GameConfig.WIN_COMBOS_SIZE];
                for (int i = 0; i < GameConfig.WIN_COMBOS_SIZE; i++) {
                    combo[i] = board[x+i][y];
                }
                combos.add(new TileCombo(combo));
            }
        }

        // vertical
        for (int x = 0; x < GameConfig.COL_SIZE; x++) {
            for (int y = 0; y + GameConfig.WIN_COMBOS_SIZE <= GameConfig.ROW_SIZE; y++) {
                Entity[] combo = new Entity[GameConfig.WIN_COMBOS_SIZE];
                for (int i = 0; i < GameConfig.WIN_COMBOS_SIZE; i++) {
                    combo[i] = board[x][y+i];
                }
                combos.add(new TileCombo(combo));
            }
        }

        // diagonals
        for (int x = 0; x < GameConfig.COL_SIZE; x++) {
            for (int y = 0; y  < GameConfig.ROW_SIZE; y++) {
                if(x + GameConfig.WIN_COMBOS_SIZE <= GameConfig.COL_SIZE && y + GameConfig.WIN_COMBOS_SIZE <= GameConfig.ROW_SIZE){
                    Entity[] combo = new Entity[GameConfig.WIN_COMBOS_SIZE];
                    for (int i = 0; i < GameConfig.WIN_COMBOS_SIZE; i++) {
                        combo[i] = board[x+i][y+i];
                    }
                    combos.add(new TileCombo(combo));
                }
                if(x - GameConfig.WIN_COMBOS_SIZE + 1 >= 0 && y - GameConfig.WIN_COMBOS_SIZE + 1>= 0){
                    Entity[] combo = new Entity[GameConfig.WIN_COMBOS_SIZE];
                    for (int i = 0; i < GameConfig.WIN_COMBOS_SIZE; i++) {
                        combo[i] = board[x-i][y-i];
                    }
                    combos.add(new TileCombo(combo));
                }
            }
        }




    }


    public void nextPlayerMove(){
        currentPlayer = (currentPlayer + 1) % players.length;
        playerMove(players[currentPlayer]);
    }
    public void playerMove(Player player){
        getEventBus().fireEvent(new PlayerEvent(PlayerEvent.PLAYER_WAITING,player));
    }

    @Override
    protected void initUI() {

        getGameScene().addUINodes(
                IntStream.rangeClosed(0, GameConfig.ROW_SIZE)
                        .mapToObj(idx->new Line(0,CELL_SIZE * idx, BOARD_WIDTH, CELL_SIZE* idx))
                        .toArray(Line[]::new)
                );
        getGameScene().addUINodes(
                IntStream.rangeClosed(0, GameConfig.COL_SIZE)
                        .mapToObj(idx->new Line(CELL_SIZE * idx, 0, CELL_SIZE * idx, BOARD_HEIGHT))
                        .toArray(Line[]::new)
        );
    }

    private boolean checkGameFinished() {
        for (TileCombo combo : combos) {
            if (combo.isComplete()) {
                playWinAnimation(combo);
                return true;
            }
        }

        for (int y = 0; y < GameConfig.ROW_SIZE; y++) {
            for (int x = 0; x < GameConfig.COL_SIZE; x++) {
                Entity tile = board[x][y];
                if (tile.getComponent(TileViewComponent.class).isEmpty()) {
                    // at least 1 tile is empty
                    return false;
                }
            }
        }

        draw();
        return true;
    }

    private void draw() {
        getDialogService().showConfirmationBox("平局\n继续?", yes -> {
            if (yes)
                getGameController().startNewGame();
            else
                getGameController().exit();
        });
    }

    private void playWinAnimation(TileCombo combo) {
        if(!winning.compareAndSet(false,true)){
            return;
        }
        Line line = new Line();
        line.setStartX(combo.getFirstTile().getCenter().getX());
        line.setStartY(combo.getFirstTile().getCenter().getY());
        line.setEndX(combo.getFirstTile().getCenter().getX());
        line.setEndY(combo.getFirstTile().getCenter().getY());
        line.setStroke(Color.YELLOW);
        line.setStrokeWidth(3);


        getGameScene().addUINode(line);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                new KeyValue(line.endXProperty(), combo.getLastTile().getCenter().getX()),
                new KeyValue(line.endYProperty(), combo.getLastTile().getCenter().getY())));
        timeline.setOnFinished(e -> gameOver(combo.getWinner()));
        timeline.play();
    }

    private void gameOver(Player winner) {

        getDialogService().showConfirmationBox(String.format("胜者: %s\n继续?",winner.name), yes -> {
            if (yes)
                getGameController().startNewGame();
            else
                getGameController().exit();
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
