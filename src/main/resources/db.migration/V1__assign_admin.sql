INSERT INTO user_roles (user_id, role_id)
SELECT app_users.id, roles.id
FROM app_users
         JOIN roles ON roles.name = 'ADMIN'
WHERE app_users.username = 'admin';