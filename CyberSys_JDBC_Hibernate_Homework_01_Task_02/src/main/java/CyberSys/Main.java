package CyberSys;

import java.sql.DriverManager;

public class Main {
    private static final String URL = "jdbc:mysql://3306/carsshop?useSSL=false";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        try {
          //DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}