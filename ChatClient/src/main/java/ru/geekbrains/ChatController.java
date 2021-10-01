package ru.geekbrains;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.ChatMessageService;
import network.MessageProcessor;
import network.MessageService;
import ru.geekbrains.logger.MessageLogger;
import ru.geekbrains.messages.MessageDTO;
import ru.geekbrains.messages.MessageType;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ResourceBundle;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 26.02.2021
 * v1.0
 * Контроллер, управляющий логикой визульного отображения
 * реализует Initializable из пакета javaFX, это позволяет при создании объекта выполнить метод initialize, что в нашем случае удобно для инициализации некоторых полей
 * MessageProcessor - интерфейс, что мы создали в пакете Network, чтобы MessageService знал, кому отдавать полученные от сервака сообщения.
 */
public class ChatController implements Initializable, MessageProcessor {

    /**
     * тут поля нашего контроллера, @FXML помечены поля являющие собой элементы интерфейса, с которыми нам надо как-то
     * программно повоздействовать.
     */
    private final String ALL = "SEND TO ALL";

    @FXML
    public TextArea chatArea;
    @FXML
    public ListView onlineUsers;
    @FXML
    public Button btnSendMessage;
    @FXML
    public TextField input;
    @FXML
    public HBox chatBox;
    @FXML
    public HBox inputBox;
    @FXML
    public MenuBar menuBar;
    @FXML
    public HBox authPanel;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passField;

    private MessageService messageService;

    /**
     * Собственно метод, который благодаря Initializable вызывается при создании контроллера.
     * В нашем случае сразу коннектится к серверу и делает залипушный список контактов
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageService = new ChatMessageService("localhost", 65500, this);
        MessageLogger logger = new MessageLogger();
//        for (MessageDTO message : logger.read(10).messages) {
//            this.showMessage(message, true);
//        }
        ArrayDeque<MessageDTO> deque = logger.read(10).messages;
        while (!deque.isEmpty()) {
            this.showMessage(deque.pop(), true);
        }
    }

    /**
     * Просто от нечего делать
     * Метод по соответствующему событию ведет нас на URL в браузере
     *
     * @param actionEvent
     * @throws URISyntaxException
     * @throws IOException
     */
    public void showHelp(ActionEvent actionEvent) throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(new URI("https://docs.google.com/document/d/1wr0YEtIc5yZtKFu-KITqYnBtp8KC28v2FEYUANL0YAM/edit#"));
    }

    /**
     * Закрытие приложения по кнопке
     *
     * @param actionEvent
     */
    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }


    /**
     * Просто залипуха, можно будет что-то навесить
     *
     * @param actionEvent
     */
    public void mockAction(ActionEvent actionEvent) {
        try {
            throw new RuntimeException("AAAAAAAAAAAAAAAAAAAA!!!!!!!");
        } catch (RuntimeException e) {
            showError(e);
        }
    }

    private void refreshUserList(MessageDTO dto) {
        dto.getUsersOnline().add(0, ALL);
        onlineUsers.setItems(FXCollections.observableArrayList(dto.getUsersOnline()));
        onlineUsers.getSelectionModel().selectFirst();
    }

    /**
     * событие на нажатие enter когда активно поле ввода
     *
     * @param actionEvent
     */
    public void pressEnter(ActionEvent actionEvent) {
        sendMessage();
    }

    /**
     * Событие нажатия на кнопку отправки сообщения
     *
     * @param actionEvent
     */
    public void btnSend(ActionEvent actionEvent) {
        sendMessage();
    }

    /**
     * Метод, который шлет сообщение серваку
     * Как видно, пока что только публичное сообщение
     */
    private void sendMessage() {
        String msg = input.getText();
        if (msg.length() == 0) return;

        MessageDTO dto = new MessageDTO();
        String selected = (String) onlineUsers.getSelectionModel().getSelectedItem();
        if (selected.equals(ALL)) dto.setMessageType(MessageType.PUBLIC_MESSAGE);
        else {
            dto.setMessageType(MessageType.PRIVATE_MESSAGE);
            dto.setTo(selected);
        }

        dto.setBody(msg);
        messageService.sendMessage(dto.convertToJson());
        input.clear();

    }

    /**
     * Выводит сообщение в окно чата
     *
     * @param message
     */
    private void showMessage(MessageDTO message) {
        MessageLogger logger = new MessageLogger();
        logger.write(message);
        this.showMessage(message, true);
    }

    private void showMessage(MessageDTO message, boolean noLog) {
        if (!noLog) {
            this.showMessage(message);
        }
        String msg = String.format("[%s] [%s] -> %s\n", message.getMessageType(), message.getFrom(), message.getBody());
        chatArea.appendText(msg);
    }

    /**
     * обработчик входящих сообщений, где мы пропишем действия на каждый вид сообщений
     *
     * @param msg
     */
    @Override
    public void processMessage(String msg) {
        Platform.runLater(() -> {
            MessageDTO dto = MessageDTO.convertFromJson(msg);
            System.out.println("Received message");
            switch (dto.getMessageType()) {
                case PUBLIC_MESSAGE, PRIVATE_MESSAGE -> showMessage(dto);
                case CLIENTS_LIST_MESSAGE -> refreshUserList(dto);
                /**
                 * Тут при получении подтверждения авторизации скрываются панели авторизации и показывается основной интерфейс
                 */
                case AUTH_CONFIRM -> {
                    ChatApp.getStage().setTitle(dto.getBody());
                    authPanel.setVisible(false);
                    chatBox.setVisible(true);
                    inputBox.setVisible(true);
                    menuBar.setVisible(true);
                }

                case ERROR_MESSAGE -> showError(dto);

            }
        });
    }

    /**
     * метод отправляющий сообщение с данными для авторизации
     *
     * @param actionEvent
     */
    public void sendAuth(ActionEvent actionEvent) {
        String log = loginField.getText();
        String pass = passField.getText();
        if (log.equals("") || pass.equals("")) return;
        MessageDTO dto = new MessageDTO();
        dto.setLogin(log);
        dto.setPassword(pass);
        dto.setMessageType(MessageType.SEND_AUTH_MESSAGE);
        messageService.sendMessage(dto.convertToJson());
        System.out.println("Sent " + log + " " + pass);
    }

    /**
     * метод, который показывает ошибки в графическом интерфейсе
     *
     * @param e
     */
    private void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong!");
        alert.setHeaderText(e.getMessage());
        VBox dialog = new VBox();
        Label label = new Label("Trace:");
        TextArea textArea = new TextArea();
        //TODO
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement el : e.getStackTrace()) {
            builder.append(el).append(System.lineSeparator());
        }
        textArea.setText(builder.toString());
        dialog.getChildren().addAll(label, textArea);
        alert.getDialogPane().setContent(dialog);
        alert.showAndWait();
    }

    /**
     * перегрузка предыдущего, будет показывать ошибки, прилетающие с сервера
     *
     * @param dto
     */
    private void showError(MessageDTO dto) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong!");
        alert.setHeaderText(dto.getMessageType().toString());
        VBox dialog = new VBox();
        Label label = new Label("Trace:");
        TextArea textArea = new TextArea();
        textArea.setText(dto.getBody());
        dialog.getChildren().addAll(label, textArea);
        alert.getDialogPane().setContent(dialog);
        alert.showAndWait();
    }
}
