package server.services;

import client.MySQLServerConnParams;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import client.services.getUsersOnlineService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 24.03.14
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public class getUsersOnlineServiceImpl extends RemoteServiceServlet implements getUsersOnlineService, MySQLServerConnParams {

    public List<String> getUsersOnline(String userNickName) {
        List<String> result = new ArrayList<String>();

        final Integer deltaSeconds = -5;

        String url = "jdbc:mysql://localhost:3306/gamearena";
        String dbUsername = "root";
        String dbPassword = "password";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection dbh = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement sqlQuery = dbh.createStatement();

            // First: set own lastEnterTime in table "accounts" to current Time
            Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
            Boolean qResult = sqlQuery.execute("UPDATE accounts SET lastEnterTime = '" + curTimestamp +"' WHERE nickName='" + userNickName + "'");

            // Найти в таблице Accounts всех пользователей, у которых lastEnterTime не меньше чем (текущее время - 5)
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(curTimestamp.getTime());
            cal.add(Calendar.SECOND, deltaSeconds);
            Timestamp onlineUsersTimestamp = new Timestamp(cal.getTime().getTime());
            ResultSet sqlResultSet = sqlQuery.executeQuery("select nickName from accounts WHERE lastEnterTime >= '" + onlineUsersTimestamp + "'");
            while (sqlResultSet.next()){
                result.add(sqlResultSet.getString("nickName"));
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
}