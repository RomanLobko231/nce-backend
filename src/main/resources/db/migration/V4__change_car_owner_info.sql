ALTER TABLE car
    DROP COLUMN name,
    DROP COLUMN email,
    DROP COLUMN phone_number;

ALTER TABLE car
    ADD COLUMN owner_id UUID;