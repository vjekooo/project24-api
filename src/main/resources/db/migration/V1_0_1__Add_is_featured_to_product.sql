ALTER TABLE product
    ADD is_featured BOOLEAN default FALSE;

ALTER TABLE product
    ALTER COLUMN is_featured SET NOT NULL;

ALTER TABLE store
    DROP COLUMN address_id;

ALTER TABLE product
    ALTER COLUMN price SET NOT NULL;