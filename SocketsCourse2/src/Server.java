import utils.ServerUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static boolean isRunning = true;

    public static void main(String[] args) {
        System.out.println("Server Advanced started.");

        // Using a thread pool for handling multiple clients
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (
                ServerSocket serverSocket = new ServerSocket(1235);
        ) {
            while (isRunning) {
                try {
                    // Accept a client connection
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected.");

                    // Handle the client in a separate thread
                    executorService.submit(() -> ServerUtil.handleClient(clientSocket));
                } catch (IOException e) {
                    if (isRunning) {
                        System.err.println("Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            executorService.shutdown();
            System.out.println("Server shut down.");
        }
    }


    public static void stopServer() {
        isRunning = false;
    }
}