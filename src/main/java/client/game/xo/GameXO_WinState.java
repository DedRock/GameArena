package client.game.xo;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 07.03.14
 * Time: 12:40
 * To change this template use File | Settings | File Templates.
 */
public class GameXO_WinState implements Serializable{
    private boolean win;
    private boolean exitOpponentWin;
    private String winPlayer;
    private int winCellRow[];
    private int winCellCol[];


    public GameXO_WinState(){
        win = false;
        winCellRow = new int[3];
        winCellCol = new int[3];
    }

    public void setWin(boolean newValue){
        win = newValue;
    }

    public boolean getWin(){
        return win;
    }

    public boolean isExitOpponentWin() {
        return exitOpponentWin;
    }

    public void setExitOpponentWin(boolean exitOpponentWin) {
        this.exitOpponentWin = exitOpponentWin;
    }

    public void setWinCellRows(int[] rows){
        winCellRow = rows;
    }

    public void setWinCellCols(int[] cols){
        winCellCol = cols;
    }

    public int getWinCellRow(int index){
        return winCellRow[index];
    }

    public int getWinCellCol(int index){
        return winCellCol[index];
    }

    public String getWinPlayer() {
        return winPlayer;
    }

    public void setWinPlayer(String winPlayer) {
        this.winPlayer = winPlayer;
    }

    /**
     * Функция проверки условий победы игры
     * @param curState
     * @return
     */
    public void isGameEnds(GameXO_State curState){
        boolean result = false;
        // 00-01-02  (1)
        if ( (curState.getCellState(0,0) == curState.getCellState(0,1)) && (curState.getCellState(0,0) == curState.getCellState(0,2)) && (curState.getCellState(0,0) != CellState.Empty )){
            winCellRow = new int[]{0,0,0};
            winCellCol = new int[]{0,1,2};
            result = true;
        }

        // 00-11-22 (2)
        if ( (curState.getCellState(0,0) == curState.getCellState(1,1)) && (curState.getCellState(0,0) == curState.getCellState(2,2)) && (curState.getCellState(0,0) != CellState.Empty )){
            winCellRow = new int[]{0,1,2};
            winCellCol = new int[]{0,1,2};
            result = true;
        }

        // 00-10-20 (3)
        if ( (curState.getCellState(0,0) == curState.getCellState(1,0)) && (curState.getCellState(0,0) == curState.getCellState(2,0)) && (curState.getCellState(0,0) != CellState.Empty )){
            winCellRow = new int[]{0,1,2};
            winCellCol = new int[]{0,0,0};
            result = true;
        }

        // 01-11-21 (4)
        if ( (curState.getCellState(0,1) == curState.getCellState(1,1)) && (curState.getCellState(0,1) == curState.getCellState(2,1)) && (curState.getCellState(0,1) != CellState.Empty )){
            winCellRow = new int[]{0,1,2};
            winCellCol = new int[]{1,1,1};
            result = true;
        }

        // 02-12-22 (5)
        if ( (curState.getCellState(0,2) == curState.getCellState(1,2)) && (curState.getCellState(0,2) == curState.getCellState(2,2)) && (curState.getCellState(0,2) != CellState.Empty )){
            winCellRow = new int[]{0,1,2};
            winCellCol = new int[]{2,2,2};
            result = true;
        }

        // 20-11-02 (6)
        if ( (curState.getCellState(2,0) == curState.getCellState(1,1)) && (curState.getCellState(2,0) == curState.getCellState(0,2)) && (curState.getCellState(2,0) != CellState.Empty )){
            winCellRow = new int[]{2,1,0};
            winCellCol = new int[]{0,1,2};
            result = true;
        }

        // 20-21-22 (7)
        if ( (curState.getCellState(2,0) == curState.getCellState(2,1)) && (curState.getCellState(2,0) == curState.getCellState(2,2)) && (curState.getCellState(2,0) != CellState.Empty )){
            winCellRow = new int[]{2,2,2};
            winCellCol = new int[]{0,1,2};
            result = true;
        }

        // 10-11-12 (8)
        if ( (curState.getCellState(1,0) == curState.getCellState(1,1)) && (curState.getCellState(1,0) == curState.getCellState(1,2)) && (curState.getCellState(1,0) != CellState.Empty )){
            winCellRow = new int[]{1,1,1};
            winCellCol = new int[]{0,1,2};
            result = true;
        }

        if (result){
            win = true;
            winPlayer = curState.getCurPlayer();
            return;
        }

        boolean gameEndsWithoutWinner = true;
        // Проверяем условие окончаня игры - отсутствие свободных клеток
        for(int row=0; row<3; row++)
            for(int col=0; col<3; col++){
                if (curState.getCellState(row, col).equals(CellState.Empty)){
                    gameEndsWithoutWinner = false;
                    break;
                }
            }
        if (gameEndsWithoutWinner){
            win = true;
            winPlayer = null;
        }

    }

}
