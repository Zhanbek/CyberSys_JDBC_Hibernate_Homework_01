package CyberSys;


import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/MyJoinsDB?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static void registerDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws FileNotFoundException, SQLException {

        registerDriver();

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            statement = connection.createStatement();

            System.out.println();
            System.out.println("=== 1. Контактні дані співробітників ===");
            String query1 = "SELECT e.Name, e.Phone, ed.Address " +
                    "FROM employees e " +
                    "LEFT JOIN employee_details ed ON e.Id = ed.Employee_Id";

            ResultSet rs = statement.executeQuery(query1);

            while (rs.next()) {
                    String name = rs.getString("Name");
                    String phone = rs.getString("Phone");
                    String address = rs.getString("Address");
                    System.out.printf("Name: %s, Phone: %s, Address: %s%n", name, phone, address);
            }

            System.out.println();
            System.out.println("=== 2. Неодружені співробітники (дата народження та телефон) ===");
            String query2 = "SELECT e.Name, e.Phone, ed.BirthDate " +
                    "FROM Employees e " +
                    "JOIN Employee_Details ed ON e.Id = ed.Employee_Id " +
                    "WHERE LOWER(ed.Marital_Status) = 'неодружений'";

            rs = statement.executeQuery(query2);

            while (rs.next()) {
                String name = rs.getString("Name");
                String phone = rs.getString("Phone");
                Date birthDate = rs.getDate("BirthDate");
                System.out.printf("Name: %s, Phone: %s, BirthDate: %s%n", name, phone, birthDate);
            }

            System.out.println();
            System.out.println("=== 3. Менеджери компанії ===");
            String query3 = "SELECT e.Name, e.Phone, ed.BirthDate " +
                    "FROM Employees e " +
                    "JOIN Employee_Details ed ON e.Id = ed.Employee_Id " +
                    "JOIN Employees_positions ep ON e.Id = ep.Employee_id " +
                    "AND SYSDATE() >= ep.begin_date AND (ep.end_date IS NULL OR SYSDATE() < ep.end_date) " +
                    "JOIN Positions p ON ep.Position_id = p.Id " +
                    "WHERE LOWER(p.Position) = 'manager'";

            rs = statement.executeQuery(query3);

            while (rs.next()) {
                String name = rs.getString("Name");
                String phone = rs.getString("Phone");
                Date birthDate = rs.getDate("BirthDate");
                System.out.printf("Name: %s, Phone: %s, BirthDate: %s%n", name, phone, birthDate);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}