package space.zchazc.gamejudgement.components;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public enum TileValue {
    X("X"), O("O"), NONE("");

    final String symbol;

    TileValue(String symbol) {
        this.symbol = symbol;
    }
}
