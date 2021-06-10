package libriary.internet;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 102L;
    String login;
    byte[] password;
    Integer id;

    public User(String login, byte[] password) {
        this.login = login;
        this.password = password;
    }


    public String getLogin(){
        return login;
    }

    public Integer getId() {
        return id;
    }

    public byte[] getPassword() {
        return password;
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
