package utils;

import net.Protocol;
import data.Student;
import data.StudentDAO;

import java.io.*;
import java.net.Socket;

public class ServerUtil {

    private static final StudentDAO studentDAO = new StudentDAO();

    public static void handleClient(Socket socket) {
        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            String clientMessage;

            while ((clientMessage = bufferedReader.readLine()) != null) {
                System.out.println("[Client] " + clientMessage);

                String command = Protocol.getCommand(clientMessage);
                String data = Protocol.getCommandData(clientMessage);

                switch (command) {
                    case Protocol.SEARCH_COMMAND:
                        handleSearch(bufferedWriter, data);
                        break;

                    case Protocol.INSERT_COMMAND:
                        handleInsert(bufferedWriter, data);
                        break;

                    case Protocol.BYE_COMMAND:
                        bufferedWriter.write("Goodbye! Connection terminated.");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        return;

                    default:
                        bufferedWriter.write("Error: Unknown command");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
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

    private static void handleSearch(BufferedWriter writer, String lastName) throws IOException {
        if (lastName == null || lastName.isEmpty()) {
            writer.write("Error: Invalid search command");
        } else {
            Student student = studentDAO.searchStudent(lastName);
            if (student != null) {
                writer.write(String.format("Student found: %s,%s,%s,%d,%d",
                        student.getFirstName(), student.getLastName(), student.getSchool(),
                        student.getSemester(), student.getPassedCourses()));
            } else {
                writer.write("Error: Student not found");
            }
        }
        writer.newLine();
        writer.flush();
    }

    private static void handleInsert(BufferedWriter writer, String data) throws IOException {
        if (data == null || data.isEmpty()) {
            writer.write("Error: Missing student information");
        } else {
            try {
                String[] studentDetails = data.split(",");
                String firstName = studentDetails[0];
                String lastName = studentDetails[1];
                String school = studentDetails[2];
                int semester = Integer.parseInt(studentDetails[3]);
                int passedCourses = Integer.parseInt(studentDetails[4]);

                studentDAO.addStudent(firstName, lastName, school, semester, passedCourses);
                writer.write("Success: Student added");
            } catch (Exception e) {
                writer.write("Error: Invalid insert data");
            }
        }
        writer.newLine();
        writer.flush();
    }
}