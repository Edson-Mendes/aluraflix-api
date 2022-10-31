CREATE TABLE tb_category (
    id serial NOT NULL,
    created_at timestamp NOT NULL,
    title varchar(50) NOT NULL,
    color varchar(10) NOT NULL,
    CONSTRAINT tb_category_pk PRIMARY KEY (id),
    CONSTRAINT f_title_unique UNIQUE (title)
);