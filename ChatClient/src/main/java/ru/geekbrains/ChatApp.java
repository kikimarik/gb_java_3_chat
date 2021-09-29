package ru.geekbrains;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 26.02.2021
 * v1.0
 */
public class ChatApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Scene scene;
    private static Stage ourStage;

    /**
     * Метод, запускающий JavaFX приложение, мы загружаем "сцену" из fxml файла, задаем размеры, назанчаем название
     * вызываем показ сцены.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        ourStage = primaryStage;
        scene = new Scene(loadFxml("/chat.fxml"), 600, 800);
        primaryStage.setTitle("My chat");
//        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Метод загружает fxml файл и парсит его
     * @param fxml
     * @return
     * @throws IOException
     */
    private static Parent loadFxml(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatApp.class.getResource(fxml));
        return fxmlLoader.load();
    }

    public static Stage getStage() {
        return ourStage;
    }

}
