package server.services.game;

import client.game.GameType;
import client.game.SimpleGameInfo;
import client.game.xo.CellState;
import client.game.xo.GameXO_State;
import client.game.xo.GameXO_WinState;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import client.services.game.GameService;
import server.mysql.DbConnectorSingleton;
import server.mysql.SqlErrorPrinter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 24.03.14
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public class GameServiceImpl extends RemoteServiceServlet implements GameService {

    ResultSet resultSet;

    @Override
    public boolean closeGame(int gameId, String myAccount) {
        boolean result = false;

        try {

            resultSet = DbConnectorSingleton.getInstance().executeQuery("SELECT * FROM games WHERE id='" + gameId + "'");
            if (resultSet.next()){
                System.out.println("Запросили информацию об игре #" + gameId);
                String player1 = resultSet.getString("player_1");
                String player2 = resultSet.getString("player_2");
                boolean gameEnd = resultSet.getBoolean("gameEnd");

                // Игра создана, но не начата
                if (myAccount.equals(player1) && (player2.isEmpty())){
                    DbConnectorSingleton.getInstance().execute("DELETE games FROM games WHERE id=" + gameId);
                    return true;
                }

                // Если игра идёт
                if (!player1.isEmpty() && !player2.isEmpty() && !gameEnd ){

                    // Определяем оппонента - он выиграет, еслии мы выйдем
                    String opponent = myAccount.equals(player1) ? player2 : player1;
                    DbConnectorSingleton.getInstance().execute("UPDATE games SET gameEnd=TRUE, gameTerminated=TRUE, winPlayer='" + opponent + "' WHERE id=" + gameId);
                    DbConnectorSingleton.getInstance().commit();
                }   return true;
            }
        } catch (SQLException e) {
            SqlErrorPrinter.print(e.getMessage());
            DbConnectorSingleton.getInstance().rollback();
        }
        return result;
    }
    @Override
    public int createGame(String myAccount) {
        int result = -1;

        try {
            // Вставим новую запись в БД
            DbConnectorSingleton.getInstance().execute("INSERT games (player_1, player_2, gameEnd, winPlayer, gameType) VALUES ('" + myAccount + "', '', false, '', 'XO')");
            resultSet = DbConnectorSingleton.getInstance().executeQuery("SELECT id FROM games WHERE player_1 = '" + myAccount + "' AND player_2 = '' AND gameEnd = false AND gameType='XO'");
            if ( resultSet.next() )
                result = Integer.parseInt(resultSet.getString("id"));
        } catch (SQLException e) {
            SqlErrorPrinter.print(e.getMessage());
        }
        return result;
    }
    @Override
    public List<SimpleGameInfo> getListOfOpenGames(String myAccount) {
        List<SimpleGameInfo> result = new ArrayList<SimpleGameInfo>();

        try {
            // Выбираем из таблицы "Games" незавершённые игры у которых зарегистрирован только один игрок - это открытые игры
            resultSet = DbConnectorSingleton.getInstance().executeQuery("SELECT id, player_1, player_2, gameType FROM games WHERE gameEnd=false");
            while (resultSet.next()){
                result.add( new SimpleGameInfo(resultSet.getInt("id"), resultSet.getString("player_1"), resultSet.getString("player_2"), GameType.valueOf(resultSet.getString("gameType"))) );
            }
        } catch (SQLException e) {
            SqlErrorPrinter.print(e.getMessage());
        }
        return result;
    }
    @Override
    public boolean joinToOpenGame(int gameId, String myAccount) {
        try {
            // Проверим, не успел ли кно-ye.elm уже добавитсья
            resultSet = DbConnectorSingleton.getInstance().executeQuery("SELECT player_2 FROM games WHERE id=" + gameId);
            if (resultSet.next())
                // Если место вакантно
                if (resultSet.getString("player_2").isEmpty()){
                    // Добавим в таблицу games к записи с "id" = gameId себя как "player_2"
                    DbConnectorSingleton.getInstance().execute("UPDATE games SET player_2='" + myAccount + "' WHERE id=" + gameId);
                    DbConnectorSingleton.getInstance().commit();
                    return true;
                }
        } catch (SQLException e) {
            SqlErrorPrinter.print(e.getMessage());
            DbConnectorSingleton.getInstance().rollback();
        }
        return false;
    }
    @Override
    public GameXO_State updateGameState(int gameId) {
        GameXO_State result = null;

        try {

            // Проверяем, не завершена ли игра по техническим причинам
            resultSet = DbConnectorSingleton.getInstance().executeQuery("SELECT gameTerminated FROM games WHERE id=" + gameId);
            if (resultSet.next()){
                result = new GameXO_State(); //
                if (resultSet.getBoolean("gameTerminated"))
                    result.setGameTerminated(true);
            }

            resultSet = DbConnectorSingleton.getInstance().executeQuery("SELECT * FROM gamestate WHERE id = '" + gameId + "'");

            if ( resultSet.next() ){
                result.setCurPlayer(resultSet.getString("curPlayer"));
                for(int row=0; row<3; row++)
                    for(int col=0; col<3; col++)
                        result.setCellState(row, col, CellState.parseString(resultSet.getString("State_" + row + col)));
            }
        } catch (SQLException e) {
            SqlErrorPrinter.print(e.getMessage());
        }
        return result;
    }
    @Override
    public String waitForOpponent(int gameId) {
        String opponent = null;

        try {
            resultSet = DbConnectorSingleton.getInstance().executeQuery("SELECT player_1, player_2 FROM games WHERE id=" + gameId);
            if (resultSet.next()){
                if (!resultSet.getString("player_2").isEmpty()){
                    String player_2 = resultSet.getString("player_2");
                    DbConnectorSingleton.getInstance().execute("INSERT gamestate (id, curPlayer) VALUES ('" + gameId + "', '" + resultSet.getString("player_1") + "')");
                    opponent = player_2;
                }
            }
        } catch (SQLException e) {
            SqlErrorPrinter.print(e.getMessage());
        }
        return opponent;
    }
    @Override
    public GameXO_WinState writeGameState(int gameId, String myAccount, GameXO_State newState) {
        GameXO_WinState result = new GameXO_WinState();

        try {
            // Определение игроков данной игры
            resultSet = DbConnectorSingleton.getInstance().executeQuery("SELECT player_1, player_2 FROM games WHERE id=" + gameId);
            if (resultSet.next()){
                String player1 = resultSet.getString("player_1");
                String player2 = resultSet.getString("player_2");

                // Проверка того, что мы точно участвуем в этой игре
                if (myAccount.equals(player1) || myAccount.equals(player2)){

                    // Проверка окончания игры
                    result.isGameEnds(newState);
                    // Если игра не закончилась - то передаём ход
                    if (!result.getWin()){
                        String curPlayer;
                        // Передача хода
                        curPlayer = myAccount.equals(player1) ? player2 : player1;
                        //Записать новое состояние игры в БД
                        DbConnectorSingleton.getInstance().execute("UPDATE gamestate SET curPlayer='" + curPlayer +
                                "', State_00 = '" + newState.getCellState(0, 0) +
                                "', State_01 = '" + newState.getCellState(0, 1) +
                                "', State_02 = '" + newState.getCellState(0, 2) +
                                "', State_10 = '" + newState.getCellState(1, 0) +
                                "', State_11 = '" + newState.getCellState(1, 1) +
                                "', State_12 = '" + newState.getCellState(1, 2) +
                                "', State_20 = '" + newState.getCellState(2, 0) +
                                "', State_21 = '" + newState.getCellState(2, 1) +
                                "', State_22 = '" + newState.getCellState(2, 2) +
                                "' WHERE id=" + gameId);
                        DbConnectorSingleton.getInstance().commit();
                    }
                    else{
                        // Записывыем в таблицу games признак окончания игры и победителя
                        DbConnectorSingleton.getInstance().execute("UPDATE games SET gameEnd=1, winPlayer='" + result.getWinPlayer() + "'");
                        DbConnectorSingleton.getInstance().commit();
                    }
                }
            }
        } catch (SQLException e) {
            SqlErrorPrinter.print(e.getMessage());
            DbConnectorSingleton.getInstance().rollback();
        }

        return result;
    }
}