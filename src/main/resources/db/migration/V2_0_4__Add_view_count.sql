ALTER TABLE product
    ADD view_count BIGINT DEFAULT 0 NOT NULL;

ALTER TABLE product
    ALTER COLUMN view_count SET NOT NULL;

ALTER TABLE store
    ADD view_count BIGINT DEFAULT 0 NOT NULL;

ALTER TABLE store
    ALTER COLUMN view_count SET NOT NULL;

ALTER TABLE product_category
    DROP CONSTRAINT pk_product_category;

ALTER TABLE store_category
    DROP CONSTRAINT pk_store_category;