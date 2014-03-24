package client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 21.03.14
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class Gamearena implements EntryPoint {

    //===== Users online form =========================================================================================
    private List<Label> usersOnlineList = new ArrayList<Label>();
    private Label title = new Label();
    private HorizontalPanel usersOnlinePanel = new HorizontalPanel();

    //============================================================================================================
    // Main Enrty-Point function
    //============================================================================================================
    public void onModuleLoad() {
        RootPanel.get("usersOnline").add( new HTML("<b>Hello GWT World!</b>") );
    }
}
