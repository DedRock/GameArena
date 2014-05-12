package server.services.chat;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import client.services.chat.ChatService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server.mysql.DbConnectorHolder;
import server.mysql.SqlErrorPrinter;
import java.sql.*;
import java.text.SimpleDateFormat;


public class ChatServiceImpl extends RemoteServiceServlet implements ChatService {

    ResultSet resultSet;

    @Override
    public void sendNewMesage(String myAccount, String message) {
        System.out.println("Account:" + myAccount + "; Message: " + message);
        try {
            DbConnectorHolder.getInstance().execute("INSERT chat (sender, text) VALUES ('" + myAccount +"', '" + message + "')");
            DbConnectorHolder.getInstance().commit();

        } catch (SQLException e) {
            SqlErrorPrinter.print(e.getMessage());
            DbConnectorHolder.getInstance().rollback();
        }
    }

    @Override
    public String getNewMassages(long lastReadMsgIndex) {
        JSONObject jsonAnswer = new JSONObject();
        JSONArray messages = new JSONArray();

        try {
            if (lastReadMsgIndex == 0){
                // Определям Id последнего сообщения
                Integer lastMsgIndex = 0;
                resultSet = DbConnectorHolder.getInstance().executeQuery("SELECT MAX(id) FROM chat");
                if (resultSet.next()){
                    lastMsgIndex = resultSet.getInt("MAX(id)");
                }

                // Берём последние 5 сообщение (или все, если их < 5)
                if (lastMsgIndex >= 5){
                    resultSet = DbConnectorHolder.getInstance().executeQuery("SELECT * FROM chat WHERE id> (" + lastMsgIndex + "-5);");
                }else{
                    resultSet = DbConnectorHolder.getInstance().executeQuery("SELECT * FROM chat");
                }

            }else{ // Возвращяем все записи, с id > lastMsgIndex
                resultSet = DbConnectorHolder.getInstance().executeQuery("select * from chat WHERE id > '" + lastReadMsgIndex + "'"); // Messages by last Period
            }

            // Формируем JSON-объект для ответа
            try {
                int counter = 0;
                while (resultSet.next()){
                    JSONObject messageDescr = new JSONObject();
                    messageDescr.put("id", resultSet.getString("id"));
                    messageDescr.put("sender", resultSet.getString("sender"));
                    messageDescr.put("text", resultSet.getString("text"));
                    messageDescr.put("time", (new SimpleDateFormat("HH:mm:ss").format((Timestamp) resultSet.getObject("time"))));
                    messages.put(counter++, messageDescr);
                }
                jsonAnswer.put("messages", messages);
            } catch (JSONException e) {
                System.out.println("Ошибка использования JSON: " + e.getMessage());
            }

        } catch (SQLException e) {
            SqlErrorPrinter.print(e.getMessage());
        }
        return jsonAnswer.toString();
    }
}