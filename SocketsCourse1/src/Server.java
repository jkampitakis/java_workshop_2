import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        System.out.println("Server started.");

        // A server socket waits for requests to come in over the network. Our client we made is trying to connect to port 1234. Thus, we want our server socket to be waiting for a connection on port 1234.
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            // The first while loop is to ensure the server is constantly running.
            while (true) {
                try (
                        // The accept() method of the ServerSocket class waits for a client connection (the program won’t advance until a client has connected).
                        // Once connected, a Socket object is returned that can be used to communicate with the client.
                        Socket socket = serverSocket.accept();
                        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
                ) {
                    System.out.println("Client connected.");

                    // The second while loop is to ensure that, once the client is connected, the server is constantly interacting with the client until the client disconnects.
                    while (true) {
                        String msgFromClient = bufferedReader.readLine();
                        if (msgFromClient == null) break; // Break if client disconnects

                        System.out.println("Client says: " + msgFromClient);

                        bufferedWriter.write("Msg received.");
                        bufferedWriter.newLine(); // So when the client calls readLine(), it can detect the end of the line
                        bufferedWriter.flush();

                        if (msgFromClient.equalsIgnoreCase("bye")) {
                            break; // Break on "bye" message
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Log any IO exceptions during communication
                } finally {
                    // This block is redundant due to try-with-resources, but you can add additional cleanup if needed
                    System.out.println("Client disconnected.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions related to ServerSocket
        }
    }
}