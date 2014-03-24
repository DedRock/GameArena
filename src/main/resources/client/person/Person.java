package client.person;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Poker
 * Date: 19.02.14
 * Time: 22:35
 * To change this template use File | Settings | File Templates.
 */
public class Person extends Account implements Serializable {
    private String name;
    private String lastName;

    public Person() {
    }

    public Person(String nickName, String name, String lastName, String password) {
        this.setNickName(nickName);
        this.setName(name);
        this.setLastName(lastName);
        this.setPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
