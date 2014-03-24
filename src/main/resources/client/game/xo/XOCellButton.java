package client.game.xo;

import com.google.gwt.user.client.ui.Button;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 07.03.14
 * Time: 13:15
 * To change this template use File | Settings | File Templates.
 */
public class XOCellButton extends Button {

    private int row, col;

    /**
     * Constructor
     * @param row
     * @param col
     */
    public XOCellButton(int row, int col){
        this.row = row;
        this.col = col;

    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
