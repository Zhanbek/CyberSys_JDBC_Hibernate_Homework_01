CREATE TABLE EmployeeDetails (
    Id INT PRIMARY KEY  AUTO_INCREMENT PRIMARY KEY,
    EmployeeId INT,
    MaritalStatus VARCHAR(50),
    BirthDate DATE,
    Address VARCHAR(300),
    FOREIGN KEY (EmployeeID) REFERENCES Employees(Id)
);