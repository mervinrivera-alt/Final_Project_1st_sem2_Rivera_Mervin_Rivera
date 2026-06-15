# Final_Project_1st_sem2_Rivera_Mervin_Rivera
# 📦 Warehouse Management System
A robust, modern desktop application built with **Java Swing** and **JDBC**, designed to streamline warehouse operations, inventory tracking, and batch sorting. 

---

## 📖 The Development Journey: Remakes, Debugs, & Recodes

Building this system wasn't just a straight line from start to finish. It took deep debugging sessions, complete UI overhauls, and structural recodes to get it right. Here is a look behind the scenes at the biggest challenges we solved:

### 1. The "Invisible Line" Bug (Database & ComboBox Fix) 🐛
* **The Technical Problem:** Our sorting update system was crashing and throwing `ArrayIndexOutOfBoundsException` and `NumberFormatException` errors when trying to read user selections.
* **In Plain English:** The system was programmed to look for an ID and a Name (like reading `1-061426-2`). However, the dropdown menu was only holding the number `123`. When the code tried to chop a line that wasn't there, it panicked and crashed.
* **The Fix:** We completely recoded the action listener. Instead of blindly chopping text, we taught the system to safely read the exact ID number, securely connect to the database to fetch the original `Batch_id`, `Arrival_date`, and `Quantities`, and update the record without losing any existing data.

### 2. The Time Machine UI (Modernizing the Design) 🎨
* **The Technical Problem:** Standard Java Swing UI components look outdated, resembling software from Windows 95. We needed a UI/UX (User Interface / User Experience) remake.
* **In Plain English:** The buttons were clunky gray boxes. We wanted the application to feel like a modern, sleek website rather than an old desktop program.
* **The Fix:** We scrapped the default look entirely. We built a flat-design navigation bar utilizing a dark navy color palette (`[44, 62, 80]`). We stripped away the ugly 3D borders and programmed dynamic `mouseEntered` and `mouseExited` hover effects. Now, when a supervisor moves their mouse over the "☰ Menu" button, it lights up and interacts smoothly.

### 3. The "Window Clutter" Recode (Mastering `CardLayout`) 🗂️
* **The Technical Problem:** Originally, navigating between the Dashboard and the Sorting screens opened multiple `JFrames` (windows), which causes memory leaks and clutters the user's screen.
* **In Plain English:** Every time the user clicked "Next", a brand new window popped up. If they clicked it 10 times, they had 10 windows open! 
* **The Fix:** We restructured the entire app architecture to use a `CardLayout`. 
  * Think of `CardLayout` like a deck of playing cards: all the screens (Dashboard, Settings, Inventory) are loaded in the same spot, but you only see the top card. 
  * We built custom communication methods (e.g., `showSpecificCard()`) so the main window could securely tell the popup dialog exactly which "card" to slide to the top, keeping the user in one clean, single-window environment.

---

## ✨ Core Features

* **📦 Inventory & Batch Sorting:** Assign and update sorting IDs and monitor quantities using dynamic SQL queries.
* **🖥️ Modern Navigation:** Sleek sidebar navigation using `CardLayout` for smooth, memory-efficient screen transitions.
* **🗄️ Database Integration:** Seamless CRUD operations connected to a MySQL backend, ensuring data integrity.
* **🛡️ Secure Validation:** Built-in form validation to prevent blank submissions and database connection errors.

## 🛠️ Technologies Used

* **Language:** Java (JDK 8+)
* **GUI Framework:** Java Swing (NetBeans GUI Builder)
* **Database:** MySQL
* **Database Connectivity:** JDBC (Java Database Connectivity)
* **IDE:** Apache NetBeans 

classDiagram
    %% Core UI Classes
    class MainMenuDashboard {
        -JButton jButtonMenu
        -JPanel jPanelMainContainer
        -CardLayout cardLayout
        +jButtonNextPageActionPerformed(evt: ActionEvent) void
    }
    

    ```mermaid class SortingWindow {
        -JComboBox jComboBox1
        -JComboBox jComboBox2
        -JTextField jTextField9
        +SortingWindow(parent: Frame, modal: boolean)
        +showSpecificCard(cardName: String) void
        -jButton7ActionPerformed(evt: ActionEvent) void
        -loadSortedTable3() void
        -loadDeliveryTable() void
    }

    %% Database & Logic Classes
    class DBConnection {
        -String url
        -String user
        -String password
        +getConnection() Connection
    }

    class CRUD_OP {
        +updateSortedBatch(specialId: int, categoryId: int, itemName: String, quantity: int, batchId: int, arrivalDate: String) boolean
    }

    %% Data Models
    class CategoryItem {
        +int id
        +String name
        +CategoryItem(id: int, name: String)
        +toString() String
    }

    %% Relationships and Dependencies
    MainMenuDashboard --> SortingWindow : Opens (Instantiates)
    SortingWindow --> CRUD_OP : Triggers operations
    CRUD_OP ..> DBConnection : Requests connection
    SortingWindow --> CategoryItem : Populates Dropdowns ```
