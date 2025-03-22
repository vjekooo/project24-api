ALTER TABLE store
    ADD IF NOT EXISTS address_id BIGINT;

ALTER TABLE store
    ADD CONSTRAINT uc_store_address UNIQUE (address_id);

ALTER TABLE store
    ADD CONSTRAINT FK_STORE_ON_ADDRESS FOREIGN KEY (address_id) REFERENCES address (id);