package client.game.xo;

import java.io.Serializable;

public class GameXO_State implements Serializable{
    private boolean gameTerminated = false;
    private String curPlayer;
    private CellState[][] gameboard = new CellState[3][3];

    /**
     * Default constructor
     */
    public GameXO_State(){

        for(int row=0; row<3; row++)
            for(int col=0; col<3; col++)
                gameboard[row][col] = CellState.Empty;
    };


    // Getters And Setters


    public boolean isGameTerminated() {
        return gameTerminated;
    }

    public void setGameTerminated(boolean gameTerminated) {
        this.gameTerminated = gameTerminated;
    }

    public CellState[][] getGameboard() {
        return gameboard;
    }

    public void setGameboard(CellState[][] gameboard) {
        this.gameboard = gameboard;
    }

    public String getCurPlayer() {
        return curPlayer;
    }

    public void setCurPlayer(String curPlayer) {
        this.curPlayer = curPlayer;
    }

    public void setCellsState( CellState[][] newGameboard){
        this.gameboard = newGameboard;
    }

    public void setCellState(int row, int column, CellState value){
        gameboard[row][column] = value;
    }

    /**
     * Вернуть состояние ячейки по указанным координатам
     * @param row - номер строки
     * @param col - номе столбца
     * @return - состояние ячейки
     */
    public CellState getCellState(int row, int col){
        return gameboard[row][col];
    }




}
