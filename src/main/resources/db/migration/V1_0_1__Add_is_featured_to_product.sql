ALTER TABLE store
    DROP COLUMN address_id;

ALTER TABLE product
    ALTER COLUMN price SET NOT NULL;