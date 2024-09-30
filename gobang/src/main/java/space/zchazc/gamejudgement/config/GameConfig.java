package space.zchazc.gamejudgement.config;

public class GameConfig {

    //设置行
    public static final int ROW_SIZE = 9;
    //设置列
    public static final int COL_SIZE = 9;
    //设置多少连子算获胜
    public static final int WIN_COMBOS_SIZE = 5;
    //设置计算的超时时间
    public static final long TIMEOUT = 3000;
    //每步的计算间隔，不可太低
    public static final long INTERVAL = 100;
}
