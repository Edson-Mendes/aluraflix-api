CREATE TABLE tb_video (
	id bigserial NOT NULL,
	created_at timestamp NOT NULL,
	title varchar(100) NOT NULL,
	description varchar(255) NOT NULL,
	url varchar(255) NOT NULL,
	CONSTRAINT tb_video_pk PRIMARY KEY (id)
);