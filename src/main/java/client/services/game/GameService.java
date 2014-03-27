package client.services.game;

import client.game.SimpleGameInfo;
import client.game.xo.GameXO_State;
import client.game.xo.GameXO_WinState;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 24.03.14
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("GameService")
public interface GameService extends RemoteService {

    /**
     * Функция выхода из игры
     * @param gameId - идентификатор игры
     * @param myAccount - мой аккаунт
     * @return true в случае успешного удаления
     */
    boolean closeGame(int gameId, String myAccount);

    /**
     * Функция воздания новой игры
     * @param myAccount
     * @return идентификатор игры
     */
    Integer createGame(String myAccount);

    /**
     * Функция получения с ссервера списка созданных игр
     * @param myAccount - мой аккаунт
     * @return список созданных на сервере игр
     */
    List<SimpleGameInfo> getListOfOpenGames(String myAccount);

    /**
     * Функция присоединения к созданной игре
     * @param gameId
     * @param myAccount
     * @return true в случае успеха
     */
    boolean joinToOpenGame(int gameId, String myAccount);

    /**
     * Функция получения состояния игры с указанным идентификатором
     * @param gameId
     * @return
     */
    GameXO_State updateGameState(int gameId);

    /**
     * Функция ожидания подключения соперника к созданной игре
     * @param gameId
     * @return аккаунт соперника
     */
    String waitForOpponent(int gameId);

    GameXO_WinState writeGameStateService(Integer gemId, String myAccount, GameXO_State newState);

    String altGetUsersOnlineService(String myAccount);

    /**
     * Utility/Convenience class.
     * Use GameService.App.getInstance() to access static instance of GameServiceAsync
     */
    public static class App {
        private static final GameServiceAsync ourInstance = (GameServiceAsync) GWT.create(GameService.class);

        public static GameServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
