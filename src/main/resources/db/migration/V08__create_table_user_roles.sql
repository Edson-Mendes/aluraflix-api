CREATE TABLE tb_user_roles (
	user_id int8 NOT NULL,
	role_id int4 NOT NULL,
	CONSTRAINT f_user_id_fk FOREIGN KEY (user_id) REFERENCES tb_user(id),
	CONSTRAINT f_role_id_fk FOREIGN KEY (role_id) REFERENCES tb_role(id)
)