# Electronic Store [Major Project]

[![View the documentation - PDF](https://img.shields.io/badge/View%20PDF%20documentation-color)](https://github.com/im-aditya-rathi/Electronic_Store/blob/master/src/main/resources/documentation/project_documentation.pdf)
[![View postman documentation](https://img.shields.io/badge/View%20postman%20documentation%20(online)-blue)](https://documenter.getpostman.com/view/18545238/2sA3s3FqgB)

## Overview
**Electronic_Store** is a comprehensive online electronics store application built with Java, Spring Boot, Hibernate, and MySQL. The application provides a full set of web services to manage users, categories, products, carts, and orders, each with well-defined JPA relationships, enabling nested API responses. The project is designed to be scalable, maintainable, and user-friendly, with a focus on clean code architecture.

## Features

### Modules
- **User**
- **Category**
- **Product**
- **Cart**
- **Order**

Each module is interconnected through JPA relationships, allowing for complex nested API responses.

### API Capabilities
- **Pagination and Sorting**: Customize API responses with pagination and sorting.
- **Nested API Responses**: Access comprehensive data structures due to well-defined JPA relationships.
- **Standard API Format**: APIs follow a consistent, easy-to-remember format.

### Request Validation
Integrated request validation ensures that all user inputs are checked before processing, reducing errors and maintaining data integrity.

### Exception Handling
Global exception handling is implemented to provide formatted error responses with clear messages, helping users understand and resolve issues efficiently.

### Logging
Robust logging mechanisms are in place to trace and resolve issues by capturing detailed logs during API execution.

### Image Handling
Supports image management for users, categories, and products, enhancing the application’s functionality with visual content.

### Architecture
The project follows a **three-layered architecture**:
1. **Controller Layer**: Handles HTTP requests and responses.
2. **Service Layer**: Contains business logic.
3. **Repository Layer**: Manages data persistence and retrieval.

### Database Persistence
Real-time data is securely stored in a MySQL database, ensuring data integrity and availability for future use.

## Technologies Used

- **Java**: The core programming language used for its object-oriented features and platform independence, allowing for robust application development.
- **Spring & Spring Boot**: Provides a powerful framework and rapid application development environment for creating enterprise-grade web applications with dependency injection and various tools to manage the application's life cycle.
- **JPA (Java Persistence API)**: Used for its ability to map Java objects to relational database tables, facilitating easier database operations and management.
- **Hibernate**: Implements JPA, providing advanced ORM capabilities to efficiently manage data persistence, lazy loading, and transaction handling.
- **MySQL Database**: Chosen for its reliability, performance, and scalability in managing relational data, ensuring data is persistently and securely stored.

## Summary
**Electronic_Store** is a well-structured, feature-rich web application offering a seamless user experience through advanced API features, image handling, and robust error management. The use of a standard API format, a three-layered architecture, and effective logging ensures that the system is maintainable, scalable, and easy to use.

---
