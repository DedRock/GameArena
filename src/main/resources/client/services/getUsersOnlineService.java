package client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 24.03.14
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("getUsersOnlineService")
public interface getUsersOnlineService extends RemoteService {

    public List<String> getUsersOnline(String myNickName);

    /**
     * Utility/Convenience class.
     * Use getUsersOnlineService.App.getInstance() to access static instance of getUsersOnlineServiceAsync
     */
    public static class App {
        private static final getUsersOnlineServiceAsync ourInstance = (getUsersOnlineServiceAsync) GWT.create(getUsersOnlineService.class);

        public static getUsersOnlineServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
