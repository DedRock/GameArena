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
     * Utility/Convenience class.
     * Use ChatService.App.getInstance() to access static instance of ChatServiceAsync
     */
    public static class App {
        private static final ChatServiceAsync ourInstance = (ChatServiceAsync) GWT.create(ChatService.class);

        public static ChatServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
