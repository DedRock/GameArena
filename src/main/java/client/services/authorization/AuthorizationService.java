package client.services.authorization;

import client.ErrorResult;
import client.person.Person;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 24.03.14
 * Time: 11:53
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("AuthorizationService")
public interface AuthorizationService extends RemoteService {

    ErrorResult checkLogin(String user, String pass);
    ErrorResult setNewAccount(Person person);
    List<String> getUsersOnline(String myNickName);

    /**
     * Utility/Convenience class.
     * Use AuthorizationService.App.getInstance() to access static instance of AuthoriяationServiceAsync
     */
    public static class App {
        private static final AuthorizationServiceAsync ourInstance = (AuthorizationServiceAsync) GWT.create(AuthorizationService.class);

        public static AuthorizationServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
