INSERT INTO tb_category (id, title, color, created_at, deleted_at, enabled) VALUES
    (200, 'Romance', '808080', '2022-11-03 10:00:00', null, true);

INSERT INTO tb_video (title, description, url, created_at, category_id) VALUES
    ('Vídeo lorem ipsum', 'Descrição do vídeo lorem ipsum', 'http://www.xpto.com/f078a', '2022-11-04 12:00:00', 200);