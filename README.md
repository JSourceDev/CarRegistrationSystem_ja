# 🚗 Car Registration System

This was my final project in Riga Coding School - Car Registration System. A simple Java desktop application that allows users to register, update, search, and delete car entries using a graphical user interface (GUI) built with Swing. Data is stored in a MySQL database, and all database operations (CRUD) are handled using JDBC.

---

## 📦 Features

- Add new car registrations
- View all registered cars
- Update existing records
- Delete car entries
- Simple and intuitive GUI (Java Swing)
- Data stored in MySQL database

---

## 🛠️ Tech Stack

- **Java** (JDK 17+ is fine)
- **Swing** for GUI
- **MySQL** for persistent storage
- **DBngin** for local MySQL server (my choice)
- **JDBC** for database connectivity

---

## 🗄️ Database Setup (MySQL using DBngin)

1. **Install & Start DBngin**

    - Download from [https://dbngin.com](https://dbngin.com)
    - Create a new MySQL server
    - Start the server and note the **port** (default is usually `3306`)

2. **Create the database:**

   Use [TablePlus](https://tableplus.com/) or another MySQL client and run:

   ```sql
   CREATE DATABASE car_registry_system;
   ```
   
3. **Create the cars table**

```sql
CREATE TABLE cars (
id INT AUTO_INCREMENT PRIMARY KEY,
brand VARCHAR(255),
model VARCHAR(255),
year INT,
registration_number VARCHAR(50),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

4. **Configure your credentials**
   
In the DBlogic.java file:

```java
private final String DB_URL = "jdbc:mysql://localhost:3306/car_registry_system";
private final String DB_USER = "root"; // your MySQL user
private final String DB_PASS = "";     // your MySQL password (if any)
```

🔁 Adjust the port if DBngin uses something other than 3306.

## 🚀 How to Run

1.	**Clone the repository**

```bash
git clone https://github.com/JSourceDev/CarRegistrationSystem_ja.git
cd car-registration-system
```

2.	**Open in IntelliJ IDEA and run**

Go to File > Open, and select the project folder.

Make sure mysql-connector-java-9.3.0.jar is in the lib/ folder and marked as a library:

Right-click the JAR → Add as Library

Run Main.java

## 📂 Project Structure

CarRegistrationSystem_ja/
├── .idea/                      
├── lib/
│   └── mysql-connector-j-9.3.0.jar 
├── out/                        
├── src/
│   ├── DBlogic/
│   │   └── DBlogic.java        
│   ├── model/
│   │   └── Car.java            
│   ├── UI/
│   │   └── CarRegistryAppGUI.java  
│   └── Main.java               
├── .gitignore
└── CarRegistrationSystem_ja.iml

## 🙋‍♂️ Author

JSourceDev

[LinkedIn](https://www.linkedin.com/in/j%C4%81nisavoti%C5%86%C5%A1/) | [GitHub](https://github.com/JSourceDev)

⸻

## 📄 License

This project is licensed under the MIT License.
