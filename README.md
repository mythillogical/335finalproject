# 🍽️ Restaurant Management System User Manual (***under construction***)

An interactive Java-based restaurant simulation app built with a graphical user interface. Servers can manage tables, take and modify orders, calculate bills with tips, and generate itemized sales reports. The system follows the MVC (Model-View-Controller) architecture and includes over 90% unit test coverage using JUnit.

## 🚀 Overview

This project was developed as the final assignment for **CSc 335 – Object Oriented Programming**. It simulates a restaurant’s front-of-house operations from the perspective of servers and managers, allowing for dynamic menu editing, table assignment, order processing, and performance tracking through an intuitive Java Swing GUI.

## 🧩 Features

- ✅ Server management (add/remove servers)
- ✅ Table assignment based on guest count and availability
- ✅ Menu editing (add/remove items with custom modifiers)
- ✅ Per-table order management (add/remove items)
- ✅ Bill calculation with tips and even-split logic
- ✅ Sales reporting (most ordered items, total revenue)
- ✅ Persistent menu via CSV file
- ✅ Full GUI using Java Swing
- ✅ Over 90% unit test coverage

## 🛠️ How to Run the Project

### 📦 Requirements
- Java 11 or higher
- Java IDE (e.g., IntelliJ, Eclipse) or command-line tools

### ▶️ Running the Application
1. Clone or download the project to your local machine.
2. Open the project in your preferred IDE.
3. Navigate to `view/Main.java`.
4. Run the `main()` method.

> Or run it via terminal:
```bash
javac view/Main.java
java view.Main
```

### Project Structure: 
├── model/                 # Backend logic (Menu, Table, Item, Bill, etc.)
├── view/                  # Swing GUI (panels, windows, buttons, dialogs)
├── test/                  # JUnit test cases
├── Menu.csv               # Menu data (Category, Name, Cost, Mods)
├── tables.txt             # Table layout (Table ID and capacity)
└── README.md              # Project documentation

### 👨‍💻 Contributors
Asifur Rahman, Michael B, Michael D, Mohammed AlNasser
