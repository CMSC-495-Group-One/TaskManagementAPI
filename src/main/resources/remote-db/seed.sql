-- These steps can be used to seed the database.
-- Manually run each step individually.
-- Reach out to Connor if the DB needs to be reset.

-- 1) Insert roles if they don't exist
INSERT INTO roles (name)
SELECT 'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

INSERT INTO roles (name)
SELECT 'USER'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER');

-- 2) Insert users with roles in Postman: POST http://localhost:8080/v1/auth/signup
--    This will ensure passwords are hashed before being stored.
--    { "username": "admin", "password": "password", "email": "admin@mail.com" }
--    { "username": "user", "password": "password", "email": "user@mail.com" }

-- 3) Assign ADMIN to admin
INSERT INTO user_roles (user_id, role_id)
SELECT app_users.id, roles.id
FROM app_users
         JOIN roles ON roles.name = 'ADMIN'
WHERE app_users.username = 'admin';

-- 4) Insert tasks for each user
INSERT INTO tasks (title, description, user_id)
SELECT 'Task 1', 'Description of Task 1', app_users.id
FROM app_users
WHERE app_users.username = 'admin';

INSERT INTO tasks (title, description, user_id)
SELECT 'Task 2', 'Description of Task 2', app_users.id
FROM app_users
WHERE app_users.username = 'user';