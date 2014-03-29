package server.services.chat;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import client.services.chat.ChatService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 27.03.14
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */
public class ChatServiceImpl extends RemoteServiceServlet implements ChatService {
    @Override
    public void sendNewMesage(String myAccount, String message) {
        String url = "jdbc:mysql://localhost:3306/gamearena";
        String dbUsername = "root";
        String dbPassword = "password";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection dbh = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement sqlQuery = dbh.createStatement();
            sqlQuery.execute("INSERT chat (sender, text) VALUES ('" + myAccount +"', '" + message + "')");
            dbh.close();
        } catch (SQLException e) {
            //System.out.println("SQL Exception");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public String getNewMassages(Long lastReadMsgIndex) {
        JSONObject result = new JSONObject();
        JSONArray messages = new JSONArray();

        String url = "jdbc:mysql://localhost:3306/gamearena";
        String dbUsername = "root";
        String dbPassword = "password";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection dbh = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement sqlQuery = dbh.createStatement();
            ResultSet resSet;

            // Если зашли первый раз - возвращаем 5 последних записей
            if (lastReadMsgIndex == 0){
                // Определям Id последнего сообщения
                Integer lastMsgIndex = 0;
                resSet = sqlQuery.executeQuery("SELECT MAX(id) FROM chat");
                if (resSet.next())
                    lastMsgIndex = resSet.getInt("MAX(id)");

                // Берём последние 5 сообщение (или все, если их < 5)
                if (lastMsgIndex >= 5){
                    resSet = sqlQuery.executeQuery("SELECT * FROM chat WHERE id> (" + lastMsgIndex + "-5);");
                }else{
                    resSet = sqlQuery.executeQuery("SELECT * FROM chat");
                }

            }else{ // Возвращяем все записи, с id > lastMsgIndex
                resSet = sqlQuery.executeQuery("select * from chat WHERE id > '" + lastReadMsgIndex + "'"); // Messages by last Period
            }

            // Формируем JSON-объект для ответа
            try {
                int counter = 0;
                while (resSet.next()){
                        JSONObject messageDescr = new JSONObject();
                        messageDescr.put("id", resSet.getString("id"));
                        messageDescr.put("sender", resSet.getString("sender"));
                        messageDescr.put("text", resSet.getString("text"));
                        messageDescr.put("time", (new SimpleDateFormat("HH:mm:ss").format((Timestamp) resSet.getObject("time")).toString()));
                        messages.put(counter++, messageDescr);
                }
                result.put("messages", messages);
                //System.out.println("Server side: " + result);
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            dbh.close();
        } catch (SQLException e) {
            //System.out.println("SQL Exception");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return result.toString();
    }
}