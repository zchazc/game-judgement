package space.zchazc.gamejudgement.operator;

import java.util.Random;

public class RandomOperator extends GoBangOperator {
    public static final RandomOperator DEFAULT = new RandomOperator(1,1);
    private final Random rd = new Random();

    public RandomOperator(int col, int row) {
        super(col, row,"随机");
    }



    @Override
    public int[] makeMove(int[][] board) {
        int x,y;
        do{
            x = rd.nextInt(col);
            y = rd.nextInt(row);
        }
        while (board[x][y] != 0);
        return new int[]{x,y};
    }
}
