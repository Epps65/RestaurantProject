# J's Corner Restaurant Management System (GUI)

## Description

This project is a JavaFX-based Graphical User Interface (GUI) designed to simulate the management of various restaurant operations. 
It provides visual workflows tailored to different employee roles, including table management, order taking, kitchen queuing, and payment processing.

## Core Features

* **Graphical User Interface:** Built with JavaFX for an intuitive visual user experience.
* **Role-Based Access Control:** Features a login screen that directs users to interfaces specific to their roles (Waiter, Manager, Busboy, Cook).
* **Visual Table Management:** Displays restaurant tables in a grid format with color-coded statuses.
* **Order Taking System:** Allows waiters to select menu items by category, add them to a table's order, and view a running total.
* **Kitchen Order Queue:** Displays orders sent by waiters in a queue for the cook to manage.
* **Payment Processing:** Provides an interface to handle payments (Cash/Card) for occupied tables.
* **Punch Clock Simulation:** Includes basic clock-in and clock-out functionality for tracking employee time.

### Table Statuses

Tables are color-coded to indicate their current status:

* **Green:** Clean and available
* **Yellow:** Occupied (currently has an active order or awaiting payment)
* **Red:** Dirty (needs cleaning after payment)
* **Other:** Set has Inactive by the Manager


### Role-Specific Functionality

* **Waiter:**
    * Views all table statuses.
    * Takes orders for 'Clean' tables.
    * Sends completed orders to the kitchen queue.
    * Initiates and processes payments for 'Occupied' tables.
* **Manager:**
    * Views all table statuses.
    * Can mark 'Dirty' tables as 'Clean'.
    * Can potentially change any table status via right-click .
    * Accesses Manager-specific menu options (currently placeholders).
* **Busboy:**
    * Views all table statuses.
    * Can mark 'Dirty' tables as 'Clean'.
* **Cook:**
    * Views incoming orders in the kitchen queue.
    * Can view the specific items within an order.
    * Marks orders as 'Ready' once prepared, removing them from the active queue.

## Setup and Running

### Prerequisites

1.  **Java Development Kit (JDK):** Ensure you have a compatible JDK installed.
2.  **JavaFX SDK:** The JavaFX SDK must be correctly configured with your IDE (e.g., Eclipse, IntelliJ) or build environment (
e.g., Maven, Gradle). Make sure your environment knows where to find the JavaFX libraries and necessary VM options are set if running from the command line.

### Steps

1.  **Compile:** Compile all `.java` files located within the `application` package. 

2.  **Run:** Execute the `Main` class within the `application` package. 

## Usage

1.  **Launch:** Start the application using the run command or your IDE's run function.
2.  **Login:** Use one of the predefined credentials at the login screen:

    | Role    | Username  | Password |
    | :------ | :-------- | :------- |
    | Waiter  | `waiter`  | `1234`   |
    | Manager | `manager` | `admin`  |
    | Busboy  | `busboy`  | `cleaner`|
    | Cook    | `cook`    | `kitchen`|

3.  **Interact:** Use the application based on your logged-in role:
    * **Waiters/Managers:**
        * Click a **Green** ('Clean') table to open the order-taking screen.
        * Select menu items by category, add them to the order, and click 'Send Order'. The table turns **Yellow** ('Occupied').
        * Click a **Yellow** ('Occupied') table to proceed to the payment screen.
        * After processing payment, the table turns **Red** ('Dirty').
    * **Busboys/Managers:**
        * Click a **Red** ('Dirty') table to mark it as **Green** ('Clean').
    * **Cooks:**
        * View orders in the 'Incoming Orders' list.
        * Select an order to view its items.
        * Click 'Mark as Ready' to move the order out of the active queue.
    * **Managers:**
        * Access the 'Manager Menu' for additional options (currently placeholders).
        * Right-clicking tables to cycle statuses.

## Notes

* The features under the 'Manager Menu' (e.g., Sales Analytics, Employee Analytics, Employee Actions) are currently **placeholders**. 
