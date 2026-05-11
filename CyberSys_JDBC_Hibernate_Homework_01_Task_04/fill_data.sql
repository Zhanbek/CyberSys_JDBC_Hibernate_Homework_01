INSERT INTO Employees (Name, Phone)
VALUES 
('Іван Шевкуненко', '+380512345676'),
('Олег Суходрев', '+380671112233');

INSERT INTO positions(Id, Position, Salary)
VALUES 
(1, 'Manager', 15000),
(2, 'QA Ingeneer', 9000);

INSERT INTO Employee_Details(Employee_Id, Marital_Status, BirthDate, Address)
VALUES 
(1, 'Неодружений', '1990-05-10', 'Київ'),
(2, 'Одружений', '1985-03-15', 'Львів');

INSERT INTO Employees_positions
(Employee_Id, Position_Id, begin_date, end_date)
VALUES 
(1, 1, '2020-10-01', '2025-03-21'),
(1, 2, '2025-03-22', NULL),
(2, 2, '2012-07-21', '2025-04-01'),
(2, 1, '2025-04-02', NULL);