package client.gui;

import com.google.gwt.user.client.ui.TextBox;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 08.05.14
 * Time: 16:06
 * To change this template use File | Settings | File Templates.
 */
public class GuiUtils {

    public static boolean checkTextBoxEmpty(TextBox tb){
        // Если  TextBox пуст
        if (tb.getText().isEmpty()){
            tb.addStyleName("errorHighLight");
            return false;
        }
        return true;
    }
}
