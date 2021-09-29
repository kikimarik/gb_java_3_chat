package network;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 08.03.2021
 * v1.0
 * Абстракция для сервиса работы с сообщениями
 */
public interface MessageService {
    void sendMessage(String msg);

    void receiveMessage(String msg);
}
