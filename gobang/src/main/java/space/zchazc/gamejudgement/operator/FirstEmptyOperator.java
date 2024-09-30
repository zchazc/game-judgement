package space.zchazc.gamejudgement.operator;

public class FirstEmptyOperator extends GoBangOperator {

    public FirstEmptyOperator(int col, int row) {
        super(col, row,"首个不为空");
    }



    @Override
    public int[] makeMove(int[][] board) {
        for (int x = 0; x < col; x++) {
            for (int y = 0; y < row; y++) {
                if(board[x][y] == 0){
                    return new int[]{x,y};
                }
            }
        }
        return null;
    }
}
