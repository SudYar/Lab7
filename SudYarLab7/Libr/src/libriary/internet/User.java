package libriary.internet;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 102L;
    String login;
    String password;
    Integer id;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }


    public String getLogin(){
        return login;
    }

    public Integer getId() {
        return id;
    }

    public String  getPassword() {
        return password;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "login = " + login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) && Objects.equals(password, user.password);
    }

}
