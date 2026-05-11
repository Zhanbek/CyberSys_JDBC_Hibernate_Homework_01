CREATE TABLE employees_positions (
    Employee_id INT,
    Position_id INT,
    begin_date DATE NOT NULL,
    end_date DATE,
    FOREIGN KEY (Employee_id) REFERENCES Employees(Id),
    FOREIGN KEY (Position_id) REFERENCES Positions(Id)
);