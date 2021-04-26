CREATE TABLE Customers(
	Email VARCHAR(255) NOT NULL, 
    Password VARCHAR(255)NOT NULL, 
    Address VARCHAR(255), 
    Name VARCHAR(50) NOT NULL,
    PRIMARY KEY(EMAIL)
   );

   
CREATE TABLE Deptarment(
DName VARCHAR(50) PRIMARY KEY,
Description VARCHAR(255),
);

   
CREATE TABLE Employees(
 	Name VARCHAR(50) NOT NULL, 
    Email VARCHAR(50) NOT NULL, 
	userid VARCHAR(55),
	IsManager? CHAR(3) NOT NULL,
    IsFullTime? CHAR(3) NOT NULL,
    Department VARCHAR(50) NOT NULL,
   	PRIMARY KEY(Email),
    FOREIGN KEY(Department) REFERENCES Department(DName)  	
	);

CREATE SEQUENCE orderId_inrement
MINVALUE 1
START WITH 50
INCREMENT BY 1
CACHE 40;

CREATE TABLE Orders(
	OrderId INT PRIMARY KEY,
    Date_Time DATETIME,
    Status VARCHAR(10),
    CustId VARCHAR(255),
    FOREIGN KEY(CustId) REFERENCES Customers(Email)
   );
   
CREATE TABLE O_Process(
	OrderId INT,
	EmpId VARCHAR(50),
	Ostatus# VARCHAR(10),
	PRIMARY KEY(OrderId,EmpId)
	FOREIGN KEY(OrderId) REFERENCES Orders(OrderId),
	FOREIGN KEY(EmpId) REFERENCES Employees(Email)
	);

CREATE TABLE ListOfItems(
	ItemName VARCHAR(55), 
    Description VARCHAR(25), 
    Price_Per_Serving NUMBER(10,2)
    PRIMARY KEY(ItemName)
   );
	
	
CREATE TABLE FoodItems(    
	ItemName VARCHAR(55), 
    Status VARCHAR(15),
    Dept VARCHAR(50),
    MadeBy VARCHAR(50),
    Quantity INT,
    PRIMARY KEY(ItemName),
    FOREIGN KEY(ItemName) REFERENCES ListOfItems(ItemName),
   	FOREIGN KEY(Dept) REFERENCES Department(DName),
   	FOREIGN KEY(MadeBy) REFERENCES Employees(Email)
  );

   
CREATE TABLE OrderDetails (
	OrderId INT,
	ItemName VARCHAR(55),
    Quantity INT,
    Amount Number(10,2),
    EMAIL VARCHAR(50),
    PRIMARY KEY(OrderId,ItemName),
    FOREIGN KEY(OrderId) REFERENCES Orders(OrderId),
    FOREIGN KEY(ItemsName) REFERENCES FoodItems(ItemsName)
   );