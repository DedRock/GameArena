package client.gui;

import client.game.GameType;
import client.game.SimpleGameInfo;
import com.google.gwt.user.client.ui.Button;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 11.03.14
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */

public class OpenGameButton extends Button {


    class SimpleGameInfoBtn extends SimpleGameInfo {}
    SimpleGameInfoBtn gameInfoBtn;

    /**
     * Constructor
     * @param type - тип игры
     * @param id - идентификатор
     * @param creator - аккаунт создателя игры
     */
    public OpenGameButton(GameType type, int id, String creator){
        gameInfoBtn = new SimpleGameInfoBtn();
        gameInfoBtn.setGameType(type);
        gameInfoBtn.setId(id);
        gameInfoBtn.setPlayer_1(creator);
        super.setHTML("Game:" + gameInfoBtn.getGameType() + "<br> Id: " + gameInfoBtn.getId() + "<br> Creator: " + gameInfoBtn.getPlayer_1());
        this.setStyleName("openGamesBtnView");
    }

    public GameType getGameType(){
        return gameInfoBtn.getGameType();
    }

    public int getGameId(){
        return gameInfoBtn.getId();
    }

    public String getGameCreator(){
        return gameInfoBtn.getPlayer_1();
    }


}
