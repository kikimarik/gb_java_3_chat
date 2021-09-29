package network;

import java.io.IOException;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 08.03.2021
 * v1.0
 *
 * Реализация сервиса работы с сообщениями по сети
 * Обрабатывает получение и отправку сообщений
 */
public class ChatMessageService implements MessageService {
    private String host;
    private int port;
    private NetworkService networkService;
    private MessageProcessor processor;

    public ChatMessageService(String host, int port, MessageProcessor processor) {
        this.host = host;
        this.port = port;
        this.processor = processor;
        connectToServer();
    }

    private void connectToServer() {
        try {
            this.networkService = new NetworkService(host, port, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String msg) {
        networkService.writeMessage(msg);
    }

    /**
     * тут как раз используется процессор, чтобы наш сервис знал, кому же отдать полученное сообщение
     * @param msg
     */
    @Override
    public void receiveMessage(String msg) {
        processor.processMessage(msg);
    }
}
