package server.services.game;

import client.MySQLServerConnParams;
import client.game.GameType;
import client.game.SimpleGameInfo;
import client.game.xo.CellState;
import client.game.xo.GameXO_State;
import client.game.xo.GameXO_WinState;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import client.services.game.GameService;

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
public class GameServiceImpl extends RemoteServiceServlet implements GameService, MySQLServerConnParams {
    @Override
    public boolean closeGame(int gameId, String myAccount) {
        boolean result = false;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection dbh = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement sqlQuery = dbh.createStatement();

            ResultSet resultSet = sqlQuery.executeQuery("SELECT * FROM games WHERE id='" + gameId + "'");
            if (resultSet.next()){
                System.out.println("Запросили информацию об игре #" + gameId);
                String player_1, player_2, opponent;
                boolean gameEnd;
                player_1 = resultSet.getString("player_1");
                player_2 = resultSet.getString("player_2");
                gameEnd = resultSet.getBoolean("gameEnd");
                System.out.println("Player_1 = " + player_1);
                System.out.println("Player_2 = " + player_2);

                // Игра создана, но не начата
                if (myAccount.equals(player_1) && (player_2.equals(""))){
                    sqlQuery.execute("DELETE games FROM games WHERE id=" + gameId);
                    return true;
                }

                // Если игра идёт
                if (!player_1.equals("") && !player_2.equals("") && !gameEnd ){

                    // Определяем оппонента - он выиграет, еслии мы выйдем
                    if (myAccount.equals(player_1))
                        opponent = player_2;
                    else
                        opponent = player_1;

                    System.out.println("Opponent = " + opponent + " wins, because you are exit !!!");
                    sqlQuery.execute("UPDATE games SET gameEnd=TRUE, gameTerminated=TRUE, winPlayer='" + opponent + "' WHERE id=" + gameId);
                }   return true;

            }
            dbh.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }
    @Override
    public Integer createGame(String myAccount) {
        Integer result = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection dbh = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement sqlQuery = dbh.createStatement();

            // Вставим новую запись в БД
            sqlQuery.execute("INSERT games (player_1, player_2, gameEnd, winPlayer, gameType) VALUES ('"+ myAccount +"', '', false, '', 'XO')");
            ResultSet resultSet = sqlQuery.executeQuery("SELECT id FROM games WHERE player_1 = '" + myAccount + "' AND player_2 = '' AND gameEnd = false AND gameType='XO'");
            if ( resultSet.next() )
                result = Integer.parseInt(resultSet.getString("id"));
            dbh.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }
    @Override
    public List<SimpleGameInfo> getListOfOpenGames(String myAccount) {
        List<SimpleGameInfo> result = new ArrayList<SimpleGameInfo>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection dbh = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement sqlQuery = dbh.createStatement();

            // Выбираем из таблицы "Games" незавершённые игры у которых зарегистрирован только один игрок - это открытые игры
            ResultSet queryResult = sqlQuery.executeQuery("SELECT id, player_1, player_2, gameType FROM games WHERE gameEnd=false");
            while (queryResult.next()){
                result.add( new SimpleGameInfo(queryResult.getInt("id"), queryResult.getString("player_1"), queryResult.getString("player_2"), GameType.valueOf(queryResult.getString("gameType"))) );
            }
            dbh.close();
        } catch (SQLException e) {
            //System.out.println("SQL Exception");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        return result;
    }
    @Override
    public boolean joinToOpenGame(int gameId, String myAccount) {
        System.out.println("Server join operation call...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection dbh = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement sqlQuery = dbh.createStatement();

            // Проверим, не успел ли кно-ye.elm уже добавитсья
            ResultSet resultSet = sqlQuery.executeQuery("SELECT player_2 FROM games WHERE id=" + gameId);
            if (resultSet.next())
                // Если место вакантно
                if (resultSet.getString("player_2").equals("")){
                    // Добавим в таблицу games к записи с "id" = gameId себя как "player_2"
                    sqlQuery.execute("UPDATE games SET player_2='" + myAccount + "' WHERE id=" + gameId);

                    // если всё прошло успешно
                    return true;
                }
            dbh.close();
        } catch (SQLException e) {
            //System.out.println("SQL Exception");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;
    }
    @Override
    public GameXO_State updateGameState(int gameId) {
        GameXO_State result = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection dbh = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement sqlQuery = dbh.createStatement();
            ResultSet resultSet;

            // Проверяем, не завершена ли игра по техническим причинам
            resultSet = sqlQuery.executeQuery("SELECT gameTerminated FROM games WHERE id=" + gameId);
            if (resultSet.next()){
                result = new GameXO_State(); //
                if (resultSet.getBoolean("gameTerminated"))
                    result.setGameTerminated(true);
            }

            resultSet = sqlQuery.executeQuery("SELECT * FROM gamestate WHERE id = '" + gameId + "'");

            if ( resultSet.next() ){
                result.setCurPlayer(resultSet.getString("curPlayer"));
                for(int row=0; row<3; row++)
                    for(int col=0; col<3; col++)
                        result.setCellState(row, col, CellState.parseString(resultSet.getString("State_" + row + col)));
            }
            dbh.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }
    @Override
    public String waitForOpponent(int gameId) {
        String opponent = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection dbh = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement sqlQuery = dbh.createStatement();

            ResultSet resultSet = sqlQuery.executeQuery("SELECT player_1, player_2 FROM games WHERE id=" + gameId);
            if (resultSet.next()){
                if (!resultSet.getString("player_2").equals("")){
                    String player_2 = resultSet.getString("player_2");
                    sqlQuery.execute("INSERT gamestate (id, curPlayer) VALUES ('" + gameId + "', '" + resultSet.getString("player_1") + "')");
                    opponent = player_2;
                }
            }
            dbh.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return opponent;
    }
    @Override
    public GameXO_WinState writeGameStateService(Integer gameId, String myAccount, GameXO_State newState) {
        GameXO_WinState result = new GameXO_WinState();
        Boolean gameOver = false;
        String curPlayer = new String();
        String winPlayer = new String();
        String player_1 = new String();
        String player_2 = new String();

        String url = "jdbc:mysql://localhost:3306/gamearena";
        String dbUsername = "root";
        String dbPassword = "password";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection dbh = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement sqlQuery = dbh.createStatement();

            // Определение игроков данной игры
            ResultSet resultSet = sqlQuery.executeQuery("SELECT player_1, player_2 FROM games WHERE id=" + gameId);
            if (resultSet.next()){
                player_1 = resultSet.getString("player_1");
                player_2 = resultSet.getString("player_2");

                // Проверка окончания игры
                result.isGameEnds(newState);
                // Если игра не закончилась - то пережаём ход
                if (!result.getWin()){
                    System.out.println("Передача хода");
                    // Передача хода
                    if (myAccount.equals(player_1))
                        curPlayer = player_2;
                    if (myAccount.equals(player_2))
                        curPlayer = player_1;
                    System.out.println("Текущий игрок = " + curPlayer);
                }
                else{
                    // Записывыем в таблицу games признак окончания игры и победителя
                    sqlQuery.execute("UPDATE games SET gameEnd=1, winPlayer='" + result.getWinPlayer() + "'");
                }

                //Записать новое состояние игры в БД
                sqlQuery.execute("UPDATE gamestate SET curPlayer='" + curPlayer +
                        "', State_00 = '" + newState.getCellState(0,0)  +
                        "', State_01 = '" + newState.getCellState(0,1)  +
                        "', State_02 = '" + newState.getCellState(0,2)  +
                        "', State_10 = '" + newState.getCellState(1,0)  +
                        "', State_11 = '" + newState.getCellState(1,1)  +
                        "', State_12 = '" + newState.getCellState(1,2)  +
                        "', State_20 = '" + newState.getCellState(2,0)  +
                        "', State_21 = '" + newState.getCellState(2,1)  +
                        "', State_22 = '" + newState.getCellState(2,2)  +
                        "' WHERE id=" + gameId);

            }
            dbh.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return result;
    }
    @Override
    public String altGetUsersOnlineService(String myAccount) {
        String result = new String();
        /*
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("number", new JSONNumber(1));
        jsonResult.put("string", new JSONString("test"));

        result = jsonResult.toString();
        */
        return  result;
    }
}