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
    private Boolean error;
    private String ErrorDescription;

    public ErrorResult() {
    }

    public String getErrorDescription() {
        return ErrorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        ErrorDescription = errorDescription;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
