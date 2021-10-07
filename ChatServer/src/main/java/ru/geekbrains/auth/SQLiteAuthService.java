package ru.geekbrains.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 09.03.2021
 * v1.0
 * <p>
 * Простой сервис авторизации через SQLite с хардкодом процесса регистрации юзеров.
 * login - Primary key
 * Чтоб не делать новую кнопку, так как не требуется по заданию.
 */
public class SQLiteAuthService implements AuthService {

    public SQLiteAuthService() throws NoSuchAlgorithmException {
        // Дефолтная миграция
        Connection connection = this.createConnection();
        String sql = "CREATE TABLE IF NOT EXISTS user (\n"
                + "	name VARCHAR(20) NOT NULL,\n"
                + "	login VARCHAR(20) NOT NULL PRIMARY KEY,\n"
                + "	password_hash VARCHAR(32) NOT NULL\n"
                + ");";
        try {
            connection.prepareStatement(sql).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Client> clients = new ArrayList<>(Arrays.asList(
                new Client("user1", "log1", "pass1"),
                new Client("user2", "log2", "pass2"),
                new Client("user3", "log3", "pass3")
        ));
        for (Client client : clients) {
            this.registerUser(client);
        }
    }

    private Connection createConnection() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:/home/kikimarik/sqlite/chat.db");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void removeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void startTransaction(Connection connection) {
        try {
            connection.beginRequest();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void commitTransaction(Connection connection) {
        try {
            connection.beginRequest();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void rollbackTransaction(Connection connection) {
        try {
            connection.beginRequest();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void start() {
        System.out.println("Auth started");
    }

    @Override
    public void registerUser(Client client) throws NoSuchAlgorithmException {
//        Connection connection = this.createConnection();
//        this.startTransaction(connection);
//        try {
//            PreparedStatement statement = connection.prepareStatement(
//                    "INSERT INTO `user` (`name`, `login`, `password_hash`) VALUES (?, ?, ?)"
//            );
//            statement.setString(1, client.getUsername());
//            statement.setString(2, client.getLogin());
//            statement.setString(3, this.createPasswordHash(client.getPassword()));
//            statement.executeUpdate();
//            this.commitTransaction(connection);
//        } catch (SQLException e) {
//            this.rollbackTransaction(connection);
//            throw new RuntimeException(e.getMessage(), e);
//        } finally {
//            this.removeConnection(connection);
//        }
    }

    @Override
    public Client authUser(String login, String password) throws NoSuchAlgorithmException {
        Connection connection = this.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `user` WHERE `login` = ? AND `password_hash` = ?"
            );
            statement.setString(1, login);
            statement.setString(2, this.createPasswordHash(password));
            if (!statement.execute()) {
                throw new AuthFailedError();
            }
            ResultSet result = statement.getResultSet();
            Client client = new Client(result.getString("login"), result.getString("username"));
            client.setAuthorized(true);
            return client;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            this.removeConnection(connection);
        }
    }

    public void changeUsername(String username, Client client) throws NoSuchAlgorithmException {
        if (!client.isAuthorized()) {
            client = this.authUser(client.getLogin(), client.getPassword());
        }
        Connection connection = this.createConnection();
        this.startTransaction(connection);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE `user` SET `name` = ? WHERE `login` = ?"
            );
            statement.setString(1, client.getUsername());
            statement.setString(2, client.getLogin());
            if (!statement.execute()) {
                throw new SQLException("Client not found.");
            }
            this.commitTransaction(connection);
        } catch (SQLException e) {
            this.rollbackTransaction(connection);
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            this.removeConnection(connection);
        }
    }

    /**
     * Программа упадет при отсутствии алгоритма хэширования - так как это критическая ошибка.
     * @param password пароль
     * @return Хэш
     * @throws NoSuchAlgorithmException в случае некорректного алгоритма
     */
    private String createPasswordHash(String password) throws NoSuchAlgorithmException {
        byte[] md5 = MessageDigest.getInstance("MD5").digest(password.getBytes());
        StringBuilder hash = new StringBuilder();
        for (byte b : md5) {
            hash.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }
        return hash.toString();
    }
}
