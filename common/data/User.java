package laba6.common.data;

import java.io.Serializable;

public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;

    public User(String username, String password){
        setUsername(username);
        setPassword(password);
    }

    public User(Integer id, String username, String password){
        setId(id);
        setUsername(username);
        setPassword(password);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public boolean checkUser(String username, String password){
        return this.password.equals(password) & this.username.equals(username);
    }

    public String getUsername() {
        return username;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
