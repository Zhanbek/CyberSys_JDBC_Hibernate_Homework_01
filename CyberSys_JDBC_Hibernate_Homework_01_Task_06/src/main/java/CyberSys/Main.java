package CyberSys;

import java.sql.*;

// !!! Перед запуском програми необхідно виконати скрипти з папки WorkBench_Scripts !!!

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/testdb?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection connection = null;

    // Допоміжний метод для виведення даних
    private static void printEmployee(ResultSet rs) throws SQLException {
        System.out.printf("ID: %-3d | %-12s %-12s | %-30s | %-15s | %-10.2f | %s | %s%n",
                rs.getInt("Id"),
                rs.getString("First_Name"),
                rs.getString("Last_Name"),
                rs.getString("Email"),
                rs.getString("Position"),
                rs.getDouble("Salary"),
                rs.getDate("Hire_Date"),
                rs.getBoolean("Is_Active") ? "Активний" : "Неактивний");
    }

    // Очищення таблиці
    private static void clearTable() {
        String clearSQL = "DELETE FROM Employees";
        try (Statement stmt = connection.createStatement()) {
            int deleted = stmt.executeUpdate(clearSQL);
            System.out.println("Очищено " + deleted + " старих записів");

            // Скидання автоінкременту
            stmt.execute("ALTER TABLE Employees AUTO_INCREMENT = 1");
        } catch (SQLException e) {
            System.err.println("Помилка очищення: " + e.getMessage());
        }
    }

    // Вставка тестових даних через MySQL запити в IDEA (українською)
    private static void insertSampleData() {
        System.out.println("\n--- Вставка даних через INSERT запити ---");

        String[] insertQueries = {
                "INSERT INTO Employees (First_Name, Last_Name, Email, Phone_Number, Position, Salary, Hire_Date, Is_Active) VALUES ('Олександр', 'Петренко', 'oleksandr.petrenko@example.com', '+380501234567', 'Розробник', 35000.00, '2020-01-15', TRUE)",
                "INSERT INTO Employees (First_Name, Last_Name, Email, Phone_Number, Position, Salary, Hire_Date, Is_Active) VALUES ('Марія', 'Шевченко', 'maria.shevchenko@example.com', '+380671234568', 'Менеджер', 55000.00, '2019-03-20', TRUE)",
                "INSERT INTO Employees (First_Name, Last_Name, Email, Phone_Number, Position, Salary, Hire_Date, Is_Active) VALUES ('Дмитро', 'Коваленко', 'dmytro.kovalenko@example.com', '+380931234569', 'Аналітик', 28000.00, '2021-06-10', TRUE)",
                "INSERT INTO Employees (First_Name, Last_Name, Email, Phone_Number, Position, Salary, Hire_Date, Is_Active) VALUES ('Олена', 'Бондаренко', 'olena.bondarenko@example.com', '+380501234570', 'Розробник', 38000.00, '2020-11-01', TRUE)",
                "INSERT INTO Employees (First_Name, Last_Name, Email, Phone_Number, Position, Salary, Hire_Date, Is_Active) VALUES ('Іван', 'Лисенко', 'ivan.lysenko@example.com', '+380671234571', 'Тестувальник', 25000.00, '2022-02-28', FALSE)",
                "INSERT INTO Employees (First_Name, Last_Name, Email, Phone_Number, Position, Salary, Hire_Date, Is_Active) VALUES ('Наталія', 'Мельник', 'natalia.melnyk@example.com', '+380931234572', 'Менеджер', 60000.00, '2018-09-15', TRUE)",
                "INSERT INTO Employees (First_Name, Last_Name, Email, Phone_Number, Position, Salary, Hire_Date, Is_Active) VALUES ('Андрій', 'Ткаченко', 'andriy.tkachenko@example.com', '+380501234573', 'Розробник', 42000.00, '2019-12-10', TRUE)"
        };

        try (Statement stmt = connection.createStatement()) {
            for (String query : insertQueries) {
                int affected = stmt.executeUpdate(query);
                if (affected > 0) {
                    System.out.println("Додано запис");
                }
            }
            System.out.println("Всього додано " + insertQueries.length + " записів");
        } catch (SQLException e) {
            System.err.println("Помилка вставки даних: " + e.getMessage());
        }
    }

    // 1. SELECT всіх активних співробітників
    private static void selectAllActiveEmployees() {
        String query = "SELECT Id, First_Name, Last_Name, Email, Phone_Number, Position, Salary, Hire_Date, Is_Active " +
                "FROM Employees WHERE Is_Active = TRUE";

        System.out.println("\n================== ВСІ АКТИВНІ СПІВРОБІТНИКИ ==================");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                printEmployee(rs);
            }
        } catch (SQLException e) {
            System.err.println("Помилка SELECT: " + e.getMessage());
        }
    }

    // 2. SELECT за посадою
    private static void selectEmployeesByPosition(String position) {
        String query = "SELECT First_Name, Last_Name, Position, Salary " +
                "FROM Employees WHERE Position = ? AND Is_Active = TRUE";

        System.out.printf("\n================== 1. ПОСАДА: %s ==================\n", position);

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, position);
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%s %s - %s: %.2f грн%n",
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getString("Position"),
                        rs.getDouble("Salary"));
            }

            if (!found) {
                System.out.println("Немає активних співробітників з посадою: " + position);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Помилка SELECT: " + e.getMessage());
        }
    }

    // 3. INSERT нового співробітника
    private static void insertEmployee(String firstName, String lastName, String email,
                                       String phoneNumber, String position, double salary,
                                       String hireDate, boolean isActive) {

        String query = "INSERT INTO Employees (First_Name, Last_Name, Email, Phone_Number, " +
                "Position, Salary, Hire_Date, Is_Active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println("\n================== 2. ДОДАВАННЯ НОВОГО СПІВРОБІТНИКА ==================");

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, position);
            pstmt.setDouble(6, salary);
            pstmt.setDate(7, Date.valueOf(hireDate));
            pstmt.setBoolean(8, isActive);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    System.out.printf("Додано співробітника: %s %s (ID: %d)%n", firstName, lastName, id);
                }
                generatedKeys.close();
            }
        } catch (SQLException e) {
            System.err.println("Помилка INSERT: " + e.getMessage());
        }
    }

    // 4. UPDATE зарплати
    private static void updateSalaryByPosition(String position, double newSalary) {

        String query = "UPDATE Employees SET Salary = ? WHERE LOWER(Position) = LOWER(?) AND Is_Active = TRUE";

        System.out.println("\n================== 3. ПІДВИЩЕННЯ ЗАРПЛАТИ ==================");

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDouble(1, newSalary);
            pstmt.setString(2, position);

            int updated = pstmt.executeUpdate();

            if (updated > 0) {
                System.out.printf("Оновлено зарплату для %d %sів: %.2f грн%n", updated, position, newSalary);
            } else {
                System.out.printf("Співробітників з позицією %s не знайдено або він неактивний%n", position);
            }
        } catch (SQLException e) {
            System.err.println("Помилка UPDATE: " + e.getMessage());
        }
    }

    // 5. DELETE
    private static void deleteEmployeeByEmail(String email) {
        String query = "DELETE FROM Employees WHERE Email = ?";

        System.out.println("\n================== 4. ВИДАЛЕННЯ СПІВРОБІТНИКА ==================");

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);

            int deleted = pstmt.executeUpdate();

            if (deleted > 0) {
                System.out.printf("Видалено співробітника з email: %s%n", email);
            } else {
                System.out.printf("Співробітника з email %s не знайдено%n", email);
            }
        } catch (SQLException e) {
            System.err.println("Помилка DELETE: " + e.getMessage());
        }
    }

    private static void demonstrateAllQueries() {
        selectAllActiveEmployees();

        selectEmployeesByPosition("Розробник");

        insertEmployee("Сергій", "Гончаренко", "sergiy.goncharenko@example.com",
                "+380971234574", "Старший розробник", 48000.00, "2023-01-10", true);

        selectAllActiveEmployees();

        updateSalaryByPosition("Розробник", 52000.00);
        selectAllActiveEmployees();

        deleteEmployeeByEmail("sergiy.goncharenko@example.com");
        selectAllActiveEmployees();
    }

    public static void main(String[] args) {
        try {
            // Підключення до БД
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Підключення до БД успішне!\n");

            // ========== ЗАПОВНЕННЯ ДАНИМИ ЧЕРЕЗ MYSQL ЗАПИТИ В IDEA ==========
            System.out.println("================== ЗАПОВНЕННЯ ТАБЛИЦІ ДАНИМИ ==================");

            // Очищення таблиці перед вставкою (щоб уникнути дублювання)
            clearTable();

            // Вставка даних через INSERT запити (українською)
            insertSampleData();

            // ========== ВИКОНАННЯ ВСІХ ЗАПИТІВ ==========
            demonstrateAllQueries();

        } catch (SQLException e) {
            System.err.println("Помилка підключення: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("\n З'єднання закрито.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}