package ru.geekbrains.auth;

import org.sqlite.JDBC;

import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;

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
    void registerUser(Client client) throws NoSuchAlgorithmException;
    Client authUser(String login, String password) throws NoSuchAlgorithmException;
    public void changeUsername(String username, Client client) throws NoSuchAlgorithmException;
}
