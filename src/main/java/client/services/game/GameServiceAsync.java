package client.services.game;

import client.game.SimpleGameInfo;
import client.game.xo.GameXO_State;
import client.game.xo.GameXO_WinState;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 24.03.14
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public interface GameServiceAsync {
    /**
     * Функция выхода из игры
     *
     * @param gameId    - идентификатор игры
     * @param myAccount - мой аккаунт
     * @return true в случае успешного удаления
     */
    void closeGame(int gameId, String myAccount, AsyncCallback<Boolean> async);

    /**
     * Функция воздания новой игры
     *
     * @param myAccount
     * @return идентификатор игры
     */
    void createGame(String myAccount, AsyncCallback<Integer> async);

    /**
     * Функция получения с ссервера списка созданных игр
     *
     * @param myAccount - мой аккаунт
     * @return список созданных на сервере игр
     */
    void getListOfOpenGames(String myAccount, AsyncCallback<List<SimpleGameInfo>> async);

    /**
     * Функция присоединения к созданной игре
     *
     * @param gameId
     * @param myAccount
     * @return true в случае успеха
     */
    void joinToOpenGame(int gameId, String myAccount, AsyncCallback<Boolean> async);

    /**
     * Функция получения состояния игры с указанным идентификатором
     *
     * @param gameId
     * @return
     */
    void updateGameState(int gameId, AsyncCallback<GameXO_State> async);

    /**
     * Функция ожидания подключения соперника к созданной игре
     *
     * @param gameId
     * @return аккаунт соперника
     */
    void waitForOpponent(int gameId, AsyncCallback<String> async);

    void writeGameState(int gemId, String myAccount, GameXO_State newState, AsyncCallback<GameXO_WinState> async);

    /**
     * Функция воздания новой игры
     *
     * @param myAccount
     * @return идентификатор игры
     */
}
