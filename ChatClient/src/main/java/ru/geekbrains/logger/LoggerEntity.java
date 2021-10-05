package ru.geekbrains.logger;

import ru.geekbrains.messages.MessageDTO;

import java.io.Serializable;
import java.util.ArrayDeque;

public class LoggerEntity implements Serializable {
    public ArrayDeque<MessageDTO> messages;
    public LoggerEntity(ArrayDeque<MessageDTO> messages) {
        this.messages = messages;
    }
}
