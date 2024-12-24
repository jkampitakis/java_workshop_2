import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerAdvanced {
    private static boolean isRunning = true;

    public static void main(String[] args) {
        System.out.println("Server Advanced started.");

        // Using a thread pool for handling multiple clients
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (
                ServerSocket serverSocket = new ServerSocket(1235)
        ) {
            while (isRunning) {
                try {
                    // Accept a client connection
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected.");

                    // Handle the client in a separate thread
                    executorService.submit(() -> handleClient(clientSocket));
                } catch (IOException e) {
                    if (isRunning) {
                        System.err.println("Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Server socket error: " + e.getMessage());
        } finally {
            executorService.shutdown();
            System.out.println("Server shut down.");
        }
    }

    private static void handleClient(Socket socket) {
        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            String clientMessage;

            while ((clientMessage = bufferedReader.readLine()) != null) {
                System.out.println("Client says: " + clientMessage);

                // Respond to the client
                bufferedWriter.write("Message received: " + clientMessage);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                // Exit loop if client sends "BYE"
                if (clientMessage.equals("bye")) {
                    System.out.println("Client disconnected.");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    public static void stopServer() {
        isRunning = false;
    }
}