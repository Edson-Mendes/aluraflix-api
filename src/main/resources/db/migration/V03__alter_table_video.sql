ALTER TABLE tb_video ADD COLUMN category_id int NOT NULL;
ALTER TABLE tb_video ADD CONSTRAINT f_category_id_fk FOREIGN KEY (category_id) REFERENCES tb_category(id);