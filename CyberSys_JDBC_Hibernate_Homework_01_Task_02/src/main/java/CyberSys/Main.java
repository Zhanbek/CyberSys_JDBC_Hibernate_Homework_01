package CyberSys;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Main {
    List<String> statements = new ArrayList<>();

    private static final String URL = "jdbc:mysql://localhost:3306/carsshop?useSSL=false";
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

    public static void main(String[] args) throws FileNotFoundException {

        String fileContent = null;
        try {
            fileContent = Files.readString(Paths.get("src/main/resources/script.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> statements = List.of(fileContent.split(";"));

        registerDriver();

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established");
            statement = connection.createStatement();

            for (String sql : statements) {
                sql = sql.trim();
                if (!sql.isEmpty()) {
                    statement.execute(sql + ";");
                    System.out.println(sql + ";");
                    System.out.println();
                }
            }



        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}