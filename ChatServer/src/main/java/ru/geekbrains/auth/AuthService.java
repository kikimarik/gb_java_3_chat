package ru.geekbrains.auth;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 09.03.2021
 * v1.0
 *
 * Абстракция сервиса авторизаций
 */
public interface AuthService {
    void start();
    void stop();
    String getUsernameByLoginPass(String login, String pass);
}
