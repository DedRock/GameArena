package server.services.authorization;

import client.ErrorResult;
import client.person.Person;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import client.services.authorization.AuthorizationService;
import server.mysql.DbConnectorHolder;
import server.mysql.SqlErrorPrinter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static java.lang.System.*;

public class AuthorizationServiceImpl extends RemoteServiceServlet implements AuthorizationService {
    ResultSet qresult;
    private static final Integer deltaSeconds = -5;

    @Override
    public ErrorResult checkLogin(String user, String pass){
        ErrorResult result = new ErrorResult();

        try{
            qresult = DbConnectorHolder.getInstance().executeQuery("select * from accounts WHERE nickName=\'"+ user +"\'");
            // Если найден хоть один результат
            if ( qresult.first()){
                if ( qresult.getString("Password").equals(pass)){
                    result.set(false, user);
                }
                else{
                    result.set(true, "Неверный пароль");
                }
            }
            else{
                result.set(true, "Пользователя с таким именем не существует.");
            }

        } catch (SQLException e ){
            out.println("Ошибка выполнения SQL-запроса: " + e.getMessage());
            result.set(true, "Server Error");

        }
        finally {
            return result;
        }
    }

    @Override
    public ErrorResult setNewAccount(Person person) {
        ErrorResult result = new ErrorResult();
        try {
            qresult = DbConnectorHolder.getInstance().executeQuery("select * from accounts WHERE nickName=\'"+ person.getNickName() +"\'");
            if ( qresult.first() ){
                result.set(true, "Пользователь с таким именем уже существует.");
            } else{
                // Добавление нового аккаунта
                DbConnectorHolder.getInstance().execute("INSERT accounts (nickName, Name, LastName, Password) VALUES ('" + person.getNickName() + "', '" + person.getName() + "', '" + person.getLastName() + "', '" + person.getPassword() +  "' )");
                result.set(true, person.getNickName());
            }
        } catch (SQLException e) {
            SqlErrorPrinter.print(e.getMessage());
            result.set(true, "Server Error");
        } finally {
            return result;
        }
    }

    @Override
    public List<String> getUsersOnline(String userNickName) {
        List<String> result = new ArrayList<String>();

        try {
            // First: set own lastEnterTime in table "accounts" to current Time
            Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
            DbConnectorHolder.getInstance().execute("UPDATE accounts SET lastEnterTime = '" + curTimestamp +"' WHERE nickName='" + userNickName + "'");
            DbConnectorHolder.getInstance().commit();

            // Найти в таблице Accounts всех пользователей, у которых lastEnterTime не меньше чем (текущее время - 5)
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(curTimestamp.getTime());
            cal.add(Calendar.SECOND, deltaSeconds);
            Timestamp onlineUsersTimestamp = new Timestamp(cal.getTime().getTime());
            qresult = DbConnectorHolder.getInstance().executeQuery("select nickName from accounts WHERE lastEnterTime >= '" + onlineUsersTimestamp + "'");
            while (qresult.next()){
                result.add(qresult.getString("nickName"));
            }
        } catch (SQLException e) {
            SqlErrorPrinter.print(e.getMessage());
            DbConnectorHolder.getInstance().rollback();
        } finally {
            return result;
        }
    }
}