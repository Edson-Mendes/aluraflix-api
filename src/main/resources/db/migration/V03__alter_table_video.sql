ALTER TABLE tb_video ADD COLUMN category_id int NOT NULL;
ALTER TABLE tb_video ADD CONSTRAINT fk_category_id_tb_video FOREIGN KEY (category_id) REFERENCES tb_category(id);