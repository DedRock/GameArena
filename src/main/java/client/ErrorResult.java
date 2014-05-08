package client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 21.02.14
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class ErrorResult implements IsSerializable {
    private boolean error;
    private String errorDescription;

    public ErrorResult() {
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        errorDescription = errorDescription;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void set(boolean error, String errorDescription){
        this.error = error;
        this.errorDescription =  errorDescription;

    }

}
