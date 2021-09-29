package ru.geekbrains.to_delete;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 09.03.2021
 * v1.0
 *
 * Класс для домашки, выпилим
 */
public class SingleClientConsoleServer {
    private static final int SERVER_PORT = 65500;

    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;
    private Thread serverConsoleThread;


    public static void main(String[] args) {
        new SingleClientConsoleServer().runServer();
    }


    private void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(SingleClientConsoleServer.SERVER_PORT)) {
            System.out.println("Server is running");
            waitAndInitConnection(serverSocket);
            startServerConsoleThread();

            while (true) {
                String str = in.readUTF();
                if (str.equals("/end")) {
                    shutdownServer(serverSocket);
                    break;
                }
                System.out.println("Retrieved message: " + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Server FINALIZED!!!!");
        }
    }

    private void shutdownServer(ServerSocket serverSocket) throws IOException {
        serverConsoleThread.interrupt();
        socket.close();
        serverSocket.close();
        System.out.println("Server is stopped!");
    }

    private void waitAndInitConnection(ServerSocket serverSocket) throws IOException {
        System.out.println("Awaiting client connection...");
        socket = serverSocket.accept();
        System.out.println("The client has connected");
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    private void startServerConsoleThread() {
        serverConsoleThread = new Thread(() -> {
            BufferedReader clientInputStream = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("You can enter message to send to client:");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (clientInputStream.ready()) {
                        String messageFromServer = clientInputStream.readLine();
                        out.writeUTF(messageFromServer);
                    }
                    Thread.sleep(500);
                } catch (InterruptedException | IOException e) {
                    break;
                }
            }
            System.out.println("Close System.in thread");
        });
//        serverConsoleThread.setDaemon(true);
        serverConsoleThread.start();
    }
}
