package client.services.chat;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 27.03.14
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("ChatService")
public interface ChatService extends RemoteService {

    /**
     * Function to send new message to players' chat
     * @param myAccount
     * @param message
     */
    void sendNewMesage(String myAccount, String message);

    /**
     * Function to receive new messages from server
     * @param lastMsgIndex - ID последнего  считанного сообщения
     * @return String = JSON-object with array of messages
     * Struct of JSON-answer:
     *  {
     *      "messages":
     *      [
     *          {"sender" : :"<></>"}
     *      ]
     *
     */
    String getNewMassages(Long lastMsgIndex);

    /**
     * Function to receive new messages from server
     *
     * @return String = JSON-object with array of messages
     */

    public static class App {
        private static final ChatServiceAsync ourInstance = (ChatServiceAsync) GWT.create(ChatService.class);

        public static ChatServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
