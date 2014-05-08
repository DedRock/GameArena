package client.services.chat;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 27.03.14
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */
public interface ChatServiceAsync {
    /**
     * Function to send new message to players' chat
     *
     * @param myAccount
     * @param message
     */
    void sendNewMesage(String myAccount, String message, AsyncCallback<Void> async);


    /**
     * Function to receive new messages from server
     *
     * @return String = JSON-object with array of messages
     */
    void getNewMassages(long lastMsgIndex, AsyncCallback<String> async);
}
