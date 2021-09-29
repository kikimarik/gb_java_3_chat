package ru.geekbrains.to_delete;

import java.io.*;
import java.net.Socket;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 09.03.2021
 * v1.0
 * Это просто из домашки, будет выпилен.
 */
public class SingleClientConsole {
    private static final int SERVER_PORT = 65500;
    private static final String SERVER_HOST = "localhost";
    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;
    private Thread clientConsoleThread;


    public static void main(String[] args) {
        new SingleClientConsole().runClient();
    }


    private void runClient() {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            System.out.println("Client is running");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            startClientConsoleThread();

            while (true) {
                String str = in.readUTF();
                if (str.equals("/end")) {
                    shutdown();
                    break;
                }
                System.out.println("Retrieved message: " + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("From finally");
        }
    }

    private void shutdown() throws IOException {
        clientConsoleThread.interrupt();
        socket.close();
        System.out.println("Client is stopped!");
    }


    private void startClientConsoleThread() {
        clientConsoleThread = new Thread(() -> {
            BufferedReader clientInputStream = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("You can enter message to send to server:");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (clientInputStream.ready()) {
                        String messageFromClient = clientInputStream.readLine();
                        out.writeUTF(messageFromClient);
                        if (messageFromClient.equals("/end")) {
                            shutdown();
                            break;
                        }
                    }
                    Thread.sleep(500);
                } catch (InterruptedException | IOException e) {
                    break;
                }
            }
            System.out.println("Close System.in thread");
        });
//        t1.start();
//        t1.join();
//        t2.start();
//        t2.join();
//        clientConsoleThread.join(1000);
//        serverConsoleThread.setDaemon(true);
        clientConsoleThread.start();
    }
}
