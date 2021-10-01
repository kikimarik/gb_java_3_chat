package ru.geekbrains.logger;

import ru.geekbrains.messages.MessageDTO;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

public class MessageLogger {

    /**
     * Добавляет в лог ново сообщение
     *
     * @param message сообщение
     */
    public void write(MessageDTO message) {
        try {
            LoggerEntity entity = this.read(10);
            ArrayDeque<MessageDTO> messages = entity.messages;
            messages.add(message);
            FileOutputStream fos = new FileOutputStream("/var/log/chat.log");
            ObjectOutputStream outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(entity);
            outputStream.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Выводит из лога последние limit сообщений
     *
     * @param limit число строк
     * @return сущность логгера
     */
    public LoggerEntity read(int limit) {
        LoggerEntity entity = new LoggerEntity(new ArrayDeque<>(limit));
        try {
            FileInputStream fis = new FileInputStream("/var/log/chat.log");
            ObjectInputStream inputStream = new ObjectInputStream(fis);
            entity = (LoggerEntity) inputStream.readObject();
            if (entity.messages.size() > limit) {
                for (int i = 0; i <= entity.messages.size() - limit; i++) {
                    entity.messages.pollFirst();
                }
            }
            inputStream.close();
            fis.close();
            return entity;
        } catch (EOFException e) {
            return entity;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
