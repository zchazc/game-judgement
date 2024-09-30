package space.zchazc.gamejudgement.operator;

public abstract class GoBangOperator {

    final int col,row;

    public final String name;

    private int playerId;

    public GoBangOperator(int col, int row){
        this.col = col;
        this.row = row;
        this.name = "未命名操作逻辑";
    }
    public GoBangOperator(int col, int row,String name){
        this.col = col;
        this.row = row;
        this.name = name;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void resetPlayerId(int playerId){
        this.playerId = playerId;
    }
    public void setPlayerId(int playerId){
        this.playerId = playerId;
    }

    public abstract int[] makeMove(int[][] board);



}
