package client.services.authorization;

import client.ErrorResult;
import client.person.Person;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 24.03.14
 * Time: 11:53
 * To change this template use File | Settings | File Templates.
 */
public interface AuthorizationServiceAsync {

    void getUsersOnline(String myNickName, AsyncCallback<List<String>> async);

    void checkLogin(String user, String pass, AsyncCallback<ErrorResult> async);

    void setNewAccount(Person perc, AsyncCallback<ErrorResult> async);
}
