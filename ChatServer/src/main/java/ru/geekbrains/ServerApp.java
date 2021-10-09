package ru.geekbrains;

import ru.geekbrains.server.ChatServer;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 05.03.2021
 * v1.0
 * <p>
 * Этот класс просто точка входа для запуска сервера
 */
public class ServerApp {

    // ЛС
    //Клиенты онлайн
    //Приветствие по имени
    //Проперти


    // j3 - Регистрация пользователей
    // j3 - Логирование
    // j3 - Тесты
    // j3 - сохранение данных в файлы
    // j3 - Улучшенное управление потоками


    //блокировка
    //Удалить переписку
    //**Смайлики в чат
    //Стили
    //Вкладки для личных чатов
    //Черный список
    //Консольно управление сервером (kick, mute ...)
    //Цензура?


    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.submit(ChatServer::new);
        pool.shutdown();
    }
}
