ALTER TABLE tb_category ADD COLUMN deleted_at timestamp DEFAULT null;
ALTER TABLE tb_category ADD COLUMN enabled bool DEFAULT true;