-- Publishers table
CREATE TABLE publishers (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            address VARCHAR(255)
);

-- Authors table
CREATE TABLE authors (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         biography TEXT
);

-- Categories table (supports hierarchy)
CREATE TABLE categories (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            parent_id INT,
                            FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- Books table
CREATE TABLE books (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       isbn VARCHAR(20) UNIQUE NOT NULL,
                       edition INT,
                       publication_year INT,
                       language VARCHAR(50),
                       summary TEXT,
                       cover_image VARCHAR(255),
                       publisher_id INT,
                       FOREIGN KEY (publisher_id) REFERENCES publishers(id)
);

-- Many-to-many: Book-Authors
CREATE TABLE book_authors (
                              book_id INT,
                              author_id INT,
                              PRIMARY KEY (book_id, author_id),
                              FOREIGN KEY (book_id) REFERENCES books(id),
                              FOREIGN KEY (author_id) REFERENCES authors(id)
);

-- Many-to-many: Book-Categories
CREATE TABLE book_categories (
                                 book_id INT,
                                 category_id INT,
                                 PRIMARY KEY (book_id, category_id),
                                 FOREIGN KEY (book_id) REFERENCES books(id),
                                 FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Members table (library borrowers)
CREATE TABLE members (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) UNIQUE NOT NULL,
                         phone VARCHAR(20),
                         address VARCHAR(255)
);

-- Borrow Transactions table
CREATE TABLE borrow_transactions (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     book_id INT NOT NULL,
                                     member_id INT NOT NULL,
                                     borrowed_date DATETIME NOT NULL,
                                     due_date DATETIME NOT NULL,
                                     return_date DATETIME,
                                     status ENUM('BORROWED', 'RETURNED') DEFAULT 'BORROWED',
                                     FOREIGN KEY (book_id) REFERENCES books(id),
                                     FOREIGN KEY (member_id) REFERENCES members(id)
);

-- Users table (system users)
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN DEFAULT TRUE
);

-- Roles table
CREATE TABLE roles (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) UNIQUE NOT NULL
);

-- User-Roles (many-to-many)
CREATE TABLE user_roles (
                            user_id INT,
                            role_id INT,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- User Activity Logs
CREATE TABLE user_activity_logs (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    username VARCHAR(100) NOT NULL,
                                    action VARCHAR(50) NOT NULL,
                                    entity VARCHAR(100),
                                    details TEXT,
                                    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);
-- Publishers
INSERT INTO publishers (id, name, address) VALUES
                                               (1, 'Penguin Random House', 'New York, USA'),
                                               (2, 'HarperCollins', 'London, UK'),
                                               (3, 'Oxford University Press', 'Oxford, UK'),
                                               (4, 'Springer', 'Berlin, Germany');

-- Authors
INSERT INTO authors (id, name, biography) VALUES
                                              (1, 'J.K. Rowling', 'Author of the Harry Potter series.'),
                                              (2, 'George Orwell', 'English novelist, essayist, and critic.'),
                                              (3, 'Albert Einstein', 'Physicist and Nobel Prize winner.'),
                                              (4, 'Stephen Hawking', 'Theoretical physicist and cosmologist.');

-- Categories
INSERT INTO categories (id, name, parent_id) VALUES
                                                 (1, 'Fiction', NULL),
                                                 (2, 'Science', NULL),
                                                 (3, 'Fantasy', 1),
                                                 (4, 'Dystopian', 1),
                                                 (5, 'Physics', 2);

-- Books
INSERT INTO books (id, title, isbn, edition, publication_year, language, summary, cover_image, publisher_id) VALUES
                                                                                                                 (1, 'Harry Potter and the Philosopher''s Stone', '9780747532699', 1, 1997, 'English', 'First book in the Harry Potter series.', NULL, 1),
                                                                                                                 (2, '1984', '9780451524935', 1, 1949, 'English', 'Dystopian novel about totalitarianism.', NULL, 2),
                                                                                                                 (3, 'Relativity: The Special and the General Theory', '9780465025268', 1, 1916, 'English', 'Einstein explains relativity.', NULL, 3),
                                                                                                                 (4, 'A Brief History of Time', '9780553380163', 1, 1988, 'English', 'Hawking explains cosmology.', NULL, 4);

-- Book-Authors
INSERT INTO book_authors (book_id, author_id) VALUES
                                                  (1, 1),
                                                  (2, 2),
                                                  (3, 3),
                                                  (4, 4);

-- Book-Categories
INSERT INTO book_categories (book_id, category_id) VALUES
                                                       (1, 3),
                                                       (2, 4),
                                                       (3, 5),
                                                       (4, 5);

-- Members
INSERT INTO members (id, name, email, phone, address) VALUES
                                                          (1, 'Alice Johnson', 'alice@example.com', '1234567890', '123 Maple Street'),
                                                          (2, 'Bob Smith', 'bob@example.com', '0987654321', '456 Oak Avenue'),
                                                          (3, 'Charlie Brown', 'charlie@example.com', '5551112222', '789 Pine Road');

-- Borrow Transactions
INSERT INTO borrow_transactions (id, book_id, member_id, borrowed_date, due_date, return_date, status) VALUES
                                                                                                           (1, 1, 1, '2025-08-01 10:00:00', '2025-08-15 10:00:00', '2025-08-14 09:30:00', 'RETURNED'),
                                                                                                           (2, 2, 2, '2025-08-05 14:00:00', '2025-08-19 14:00:00', NULL, 'BORROWED');

-- Roles
INSERT INTO roles (id, name) VALUES
                                 (1, 'ADMIN'),
                                 (2, 'LIBRARIAN'),
                                 (3, 'STAFF');

-- Users
INSERT INTO users (id, username, password, enabled) VALUES
                                                        (1, 'admin', '$2a$10$Md9RAZt9TTy1qckz0TnCI.a2KvZ5BF5DT/Te7byWRzPL16bN7tcMa', 1),
                                                        (2, 'librarian', '$2a$10$Md9RAZt9TTy1qckz0TnCI.a2KvZ5BF5DT/Te7byWRzPL16bN7tcMa', 1),
                                                        (3, 'staff', '$2a$10$Md9RAZt9TTy1qckz0TnCI.a2KvZ5BF5DT/Te7byWRzPL16bN7tcMa', 1);

-- User-Roles
INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1),
                                              (2, 2),
                                              (3, 3);

-- User Activity Logs
INSERT INTO user_activity_logs (id, username, action, entity, details, timestamp) VALUES
                                                                                      (1, 'admin', 'CREATE', 'Book', 'Added new book Harry Potter', '2025-08-10 09:00:00'),
                                                                                      (2, 'librarian', 'BORROW', 'Book', 'Member borrowed 1984', '2025-08-11 12:00:00'),
                                                                                      (3, 'staff', 'RETURN', 'Book', 'Member returned Harry Potter', '2025-08-14 09:30:00');

