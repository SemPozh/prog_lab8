package laba7.common.data;

public class User {
    private Integer id;
    private String username;
    private String password;

    public User(Integer id, String username, String password){
        this.id = id;
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
}
