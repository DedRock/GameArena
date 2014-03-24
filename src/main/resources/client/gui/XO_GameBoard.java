package client.gui;

import client.game.xo.CellState;
import client.game.xo.GameXO_State;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;


/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 05.03.14
 * Time: 12:10
 * To change this template use File | Settings | File Templates.
 */
public class XO_GameBoard extends Widget {

    private String myAccount;
    private boolean myAction;
    private VerticalPanel gameboardPanel =  new VerticalPanel();
    private Label systemMessage = new Label();
    private FlexTable gameTable = new FlexTable();
    private Button gameTableButtons[][] = new Button[3][3];

    /**
     * Constructor
     * @param myAccount
     */
    public XO_GameBoard(String myAccount){
        this.myAccount = myAccount;

        gameboardPanel.setWidth("100%");

        gameboardPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        // TableSettings
        //gameTable.setCellPadding(10);
        //gameTable.setCellSpacing(6);
        gameTable.setBorderWidth(1);

        for(int row=0; row<3; row++)
            for(int col=0;col<3; col++){
                gameTableButtons[row][col] = new Button();
                gameTableButtons[row][col].setSize("25px", "25px");
            }
    }


    public void setMyAccount(String newValue){
        this.myAccount = newValue;
    }

    /**
     * Отрисовать игровое поле для текущей ситуации
     * @param curState
     */
    public void updateGameboard(GameXO_State curState){
        System.out.println("Cookies.user = " + Cookies.getCookie("user") + ", curPlayer = " + curState.getCurPlayer());
        if (Cookies.getCookie("user").equals(curState.getCurPlayer()))
            myAction = true;
        else
            myAction = false;

        CellState curCellState;
        System.out.println("MyAction = " + myAction);
        for(int row=0; row < 3; row++){
            for(int col=0; col < 3; col++){
                curCellState = curState.getCellState(row, col);
                System.out.println("State of [" + row + ", " + col + "] = " + curCellState);
                if (curCellState == CellState.Empty){
                    System.out.println("Empty State.");
                    if (! myAction){
                        gameTableButtons[row][col].setEnabled(false);
                        gameTableButtons[row][col].setStyleName("ocupyXOCell");
                    }
                    else{
                        gameTableButtons[row][col].setEnabled(true);
                        System.out.println("Button is Enabled.");
                        gameTableButtons[row][col].setStyleName("freeXOCell");
                    }
                } else if (curCellState == CellState.X){
                    gameTableButtons[row][col].setEnabled(false);
                    gameTableButtons[row][col].setText("X");
                    gameTableButtons[row][col].setStyleName("ocupyXOCell");
                } else if (curCellState == CellState.O){
                    gameTableButtons[row][col].setEnabled(false);
                    gameTableButtons[row][col].setText("O");
                    gameTableButtons[row][col].setStyleName("ocupyXOCell");
                }
                gameTable.setWidget(row, col, gameTableButtons[row][col]);
            }
        }
        systemMessage.setText("Current player = " + curState.getCurPlayer());
        systemMessage.setStyleName("boldText");
        RootPanel.get("gamePanel").clear();
        gameboardPanel.add(systemMessage);
        gameboardPanel.add(gameTable);
        RootPanel.get("gamePanel").add(gameboardPanel);
    }


}
