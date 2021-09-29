package ru.geekbrains.to_delete;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 05.03.2021
 * v1.0
 * Эхо сервер из примеров. Выпилим
 */
//InputStream  Reader
//OutputStream  Writer
public class EchoServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(65500)) {
            System.out.println("Server started!");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            while (true) {

                String message = in.readUTF();
                System.out.println("Received " + message);
                out.writeUTF("Echo: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
