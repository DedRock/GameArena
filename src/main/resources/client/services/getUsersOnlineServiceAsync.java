package client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 24.03.14
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public interface getUsersOnlineServiceAsync {
    void getUsersOnline(String myNickName, AsyncCallback<List<String>> async);
}
