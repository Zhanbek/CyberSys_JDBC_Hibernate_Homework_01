CREATE TABLE employee_details (
    Id INT PRIMARY KEY  AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    Marital_Status VARCHAR(50),
    BirthDate DATE,
    Address VARCHAR(300),
    FOREIGN KEY (employee_id) REFERENCES Employees(Id)
);
