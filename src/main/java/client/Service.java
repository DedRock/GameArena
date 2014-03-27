package client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 26.03.14
 * Time: 19:30
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("Service")
public interface Service extends RemoteService {

    void test();
    /**
     * Utility/Convenience class.
     * Use Service.App.getInstance() to access static instance of ServiceAsync
     */
    public static class App {
        private static final ServiceAsync ourInstance = (ServiceAsync) GWT.create(Service.class);

        public static ServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
