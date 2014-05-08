package client.gui.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 08.05.14
 * Time: 8:48
 * To change this template use File | Settings | File Templates.
 */
public class LoginEvent extends GwtEvent<IGuiEvents> {

    public static Type<IGuiEvents> TYPE = new Type<IGuiEvents>();


    @Override
    public Type<IGuiEvents> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(IGuiEvents handler) {
        handler.logIn(this);
    }
}
