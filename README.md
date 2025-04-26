# 🍽️ Restaurant Management System

An interactive Java-based restaurant simulation application with a graphical user interface.  
Servers can manage tables, take and modify orders, calculate bills with tips, and generate detailed sales reports.  
The system is built following the **MVC (Model-View-Controller)** architecture with persistent storage and **90%+ unit test coverage** using JUnit 5.

---

## 🚀 Project Overview

This project was developed as the final assignment for **CSc 335 – Object-Oriented Programming and Design** (Spring 2025).  
It simulates a restaurant’s front-of-house operations from the perspective of servers and managers, allowing:
- Dynamic menu editing
- Table assignment
- Order management
- Performance tracking

All features are delivered through an intuitive **Java Swing** GUI.

---

## 🧩 Features

- ✅ **Server Management** (add and remove servers)
- ✅ **Table Management** (assign tables based on availability and guest size)
- ✅ **Menu Management** (add/remove menu items with optional custom modifiers)
- ✅ **Order Management** (take and edit customer orders at tables)
- ✅ **Bill Processing** (split bills evenly among guests, add tips)
- ✅ **Sales Reporting** (itemized sales reports and tips tracking)
- ✅ **Persistence**
  - **Menu** stored and loaded from a CSV file (`Menu.csv`)
  - **Servers** and **Closed Bills** serialized using Java I/O
- ✅ **Graphical User Interface** fully implemented using Java Swing
- ✅ **Unit Testing** over 90% test coverage using JUnit 5

---

## 🛠️ How to Run the Project

### 📦 Requirements
- Java 11 or higher
- Java IDE (e.g., IntelliJ IDEA, Eclipse) **or** terminal with `javac` and `java`
- JUnit 5 (for running test cases)

---

### ▶️ Running the Application

#### Option 1: Using an IDE
1. Clone or download this repository.
2. Open the project in your preferred IDE (IntelliJ recommended).
3. Navigate to `src/view/RestaurantGUI.java`.
4. Run the `main()` method.

#### Option 2: Using Terminal
1. Navigate to the project directory:
    ```bash
    cd path/to/335finalproject
    ```
2. Compile the source files:
    ```bash
    javac src/view/RestaurantGUI.java
    ```
3. Run the application:
    ```bash
    java -cp src view.RestaurantGUI
    ```

> **Note:** Make sure `Menu.csv` and `tables.txt` exist in the correct directory when running!

---

### 📂 Project Structure
![Final_Project_UML](https://github.com/user-attachments/assets/24385d19-b7ed-4699-a363-a66434f293d5)
---

## ✅ Unit Testing

- Over **90%** code coverage with **JUnit 5**.
- Test files are located inside the `src/test/` directory.
- To run tests:
  - Use your IDE's built-in test runner
  - Or run via terminal if JUnit 5 is properly configured.

---

## ✨ Key Technologies Used

| Technology | Purpose |
|------------|---------|
| Java 11+ | Core programming language |
| Java Swing | GUI development |
| Java I/O | File reading/writing and object persistence |
| JUnit 5 | Unit testing |
| MVC Design Pattern | Clean separation of Model, View, Controller |

---

## 👨‍💻 Contributors

- Michael B
- Michael D
- Asifur Rahman
- Mohammed AlNasser

---

## 🗂️ Notes

- Menu items are saved persistently in `Menu.csv`.
- Server and closed bill data are saved as serialized `.dat` files.
- If data files are missing on startup, the application will create fresh ones automatically.

---

# 🎯 Thank you for using Restaurant Management System! 
