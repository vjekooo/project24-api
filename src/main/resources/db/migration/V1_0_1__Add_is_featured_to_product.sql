ALTER TABLE store
    DROP COLUMN IF EXISTS address_id;

ALTER TABLE product
    ALTER COLUMN price SET NOT NULL;