ALTER TABLE product_image
    DROP CONSTRAINT fk6oo0cvcdtb6qmwsga468uuukk;

ALTER TABLE users
    DROP CONSTRAINT fkditu6lr4ek16tkxtdsne0gxib;

ALTER TABLE store_media
    DROP CONSTRAINT fkfb72isuwppif6jufg89mwu382;

ALTER TABLE users
    DROP CONSTRAINT fkhmd3m5tgfs282j7f6svl8kalp;

ALTER TABLE store
    DROP CONSTRAINT fkp2sen6ouwnlht537csk0kip90;

ALTER TABLE product
    ADD is_featured BOOLEAN default FALSE;

ALTER TABLE product
    ALTER COLUMN is_featured SET NOT NULL;

DROP TABLE product_image CASCADE;

DROP TABLE store_media CASCADE;

ALTER TABLE store
    DROP COLUMN address_id;

ALTER TABLE users
    DROP COLUMN address_id;

ALTER TABLE users
    DROP COLUMN store_id;

ALTER TABLE product
    ALTER COLUMN price SET NOT NULL;