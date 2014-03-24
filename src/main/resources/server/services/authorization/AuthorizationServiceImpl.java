package server.services.authorization;

import client.ErrorResult;
import client.person.Person;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import client.services.authorization.AuthorizationService;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 24.03.14
 * Time: 11:53
 * To change this template use File | Settings | File Templates.
 */
public class AuthorizationServiceImpl extends RemoteServiceServlet implements AuthorizationService {
    @Override
    public ErrorResult checkLogin(String user, String pass) {

        ErrorResult result = new ErrorResult();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            result.setError(true);
        }

        String url = "jdbc:mysql://localhost:3306/gamearena";
        String dbUsername = "root";
        String dbPassword = "password";
        try {
            Connection dbh = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement query = dbh.createStatement();
            ResultSet qresult = query.executeQuery("select * from accounts WHERE nickName=\'"+ user +"\'");
            // Если найден хоть один результат
            if ( qresult.first()){
                if ( qresult.getString("Password").equals(pass)){
                    result.setError(false);
                    result.setErrorDescription(user);
                }
                else{
                    result.setError(true);
                    result.setErrorDescription("Неверный пароль");
                }
            }
            else{
                result.setError(true);
                result.setErrorDescription("Пользователя с таким именем не существует.");
            }
            dbh.close();
        } catch (SQLException e) {
            result.setError(true);
            result.setErrorDescription("Извините. Ошибка при обращении к базе данных.");
        }
        return result;
    }
    @Override
    public ErrorResult setNewAccount(Person perc) {
        ErrorResult result = new ErrorResult();
        result.setError(true); // изначально считаем операцию не выполненой до подтверждения обратного

        String nickName = perc.getNickName();
        String name = perc.getName();
        String lastName = perc.getLastName();
        String password = perc.getPassword();

        String url = "jdbc:mysql://localhost:3306/gamearena";
        String dbUsername = "root";
        String dbPassword = "password";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection dbh = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Проверка на существование аккаунта с текущим nickName
            Statement sqlQuery = dbh.createStatement();
            ResultSet userQueryResultSet = sqlQuery.executeQuery("select * from accounts WHERE nickName=\'"+ perc.getNickName() +"\'");
            if ( userQueryResultSet.first() ){
                result.setErrorDescription("Пользователь с таким именем уже существует.");
                return result;
            }

            // Добавление нового аккаунта
            Boolean qResult = sqlQuery.execute("INSERT accounts (nickName, Name, LastName, Password) VALUES ('" + perc.getNickName() + "', '" + perc.getName() + "', '" + perc.getLastName() + "', '" + perc.getPassword() +  "' )");
            result.setError(false);
            result.setErrorDescription(perc.getNickName());

            dbh.close();
        } catch (SQLException e) {
            result.setErrorDescription("Извините. Ошибка при обращении к базе данных.");
        } catch (ClassNotFoundException e) {
        }

        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }
}