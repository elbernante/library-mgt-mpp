PRAGMA foreign_keys = ON;

CREATE TABLE "user" (
    "user_id"   TEXT NOT NULL UNIQUE,
    "password"  TEXT NOT NULL,
    "firstname" TEXT,
    "lastname"  TEXT,
    PRIMARY KEY(user_id)
);

CREATE TABLE "role" (
    "id" INTEGER NOT NULL,
    "title" TEXT,
    "description" TEXT,
    PRIMARY KEY(id)
);

CREATE TABLE "user_role" (
    "user_id"   TEXT NOT NULL,
    "role_id"   TEXT NOT NULL,
    FOREIGN KEY(user_id) REFERENCES "user"(user_id),
    FOREIGN KEY(role_id) REFERENCES role(id),
    UNIQUE(role_id, user_id)
);

-- Define roles
INSERT INTO role (id, title, description) VALUES (0, "ADMINISTRATOR", "Administrator user");
INSERT INTO role (id, title, description) VALUES (1, "LIBRARIAN", "Librarian user");

-- Admin user
INSERT INTO "user"(user_id, password, firstname, lastname) VALUES ("admin", "202cb962ac59075b964b07152d234b70", "Mr. Admin", "User 0");
INSERT INTO user_role(user_id, role_id) VALUES("admin", 0);

-- Librarian user
INSERT INTO "user"(user_id, password, firstname, lastname) VALUES ("lib", "202cb962ac59075b964b07152d234b70", "Mr. Librarian", "User 1");
INSERT INTO user_role(user_id, role_id) VALUES("lib", 1);

-- Both (admin and librarian) user
INSERT INTO "user"(user_id, password, firstname, lastname) VALUES ("both", "202cb962ac59075b964b07152d234b70", "Mr. Both", "User 2");
INSERT INTO user_role(user_id, role_id) VALUES("both", 0);
INSERT INTO user_role(user_id, role_id) VALUES("both", 1);
