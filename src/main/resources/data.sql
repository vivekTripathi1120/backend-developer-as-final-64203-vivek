-- ROLES

INSERT INTO roles (role_id, name, deleted_flag) VALUES (1, 'ADMIN', FALSE);
INSERT INTO roles (role_id, name, deleted_flag) VALUES (2, 'USER', FALSE);

------------------------------------------------------------------------------------------

-- USERS

INSERT INTO users (id, username, email, password, deleted_flag)
VALUES (1, 'admin', 'admin@example.com', '$2a$10$SJM25Wbd1GEpYsKC0V0HvegRd8250zrcHsf3Lh0KicYDmIZdIg28i', FALSE);

INSERT INTO users (id, username, email, password, deleted_flag)
VALUES (2, 'vivek', 'vivek@example.com', '$2a$10$OZcDeI/27eXYQzZTzFOcg.6mqNvH9XO8qI3WeVObQYbLsf0XKTcQm', FALSE);

INSERT INTO users (id, username, email, password, deleted_flag)
VALUES (3, 'john', 'john@example.com', '$2a$10$OZcDeI/27eXYQzZTzFOcg.6mqNvH9XO8qI3WeVObQYbLsf0XKTcQm', FALSE);

------------------------------------------------------------------------------------------
-- RESOURCES

INSERT INTO resources (id, name, description, price, available, deleted_flag)
VALUES (1, 'Conference Room A', 'Large conference room for meetings', 1500.00, TRUE, FALSE);

INSERT INTO resources (id, name, description, price, available, deleted_flag)
VALUES (2, 'Conference Room B', 'Medium conference room for team meetings', 1000.00, TRUE, FALSE);

INSERT INTO resources (id, name, description, price, available, deleted_flag)
VALUES (3, 'Company Vehicle', 'Vehicle available for business travel', 2500.00, TRUE, FALSE);

INSERT INTO resources (id, name, description, price, available, deleted_flag)
VALUES (4, 'Projector', 'High resolution projector for presentations', 500.00, TRUE, FALSE);



------------------------------------------------------------------------------------------

-- USER ROLES

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2);


------------------------------------------------------------------------------------------

-- RESERVATIONS

INSERT INTO reservations (id, user_id, resource_id, start_time, end_time, price, deleted_flag, status)
VALUES (1, 2, 1, '2026-09-01 10:00:00', '2026-09-01 12:00:00', 3000.00, FALSE, 'CONFIRMED');

INSERT INTO reservations (id, user_id, resource_id, start_time, end_time, price, deleted_flag, status)
VALUES (2, 2, 2, '2026-09-02 14:00:00', '2026-09-02 16:00:00', 2000.00, FALSE, 'PENDING');

INSERT INTO reservations (id, user_id, resource_id, start_time, end_time, price, deleted_flag, status)
VALUES (3, 3, 3, '2026-09-03 09:00:00', '2026-09-03 18:00:00', 22500.00, FALSE, 'CANCELLED');


------------------------------------------------------------------------------------------


ALTER TABLE roles ALTER COLUMN role_id RESTART WITH 3;
ALTER TABLE users ALTER COLUMN id RESTART WITH 4;
ALTER TABLE resources ALTER COLUMN id RESTART WITH 5;
ALTER TABLE reservations ALTER COLUMN id RESTART WITH 4;