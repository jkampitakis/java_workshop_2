package data;

import net.DatabaseConnection;
import java.sql.*;

public class StudentDAO {

    public void addStudent(String firstName, String lastName, String school, int semester, int passedCourses) {
        String sql = "INSERT INTO students (first_name, last_name, school, semester, passed_courses) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, school);
            pstmt.setInt(4, semester);
            pstmt.setInt(5, passedCourses);
            pstmt.executeUpdate();
            System.out.println("data.Student added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student searchStudent(String lastName) {
        String sql = "SELECT * FROM students WHERE last_name = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lastName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("school"),
                        rs.getInt("semester"),
                        rs.getInt("passed_courses")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no student is found
    }


}
