CREATE SEQUENCE IF NOT EXISTS address_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS favorite_product_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS favorite_store_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS store_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS verification_token_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE address
(
    id           BIGINT                      NOT NULL,
    street       VARCHAR(255),
    house_number VARCHAR(255),
    city         VARCHAR(255),
    postal_code  VARCHAR(255),
    user_id      BIGINT,
    store_id     BIGINT,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE favorite_product
(
    id         BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    user_id    BIGINT,
    CONSTRAINT pk_favoriteproduct PRIMARY KEY (id)
);

CREATE TABLE favorite_store
(
    id       BIGINT NOT NULL,
    store_id BIGINT NOT NULL,
    user_id  BIGINT,
    CONSTRAINT pk_favoritestore PRIMARY KEY (id)
);

CREATE TABLE product
(
    id          BIGINT                      NOT NULL,
    name        VARCHAR(255),
    description TEXT,
    store_id    BIGINT,
    price       DECIMAL(10, 2)              NOT NULL,
    discount    DOUBLE PRECISION,
    is_featured BOOLEAN                     NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE product_image (
    product_id bigint NOT NULL,
    image character varying(255)
);

CREATE TABLE store
(
    id          BIGINT                      NOT NULL,
    name        VARCHAR(255),
    description TEXT,
    user_id     BIGINT,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_store PRIMARY KEY (id)
);

CREATE TABLE store_media (
    store_id bigint NOT NULL,
    type smallint,
    url character varying(255),
    CONSTRAINT store_media_type_check CHECK (((type >= 0) AND (type <= 1)))
);

CREATE TABLE users
(
    id         BIGINT                      NOT NULL,
    email      VARCHAR(255)                NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    password   VARCHAR(255),
    role       SMALLINT,
    enabled    BOOLEAN,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE verification_token
(
    id          BIGINT NOT NULL,
    token       VARCHAR(255),
    user_id     BIGINT NOT NULL,
    expiry_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_verificationtoken PRIMARY KEY (id)
);

ALTER TABLE address
    ADD CONSTRAINT uc_address_store UNIQUE (store_id);

ALTER TABLE address
    ADD CONSTRAINT uc_address_user UNIQUE (user_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE verification_token
    ADD CONSTRAINT uc_verificationtoken_user UNIQUE (user_id);

ALTER TABLE address
    ADD CONSTRAINT FK_ADDRESS_ON_STORE FOREIGN KEY (store_id) REFERENCES store (id);

ALTER TABLE address
    ADD CONSTRAINT FK_ADDRESS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE favorite_product
    ADD CONSTRAINT FK_FAVORITEPRODUCT_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE favorite_store
    ADD CONSTRAINT FK_FAVORITESTORE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_STORE FOREIGN KEY (store_id) REFERENCES store (id);

ALTER TABLE store
    ADD CONSTRAINT FK_STORE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE verification_token
    ADD CONSTRAINT FK_VERIFICATIONTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);