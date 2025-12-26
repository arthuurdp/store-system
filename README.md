# Store System 

A Java console application designed to manage products and purchases for a retail store. This project serves as a comprehensive demonstration of **Object-Oriented Programming (OOP)**, **Layered Architecture**, and manual **Persistence** using **JDBC** and **MySQL**.

## Educational Purpose & Concepts

This project was developed with focus on core software engineering principles and the internal workings of data persistence. Instead of relying on high-level frameworks (like Spring Data or Hibernate).

### 1. Manual ORM & Data Grouping
One of the most challenging aspects of this project was mapping relational data (SQL) to objects (Java) when dealing with **1:N** relationships. 
*   **The Challenge**: SQL `JOIN`s return "flattened" rows, duplicating purchase data for every item.
*   **The Solution**: I implemented a **Map-based grouping logic** in the DAO layer. Using a `HashMap<Integer, Purchase>`, the system ensures that multiple result set rows are aggregated into a single `Purchase` object with a list of products, preventing object duplication and ensuring data integrity.

### 2. Transaction Management
To guarantee consistency (especially when a purchase affects both the `purchases` and `products` tables), I implemented manual **Transaction Control**:
*   Disabling `autoCommit`.
*   Using `commit()` for successful operations.
*   Implementing `rollback()` in `catch` blocks to prevent partial data updates.

### 3. Layered Architecture & Design Patterns
*   **DAO Pattern**: Completely decouples the business logic from the SQL details.
*   **Service Layer**: Acts as a bridge, orchestrating complex business rules like stock updates and tax calculations.
*   **Dependency Injection**: The `StoreSystem` receives its services via constructor, making it easier to test and maintain.
*   **Factory Pattern**: Used `DaoFactory` to hide implementation details of the DAOs.

### 4. Error Handling
Custom `DbException` implementation to wrap `SQLException`, keeping the method signatures clean while still providing meaningful error propagation.

---

## Author
**Arthur Dall Agnol Pinheiro**
* Estudante de Análise e Desenvolvimento de Sistemas,  UPF - Passo Fundo.
* [LinkedIn](https://www.linkedin.com/in/arthur-dall-agnol-pinheiro-b04285357/)
