package client.person;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Poker
 * Date: 19.02.14
 * Time: 22:29
 * To change this template use File | Settings | File Templates.
 */
public class Account implements Serializable{

    private String nickName;
    private String password;

    public Account() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
