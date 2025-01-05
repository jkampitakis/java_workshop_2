import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import net.Protocol;

public class Client {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 1235); // Create a socket connection to localhost on port 1234
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Wrap InputStreamReader with BufferedReader for efficiency
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Wrap OutputStreamWriter with BufferedWriter for efficiency
                Scanner scanner = new Scanner(System.in) // System.in -> an InputStream connected to the keyboard to get input from console
        ) {
            while (true) {
                System.out.println("Enter an Action ('search', 'insert', 'bye'):");
                String action = scanner.nextLine();

                String request = null;

                if(action.equalsIgnoreCase("search")){
                    System.out.println("Enter last name of the student:");
                    String lastName = scanner.nextLine();
                    request = Protocol.createSearchRequest(lastName);
                }else if (action.equalsIgnoreCase("insert")){
                    System.out.println("Enter student data in the format firstName,lastName,school,semester,passedCourses:");
                    String studentData = scanner.nextLine();
                    String[] details = studentData.split(",");
                    if(details.length != 5){
                        System.out.println("Invalid student data format!");
                        continue;
                    }
                    request = Protocol.createInsertRequest(details[0],details[1],details[2],Integer.parseInt(details[3]),Integer.parseInt(details[4]));
                }else if (action.equalsIgnoreCase("bye")){
                    request = Protocol.createByeRequest();
                }else {
                    System.out.println("Invalid action!");
                    continue;
                }

                //send the request to the server
                bufferedWriter.write(request);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                //Read and handle the server response
                String response = bufferedReader.readLine();
                System.out.println("[Server] " + response);

                if(action.equalsIgnoreCase("bye")){
                    break;
                }
            }

            System.out.println("Client disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}