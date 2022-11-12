CREATE TABLE tb_role (
	id serial NOT NULL,
	name varchar(25) NOT NULL,
	CONSTRAINT f_name_unique UNIQUE (name),
	CONSTRAINT tb_role_pk PRIMARY KEY (id)
);