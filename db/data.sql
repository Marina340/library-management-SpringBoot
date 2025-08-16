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
