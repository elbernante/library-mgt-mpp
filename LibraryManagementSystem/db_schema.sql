PRAGMA foreign_keys = ON;

CREATE TABLE "user" (
  "user_id"   TEXT NOT NULL UNIQUE,
  "password"  TEXT NOT NULL,
  "firstname" TEXT,
  "lastname"  TEXT,
  PRIMARY KEY (user_id)
);

CREATE TABLE "role" (
  "id"          INTEGER NOT NULL,
  "title"       TEXT,
  "description" TEXT,
  PRIMARY KEY (id)
);

CREATE TABLE "user_role" (
  "user_id" TEXT NOT NULL,
  "role_id" TEXT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES "user" (user_id),
  FOREIGN KEY (role_id) REFERENCES role (id),
  UNIQUE (role_id, user_id)
);

CREATE TABLE "book" (
  "isbn"  TEXT PRIMARY KEY,
  "title" TEXT NOT NULL
);

CREATE TABLE "author" (
  "author_id"   INTEGER PRIMARY KEY AUTOINCREMENT,
  "firstname"   TEXT NOT NULL,
  "lastname"    TEXT NOT NULL,
  "street"      TEXT,
  "city"        TEXT,
  "state"       TEXT,
  "zip"         TEXT,
  "phone"       TEXT,
  "credentials" TEXT,
  "bio"         TEXT
);

CREATE TABLE "book_author" (
  "book_isbn" TEXT    NOT NULL,
  "author_id" INTEGER NOT NULL,
  FOREIGN KEY (book_isbn) REFERENCES "book" (isbn),
  FOREIGN KEY (author_id) REFERENCES author (author_id),
  UNIQUE (book_isbn, author_id)
);

CREATE TABLE "book_copy" (
  "copy_id"   INTEGER NOT NULL,
  "book_isbn" TEXT    NOT NULL,
  "available" INTEGER NOT NULL,
  FOREIGN KEY (book_isbn) REFERENCES "book" (isbn),
  UNIQUE (copy_id, book_isbn)
);

-- Define roles
INSERT INTO role (id, title, description) VALUES (0, "ADMINISTRATOR", "Administrator user");
INSERT INTO role (id, title, description) VALUES (1, "LIBRARIAN", "Librarian user");

-- Admin user
INSERT INTO "user" (user_id, password, firstname, lastname)
VALUES ("admin", "202cb962ac59075b964b07152d234b70", "Mr. Admin", "User 0");
INSERT INTO user_role (user_id, role_id) VALUES ("admin", 0);

-- Librarian user
INSERT INTO "user" (user_id, password, firstname, lastname)
VALUES ("lib", "202cb962ac59075b964b07152d234b70", "Mr. Librarian", "User 1");
INSERT INTO user_role (user_id, role_id) VALUES ("lib", 1);

-- Both (admin and librarian) user
INSERT INTO "user" (user_id, password, firstname, lastname)
VALUES ("both", "202cb962ac59075b964b07152d234b70", "Mr. Both", "User 2");
INSERT INTO user_role (user_id, role_id) VALUES ("both", 0);
INSERT INTO user_role (user_id, role_id) VALUES ("both", 1);

-- Author
INSERT INTO author (author_id, firstname, lastname, street, city, state, zip)
VALUES (1, 'Michael', 'Peacock', '2648 Hemlock Lane', 'Harlingen', 'TX', '78550');
INSERT INTO author (author_id, firstname, lastname, street, city, state, zip)
VALUES (2, 'Justin', 'Cortez', '1202 Retreat Avenue', 'Birmingham', 'AL', '35203');
INSERT INTO author (author_id, firstname, lastname, street, city, state, zip)
VALUES (3, 'Josephine', 'Morris', '4373 Rinehart Road', 'Sunrise', 'FL', '33323');
INSERT INTO author (author_id, firstname, lastname, street, city, state, zip)
VALUES (4, 'Christine', 'Flanagan', '3016 Sherwood Circle', 'Leesville', 'LA', '71446');

-- Book
INSERT INTO book (isbn, title) VALUES ('978-1-84951-918-2', 'Creating Development Environments with Vagrant');
INSERT INTO book (isbn, title) VALUES ('978-1-78355-371-6', 'GitHub Essentials');
INSERT INTO book (isbn, title) VALUES ('978-1-78398-478-7', 'Orchestrating Docker');

-- Book & Author
INSERT INTO book_author (book_isbn, author_id) VALUES ('978-1-84951-918-2', 1);
INSERT INTO book_author (book_isbn, author_id) VALUES ('978-1-84951-918-2', 2);
INSERT INTO book_author (book_isbn, author_id) VALUES ('978-1-78355-371-6', 3);
INSERT INTO book_author (book_isbn, author_id) VALUES ('978-1-78398-478-7', 4);

-- Book Copy
INSERT INTO book_copy (copy_id, book_isbn, available) VALUES (1, '978-1-84951-918-2', 1);
INSERT INTO book_copy (copy_id, book_isbn, available) VALUES (2, '978-1-84951-918-2', 0);
INSERT INTO book_copy (copy_id, book_isbn, available) VALUES (3, '978-1-84951-918-2', 1);
INSERT INTO book_copy (copy_id, book_isbn, available) VALUES (1, '978-1-78355-371-6', 1);
INSERT INTO book_copy (copy_id, book_isbn, available) VALUES (2, '978-1-78355-371-6', 0);
INSERT INTO book_copy (copy_id, book_isbn, available) VALUES (1, '978-1-78398-478-7', 1);
INSERT INTO book_copy (copy_id, book_isbn, available) VALUES (2, '978-1-78398-478-7', 1);
INSERT INTO book_copy (copy_id, book_isbn, available) VALUES (3, '978-1-78398-478-7', 1);