CREATE SEQUENCE IF NOT EXISTS media_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE media
(
    id         BIGINT NOT NULL,
    image_url  VARCHAR(255),
    product_id BIGINT,
    store_id   BIGINT,
    CONSTRAINT pk_media PRIMARY KEY (id)
);

ALTER TABLE store
    ADD address_id BIGINT;

ALTER TABLE store
    ADD CONSTRAINT uc_store_address UNIQUE (address_id);

ALTER TABLE media
    ADD CONSTRAINT FK_MEDIA_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE media
    ADD CONSTRAINT FK_MEDIA_ON_STORE FOREIGN KEY (store_id) REFERENCES store (id);

ALTER TABLE store
    ADD CONSTRAINT FK_STORE_ON_ADDRESS FOREIGN KEY (address_id) REFERENCES address (id);