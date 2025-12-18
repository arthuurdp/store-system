# Store System

A Java console application designed to manage products and purchases for a retail store. This project implements a modern, professional **Layered Architecture** with persistence in a **MySQL database** via **JDBC**.

## Features

The system offers a full suite of functionalities to manage inventory and sales:

### Product Management
- **addProduct():** Register new products.
- **excludeProduct():** Remove products from the inventory.
- **changeProduct():** Update name, quantity, or price.
- **listProducts():** View all stock items sorted by name.

### Purchase Management
- **Add Purchase:** Create a new purchase with multiple items.
- **Stock Integration:** Automatically deducts from stock when a product is added to a purchase.
- **Exclude Purchase:** Cancel a purchase and automatically return all items to stock.
- **Change Purchase:** Add or remove products from an existing purchase, with real-time stock updates.
- **List Purchases:** View all recorded purchases with detailed itemized lists.

### Financial Calculations
- **Dynamic Totals:** Automatically calculates the subtotal of each purchase.
- **ICMS Tax:** Applies a standard ICMS tax (17%) to the total purchase value.

## Architecture

The project follows a **Layered Architecture** pattern, ensuring separation of concerns and maintainability:

1. **Entities (`src/entities`)**: Pure data objects (`Product`, `Purchase`) containing core domain logic.
2. **DAO Layer (`src/dao`)**: Data Access Objects that encapsulate all database interactions using JDBC.
3. **Service Layer (`src/services`)**: Orchestrates business rules and coordinates between the UI and the DAO. This layer handles complex operations like stock management.
4. **UI Layer (`src/system`)**: Manages user interaction and console I/O, decoupled from business logic via **Dependency Injection**.
5. **Database Layer (`src/db`)**: Manages the connection pool and SQL exception handling.

## Tech Stack

- **Java**: Core programming language.
- **JDBC**: Java Database Connectivity for persistence.
- **MySQL**: Relational database.
- **MySQL Connector/J**: Official JDBC driver for MySQL.

## Setup & Installation

### 1. Database Setup
1. Create a MySQL database named storejdbc.
2. Run the provided `script.sql` file to create the necessary tables:
   ```sql
   -- Run this in your MySQL client
   SOURCE script.sql;
   ```

### 2. Configure Connection
1. Create a `db.properties` file in the project root (or root of the source) with your credentials:
   ```properties
   user=your_username
   password=your_password
   dburl=jdbc:mysql://localhost:3306/store_db
   useSSL=false
   ```
2. Ensure the `lib/mysql-connector-j-9.5.0.jar` is added to your project's classpath.

### 3. Run the Application
Compile and run the `Main` class located in `src/application/Main.java`.

## 📖 Usage Example

Upon running the program, you will interact with the following menu:

```text
-=-=-=-=-=-=-=-=- MENU -=-=-=-=-=-=-=-=-=-=
1 - Add Product
2 - Exclude Product
3 - Add Purchase
4 - Exclude Purchase
5 - Change Product
6 - Change Purchase
7 - List Products
8 - List Purchases
9 - Exit Program
-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
Choose an option:
```

## Best Practices Implemented
- **Single Responsibility Principle (SRP)**: Each class has a clear, focused purpose.
- **Dependency Injection**: Services are injected into the UI layer.
- **Transactional Integrity**: Database operations use manual commits and rollbacks to ensure data consistency.
- **Clean Code**: Meaningful naming, consistent formatting, and decoupled components.

---
## Author
Arthur Dall Agnol Pinheiro.
