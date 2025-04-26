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
- ✅ **Graphical User Interface** fully implemented using Java Swing with help using AI (ChatGPT)
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

![Screenshot 2025-04-25 at 6 10 17 PM](https://github.com/user-attachments/assets/3d585cf6-0f58-4810-9754-2b2c1acb6b05)
![Screenshot 2025-04-25 at 6 11 27 PM](https://github.com/user-attachments/assets/53ca50dd-6065-4f0b-985b-7ccead64cf07)
![Screenshot 2025-04-25 at 6 11 52 PM](https://github.com/user-attachments/assets/d58260b9-df7c-405d-bcb4-2ec8bc89a16f)
![Screenshot 2025-04-25 at 6 12 12 PM](https://github.com/user-attachments/assets/730c6cef-fc2a-47db-87ea-f91ce1afbce4)
![Screenshot 2025-04-25 at 6 13 04 PM](https://github.com/user-attachments/assets/2bbfe93b-8d9e-4095-8f33-666d55de6147)
![Screenshot 2025-04-25 at 6 13 19 PM](https://github.com/user-attachments/assets/52ef5355-ca01-4cbd-897c-f3664a1671ae)
![Screenshot 2025-04-25 at 6 13 38 PM](https://github.com/user-attachments/assets/472680e0-e025-44de-9d4a-4c84122b9ca0)
![Screenshot 2025-04-25 at 6 13 59 PM](https://github.com/user-attachments/assets/dc90ff29-58fa-49e4-b9f4-26e0d2772338)
![Screenshot 2025-04-25 at 6 14 14 PM](https://github.com/user-attachments/assets/5dfd8da4-5f93-4346-9fd5-f8444cb3eb92)

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
