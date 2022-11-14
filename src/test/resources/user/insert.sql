INSERT INTO tb_user (name, email, password, created_at, enabled) VALUES
    ('Lorem Ipsum', 'lorem@email.com', '{bcrypt}$2a$10$pdxnVynvXe5OyB3Rf2gK8.1CLKRSK6rRnk9SKDLZyK3JXXCZo8e6W', '2022-11-14 10:00:00', true);

INSERT INTO tb_user_roles (user_id, role_id) VALUES
    (1, 1);