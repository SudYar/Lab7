package libriary.internet;

import java.io.Serializable;
import java.net.Socket;
import java.util.Objects;

public class UserConnection implements Serializable {
    private static final long serialVersionUID = 101L;

    private Integer id;
    private User user;
    private Socket socket;

    public UserConnection(User user, Socket socket) {
        this.user = user;
        this.socket = socket;
    }

    public UserConnection(Socket socket) {
        this.socket = socket;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public String toString() {
        return (id == null ? "" : "id подключения: " + id.toString()) +
                "\n\tПользователь: " + (user==null ? "Клиент ещё не авторизовался" : user) +
                "\n\tЕго подключение: " + socket.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserConnection that = (UserConnection) o;
        return Objects.equals(socket, that.socket);
    }

}
