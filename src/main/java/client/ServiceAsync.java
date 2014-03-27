package client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 26.03.14
 * Time: 19:30
 * To change this template use File | Settings | File Templates.
 */
public interface ServiceAsync {
    void test(AsyncCallback<Void> async);
}
