package ru.geekbrains.to_delete;

import javafx.application.Platform;

import javax.xml.crypto.dsig.dom.DOMSignContext;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 05.03.2021
 * v1.0
 * Тоже будет ВЫПИЛЕН
 */
public class NetworkHelper {
//    private final Socket socket;
//    private final DataInputStream inputStream;
//    private final DataOutputStream outputStream;
//
//    public NetworkHelper(String address, int port, MessageServiceOld messageService) throws IOException {
//        this.socket = new Socket(address, port);
//        this.inputStream = new DataInputStream(socket.getInputStream());
//        this.outputStream = new DataOutputStream(socket.getOutputStream());
//
//        new Thread(() -> {
//           while (true) {
//               try {
//                   String msg = inputStream.readUTF();
//                   Platform.runLater(() -> messageService.receiveMessage(msg));
//               } catch (IOException e) {
//                   e.printStackTrace();
//               }
//           }
//        }).start();
//    }
//
//    public void writeMessage(String msg) {
//        try {
//            outputStream.writeUTF(msg);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
