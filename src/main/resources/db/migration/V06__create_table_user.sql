CREATE TABLE tb_user (
	id bigserial NOT NULL,
	name varchar(100) NOT NULL,
	email varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	created_at timestamp NOT NULL,
	enabled bool NOT NULL,
	deleted_at timestamp,
	CONSTRAINT f_email_unique UNIQUE (email),
	CONSTRAINT tb_user_pk PRIMARY KEY (id)
);