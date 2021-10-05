package ru.geekbrains.auth;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 09.03.2021
 * v1.0
 *
 * Сущность клиента для идентификации и авторизации
 */
public class Client {
    private String username;
    private String login;
    private String password;
    private Boolean isAuthorized = false;

    public Client(String username, String login, String password) {
        this.username = username;
        this.login = login;
        this.password = password;
    }

    public Client(String username, String login) {
        this.username = username;
        this.login = login;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(Boolean authorized) {
        isAuthorized = authorized;
    }
}
