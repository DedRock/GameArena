package client.gui.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 08.05.14
 * Time: 8:46
 * To change this template use File | Settings | File Templates.
 */
public interface IGuiEvents extends EventHandler {

    void logIn(LoginEvent event);
    void logOut(LogoutEvent event);
}
