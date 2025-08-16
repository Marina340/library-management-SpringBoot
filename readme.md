Table details (all tables with columns, types, and relationships).

How to use the API (endpoints, HTTP methods, request body, response, roles, and JWT usage).

Here’s a ready-to-use version:

# Library Management System

## Overview
This is a **Spring Boot Library Management System** with:

- Role-based access control (ADMIN, LIBRARIAN, STAFF)
- JWT authentication
- User activity logging
- CRUD operations for books, authors, categories, members, publishers, and users
- Borrow/Return tracking

---

## Database Tables

### Roles & Users

| Table       | Columns                                  | Type/Constraints              | Description |
|------------ |---------------------------------------- |------------------------------ |------------ |
| `Roles`     | `id`                                      | BIGINT, PK, AUTO_INCREMENT    | Role ID |
|            | `name`                                    | VARCHAR(50), NOT NULL         | Role name (ADMIN, LIBRARIAN, STAFF) |
| `Users`     | `id`                                      | BIGINT, PK, AUTO_INCREMENT    | User ID |
|            | `username`                                | VARCHAR(100), UNIQUE, NOT NULL | Username |
|            | `password`                                | VARCHAR(255), NOT NULL        | BCrypt password hash |
|            | `enabled`                                 | BOOLEAN, default TRUE         | Is user active |
| `User_Roles`| `user_id`                                 | BIGINT, FK → Users(id)        | Mapping table |
|            | `role_id`                                 | BIGINT, FK → Roles(id)        | Mapping table |

---

### Categories

| Table       | Columns                   | Type/Constraints             | Description |
|------------ |-------------------------- |----------------------------- |------------ |
| `Categories`| `id`                      | BIGINT, PK, AUTO_INCREMENT   | Category ID |
|            | `name`                     | VARCHAR(100), NOT NULL       | Category name |
|            | `parent_id`                | BIGINT, FK → Categories(id)  | Optional parent category for hierarchy |

---

### Publishers

| Table        | Columns               | Type/Constraints           | Description |
|------------- |--------------------- |--------------------------- |------------ |
| `Publishers` | `id`                  | BIGINT, PK, AUTO_INCREMENT | Publisher ID |
|             | `name`                 | VARCHAR(100), NOT NULL     | Publisher name |
|             | `address`              | VARCHAR(255)               | Publisher address |

---

### Authors & Books

| Table        | Columns                               | Type/Constraints                   | Description |
|------------- |------------------------------------- |---------------------------------- |------------ |
| `Authors`    | `id`                                  | BIGINT, PK, AUTO_INCREMENT         | Author ID |
|             | `name`                                 | VARCHAR(100), NOT NULL             | Author name |
|             | `biography`                            | TEXT                               | Author bio |
| `Books`      | `id`                                  | BIGINT, PK, AUTO_INCREMENT         | Book ID |
|             | `title`                                | VARCHAR(255), NOT NULL             | Book title |
|             | `isbn`                                 | VARCHAR(20)                        | ISBN code |
|             | `edition`                              | INT                                 | Edition number |
|             | `publication_year`                      | INT                                 | Year of publication |
|             | `language`                             | VARCHAR(50)                        | Language |
|             | `summary`                              | TEXT                               | Book summary |
|             | `cover_image`                          | VARCHAR(255)                        | Path to cover image |
|             | `publisher_id`                          | BIGINT, FK → Publishers(id)        | Publisher reference |
| `Book_Authors` | `book_id`                            | BIGINT, FK → Books(id)             | Many-to-Many mapping |
|             | `author_id`                             | BIGINT, FK → Authors(id)           | Many-to-Many mapping |
| `Book_Categories` | `book_id`                         | BIGINT, FK → Books(id)             | Many-to-Many mapping |
|             | `category_id`                           | BIGINT, FK → Categories(id)        | Many-to-Many mapping |

---

### Members & Borrow Transactions

| Table                  | Columns                                   | Type/Constraints                 | Description |
|----------------------- |----------------------------------------- |-------------------------------- |------------ |
| `Members`              | `id`                                     | BIGINT, PK, AUTO_INCREMENT       | Member ID |
|                        | `name`                                   | VARCHAR(100), NOT NULL           | Member name |
|                        | `email`                                  | VARCHAR(100), NOT NULL, UNIQUE  | Email |
|                        | `phone`                                  | VARCHAR(20)                      | Phone number |
|                        | `address`                                | VARCHAR(255)                     | Address |
| `Borrow_Transactions`  | `id`                                     | BIGINT, PK, AUTO_INCREMENT       | Transaction ID |
|                        | `book_id`                                | BIGINT, FK → Books(id)           | Borrowed book |
|                        | `member_id`                              | BIGINT, FK → Members(id)         | Borrowing member |
|                        | `borrowed_date`                          | DATETIME, NOT NULL               | Date borrowed |
|                        | `due_date`                               | DATETIME, NOT NULL               | Due date |
|                        | `return_date`                            | DATETIME                         | Return date |
|                        | `status`                                 | VARCHAR(50)                      | BORROWED or RETURNED |

---

### User Activity Logs

| Table                | Columns                          | Type/Constraints             | Description |
|--------------------- |-------------------------------- |----------------------------- |------------ |
| `user_activity_logs` | `id`                             | BIGINT, PK, AUTO_INCREMENT  | Log ID |
|                     | `username`                        | VARCHAR(255), NOT NULL       | Username performing action |
|                     | `action`                          | VARCHAR(50), NOT NULL        | CREATE, UPDATE, DELETE, LOGIN, etc. |
|                     | `entity`                          | VARCHAR(100), NOT NULL       | Affected entity/table |
|                     | `details`                         | TEXT                          | Optional details |
|                     | `timestamp`                        | DATETIME, default CURRENT_TIMESTAMP | Action timestamp |

---

## Security

- **Roles**:
  - ADMIN → Full access
  - LIBRARIAN → Manage books, members, publishers, categories,authors
  - STAFF → Borrow/return books, view entities
- **Authentication**: JWT token-based
- **Authorization**: `@PreAuthorize` for method-level access control
- **Password**: BCrypt hashed

---

## API Usage

### Authentication

**Login**

- `POST /auth/login`
- Body:

```json
{
  "username": "admin",
  "password": "password"
}


Response:

{
  "token": "<JWT_TOKEN>"
}


Use in request headers for protected endpoints:

Authorization: Bearer <JWT_TOKEN>

Users (ADMIN only)
Method	Endpoint	Description
GET	/users	List all users
POST	/users	Create new user
PUT	/users/{id}	Update user
DELETE	/users/{id}	Delete user
Books
Method	Endpoint	Roles Allowed	Notes
GET	/books	ADMIN, LIBRARIAN, STAFF	Paginated, optional page & size
GET	/books/{id}	ADMIN, LIBRARIAN, STAFF	Get specific book
POST	/books	ADMIN, LIBRARIAN	Multipart: book JSON + coverImage
PUT	/books/{id}	ADMIN, LIBRARIAN	Multipart: book JSON + optional coverImage
DELETE	/books/{id}	ADMIN	Delete book
Categories / Members / Publishers

Similar CRUD endpoints.

Roles:

CREATE/UPDATE → ADMIN, LIBRARIAN

DELETE → ADMIN

READ → ADMIN, LIBRARIAN, STAFF

Borrow Transactions
Method	Endpoint	Roles Allowed
POST	/borrow/borrowBook	ADMIN, LIBRARIAN, STAFF
POST	/borrow/returnBook	ADMIN, LIBRARIAN, STAFF
GET	/borrow/all	ADMIN, LIBRARIAN

Borrow/return requires memberId and bookId or transactionId.

User Activity Logs
GET /logs → List all actions (ADMIN only)

GET /logs → List all actions by a specific user( username="" in request param )(ADMIN only)

Pagination

All list endpoints support page and size request parameters:

GET /books?page=0&size=10

Example cURL Request
curl -X POST "http://localhost:8080/auth/login" \
-H "Content-Type: application/json" \
-d '{"username":"admin","password":"password"}'

curl -X POST "http://localhost:8080/books" \
-H "Authorization: Bearer <JWT_TOKEN>" \
-F "book={\"title\":\"New Book\",\"isbn\":\"1234567890\"};type=application/json" \
-F "coverImage=@/path/to/image.jpg"


This project uses Spring Boot and Docker. The Java application runs in a container, while it connects to an external MySQL database (can be a local XAMPP/MySQL installation or another DB server).

1. Prepare the Database

Make sure MySQL is installed and running outside Docker (e.g., XAMPP, MAMP, or a remote DB).

Create a database named library_db.

CREATE DATABASE library_db;


Run the SQL scripts to create tables and seed demo data:

# If using MySQL CLI
mysql -u root -p library_db < db/schema.sql
mysql -u root -p library_db < db/data.sql


If your root has no password, omit the -p flag:

mysql -u root library_db < db/schema.sql
mysql -u root library_db < db/data.sql

2. Configure the Application

The project reads DB credentials and server port from environment variables:

SPRING_DATASOURCE_URL: "jdbc:mysql://<DB_HOST>:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
SPRING_DATASOURCE_USERNAME: "root"
SPRING_DATASOURCE_PASSWORD: ""  # empty if root has no password
SERVER_PORT: "8080"
JWT_SECRET: "YourSuperSecretKeyForJwtMustBeAtLeast32Bytes"
JWT_EXPIRATION: "86400000"


Update the docker-compose.yml environment section for the app service with your database host and credentials.
Example for local XAMPP MySQL:

SPRING_DATASOURCE_URL: "jdbc:mysql://host.docker.internal:3306/library_db?useSSL=false&serverTimezone=UTC"
SPRING_DATASOURCE_USERNAME: "root"
SPRING_DATASOURCE_PASSWORD: ""


host.docker.internal allows Docker containers to access services running on your host machine.

3. Build and Run the Docker Container

Build the Spring Boot app image:

docker compose build app


Run the container:

docker compose up -d


Only the Java app container will run. No DB container is needed if using external MySQL.

Check logs to ensure the app connected successfully:

docker logs -f library-app

4. Access the Application

Base URL: http://localhost:8080 (or the port you configured)

Login: POST /auth/login with username/password from seeded data.

5. Optional: Run Without Docker

You can also run the project directly from your IDE:

Make sure the external MySQL database is running.

Update application.properties / application.yml with DB credentials.

Run the Spring Boot main class (LibraryManagementApplication.java).

Notes

Passwords: Must always be BCrypt hashed when creating users.

JWT: Expiration is configured by JWT_EXPIRATION environment variable.

External DB: Ensure the DB user has permissions for SELECT, INSERT, UPDATE, and DELETE on library_db.

File Uploads: Ensure your container has access to any directories storing book cover images if using multipart uploads.
