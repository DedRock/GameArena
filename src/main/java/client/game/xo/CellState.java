package client.game.xo;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 05.03.14
 * Time: 17:00
 * To change this template use File | Settings | File Templates.
 */
public enum CellState {

    Empty, X, O;

    // Парсинг строки на занчения перечисления
    static public CellState parseString(String s){
        if (s.equals("X"))
            return CellState.X;
        else if (s.equals("O"))
            return CellState.O;
        else
            return CellState.Empty;
    };

}
